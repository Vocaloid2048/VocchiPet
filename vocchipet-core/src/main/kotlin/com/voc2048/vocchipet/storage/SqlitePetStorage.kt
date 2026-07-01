package com.voc2048.vocchipet.storage

import com.google.gson.Gson
import com.voc2048.vocchipet.api.Pet
import com.voc2048.vocchipet.api.PetStats
import com.voc2048.vocchipet.api.StatComponent
import com.voc2048.vocchipet.api.Tier
import com.voc2048.vocchipet.api.storage.PetStorage
import com.voc2048.vocchipet.PetImpl
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

/**
 * 使用 SQLite 實作的數據儲存器。
 * Data storage implementation using SQLite.
 */
class SqlitePetStorage(
    private val dbFile: File,
    private val executor: Executor
) : PetStorage {

    private val url = "jdbc:sqlite:${dbFile.absolutePath}"
    private val gson = Gson()

    private fun getConnection(): Connection {
        return DriverManager.getConnection(url)
    }

    override fun init(): CompletableFuture<Void> {
        return CompletableFuture.runAsync({
            dbFile.parentFile.mkdirs()
            getConnection().use { conn ->
                val sql = """
                    CREATE TABLE IF NOT EXISTS vocchipet_data (
                        pet_uuid TEXT PRIMARY KEY,
                        owner_uuid TEXT NOT NULL,
                        tamer_uuid TEXT NOT NULL,
                        pet_type TEXT NOT NULL,
                        pet_name TEXT NOT NULL,
                        level INTEGER NOT NULL,
                        exp INTEGER NOT NULL,
                        affection REAL NOT NULL,
                        subspecies INTEGER NOT NULL DEFAULT 0,
                        stats_json TEXT NOT NULL
                    );
                    CREATE TABLE IF NOT EXISTS vocchipet_players (
                        player_uuid TEXT PRIMARY KEY,
                        max_bag_pages INTEGER NOT NULL DEFAULT 1
                    );
                    CREATE TABLE IF NOT EXISTS vocchipet_bag (
                        player_uuid TEXT NOT NULL,
                        page INTEGER NOT NULL,
                        slot INTEGER NOT NULL,
                        pet_uuid TEXT,
                        PRIMARY KEY (player_uuid, page, slot)
                    );
                """.trimIndent()
                conn.createStatement().execute(sql)
                
                // Migration: Check for pet_name column
                try {
                    conn.createStatement().execute("ALTER TABLE vocchipet_data ADD COLUMN pet_name TEXT DEFAULT ''")
                } catch (e: Exception) {}
            }
        }, executor)
    }

    override fun savePet(pet: Pet): CompletableFuture<Void> {
        return CompletableFuture.runAsync({
            getConnection().use { conn ->
                val sql = """
                    INSERT OR REPLACE INTO vocchipet_data 
                    (pet_uuid, owner_uuid, tamer_uuid, pet_type, pet_name, level, exp, affection, subspecies, stats_json) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """.trimIndent()
                val pstmt: PreparedStatement = conn.prepareStatement(sql)
                pstmt.setString(1, pet.getUniqueId().toString())
                pstmt.setString(2, pet.getOwnerId().toString())
                pstmt.setString(3, pet.getTamerId().toString())
                pstmt.setString(4, pet.getType())
                pstmt.setString(5, pet.getName())
                pstmt.setInt(6, pet.getLevel())
                pstmt.setInt(7, pet.getExp())
                pstmt.setDouble(8, pet.getAffection())
                pstmt.setInt(9, if (pet.isSubspecies()) 1 else 0)
                pstmt.setString(10, gson.toJson(pet.getStats()))
                pstmt.executeUpdate()
            }
        }, executor)
    }

    override fun loadPet(petId: UUID): CompletableFuture<Pet?> {
        return CompletableFuture.supplyAsync({
            getConnection().use { conn ->
                val sql = "SELECT * FROM vocchipet_data WHERE pet_uuid = ?;"
                val pstmt = conn.prepareStatement(sql)
                pstmt.setString(1, petId.toString())
                val rs = pstmt.executeQuery()
                if (rs.next()) {
                    mapResultSetToPet(rs)
                } else {
                    null
                }
            }
        }, executor)
    }

    override fun loadPetsByOwner(ownerId: UUID): CompletableFuture<List<Pet>> {
        return CompletableFuture.supplyAsync({
            val pets = mutableListOf<Pet>()
            getConnection().use { conn ->
                val sql = "SELECT * FROM vocchipet_data WHERE owner_uuid = ?;"
                val pstmt = conn.prepareStatement(sql)
                pstmt.setString(1, ownerId.toString())
                val rs = pstmt.executeQuery()
                while (rs.next()) {
                    pets.add(mapResultSetToPet(rs))
                }
            }
            pets
        }, executor)
    }

    private fun mapResultSetToPet(rs: ResultSet): Pet {
        val statsJson = rs.getString("stats_json")
        val stats = try {
            gson.fromJson(statsJson, PetStats::class.java)
        } catch (e: Exception) {
            PetStats(
                hp = StatComponent(0, Tier.TIER_0),
                attack = StatComponent(0, Tier.TIER_0),
                defense = StatComponent(0, Tier.TIER_0),
                speed = StatComponent(0, Tier.TIER_0),
                focus = StatComponent(0, Tier.TIER_0),
                skills = arrayOfNulls<String>(4)
            )
        }
        return PetImpl(
            uuid = UUID.fromString(rs.getString("pet_uuid")),
            ownerId = UUID.fromString(rs.getString("owner_uuid")),
            tamerId = UUID.fromString(rs.getString("tamer_uuid")),
            type = rs.getString("pet_type"),
            name = rs.getString("pet_name"),
            level = rs.getInt("level"),
            exp = rs.getInt("exp"),
            affection = rs.getDouble("affection"),
            stats = stats,
            subspecies = rs.getInt("subspecies") == 1
        )
    }
}

package com.voc2048.vocchiPet.storage

import com.voc2048.vocchipet.api.Element
import com.voc2048.vocchipet.api.Pet
import com.voc2048.vocchipet.api.storage.PetStorage
import com.voc2048.vocchiPet.PetImpl
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

/**
 * 使用 SQLite 實作的寵物數據儲存器。
 * Pet data storage implementation using SQLite.
 *
 * @property dbFile SQLite 資料庫檔案 / The SQLite database file.
 * @property executor 用於執行非同步任務的執行器 / Executor for running asynchronous tasks.
 */
class SqlitePetStorage(
    private val dbFile: File,
    private val executor: Executor
) : PetStorage {

    private val url = "jdbc:sqlite:${dbFile.absolutePath}"

    /**
     * 獲取資料庫連接。
     * Gets a database connection.
     *
     * @return 資料庫連接物件 / The database connection object.
     */
    private fun getConnection(): Connection {
        return DriverManager.getConnection(url)
    }

    /**
     * 初始化儲存媒介，建立 vocchipet_data 資料表。
     * Initializes the storage medium, creating the vocchipet_data table.
     *
     * @return 非同步執行的 CompletableFuture / A CompletableFuture for asynchronous execution.
     */
    override fun init(): CompletableFuture<Void> {
        return CompletableFuture.runAsync({
            dbFile.parentFile.mkdirs()
            getConnection().use { conn ->
                val sql = """
                    CREATE TABLE IF NOT EXISTS vocchipet_data (
                        pet_uuid TEXT PRIMARY KEY,
                        owner_uuid TEXT NOT NULL,
                        pet_type TEXT NOT NULL,
                        level INTEGER NOT NULL,
                        exp INTEGER NOT NULL,
                        affection REAL NOT NULL,
                        element TEXT NOT NULL
                    );
                """.trimIndent()
                conn.createStatement().execute(sql)
            }
        }, executor)
    }

    /**
     * 非同步儲存寵物數據。
     * Asynchronously saves pet data.
     *
     * @param pet 要儲存的寵物實例 / The pet instance to save.
     * @return 非同步執行的 CompletableFuture / A CompletableFuture for asynchronous execution.
     */
    override fun savePet(pet: Pet): CompletableFuture<Void> {
        return CompletableFuture.runAsync({
            getConnection().use { conn ->
                val sql = """
                    INSERT OR REPLACE INTO vocchipet_data 
                    (pet_uuid, owner_uuid, pet_type, level, exp, affection, element) 
                    VALUES (?, ?, ?, ?, ?, ?, ?);
                """.trimIndent()
                val pstmt: PreparedStatement = conn.prepareStatement(sql)
                pstmt.setString(1, pet.getUniqueId().toString())
                pstmt.setString(2, pet.getOwnerId().toString())
                pstmt.setString(3, pet.getType())
                pstmt.setInt(4, pet.getLevel())
                pstmt.setInt(5, pet.getExp())
                pstmt.setDouble(6, pet.getAffection())
                pstmt.setString(7, pet.getElement().name)
                pstmt.executeUpdate()
            }
        }, executor)
    }

    /**
     * 非同步讀取特定 UUID 的寵物數據。
     * Asynchronously loads pet data for a specific UUID.
     *
     * @param petId 寵物的唯一識別碼 / The unique identifier of the pet.
     * @return 包含寵物實例的 CompletableFuture，若不存在則為 null / A CompletableFuture containing the pet instance, or null if not found.
     */
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

    /**
     * 非同步讀取特定玩家擁有的所有寵物。
     * Asynchronously loads all pets owned by a specific player.
     *
     * @param ownerId 玩家的唯一識別碼 / The unique identifier of the owner.
     * @return 包含寵物清單的 CompletableFuture / A CompletableFuture containing a list of pets.
     */
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

    /**
     * 將 ResultSet 的當前行映射為 Pet 實例。
     * Maps the current row of a ResultSet to a Pet instance.
     *
     * @param rs 資料庫查詢結果集 / The database query result set.
     * @return 寵物實例 / The pet instance.
     */
    private fun mapResultSetToPet(rs: ResultSet): Pet {
        return PetImpl(
            uuid = UUID.fromString(rs.getString("pet_uuid")),
            ownerId = UUID.fromString(rs.getString("owner_uuid")),
            type = rs.getString("pet_type"),
            level = rs.getInt("level"),
            exp = rs.getInt("exp"),
            affection = rs.getDouble("affection"),
            element = Element.valueOf(rs.getString("element"))
        )
    }
}

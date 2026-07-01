package com.voc2048.vocchipet.storage

import com.voc2048.vocchipet.api.Element
import com.voc2048.vocchipet.api.Pet
import com.voc2048.vocchipet.api.PetStats
import com.voc2048.vocchipet.api.StatComponent
import com.voc2048.vocchipet.api.Tier
import com.voc2048.vocchipet.api.storage.ModelStorage
import com.voc2048.vocchipet.api.storage.PetStorage
import com.voc2048.vocchipet.PetImpl
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

/**
 * 使用 SQLite 實作的數據儲存器，包含寵物數據與模型映射。
 * Data storage implementation using SQLite, including pet data and model mappings.
 *
 * @property dbFile SQLite 資料庫檔案 / The SQLite database file.
 * @property executor 用於執行非同步任務的執行器 / Executor for running asynchronous tasks.
 */
class SqlitePetStorage(
    private val dbFile: File,
    private val executor: Executor
) : PetStorage, ModelStorage {

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
                        tamer_uuid TEXT NOT NULL,
                        pet_type TEXT NOT NULL,
                        level INTEGER NOT NULL,
                        exp INTEGER NOT NULL,
                        affection REAL NOT NULL,
                        element TEXT NOT NULL,
                        streaming INTEGER NOT NULL DEFAULT 0,
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
                    CREATE TABLE IF NOT EXISTS vocchipet_models (
                        model_key TEXT PRIMARY KEY,
                        material TEXT NOT NULL,
                        custom_model_data INTEGER NOT NULL
                    );
                """.trimIndent()
                conn.createStatement().execute(sql)
                
                // 檢查並為舊資料表添加欄位 (Migration)
                // Check and add columns for existing table (Migration)
                try {
                    conn.createStatement().execute("ALTER TABLE vocchipet_data ADD COLUMN tamer_uuid TEXT DEFAULT ''")
                    conn.createStatement().execute("UPDATE vocchipet_data SET tamer_uuid = owner_uuid WHERE tamer_uuid = ''")
                } catch (e: Exception) {}
                
                try {
                    conn.createStatement().execute("ALTER TABLE vocchipet_data ADD COLUMN streaming INTEGER DEFAULT 0")
                } catch (e: Exception) {}
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
                    (pet_uuid, owner_uuid, tamer_uuid, pet_type, level, exp, affection, element, streaming, stats_json) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """.trimIndent()
                val pstmt: PreparedStatement = conn.prepareStatement(sql)
                pstmt.setString(1, pet.getUniqueId().toString())
                pstmt.setString(2, pet.getOwnerId().toString())
                pstmt.setString(3, pet.getTamerId().toString())
                pstmt.setString(4, pet.getType())
                pstmt.setInt(5, pet.getLevel())
                pstmt.setInt(6, pet.getExp())
                pstmt.setDouble(7, pet.getAffection())
                pstmt.setString(8, pet.getElement().name)
                pstmt.setInt(9, if (pet.isStreaming()) 1 else 0)
                // TODO: 序列化 stats 為 JSON
                pstmt.setString(10, "{}")
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
     * 非同步儲存模型映射。
     * Asynchronously saves a model mapping.
     */
    override fun saveModelMapping(key: String, itemStack: ItemStack): CompletableFuture<Void> {
        return CompletableFuture.runAsync({
            getConnection().use { conn ->
                val sql = "INSERT OR REPLACE INTO vocchipet_models (model_key, material, custom_model_data) VALUES (?, ?, ?);"
                val pstmt = conn.prepareStatement(sql)
                pstmt.setString(1, key)
                pstmt.setString(2, itemStack.type.name)
                val meta = itemStack.itemMeta
                pstmt.setInt(3, if (meta != null && meta.hasCustomModelData()) meta.customModelData else 0)
                pstmt.executeUpdate()
            }
        }, executor)
    }

    /**
     * 非同步讀取所有模型映射。
     * Asynchronously loads all model mappings.
     */
    override fun loadAllModels(): CompletableFuture<Map<String, ItemStack>> {
        return CompletableFuture.supplyAsync({
            val models = mutableMapOf<String, ItemStack>()
            getConnection().use { conn ->
                val sql = "SELECT * FROM vocchipet_models;"
                val rs = conn.createStatement().executeQuery(sql)
                while (rs.next()) {
                    val key = rs.getString("model_key")
                    val material = Material.valueOf(rs.getString("material"))
                    val customModelData = rs.getInt("custom_model_data")
                    val item = ItemStack(material)
                    val meta = item.itemMeta
                    meta?.setCustomModelData(customModelData)
                    item.itemMeta = meta
                    models[key] = item
                }
            }
            models
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
        // TODO: 解析 JSON 數據 / Parse JSON data
        val stats = PetStats(
            hp = StatComponent(0, Tier.D, 0),
            attack = StatComponent(0, Tier.D, 0),
            defense = StatComponent(0, Tier.D, 0),
            speed = StatComponent(0, Tier.D, 0),
            focus = StatComponent(0, Tier.D, 0),
            availableTp = 0,
            skills = arrayOfNulls<String>(6)
        )
        return PetImpl(
            uuid = UUID.fromString(rs.getString("pet_uuid")),
            ownerId = UUID.fromString(rs.getString("owner_uuid")),
            tamerId = UUID.fromString(rs.getString("tamer_uuid")),
            type = rs.getString("pet_type"),
            level = rs.getInt("level"),
            exp = rs.getInt("exp"),
            affection = rs.getDouble("affection"),
            element = Element.valueOf(rs.getString("element")),
            stats = stats,
            streaming = rs.getInt("streaming") == 1
        )
    }
}

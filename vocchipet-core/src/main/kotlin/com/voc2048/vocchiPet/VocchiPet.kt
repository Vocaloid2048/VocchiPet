package com.voc2048.vocchiPet

import com.voc2048.vocchipet.api.render.ModelEngine
import com.voc2048.vocchipet.api.render.ModelRegistry
import com.voc2048.vocchipet.api.storage.ModelStorage
import com.voc2048.vocchipet.api.storage.PetStorage
import com.voc2048.vocchiPet.render.DefaultModelRegistry
import com.voc2048.vocchiPet.render.PetSpawnManager
import com.voc2048.vocchiPet.storage.SqlitePetStorage
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * VocchiPet 插件的主類別。
 * The main class of the VocchiPet plugin.
 */
class VocchiPet : JavaPlugin() {

    private lateinit var petStorage: PetStorage
    private lateinit var modelStorage: ModelStorage
    private lateinit var modelRegistry: ModelRegistry
    private lateinit var modelEngine: ModelEngine
    private lateinit var databaseExecutor: Executor

    override fun onEnable() {
        // 初始化非同步執行器
        databaseExecutor = Executors.newSingleThreadExecutor { runnable ->
            Thread(runnable, "VocchiPet-Database-Thread")
        }

        // 初始化儲存器與註冊表
        val dbFile = File(dataFolder, "storage.db")
        val sqliteStorage = SqlitePetStorage(dbFile, databaseExecutor)
        petStorage = sqliteStorage
        modelStorage = sqliteStorage
        
        modelRegistry = DefaultModelRegistry()
        modelEngine = PetSpawnManager(modelRegistry)

        sqliteStorage.init().thenCompose {
            logger.info("SQLite 數據庫初始化成功。")
            // 加載所有模型映射
            modelStorage.loadAllModels()
        }.thenAccept { models ->
            models.forEach { (key, item) ->
                modelRegistry.registerModel(key, item)
            }
            logger.info("已加載 ${models.size} 個模型映射。")
        }.exceptionally { ex ->
            logger.severe("初始化過程中發生錯誤: ${ex.message}")
            null
        }

        logger.info("VocchiPet 已啟動！")
    }

    override fun onDisable() {
        logger.info("VocchiPet 已關閉。")
        logger.info("VocchiPet has been disabled.")
    }

    /**
     * 獲取寵物數據儲存器。
     * Gets the pet data storage.
     *
     * @return 寵物儲存器實例 / The pet storage instance.
     */
    fun getPetStorage(): PetStorage = petStorage

    /**
     * 獲取模型註冊表。
     * Gets the model registry.
     *
     * @return 模型註冊表實例 / The model registry instance.
     */
    fun getModelRegistry(): ModelRegistry = modelRegistry

    /**
     * 獲取模型渲染引擎。
     * Gets the model rendering engine.
     *
     * @return 模型引擎實例 / The model engine instance.
     */
    fun getModelEngine(): ModelEngine = modelEngine
}

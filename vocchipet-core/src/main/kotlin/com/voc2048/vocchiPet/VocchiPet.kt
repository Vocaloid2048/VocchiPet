package com.voc2048.vocchiPet

import com.voc2048.vocchipet.api.storage.PetStorage
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
    private lateinit var databaseExecutor: Executor

    override fun onEnable() {
        // 初始化非同步執行器
        // Initialize the asynchronous executor
        databaseExecutor = Executors.newSingleThreadExecutor { runnable ->
            Thread(runnable, "VocchiPet-Database-Thread")
        }

        // 初始化儲存器
        // Initialize the storage
        val dbFile = File(dataFolder, "storage.db")
        petStorage = SqlitePetStorage(dbFile, databaseExecutor)

        petStorage.init().thenRun {
            logger.info("SQLite 數據庫初始化成功。")
            logger.info("SQLite database initialized successfully.")
        }.exceptionally { ex ->
            logger.severe("SQLite 數據庫初始化失敗: ${ex.message}")
            logger.severe("Failed to initialize SQLite database: ${ex.message}")
            null
        }

        logger.info("VocchiPet 已啟動！")
        logger.info("VocchiPet has been enabled!")
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
}

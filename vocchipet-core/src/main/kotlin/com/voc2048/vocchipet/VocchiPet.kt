package com.voc2048.vocchipet

import com.voc2048.vocchipet.api.storage.PetStorage
import com.voc2048.vocchipet.storage.SqlitePetStorage
import com.voc2048.vocchipet.interaction.*
import com.voc2048.vocchipet.core.PetSpeciesRegistry
import com.voc2048.vocchipet.core.PetManager
import com.voc2048.vocchipet.core.capsule.CapsuleManager
import com.voc2048.vocchipet.core.capsule.CaptureListener
import com.voc2048.vocchipet.interaction.*
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
    
    private lateinit var speciesRegistry: PetSpeciesRegistry
    private lateinit var petManager: PetManager
    private lateinit var capsuleManager: CapsuleManager
    private lateinit var bagGui: PetBagGui
    private lateinit var mainMenuGui: MainMenuGui
    private lateinit var constructionGui: PetConstructionMasterGui

    override fun onEnable() {
        // 初始化非同步執行器
        databaseExecutor = Executors.newSingleThreadExecutor { runnable ->
            Thread(runnable, "VocchiPet-Database-Thread")
        }

        // 初始化種類註冊表與資料
        speciesRegistry = PetSpeciesRegistry()
        com.voc2048.vocchipet.core.TestData.getTestSpecies().forEach {
            speciesRegistry.register(it)
        }

        // 初始化管理器
        petManager = PetManager()
        capsuleManager = CapsuleManager(this)
        capsuleManager.registerRecipes()

        // 初始化 GUI
        bagGui = PetBagGui(this)
        mainMenuGui = MainMenuGui(this)
        constructionGui = PetConstructionMasterGui(this)

        // 初始化儲存器
        val dbFile = File(dataFolder, "storage.db")
        val sqliteStorage = SqlitePetStorage(dbFile, databaseExecutor)
        petStorage = sqliteStorage
        
        sqliteStorage.init().thenAccept {
            logger.info("SQLite 數據庫初始化成功。")
        }.exceptionally { ex ->
            logger.severe("初始化過程中發生錯誤: ${ex.message}")
            null
        }

        // 註冊指令
        val commandExecutor = PetCommandExecutor(this, bagGui, mainMenuGui, constructionGui)
        getCommand("vocchipet")?.setExecutor(commandExecutor)
        getCommand("vocchipet")?.tabCompleter = commandExecutor

        // 註冊事件監聽器
        val guiListener = GuiListener(this, bagGui, mainMenuGui, constructionGui)
        server.pluginManager.registerEvents(guiListener, this)
        server.pluginManager.registerEvents(CaptureListener(this), this)
        server.pluginManager.registerEvents(InteractionListener(this, guiListener, mainMenuGui), this)
        server.pluginManager.registerEvents(CombatListener(this), this)

        logger.info("VocchiPet 已啟動！")
    }

    override fun onDisable() {
        logger.info("VocchiPet 已關閉。")
    }

    /**
     * 獲取寵物數據儲存器。
     * Gets the pet data storage.
     *
     * @return 寵物儲存器實例 / The pet storage instance.
     */
    fun getPetStorage(): PetStorage = petStorage

    /**
     * 獲取寵物種類註冊表。
     * Gets the pet species registry.
     *
     * @return 種類註冊表實例 / The species registry instance.
     */
    fun getSpeciesRegistry(): PetSpeciesRegistry = speciesRegistry

    /**
     * 獲取寵物管理器。
     * Gets the pet manager.
     *
     * @return 寵物管理器實例 / The pet manager instance.
     */
    fun getPetManager(): PetManager = petManager

    /**
     * 獲取寵物膠囊管理器。
     * Gets the pet capsule manager.
     *
     * @return 膠囊管理器實例 / The capsule manager instance.
     */
    fun getCapsuleManager(): CapsuleManager = capsuleManager
}

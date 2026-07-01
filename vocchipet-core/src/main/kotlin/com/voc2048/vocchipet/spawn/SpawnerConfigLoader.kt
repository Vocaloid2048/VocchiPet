package com.voc2048.vocchipet.spawn

import com.voc2048.vocchipet.api.Rarity
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.lang.reflect.InvocationTargetException

/**
 * 負責從 YAML 檔案加載刷新配置。
 * Responsible for loading spawn configurations from a YAML file.
 */
object SpawnerConfigLoader {

    /**
     * 加載 spawner.yml 檔案。
     * Loads the spawner.yml file.
     *
     * @param file YAML 檔案 / The YAML file.
     * @return 加載後的生態域配置對應表（鍵為生態域名稱字串） / The loaded biome configuration map (keys are biome name strings).
     */
    fun load(file: File): Map<String, BiomeSpawnConfig> {
        val config = YamlConfiguration.loadConfiguration(file)
        val biomeConfigs = mutableMapOf<String, BiomeSpawnConfig>()

        val biomesSection = config.getConfigurationSection("biomes") ?: return emptyMap()

        for (biomeKey in biomesSection.getKeys(false)) {
            val biomeName = biomeKey.uppercase()
            // 驗證是否為有效生態域名稱（若 Bukkit 不可用則跳過驗證）
            // Validate if it's a valid biome name (skip validation if Bukkit is unavailable)
            if (!isValidBiomeName(biomeName)) {
                continue
            }

            val biomeSection = biomesSection.getConfigurationSection(biomeKey) ?: continue
            
            // 加載稀有度機率
            // Load rarity chances
            val rarityChances = mutableMapOf<Rarity, Double>()
            val raritySection = biomeSection.getConfigurationSection("rarity-chances")
            raritySection?.let { section ->
                for (rarityName in section.getKeys(false)) {
                    val rarity = try {
                        Rarity.valueOf(rarityName.uppercase())
                    } catch (e: IllegalArgumentException) {
                        continue
                    }
                    rarityChances[rarity] = section.getDouble(rarityName)
                }
            }

            // 加載寵物權重群組
            // Load pet weight groups
            val spawnGroups = mutableMapOf<Rarity, Map<String, Int>>()
            val groupsSection = biomeSection.getConfigurationSection("spawn-groups")
            groupsSection?.let { section ->
                for (rarityName in section.getKeys(false)) {
                    val rarity = try {
                        Rarity.valueOf(rarityName.uppercase())
                    } catch (e: IllegalArgumentException) {
                        continue
                    }
                    
                    val groupSection = section.getConfigurationSection(rarityName) ?: continue
                    val pets = mutableMapOf<String, Int>()
                    for (petId in groupSection.getKeys(false)) {
                        pets[petId] = groupSection.getInt(petId)
                    }
                    spawnGroups[rarity] = pets
                }
            }

            biomeConfigs[biomeName] = BiomeSpawnConfig(rarityChances, spawnGroups)
        }

        return biomeConfigs
    }

    /**
     * 驗證生態域名稱是否有效。
     * 若 Bukkit 環境不可用（如單元測試），則接受所有名稱。
     * Validates whether a biome name is valid.
     * If the Bukkit environment is unavailable (e.g., unit tests), accepts all names.
     *
     * @param name 生態域名稱 / The biome name.
     * @return 是否有效 / Whether it is valid.
     */
    private fun isValidBiomeName(name: String): Boolean {
        return try {
            val biomeClass = Class.forName("org.bukkit.block.Biome")
            try {
                biomeClass.getMethod("valueOf", String::class.java).invoke(null, name)
                true
            } catch (e: InvocationTargetException) {
                if (e.targetException is IllegalArgumentException) false else true
            }
        } catch (e: Throwable) {
            // Bukkit 環境不可用（如單元測試），接受所有名稱
            // Bukkit environment unavailable (e.g., unit tests), accept all names
            true
        }
    }
}

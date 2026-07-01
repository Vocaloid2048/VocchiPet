package com.voc2048.vocchiPet.spawn

import com.voc2048.vocchipet.api.Rarity
import org.bukkit.block.Biome

/**
 * 處理野生寵物生成邏輯的引擎。
 * Engine for handling wild pet spawning logic.
 *
 * @property biomeConfigs 生態域配置對應表（鍵為生態域名稱字串） / Biome configuration map (keys are biome name strings).
 */
class SpawnEngine(private val biomeConfigs: Map<String, BiomeSpawnConfig>) {

    /**
     * 根據生態域名稱計算應生成的寵物 ID。
     * Calculates the pet ID to spawn based on the biome name.
     *
     * @param biomeName 生態域名稱 / The biome name.
     * @return 寵物 ID，若該生態域未配置則返回 null / The pet ID, or null if the biome is not configured.
     */
    fun calculateSpawn(biomeName: String): String? {
        val config = biomeConfigs[biomeName.uppercase()] ?: return null
        
        // 第一步：選擇稀有度
        // Step 1: Select Rarity
        val raritySelector = WeightedSelector<Rarity>()
        config.rarityChances.forEach { (rarity, chance) ->
            raritySelector.add(chance, rarity)
        }
        
        val selectedRarity = raritySelector.select() ?: return null
        
        // 第二步：在該稀有度中選擇寵物 ID
        // Step 2: Select Pet ID within that rarity
        val petGroup = config.spawnGroups[selectedRarity] ?: return null
        if (petGroup.isEmpty()) return null

        val petSelector = WeightedSelector<String>()
        petGroup.forEach { (petId, weight) ->
            petSelector.add(weight.toDouble(), petId)
        }
        
        return petSelector.select()
    }

    /**
     * 根據生態域計算應生成的寵物 ID。
     * Calculates the pet ID to spawn based on the biome.
     *
     * @param biome 生態域 / The biome.
     * @return 寵物 ID，若該生態域未配置則返回 null / The pet ID, or null if the biome is not configured.
     */
    fun calculateSpawn(biome: Biome): String? = calculateSpawn(biome.name())

    /**
     * 獲取特定生態域名稱的配置。
     * Gets the configuration for a specific biome name.
     *
     * @param biomeName 生態域名稱 / The biome name.
     * @return 生態域配置，若不存在則返回 null / The biome configuration, or null if not found.
     */
    fun getBiomeConfig(biomeName: String): BiomeSpawnConfig? = biomeConfigs[biomeName.uppercase()]

    /**
     * 獲取特定生態域的配置。
     * Gets the configuration for a specific biome.
     *
     * @param biome 生態域 / The biome.
     * @return 生態域配置，若不存在則返回 null / The biome configuration, or null if not found.
     */
    fun getBiomeConfig(biome: Biome): BiomeSpawnConfig? = getBiomeConfig(biome.name())
}

package com.voc2048.vocchipet.spawn

import com.voc2048.vocchipet.api.Rarity

/**
 * 代表單一生態域的刷新配置。
 * Represents the spawn configuration for a single biome.
 *
 * @property rarityChances 各稀有度的生成機率 / Spawn chances for each rarity.
 * @property spawnGroups 各稀有度分類下的寵物與其權重 / Pets and their weights under each rarity category.
 */
data class BiomeSpawnConfig(
    val rarityChances: Map<Rarity, Double>,
    val spawnGroups: Map<Rarity, Map<String, Int>>
)

package com.voc2048.vocchipet.core.spawn

import kotlin.random.Random

/**
 * 野生寵物生成管理器，負責權重算法與邏輯。
 * Wild pet spawn manager, responsible for weight algorithms and logic.
 */
object SpawnManager {

    /**
     * 根據權重列表選擇一個項目。
     * Selects an item based on a weight list.
     * @param items 帶權重的項目映射 / Map of items with their weights.
     * @return 隨機選中的ID / The randomly selected ID.
     */
    fun selectByWeight(items: Map<String, Int>): String? {
        val totalWeight = items.values.sum()
        if (totalWeight <= 0) return null
        
        var randomValue = Random.nextInt(totalWeight)
        for ((id, weight) in items) {
            randomValue -= weight
            if (randomValue < 0) return id
        }
        return items.keys.last()
    }
}

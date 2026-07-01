package com.voc2048.vocchipet.spawn

import java.util.*
import kotlin.random.Random

/**
 * 權重隨機選擇器，用於依據權重選擇物件。
 * Weighted random selector, used to select items based on their weights.
 *
 * @param T 選項的類型 / The type of the items.
 */
class WeightedSelector<T> {
    private val map: NavigableMap<Double, T> = TreeMap()
    private var totalWeight = 0.0

    /**
     * 新增一個帶有權重的選項。
     * Adds an item with a specific weight.
     *
     * @param weight 權重數值 / The weight value.
     * @param result 選項內容 / The item to select.
     */
    fun add(weight: Double, result: T) {
        if (weight <= 0) return
        totalWeight += weight
        map[totalWeight] = result
    }

    /**
     * 獲取總權重。
     * Gets the total weight.
     *
     * @return 總權重 / The total weight.
     */
    fun getTotalWeight(): Double = totalWeight

    /**
     * 隨機選擇一個選項。
     * Randomly selects an item.
     *
     * @return 獲選的選項，若無選項則返回 null / The selected item, or null if empty.
     */
    fun select(): T? {
        if (totalWeight <= 0) return null
        val value = Random.nextDouble() * totalWeight
        return map.higherEntry(value)?.value
    }

    /**
     * 使用固定的隨機值選擇一個選項（主要用於測試）。
     * Selects an item using a fixed random value (mainly for testing).
     *
     * @param randomValue 0.0 到 1.0 之間的隨機值 / Random value between 0.0 and 1.0.
     * @return 獲選的選項 / The selected item.
     */
    fun selectFixed(randomValue: Double): T? {
        if (totalWeight <= 0) return null
        val value = randomValue * totalWeight
        return map.higherEntry(value)?.value
    }
}

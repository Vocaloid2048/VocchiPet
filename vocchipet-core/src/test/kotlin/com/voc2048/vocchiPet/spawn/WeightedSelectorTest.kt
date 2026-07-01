package com.voc2048.vocchipet.spawn

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.RepeatedTest

/**
 * 測試權重隨機選擇器的核心邏輯。
 * Tests the core logic of the weighted random selector.
 */
class WeightedSelectorTest {

    private lateinit var selector: WeightedSelector<String>

    @BeforeEach
    fun setUp() {
        selector = WeightedSelector()
    }

    @Test
    @DisplayName("單一選項權重選擇應 100% 命中 / Single item should always be selected")
    fun testSingleItemAlwaysSelected() {
        selector.add(1.0, "only_item")
        assertEquals("only_item", selector.select())
        assertEquals(1.0, selector.getTotalWeight())
    }

    @Test
    @DisplayName("空選擇器應返回 null / Empty selector should return null")
    fun testEmptySelectorReturnsNull() {
        assertNull(selector.select())
        assertEquals(0.0, selector.getTotalWeight())
    }

    @Test
    @DisplayName("零或負權重應被忽略 / Zero or negative weights should be ignored")
    fun testZeroAndNegativeWeightsIgnored() {
        selector.add(0.0, "zero")
        selector.add(-1.0, "negative")
        selector.add(5.0, "valid")
        assertEquals("valid", selector.select())
        assertEquals(5.0, selector.getTotalWeight())
    }

    @Test
    @DisplayName("總權重計算應正確 / Total weight calculation should be correct")
    fun testTotalWeightCalculation() {
        selector.add(10.0, "a")
        selector.add(20.0, "b")
        selector.add(30.0, "c")
        assertEquals(60.0, selector.getTotalWeight(), 0.0001)
    }

    @Test
    @DisplayName("固定隨機值選擇測試 / Fixed random value selection test")
    fun testFixedRandomSelection() {
        selector.add(10.0, "a")  // 0.0 ~ 10.0
        selector.add(20.0, "b")  // 10.0 ~ 30.0
        selector.add(30.0, "c")  // 30.0 ~ 60.0

        // randomValue = 0.0 -> 0.0 * 60 = 0 -> "a" (higherEntry(0.0) = 10.0 -> "a")
        assertEquals("a", selector.selectFixed(0.0))

        // randomValue = 0.15 -> 9.0 -> "a" (higherEntry(9.0) = 10.0 -> "a")
        assertEquals("a", selector.selectFixed(0.15))

        // randomValue = 0.2 -> 12.0 -> "b" (higherEntry(12.0) = 30.0 -> "b")
        assertEquals("b", selector.selectFixed(0.2))

        // randomValue = 0.5 -> 30.0 -> "c" (higherEntry(30.0) = 60.0 -> "c")
        assertEquals("c", selector.selectFixed(0.5))

        // randomValue = 0.9 -> 54.0 -> "c" (higherEntry(54.0) = 60.0 -> "c")
        assertEquals("c", selector.selectFixed(0.9))
    }

    @RepeatedTest(100)
    @DisplayName("多次隨機選擇不應返回 null / Multiple random selections should not return null")
    fun testRepeatedSelectionNotNull() {
        selector.add(1.0, "x")
        selector.add(2.0, "y")
        selector.add(3.0, "z")
        assertNotNull(selector.select())
    }

    @Test
    @DisplayName("權重分佈機率統計測試 / Weight distribution probability statistical test")
    fun testWeightDistribution() {
        selector.add(70.0, "common")
        selector.add(20.0, "uncommon")
        selector.add(10.0, "rare")

        val counts = mutableMapOf("common" to 0, "uncommon" to 0, "rare" to 0)
        val iterations = 10000

        repeat(iterations) {
            val result = selector.select() ?: "unknown"
            counts[result] = counts.getOrDefault(result, 0) + 1
        }

        val commonRate = counts["common"]!!.toDouble() / iterations
        val uncommonRate = counts["uncommon"]!!.toDouble() / iterations
        val rareRate = counts["rare"]!!.toDouble() / iterations

        // 允許 ±5% 的誤差 / Allow ±5% deviation
        assertTrue(commonRate in 0.65..0.75, "common 機率應約 70%，實際為 $commonRate")
        assertTrue(uncommonRate in 0.15..0.25, "uncommon 機率應約 20%，實際為 $uncommonRate")
        assertTrue(rareRate in 0.05..0.15, "rare 機率應約 10%，實際為 $rareRate")
    }
}

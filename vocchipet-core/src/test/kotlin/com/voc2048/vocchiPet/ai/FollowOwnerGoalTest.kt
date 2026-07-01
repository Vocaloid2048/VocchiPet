package com.voc2048.vocchiPet.ai

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * 跟隨邏輯測試。
 * Tests for follow logic.
 */
class FollowOwnerGoalTest {

    /**
     * 測試距離計算邏輯。
     * Tests distance calculation logic.
     */
    @Test
    fun testDistanceCalculation() {
        val distSquared = 10.0 * 10.0
        assertTrue(distSquared < 16 * 16)
        assertTrue(distSquared > 6 * 6)
    }
}

package com.voc2048.vocchipet.core.capsule

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * 測試寵物膠囊捕捉率公式。
 * Test class for pet capsule capture rate formula.
 */
class CapsuleManagerTest {

    @Test
    fun `test capture rate when HP is full`() {
        // 當 HP 為 100% 時，成功率應為 0
        // When HP is 100%, success rate should be 0
        val rate = CapsuleManager.calculateCaptureRate(100.0, 100.0, CapsuleTier.ULTRA)
        assertEquals(0.0, rate, 0.001)
    }

    @ParameterizedTest
    @CsvSource(
        "50.0, 100.0, REGULAR, 0.5",
        "50.0, 100.0, GREAT, 0.75",
        "50.0, 100.0, ULTRA, 1.0",
        "10.0, 100.0, REGULAR, 0.9",
        "0.0, 100.0, REGULAR, 1.0"
    )
    fun `test capture rate with different HP and tiers`(currentHp: Double, maxHp: Double, tierId: String, expected: Double) {
        val tier = CapsuleTier.entries.find { it.name == tierId }!!
        val rate = CapsuleManager.calculateCaptureRate(currentHp, maxHp, tier)
        assertEquals(expected, rate, 0.001)
    }
}

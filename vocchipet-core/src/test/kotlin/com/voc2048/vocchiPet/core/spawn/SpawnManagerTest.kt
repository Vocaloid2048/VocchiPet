package com.voc2048.vocchiPet.core.spawn

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.RepeatedTest

class SpawnManagerTest {

    @RepeatedTest(1000)
    fun testSelectByWeight() {
        val items = mapOf(
            "common" to 80,
            "rare" to 20
        )
        
        val results = mutableListOf<String>()
        repeat(1000) {
            SpawnManager.selectByWeight(items)?.let { results.add(it) }
        }
        
        val commonCount = results.count { it == "common" }
        // 驗證比例大致正確 (80% +/- 5%)
        assertTrue(commonCount in 750..850, "Common 權重分佈異常: $commonCount")
    }
}

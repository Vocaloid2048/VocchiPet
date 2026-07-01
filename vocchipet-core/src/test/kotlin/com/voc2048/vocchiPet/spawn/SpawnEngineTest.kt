package com.voc2048.vocchiPet.spawn

import com.voc2048.vocchipet.api.Rarity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * 測試生成引擎的機率計算邏輯。
 * Tests the probability calculation logic of the spawn engine.
 */
class SpawnEngineTest {

    private lateinit var engine: SpawnEngine

    @BeforeEach
    fun setUp() {
        val plainsConfig = BiomeSpawnConfig(
            rarityChances = mapOf(
                Rarity.COMMON to 70.0,
                Rarity.UNCOMMON to 20.0,
                Rarity.RARE to 10.0
            ),
            spawnGroups = mapOf(
                Rarity.COMMON to mapOf("slime_green" to 100, "rabbit_brown" to 50),
                Rarity.UNCOMMON to mapOf("fox_red" to 10),
                Rarity.RARE to mapOf("wolf_grey" to 5)
            )
        )

        val desertConfig = BiomeSpawnConfig(
            rarityChances = mapOf(
                Rarity.COMMON to 100.0
            ),
            spawnGroups = mapOf(
                Rarity.COMMON to mapOf("scorpio_sand" to 1)
            )
        )

        engine = SpawnEngine(
            mapOf(
                "PLAINS" to plainsConfig,
                "DESERT" to desertConfig
            )
        )
    }

    @Test
    @DisplayName("已配置生態域應返回有效寵物 ID / Configured biome should return valid pet ID")
    fun testConfiguredBiomeReturnsPetId() {
        val petId = engine.calculateSpawn("PLAINS")
        assertNotNull(petId)
        assertTrue(
            petId in listOf("slime_green", "rabbit_brown", "fox_red", "wolf_grey"),
            "返回的寵物 ID 應在配置範圍內"
        )
    }

    @Test
    @DisplayName("未配置生態域應返回 null / Unconfigured biome should return null")
    fun testUnconfiguredBiomeReturnsNull() {
        assertNull(engine.calculateSpawn("OCEAN"))
    }

    @Test
    @DisplayName("單一寵物權重生態域應始終返回該寵物 / Single pet biome should always return that pet")
    fun testSinglePetAlwaysReturned() {
        repeat(10) {
            assertEquals("scorpio_sand", engine.calculateSpawn("DESERT"))
        }
    }

    @Test
    @DisplayName("獲取生態域配置應正確 / Getting biome config should be correct")
    fun testGetBiomeConfig() {
        val plainsConfig = engine.getBiomeConfig("PLAINS")
        assertNotNull(plainsConfig)
        assertEquals(3, plainsConfig!!.rarityChances.size)
        assertTrue(plainsConfig.rarityChances.containsKey(Rarity.COMMON))

        val oceanConfig = engine.getBiomeConfig("OCEAN")
        assertNull(oceanConfig)
    }

    @Test
    @DisplayName("機率分佈統計驗證 / Probability distribution statistical validation")
    fun testProbabilityDistribution() {
        val counts = mutableMapOf<String, Int>()
        val iterations = 5000

        repeat(iterations) {
            val petId = engine.calculateSpawn("PLAINS") ?: "unknown"
            counts[petId] = counts.getOrDefault(petId, 0) + 1
        }

        // 稀有度機率：COMMON 70%, UNCOMMON 20%, RARE 10%
        // COMMON 內：slime_green 權重 100, rabbit_brown 權重 50
        //   slime_green 機率 = 70% * (100/150) = 46.67%
        //   rabbit_brown 機率 = 70% * (50/150) = 23.33%
        // UNCOMMON 內：fox_red 100% -> 20%
        // RARE 內：wolf_grey 100% -> 10%

        val slimeRate = counts["slime_green"]!!.toDouble() / iterations
        val rabbitRate = counts["rabbit_brown"]!!.toDouble() / iterations
        val foxRate = counts["fox_red"]!!.toDouble() / iterations
        val wolfRate = counts["wolf_grey"]!!.toDouble() / iterations

        // 允許 ±6% 的誤差（因為樣本數較小）/ Allow ±6% deviation
        assertTrue(slimeRate in 0.40..0.55, "slime_green 機率應約 46.7%，實際為 $slimeRate")
        assertTrue(rabbitRate in 0.18..0.30, "rabbit_brown 機率應約 23.3%，實際為 $rabbitRate")
        assertTrue(foxRate in 0.14..0.26, "fox_red 機率應約 20%，實際為 $foxRate")
        assertTrue(wolfRate in 0.04..0.16, "wolf_grey 機率應約 10%，實際為 $wolfRate")
    }
}

package com.voc2048.vocchiPet.spawn

import com.voc2048.vocchipet.api.Rarity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.api.Assumptions.assumeTrue
import java.io.File

/**
 * 測試 SpawnerConfigLoader 的 YAML 配置加載邏輯。
 * Tests the YAML configuration loading logic of SpawnerConfigLoader.
 */
class SpawnerConfigLoaderTest {

    @Test
    @DisplayName("加載有效 spawner.yml 應正確解析 / Loading valid spawner.yml should parse correctly")
    fun testLoadValidConfig(@TempDir tempDir: File) {
        val configFile = File(tempDir, "spawner.yml")
        configFile.writeText(
            """
            biomes:
              PLAINS:
                rarity-chances:
                  COMMON: 70.0
                  UNCOMMON: 20.0
                  RARE: 10.0
                spawn-groups:
                  COMMON:
                    slime_green: 100
                    rabbit_brown: 50
                  UNCOMMON:
                    fox_red: 10
                  RARE:
                    wolf_grey: 5
            """.trimIndent()
        )

        val configs = SpawnerConfigLoader.load(configFile)
        assertEquals(1, configs.size)
        assertTrue(configs.containsKey("PLAINS"))

        val plainsConfig = configs["PLAINS"]!!
        assertEquals(3, plainsConfig.rarityChances.size)
        assertEquals(70.0, plainsConfig.rarityChances[Rarity.COMMON]!!, 0.0001)
        assertEquals(20.0, plainsConfig.rarityChances[Rarity.UNCOMMON]!!, 0.0001)
        assertEquals(10.0, plainsConfig.rarityChances[Rarity.RARE]!!, 0.0001)

        assertEquals(3, plainsConfig.spawnGroups.size)
        assertEquals(2, plainsConfig.spawnGroups[Rarity.COMMON]?.size)
        assertEquals(100, plainsConfig.spawnGroups[Rarity.COMMON]?.get("slime_green"))
        assertEquals(50, plainsConfig.spawnGroups[Rarity.COMMON]?.get("rabbit_brown"))
        assertEquals(10, plainsConfig.spawnGroups[Rarity.UNCOMMON]?.get("fox_red"))
        assertEquals(5, plainsConfig.spawnGroups[Rarity.RARE]?.get("wolf_grey"))
    }

    @Test
    @DisplayName("空 biomes 區塊應返回空 Map / Empty biomes section should return empty map")
    fun testEmptyBiomesSection(@TempDir tempDir: File) {
        val configFile = File(tempDir, "spawner.yml")
        configFile.writeText("biomes:")
        val configs = SpawnerConfigLoader.load(configFile)
        assertTrue(configs.isEmpty())
    }

    @Test
    @DisplayName("無效生態域名稱應被忽略 / Invalid biome names should be ignored")
    fun testInvalidBiomeNamesIgnored(@TempDir tempDir: File) {
        // 若 Bukkit 環境不可用則跳過此測試（如單元測試環境）
        // Skip this test if Bukkit environment is unavailable (e.g., unit test environment)
        val bukkitAvailable = try {
            Class.forName("org.bukkit.block.Biome")
            true
        } catch (e: Throwable) {
            false
        }
        assumeTrue(bukkitAvailable, "Bukkit 環境不可用，跳過生態域驗證測試")

        val configFile = File(tempDir, "spawner.yml")
        configFile.writeText(
            """
            biomes:
              INVALID_BIOME_NAME:
                rarity-chances:
                  COMMON: 100.0
                spawn-groups:
                  COMMON:
                    test_pet: 1
              PLAINS:
                rarity-chances:
                  COMMON: 100.0
                spawn-groups:
                  COMMON:
                    valid_pet: 1
            """.trimIndent()
        )

        val configs = SpawnerConfigLoader.load(configFile)
        assertEquals(1, configs.size)
        assertTrue(configs.containsKey("PLAINS"))
        assertEquals("valid_pet", configs["PLAINS"]!!.spawnGroups[Rarity.COMMON]!!.keys.first())
    }

    @Test
    @DisplayName("無效稀有度名稱應被忽略 / Invalid rarity names should be ignored")
    fun testInvalidRarityNamesIgnored(@TempDir tempDir: File) {
        val configFile = File(tempDir, "spawner.yml")
        configFile.writeText(
            """
            biomes:
              PLAINS:
                rarity-chances:
                  COMMON: 50.0
                  INVALID_RARITY: 50.0
                spawn-groups:
                  COMMON:
                    valid_pet: 1
                  INVALID_RARITY:
                    invalid_pet: 1
            """.trimIndent()
        )

        val configs = SpawnerConfigLoader.load(configFile)
        val plainsConfig = configs["PLAINS"]!!
        assertEquals(1, plainsConfig.rarityChances.size)
        assertEquals(50.0, plainsConfig.rarityChances[Rarity.COMMON]!!, 0.0001)
        assertEquals(1, plainsConfig.spawnGroups.size)
        assertTrue(plainsConfig.spawnGroups[Rarity.COMMON]?.containsKey("valid_pet") == true)
    }

    @Test
    @DisplayName("多生態域配置應正確解析 / Multiple biome configs should parse correctly")
    fun testMultipleBiomes(@TempDir tempDir: File) {
        val configFile = File(tempDir, "spawner.yml")
        configFile.writeText(
            """
            biomes:
              PLAINS:
                rarity-chances:
                  COMMON: 100.0
                spawn-groups:
                  COMMON:
                    plains_pet: 1
              DESERT:
                rarity-chances:
                  COMMON: 100.0
                spawn-groups:
                  COMMON:
                    desert_pet: 1
              OCEAN:
                rarity-chances:
                  COMMON: 100.0
                spawn-groups:
                  COMMON:
                    ocean_pet: 1
            """.trimIndent()
        )

        val configs = SpawnerConfigLoader.load(configFile)
        assertEquals(3, configs.size)
        assertTrue(configs.containsKey("PLAINS"))
        assertTrue(configs.containsKey("DESERT"))
        assertTrue(configs.containsKey("OCEAN"))
    }

    @Test
    @DisplayName("機率加總驗證：100% 分佈 / Probability sum validation: 100% distribution")
    fun testProbabilitySumValidation(@TempDir tempDir: File) {
        val configFile = File(tempDir, "spawner.yml")
        configFile.writeText(
            """
            biomes:
              PLAINS:
                rarity-chances:
                  COMMON: 50.0
                  UNCOMMON: 30.0
                  RARE: 15.0
                  EPIC: 4.0
                  LEGENDARY: 0.9
                  MYTHICAL: 0.1
                spawn-groups:
                  COMMON:
                    pet_a: 50
                    pet_b: 50
                  UNCOMMON:
                    pet_c: 30
                    pet_d: 20
                  RARE:
                    pet_e: 10
                    pet_f: 5
                  EPIC:
                    pet_g: 3
                    pet_h: 2
                  LEGENDARY:
                    pet_i: 1
                  MYTHICAL:
                    pet_j: 1
            """.trimIndent()
        )

        val configs = SpawnerConfigLoader.load(configFile)
        val plainsConfig = configs["PLAINS"]!!

        // 驗證稀有度機率加總為 100% / Verify rarity chances sum to 100%
        val totalChance = plainsConfig.rarityChances.values.sum()
        assertEquals(100.0, totalChance, 0.0001, "稀有度機率總和應為 100%")

        // 驗證每個稀有度內的寵物權重都存在 / Verify pet weights exist in each rarity
        assertEquals(2, plainsConfig.spawnGroups[Rarity.COMMON]?.size)
        assertEquals(2, plainsConfig.spawnGroups[Rarity.UNCOMMON]?.size)
        assertEquals(2, plainsConfig.spawnGroups[Rarity.RARE]?.size)
        assertEquals(2, plainsConfig.spawnGroups[Rarity.EPIC]?.size)
        assertEquals(1, plainsConfig.spawnGroups[Rarity.LEGENDARY]?.size)
        assertEquals(1, plainsConfig.spawnGroups[Rarity.MYTHICAL]?.size)
    }
}

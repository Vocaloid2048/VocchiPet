package com.voc2048.vocchipet.core

import com.voc2048.vocchipet.api.*
import org.bukkit.entity.EntityType

/**
 * 測試寵物數據定義。
 * Test pet data definitions.
 */
object TestData {

    /**
     * 獲取預設的 5 隻測試寵物種類。
     * Gets the default 5 test pet species.
     *
     * @return 寵物種類列表 / A list of pet species.
     */
    fun getTestSpecies(): List<PetSpecies> {
        return listOf(
            PetSpecies(
                "fire_dragon",
                "§c炙焰紅龍",
                EntityType.BLAZE,
                createBaseStats(120, 15, 10, 8, 5)
            ),
            PetSpecies(
                "water_turtle",
                "§b深海玄武",
                EntityType.TURTLE,
                createBaseStats(150, 8, 20, 5, 7)
            ),
            PetSpecies(
                "grass_spirit",
                "§a翡翠精靈",
                EntityType.SNIFFER,
                createBaseStats(80, 10, 8, 15, 12)
            ),
            PetSpecies(
                "light_angel",
                "§e聖光天使",
                EntityType.BEE,
                createBaseStats(100, 12, 12, 12, 15)
            ),
            PetSpecies(
                "dark_demon",
                "§8影裔魔王",
                EntityType.WITHER_SKELETON,
                createBaseStats(110, 18, 5, 10, 8)
            )
        )
    }

    private fun createBaseStats(hp: Int, atk: Int, def: Int, spd: Int, foc: Int): PetStats {
        return PetStats(
            hp = StatComponent(hp, Tier.TIER_0),
            attack = StatComponent(atk, Tier.TIER_0),
            defense = StatComponent(def, Tier.TIER_0),
            speed = StatComponent(spd, Tier.TIER_0),
            focus = StatComponent(foc, Tier.TIER_0),
            skills = arrayOfNulls<String>(4)
        )
    }
}

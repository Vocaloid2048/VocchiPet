package com.voc2048.vocchipet.core

import com.voc2048.vocchipet.api.*
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

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
                Element.FIRE,
                createBaseStats(120, 15, 10, 8, 5),
                ItemStack(Material.BLAZE_SPAWN_EGG)
            ),
            PetSpecies(
                "water_turtle",
                "§b深海玄武",
                Element.WATER,
                createBaseStats(150, 8, 20, 5, 7),
                ItemStack(Material.TURTLE_SPAWN_EGG)
            ),
            PetSpecies(
                "grass_spirit",
                "§a翡翠精靈",
                Element.GRASS,
                createBaseStats(80, 10, 8, 15, 12),
                ItemStack(Material.SNIFFER_SPAWN_EGG)
            ),
            PetSpecies(
                "light_angel",
                "§e聖光天使",
                Element.LIGHT,
                createBaseStats(100, 12, 12, 12, 15),
                ItemStack(Material.BEE_SPAWN_EGG)
            ),
            PetSpecies(
                "dark_demon",
                "§8影裔魔王",
                Element.DARK,
                createBaseStats(110, 18, 5, 10, 8),
                ItemStack(Material.WITHER_SKELETON_SPAWN_EGG)
            )
        )
    }

    private fun createBaseStats(hp: Int, atk: Int, def: Int, spd: Int, foc: Int): PetStats {
        return PetStats(
            hp = StatComponent(hp, Tier.D, 0),
            attack = StatComponent(atk, Tier.D, 0),
            defense = StatComponent(def, Tier.D, 0),
            speed = StatComponent(spd, Tier.D, 0),
            focus = StatComponent(foc, Tier.D, 0),
            availableTp = 0,
            skills = arrayOfNulls<String>(6)
        )
    }
}

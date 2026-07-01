package com.voc2048.vocchipet.core

import com.voc2048.vocchipet.api.Element
import com.voc2048.vocchipet.api.PetStats
import com.voc2048.vocchipet.api.StatComponent
import com.voc2048.vocchipet.api.Tier

/**
 * 測試寵物數據定義。
 * Test pet data definitions.
 */
object TestData {

    /**
     * 獲取預設的 5 隻測試寵物屬性。
     * Gets the default 5 test pet stats.
     *
     * @return 屬性映射 / A map of pet names to their stats.
     */
    fun getTestPetStats(): Map<String, PetStats> {
        return mapOf(
            "FireDragon" to createBaseStats(Element.FIRE),
            "WaterTurtle" to createBaseStats(Element.WATER),
            "GrassSpirit" to createBaseStats(Element.GRASS),
            "LightAngel" to createBaseStats(Element.LIGHT),
            "DarkDemon" to createBaseStats(Element.DARK)
        )
    }

    private fun createBaseStats(element: Element): PetStats {
        return PetStats(
            hp = StatComponent(100, Tier.D, 0),
            attack = StatComponent(10, Tier.D, 0),
            defense = StatComponent(10, Tier.D, 0),
            speed = StatComponent(10, Tier.D, 0),
            focus = StatComponent(10, Tier.D, 0),
            availableTp = 0,
            skills = arrayOfNulls<String>(6)
        )
    }
}

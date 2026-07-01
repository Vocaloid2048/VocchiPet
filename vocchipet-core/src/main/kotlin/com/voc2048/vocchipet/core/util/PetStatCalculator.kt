package com.voc2048.vocchipet.core.util

import com.voc2048.vocchipet.api.Pet
import com.voc2048.vocchipet.api.StatComponent
import com.voc2048.vocchipet.api.Tier

/**
 * 寵物屬性計算工具類。
 * Utility class for pet stat calculation.
 */
object PetStatCalculator {

    private const val HP_COEFFICIENT = 10.0
    private const val ATTACK_COEFFICIENT = 2.0
    private const val DEFENSE_COEFFICIENT = 2.0
    private const val SPEED_COEFFICIENT = 1.0
    private const val FOCUS_COEFFICIENT = 1.0

    /**
     * 計算實際屬性值。
     * Calculates the actual stat value.
     * 公式: (基礎屬性 + 先天階級 * 係數) * 等級加成
     * Formula: (Base + Tier * Coefficient) * Level Factor
     *
     * @param component 屬性組件 / Stat component.
     * @param level 等級 / Level.
     * @param coefficient 階級修正係數 / Tier modification coefficient.
     * @return 實際屬性值 / Actual stat value.
     */
    fun calculateStat(component: StatComponent, level: Int, coefficient: Double): Double {
        val tierValue = component.potential.ordinal // TIER_0 to TIER_15
        val baseValue = component.base.toDouble()
        val levelFactor = 1.0 + (level - 1) * 0.1
        return (baseValue + tierValue * coefficient) * levelFactor
    }

    /**
     * 計算最大生命值。
     * Calculates max HP.
     *
     * @param pet 寵物實例 / Pet instance.
     * @return 最大生命值 / Max HP.
     */
    fun calculateMaxHp(pet: Pet): Double {
        return calculateStat(pet.getStats().hp, pet.getLevel(), HP_COEFFICIENT)
    }

    /**
     * 計算攻擊力。
     * Calculates attack power.
     *
     * @param pet 寵物實例 / Pet instance.
     * @return 攻擊力 / Attack power.
     */
    fun calculateAttack(pet: Pet): Double {
        return calculateStat(pet.getStats().attack, pet.getLevel(), ATTACK_COEFFICIENT)
    }

    /**
     * 計算防禦力。
     * Calculates defense power.
     *
     * @param pet 寵物實例 / Pet instance.
     * @return 防禦力 / Defense power.
     */
    fun calculateDefense(pet: Pet): Double {
        return calculateStat(pet.getStats().defense, pet.getLevel(), DEFENSE_COEFFICIENT)
    }
}

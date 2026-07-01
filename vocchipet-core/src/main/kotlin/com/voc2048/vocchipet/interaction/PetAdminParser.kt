package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.PetImpl
import com.voc2048.vocchipet.api.Element
import com.voc2048.vocchipet.api.PetSpecies
import com.voc2048.vocchipet.api.PetStats
import com.voc2048.vocchipet.api.Tier
import java.util.UUID

/**
 * 用於解析管理員指令參數的工具類。
 * Utility class for parsing admin command parameters.
 */
object PetAdminParser {

    /**
     * 解析參數字串為鍵值對。
     * Parses parameter string into key-value pairs.
     *
     * @param paramStr 參數字串 / Parameter string.
     * @return 參數 Map / Parameter map.
     */
    fun parseParameters(paramStr: String): Map<String, String> {
        val result = mutableMapOf<String, String>()
        val pairs = paramStr.split(" ")
        for (pair in pairs) {
            if (pair.isBlank()) continue
            val kv = pair.split("=")
            val key = kv[0].lowercase()
            if (kv.size == 1) {
                result[key] = "true"
            } else {
                result[key] = kv[1]
            }
        }
        return result
    }

    /**
     * 根據鍵值對字串解析並創建寵物實例。
     * Parses and creates a pet instance based on a key-value pair string.
     *
     * @param species 寵物種類 / Pet species.
     * @param ownerId 主人 UUID / Owner UUID.
     * @param paramStr 參數字串 / Parameter string.
     * @return 寵物實例 / Pet instance.
     */
    fun parseAndCreatePet(species: PetSpecies, ownerId: UUID, paramStr: String): PetImpl {
        val params = parseParameters(paramStr)
        
        var level = params["level"]?.toIntOrNull() ?: 1
        val tiers = Tier.entries.toTypedArray()
        
        fun getTier(key: String) = params[key]?.uppercase()?.let { try { Tier.valueOf(it) } catch(e: Exception) { null } } ?: tiers.random()
        fun getAt(key: String) = params[key]?.toIntOrNull() ?: 0

        val itHp = getTier("it_hp")
        val itAtk = getTier("it_atk")
        val itDef = getTier("it_def")
        val itSpd = getTier("it_spd")
        val itFcs = params["it_fcs"]?.let { getTier("it_fcs") } ?: getTier("it_focus")
        
        val atHp = getAt("at_hp")
        val atAtk = getAt("at_atk")
        val atDef = getAt("at_def")
        val atSpd = getAt("at_spd")
        val atFcs = params["at_fcs"]?.let { getAt("at_fcs") } ?: getAt("at_focus")

        var element = params["element"]?.uppercase()?.let { try { Element.valueOf(it) } catch(e: Exception) { null } } ?: species.element
        var streaming = params["shinny"]?.toBoolean() ?: params["shiny"]?.toBoolean() ?: false

        val baseStats = species.baseStats
        val stats = PetStats(
            hp = baseStats.hp.copy(potential = itHp, trained = atHp),
            attack = baseStats.attack.copy(potential = itAtk, trained = atAtk),
            defense = baseStats.defense.copy(potential = itDef, trained = atDef),
            speed = baseStats.speed.copy(potential = itSpd, trained = atSpd),
            focus = baseStats.focus.copy(potential = itFcs, trained = atFcs),
            availableTp = 0,
            skills = arrayOfNulls(6)
        )

        return PetImpl(
            uuid = UUID.randomUUID(),
            ownerId = ownerId,
            tamerId = ownerId,
            type = species.id,
            level = level,
            exp = 0,
            affection = 20.0,
            element = element,
            stats = stats,
            streaming = streaming
        )
    }
}

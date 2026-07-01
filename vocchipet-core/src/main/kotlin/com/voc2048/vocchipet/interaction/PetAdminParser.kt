package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.PetImpl
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
        
        val level = params["level"]?.toIntOrNull() ?: 1
        val tiers = Tier.entries.toTypedArray()
        
        fun getTier(key: String): Tier {
            val value = params[key] ?: return tiers.random()
            // 嘗試解析為數字 (1-16)
            val num = value.toIntOrNull()
            if (num != null) {
                val index = (num - 1).coerceIn(0, 15)
                return tiers[index]
            }
            // 嘗試解析為 Enum 名稱
            return try { Tier.valueOf(value.uppercase()) } catch(e: Exception) { tiers.random() }
        }

        val itHp = getTier("it_hp")
        val itAtk = getTier("it_atk")
        val itDef = getTier("it_def")
        val itSpd = getTier("it_spd")
        val itFcs = if (params.containsKey("it_fcs")) getTier("it_fcs") else getTier("it_focus")
        
        val subspecies = params["sub"]?.toBoolean() ?: params["subspecies"]?.toBoolean() ?: false
        val name = params["name"] ?: species.displayName

        val baseStats = species.baseStats
        val stats = PetStats(
            hp = baseStats.hp.copy(potential = itHp),
            attack = baseStats.attack.copy(potential = itAtk),
            defense = baseStats.defense.copy(potential = itDef),
            speed = baseStats.speed.copy(potential = itSpd),
            focus = baseStats.focus.copy(potential = itFcs),
            skills = arrayOfNulls(4)
        )

        return PetImpl(
            uuid = UUID.randomUUID(),
            ownerId = ownerId,
            tamerId = ownerId,
            type = species.id,
            name = name,
            level = level,
            exp = 0,
            affection = 20.0,
            stats = stats,
            subspecies = subspecies
        )
    }
}

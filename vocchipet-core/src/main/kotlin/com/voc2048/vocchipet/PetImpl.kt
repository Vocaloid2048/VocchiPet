package com.voc2048.vocchipet

import com.voc2048.vocchipet.api.*
import java.util.UUID

/**
 * 寵物介面的基本實作。
 * Basic implementation of the Pet interface.
 *
 * @property uuid 寵物的唯一識別碼 / The unique identifier of the pet.
 * @property ownerId 當前主人的唯一識別碼 / The unique identifier of the current owner.
 * @property tamerId 最初馴養者的唯一識別碼 / The unique identifier of the original tamer.
 * @property type 寵物類型 / The type of the pet.
 * @property level 寵物等級 / The level of the pet.
 * @property exp 寵物經驗值 / The experience points of the pet.
 * @property affection 寵物好感度 / The affection level of the pet.
 * @property element 寵物元素屬性 / The elemental attribute of the pet.
 * @property stats 寵物屬性結構 / The stats structure of the pet.
 */
data class PetImpl(
    private val uuid: UUID,
    private var ownerId: UUID,
    private val tamerId: UUID,
    private val type: String,
    private var level: Int,
    private var exp: Int,
    private var affection: Double,
    private val element: Element,
    private val stats: PetStats
) : Pet {

    companion object {
        /**
         * 根據種類與資質創建新的寵物實例。
         * Creates a new pet instance based on species and quality.
         *
         * @param species 寵物種類 / Pet species.
         * @param ownerId 主人 UUID / Owner UUID.
         * @param quality 初始資質 / Initial quality.
         * @return 寵物實例 / Pet instance.
         */
        fun create(species: PetSpecies, ownerId: UUID, quality: Tier): PetImpl {
            val baseStats = species.baseStats
            val stats = PetStats(
                hp = baseStats.hp.copy(potential = quality),
                attack = baseStats.attack.copy(potential = quality),
                defense = baseStats.defense.copy(potential = quality),
                speed = baseStats.speed.copy(potential = quality),
                focus = baseStats.focus.copy(potential = quality),
                availableTp = 0,
                skills = arrayOfNulls(6)
            )
            return PetImpl(
                uuid = UUID.randomUUID(),
                ownerId = ownerId,
                tamerId = ownerId,
                type = species.id,
                level = 1,
                exp = 0,
                affection = 20.0, // 初始好感度：普通
                element = species.element,
                stats = stats
            )
        }
    }

    override fun getUniqueId(): UUID = uuid

    override fun getOwnerId(): UUID = ownerId

    override fun setOwnerId(ownerId: UUID) {
        this.ownerId = ownerId
    }

    override fun getTamerId(): UUID = tamerId

    override fun getType(): String = type

    override fun getLevel(): Int = level

    override fun getExp(): Int = exp

    override fun getAffection(): Double = affection

    override fun setAffection(affection: Double) {
        this.affection = affection
    }

    override fun getElement(): Element = element

    override fun getStats(): PetStats = stats
}

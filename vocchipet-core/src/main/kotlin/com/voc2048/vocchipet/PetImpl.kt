package com.voc2048.vocchipet

import com.voc2048.vocchipet.api.*
import java.util.UUID

/**
 * 寵物介面的基本實作。
 * Basic implementation of the Pet interface.
 */
data class PetImpl(
    private val uuid: UUID,
    private var ownerId: UUID,
    private val tamerId: UUID,
    private val type: String,
    private var name: String,
    private var level: Int,
    private var exp: Int,
    private var affection: Double,
    private var stats: PetStats,
    private var subspecies: Boolean = false
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
                skills = arrayOfNulls(4)
            )
            return PetImpl(
                uuid = UUID.randomUUID(),
                ownerId = ownerId,
                tamerId = ownerId,
                type = species.id,
                name = species.displayName,
                level = 1,
                exp = 0,
                affection = 20.0,
                stats = stats,
                subspecies = false
            )
        }
    }

    override fun getName(): String = name

    override fun setName(name: String) {
        this.name = name
    }

    override fun getUniqueId(): UUID = uuid

    override fun getOwnerId(): UUID = ownerId

    override fun setOwnerId(ownerId: UUID) {
        this.ownerId = ownerId
    }

    override fun getTamerId(): UUID = tamerId

    override fun getType(): String = type

    override fun getLevel(): Int = level

    fun setLevel(level: Int) {
        this.level = level
    }

    override fun getExp(): Int = exp

    fun setExp(exp: Int) {
        this.exp = exp
    }

    override fun getAffection(): Double = affection

    override fun setAffection(affection: Double) {
        this.affection = affection
    }

    override fun getStats(): PetStats = stats

    override fun isSubspecies(): Boolean = subspecies

    override fun setSubspecies(subspecies: Boolean) {
        this.subspecies = subspecies
    }
}

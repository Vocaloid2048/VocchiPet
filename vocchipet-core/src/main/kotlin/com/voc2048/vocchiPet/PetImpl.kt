package com.voc2048.vocchiPet

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

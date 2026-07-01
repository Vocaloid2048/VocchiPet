package com.voc2048.vocchiPet

import com.voc2048.vocchipet.api.Element
import com.voc2048.vocchipet.api.Pet
import java.util.UUID

/**
 * 寵物介面的基本實作。
 * Basic implementation of the Pet interface.
 *
 * @property uuid 寵物的唯一識別碼 / The unique identifier of the pet.
 * @property ownerId 主人的唯一識別碼 / The unique identifier of the owner.
 * @property type 寵物類型 / The type of the pet.
 * @property level 寵物等級 / The level of the pet.
 * @property exp 寵物經驗值 / The experience points of the pet.
 * @property affection 寵物好感度 / The affection level of the pet.
 * @property element 寵物元素屬性 / The elemental attribute of the pet.
 */
data class PetImpl(
    private val uuid: UUID,
    private val ownerId: UUID,
    private val type: String,
    private var level: Int,
    private var exp: Int,
    private var affection: Double,
    private val element: Element
) : Pet {

    override fun getUniqueId(): UUID = uuid

    override fun getOwnerId(): UUID = ownerId

    override fun getType(): String = type

    override fun getLevel(): Int = level

    override fun getExp(): Int = exp

    override fun getAffection(): Double = affection

    override fun getElement(): Element = element
}

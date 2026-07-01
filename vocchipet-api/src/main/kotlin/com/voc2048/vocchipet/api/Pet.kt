package com.voc2048.vocchipet.api

import java.util.UUID

/**
 * 代表遊戲中的寵物實例。
 * Represents a pet instance in the game.
 */
interface Pet {

    /**
     * 獲取寵物的唯一識別碼。
     * Gets the unique identifier (UUID) of the pet.
     *
     * @return 寵物的 UUID / The UUID of the pet.
     */
    fun getUniqueId(): UUID

    /**
     * 獲取主人的唯一識別碼。
     * Gets the unique identifier (UUID) of the owner.
     *
     * @return 主人的 UUID / The UUID of the owner.
     */
    fun getOwnerId(): UUID

    /**
     * 獲取寵物的類型。
     * Gets the type of the pet.
     *
     * @return 寵物類型名稱 / The type name of the pet.
     */
    fun getType(): String

    /**
     * 獲取寵物的等級。
     * Gets the level of the pet.
     *
     * @return 寵物等級 / The level of the pet.
     */
    fun getLevel(): Int

    /**
     * 獲取寵物的經驗值。
     * Gets the experience points of the pet.
     *
     * @return 寵物經驗值 / The experience points of the pet.
     */
    fun getExp(): Int

    /**
     * 獲取寵物的當前好感度。
     * Gets the current affection level of the pet.
     *
     * @return 寵物的好感度數值 / The affection level value of the pet.
     */
    fun getAffection(): Double

    /**
     * 獲取寵物的元素屬性。
     * Gets the elemental attribute of the pet.
     *
     * @return 寵物的屬性類型 / The attribute type of the pet.
     */
    fun getElement(): Element
}

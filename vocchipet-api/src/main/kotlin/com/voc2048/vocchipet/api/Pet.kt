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
     * 獲取寵物的顯示名稱。
     * Gets the display name of the pet.
     *
     * @return 寵物的顯示名稱 / The display name of the pet.
     */
    fun getName(): String

    /**
     * 獲取寵物的當前好感度（0-100）。
     * Gets the current affection level of the pet (0-100).
     *
     * @return 寵物的好感度數值 / The affection level value of the pet.
     */
    fun getAffection(): Int

    /**
     * 獲取寵物的元素屬性。
     * Gets the elemental attribute of the pet.
     *
     * @return 寵物的屬性類型 / The attribute type of the pet.
     */
    fun getElement(): Element
}

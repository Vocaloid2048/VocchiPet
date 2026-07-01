package com.voc2048.vocchipet.api

import java.util.UUID

/**
 * 代表遊戲中的寵物實例。
 * Represents a pet instance in the game.
 */
interface Pet {
    /**
     * 獲取寵物的顯示名稱。
     * Gets the display name of the pet.
     *
     * @return 寵物名稱 / The name of the pet.
     */
    fun getName(): String

    /**
     * 設定寵物的顯示名稱。
     * Sets the display name of the pet.
     *
     * @param name 新名稱 / The new name.
     */
    fun setName(name: String)

    /**
     * 獲取寵物的屬性數值。
     * Gets the stats of the pet.
     */
    fun getStats(): PetStats

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
     * 設定新的主人。
     * Sets a new owner for the pet.
     *
     * @param ownerId 新主人的 UUID / The UUID of the new owner.
     */
    fun setOwnerId(ownerId: UUID)

    /**
     * 獲取最初馴養者（捕捉者）的唯一識別碼。
     * Gets the unique identifier (UUID) of the original tamer (capturer).
     *
     * @return 馴養者的 UUID / The UUID of the tamer.
     */
    fun getTamerId(): UUID

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
     * 設定寵物的好感度。
     * Sets the affection level of the pet.
     *
     * @param affection 新的好感度數值 / The new affection level value.
     */
    fun setAffection(affection: Double)

    /**
     * 獲取寵物是否為特殊亞種（如流光或染色）。
     * Gets whether the pet is a special subspecies (e.g., shiny or colored).
     *
     * @return 若為特殊亞種則返回 true / True if it is a special subspecies.
     */
    fun isSubspecies(): Boolean

    /**
     * 設定寵物的亞種狀態。
     * Sets the subspecies status of the pet.
     *
     * @param subspecies 是否為亞種 / Whether it is a subspecies.
     */
    fun setSubspecies(subspecies: Boolean)
}

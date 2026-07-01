package com.voc2048.vocchipet.core

import com.voc2048.vocchipet.api.Pet
import java.util.UUID

/**
 * 管理玩家與其召喚寵物關係的類別。
 * Class for managing the relationship between players and their summoned pets.
 */
class PetManager {
    // 玩家 UUID -> 召喚中的寵物
    private val summonedPets = mutableMapOf<UUID, Pet>()

    /**
     * 設定玩家當前召喚的寵物。
     * Sets the pet currently summoned by the player.
     *
     * @param playerID 玩家 UUID / Player UUID.
     * @param pet 寵物實例 / Pet instance.
     */
    fun setSummonedPet(playerID: UUID, pet: Pet) {
        summonedPets[playerID] = pet
    }

    /**
     * 獲取玩家當前召喚的寵物。
     * Gets the pet currently summoned by the player.
     *
     * @param playerID 玩家 UUID / Player UUID.
     * @return 寵物實例，若無召喚則為 null / Pet instance, or null if none.
     */
    fun getSummonedPet(playerID: UUID): Pet? = summonedPets[playerID]

    /**
     * 移除玩家當前召喚的寵物。
     * Removes the pet currently summoned by the player.
     *
     * @param playerID 玩家 UUID / Player UUID.
     */
    fun removeSummonedPet(playerID: UUID) {
        summonedPets.remove(playerID)
    }
}

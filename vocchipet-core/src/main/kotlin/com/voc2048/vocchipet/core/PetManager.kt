package com.voc2048.vocchipet.core

import com.voc2048.vocchipet.api.Pet
import org.bukkit.entity.Mob
import java.util.UUID

/**
 * 管理玩家與其召喚寵物關係的類別。
 * Class for managing the relationship between players and their summoned pets.
 */
class PetManager(private val plugin: com.voc2048.vocchipet.VocchiPet) {
    // 玩家 UUID -> 召喚中的寵物實例
    private val summonedPets = mutableMapOf<UUID, com.voc2048.vocchipet.api.Pet>()
    
    // 玩家 UUID -> 召喚出的實體
    private val summonedEntities = mutableMapOf<UUID, Mob>()
    
    // 玩家 UUID -> AI 任務 ID
    private val aiTasks = mutableMapOf<UUID, Int>()

    /**
     * 設定玩家當前召喚的寵物。
     * Sets the pet currently summoned by the player.
     *
     * @param playerID 玩家 UUID / Player UUID.
     * @param pet 寵物實例 / Pet instance.
     * @param entity 召喚出的實體 / The summoned entity.
     * @param taskId AI 任務 ID / AI task ID.
     */
    fun setSummonedPet(playerID: UUID, pet: Pet, entity: Mob, taskId: Int) {
        // 如果原本已有召喚，先收回
        removeSummonedPet(playerID)
        
        summonedPets[playerID] = pet
        summonedEntities[playerID] = entity
        aiTasks[playerID] = taskId
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
     * 獲取玩家當前召喚的實體。
     * Gets the entity currently summoned by the player.
     *
     * @param playerID 玩家 UUID / Player UUID.
     * @return 實體實例，若無召喚則為 null / Entity instance, or null if none.
     */
    fun getSummonedEntity(playerID: UUID): Mob? = summonedEntities[playerID]

    /**
     * 移除玩家當前召喚的寵物與實體，並保存當前狀態。
     * Removes the pet and entity currently summoned by the player and saves the current state.
     *
     * @param playerID 玩家 UUID / Player UUID.
     */
    fun removeSummonedPet(playerID: UUID) {
        val pet = summonedPets.remove(playerID)
        val entity = summonedEntities.remove(playerID)
        
        if (pet != null && entity != null) {
            // 保存當前血量
            pet.setCurrentHp(entity.health)
            // 觸發非同步保存
            plugin.getPetStorage().savePet(pet)
        }
        
        entity?.remove()
        aiTasks.remove(playerID)?.let { org.bukkit.Bukkit.getScheduler().cancelTask(it) }
    }
}

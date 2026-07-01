package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.VocchiPet
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent

/**
 * 處理寵物戰鬥相關的監聽器。
 * Listener for handling pet combat related events.
 */
class CombatListener(private val plugin: VocchiPet) : Listener {

    /**
     * 處理實體受傷事件，允許主人攻擊自己的寵物，並讓寵物協同攻擊。
     * Handles entity damage events, allowing owners to attack their own pets, and enabling pet co-attacks.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        val victim = event.entity
        
        // 主人攻擊寵物：解鎖傷害
        if (damager is Player) {
            val summonedEntity = plugin.getPetManager().getSummonedEntity(damager.uniqueId)
            if (victim == summonedEntity) {
                event.isCancelled = false
                return
            }

            // 主人攻擊其他生物：寵物協同攻擊
            if (victim is LivingEntity && victim != summonedEntity) {
                summonedEntity?.target = victim
            }
        }

        // 寵物受到攻擊：反擊攻擊者
        if (victim is Mob) {
            val ownerId = findOwnerByEntity(victim)
            if (ownerId != null && damager is LivingEntity) {
                victim.target = damager
            }
        }
    }

    /**
     * 防止寵物主動攻擊主人。
     * Prevents pets from attacking their owners.
     */
    @EventHandler
    fun onPetTargetOwner(event: EntityTargetLivingEntityEvent) {
        val petEntity = event.entity as? Mob ?: return
        val target = event.target as? Player ?: return
        
        val ownerPet = plugin.getPetManager().getSummonedPet(target.uniqueId)
        val ownerEntity = plugin.getPetManager().getSummonedEntity(target.uniqueId)
        
        if (petEntity == ownerEntity) {
            event.isCancelled = true
        }
    }

    /**
     * 處理寵物死亡事件。
     * Handles pet death events.
     */
    @EventHandler
    fun onPetDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val ownerId = findOwnerByEntity(entity) ?: return
        val pet = plugin.getPetManager().getSummonedPet(ownerId) ?: return

        // 廣播死亡訊息
        val deathMsg = Component.text("[VocchiPet] ", NamedTextColor.RED)
            .append(Component.text("玩家 ", NamedTextColor.WHITE))
            .append(Component.text(Bukkit.getPlayer(ownerId)?.name ?: "未知", NamedTextColor.YELLOW))
            .append(Component.text(" 的寵物 ", NamedTextColor.WHITE))
            .append(Component.text(pet.getName(), NamedTextColor.AQUA))
            .append(Component.text(" 在戰鬥中倒下了！", NamedTextColor.WHITE))
        
        Bukkit.getServer().broadcast(deathMsg)
        
        // 移除掉落物
        event.drops.clear()
        event.droppedExp = 0
        
        // 更新資料庫血量為 0
        pet.setCurrentHp(0.0)
        plugin.getPetStorage().savePet(pet)
        
        // 從管理器移除
        plugin.getPetManager().removeSummonedPet(ownerId)
    }

    private fun findOwnerByEntity(entity: LivingEntity): java.util.UUID? {
        for (player in Bukkit.getOnlinePlayers()) {
            if (plugin.getPetManager().getSummonedEntity(player.uniqueId) == entity) {
                return player.uniqueId
            }
        }
        return null
    }
}

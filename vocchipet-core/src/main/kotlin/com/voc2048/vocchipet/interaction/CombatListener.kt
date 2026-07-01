package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.VocchiPet
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * 處理寵物戰鬥相關的監聽器。
 * Listener for handling pet combat related events.
 */
class CombatListener(private val plugin: VocchiPet) : Listener {

    /**
     * 處理實體受傷事件，允許主人攻擊自己的寵物。
     * Handles entity damage events, allowing owners to attack their own pets.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        val victim = event.entity
        
        if (damager is Player) {
            val summonedEntity = plugin.getPetManager().getSummonedEntity(damager.uniqueId)
            
            // 如果受害者是玩家召喚的寵物
            // If the victim is the player's summoned pet
            if (victim == summonedEntity) {
                // 解鎖傷害：即使有其他插件保護也強制允許
                // Unlock damage: Force allow even if other plugins protect
                event.isCancelled = false
            }
        }
    }
}

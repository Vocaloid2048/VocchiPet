package com.voc2048.vocchiPet.interaction

import com.voc2048.vocchiPet.VocchiPet
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.ItemDisplay
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * 處理玩家與寵物互動的監聽器。
 * Listener for handling player interactions with pets.
 *
 * @property plugin 插件實例 / Plugin instance.
 */
class InteractionListener(private val plugin: VocchiPet) : Listener {

    // 簡單的冷卻記錄：Pet UUID -> 結束時間
    private val cooldowns = mutableMapOf<UUID, Long>()

    /**
     * 處理玩家點擊寵物的互動事件。
     * Handles player interaction events with pets.
     *
     * @param event 互動事件 / Interaction event.
     */
    @EventHandler
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        val entity = event.rightClicked
        if (entity !is ItemDisplay) return

        val player = event.player
        // 這裡需要透過某種方式從 ItemDisplay 找到對應的 Pet 實例
        // 目前暫時無法實作，因為沒有 PetManager
        val pet = null // 待補完 

        if (pet == null) return

        // 撫摸邏輯：空手且蹲下
        if (player.isSneaking && player.inventory.itemInMainHand.type == Material.AIR) {
            handlePat(player, pet)
        } else {
            // 打開 GUI
            openInteractionGui(player, pet)
        }
    }

    private fun handlePat(player: org.bukkit.entity.Player, pet: com.voc2048.vocchipet.api.Pet) {
        val now = System.currentTimeMillis()
        val cooldownEnd = cooldowns[pet.getUniqueId()] ?: 0L

        if (now < cooldownEnd) {
            player.sendMessage("§c寵物看起來還不想被撫摸...")
            return
        }

        // 好感度 +2
        pet.setAffection(pet.getAffection() + 2.0)
        
        // 非同步儲存
        plugin.getPetStorage().savePet(pet)

        // 特效與提示
        player.world.spawnParticle(Particle.HEART, player.location.add(0.0, 1.0, 0.0), 5)
        player.sendMessage("§a你摸了摸 ${pet.getType()}，它感覺很開心！")

        // 設置 10 分鐘冷卻
        cooldowns[pet.getUniqueId()] = now + TimeUnit.MINUTES.toMillis(10)
    }

    private fun openInteractionGui(player: org.bukkit.entity.Player, pet: com.voc2048.vocchipet.api.Pet) {
        // 待實作 GUI...
        player.sendMessage("§e打開寵物選單...")
    }
}

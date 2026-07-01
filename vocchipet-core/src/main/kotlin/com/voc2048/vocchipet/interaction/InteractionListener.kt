package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.VocchiPet
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Mob
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
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
class InteractionListener(
    private val plugin: VocchiPet,
    private val guiListener: GuiListener,
    private val mainMenuGui: MainMenuGui
) : Listener {

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
        if (entity !is Mob) return

        val player = event.player
        
        // 檢查是否為玩家召喚的寵物
        val summonedEntity = plugin.getPetManager().getSummonedEntity(player.uniqueId)
        val pet = plugin.getPetManager().getSummonedPet(player.uniqueId)

        if (entity != summonedEntity || pet == null) return

        // 阻斷命名牌交互
        if (player.inventory.itemInMainHand.type == Material.NAME_TAG) {
            event.isCancelled = true
            player.sendMessage("§cVocchi 寵物極具靈性，拒絕使用普通的命名牌，請透過主選單 GUI 進行免費改名！")
            return
        }

        // 撫摸邏輯：空手且蹲下
        if (player.isSneaking && player.inventory.itemInMainHand.type == Material.AIR) {
            event.isCancelled = true
            handlePat(player, pet)
        } else {
            // 打開主選單 GUI
            event.isCancelled = true
            mainMenuGui.open(player)
        }
    }

    /**
     * 處理改名聊天輸入。
     * Handles renaming via chat input.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    fun onAsyncChat(event: AsyncChatEvent) {
        val player = event.player
        if (guiListener.isRenaming(player)) {
            event.isCancelled = true
            val name = PlainTextComponentSerializer.plainText().serialize(event.message())
            
            // 返回主線程執行改名邏輯
            org.bukkit.Bukkit.getScheduler().runTask(plugin, Runnable {
                guiListener.handleChatRename(player, name)
            })
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
        player.world.playSound(player.location, Sound.ENTITY_CAT_PURR, 1.0f, 1.0f)
        player.sendMessage("§a你摸了摸 ${pet.getName()}，它感覺很開心！")

        // 設置 10 分鐘冷卻
        cooldowns[pet.getUniqueId()] = now + TimeUnit.MINUTES.toMillis(10)
    }
}

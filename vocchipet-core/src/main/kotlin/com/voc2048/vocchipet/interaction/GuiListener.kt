package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.VocchiPet
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * 處理 GUI 點擊事件的監聽器。
 * Listener for handling GUI click events.
 *
 * @property plugin 插件實例 / Plugin instance.
 * @property bagGui 寵物背包 GUI / Pet bag GUI.
 * @property mainMenuGui 主選單 GUI / Main menu GUI.
 */
class GuiListener(
    private val plugin: VocchiPet,
    private val bagGui: PetBagGui,
    private val mainMenuGui: MainMenuGui
) : Listener {

    /**
     * 處理背包點擊。
     * Handles inventory clicks.
     *
     * @param event 點擊事件 / Click event.
     */
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val title = event.view.title
        val currentItem = event.currentItem ?: return

        if (title.contains("VocchiPet 主選單")) {
            event.isCancelled = true
            when (currentItem.type) {
                Material.CHEST -> bagGui.openBag(player, 1)
                Material.BARRIER -> player.closeInventory()
                else -> {}
            }
            return
        }

        if (title.contains("寵物背包")) {
            event.isCancelled = true
            
            val displayName = currentItem.itemMeta?.displayName ?: ""
            
            if (displayName.contains("下一頁")) {
                val currentPage = title.filter { it.isDigit() }.toIntOrNull() ?: 1
                bagGui.openBag(player, currentPage + 1)
            } else if (displayName.contains("上一頁")) {
                val currentPage = title.filter { it.isDigit() }.toIntOrNull() ?: 1
                if (currentPage > 1) {
                    bagGui.openBag(player, currentPage - 1)
                }
            } else if (currentItem.type != Material.BLACK_STAINED_GLASS_PANE && currentItem.type != Material.PAPER) {
                // 點擊了寵物
                player.sendMessage("§a你選擇了寵物：$displayName (召喚功能實作中)")
                player.closeInventory()
            }
        }
    }
}

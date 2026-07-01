package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.VocchiPet
import com.voc2048.vocchipet.api.Element
import com.voc2048.vocchipet.api.Tier
import com.voc2048.vocchipet.PetImpl
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import java.util.UUID

/**
 * 處理 GUI 點擊事件的監聽器。
 * Listener for handling GUI click events.
 *
 * @property plugin 插件實例 / Plugin instance.
 * @property bagGui 寵物背包 GUI / Pet bag GUI.
 * @property mainMenuGui 主選單 GUI / Main menu GUI.
 * @property constructionGui 寵物構造大師 GUI / Pet Construction Master GUI.
 */
class GuiListener(
    private val plugin: VocchiPet,
    private val bagGui: PetBagGui,
    private val mainMenuGui: MainMenuGui,
    private val constructionGui: PetConstructionMasterGui
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
            return
        }

        if (title.contains("寵物構造大師")) {
            event.isCancelled = true
            handleConstructionClick(player, event)
            return
        }
    }

    private fun handleConstructionClick(admin: Player, event: InventoryClickEvent) {
        val inv = event.inventory
        val slot = event.rawSlot
        val currentItem = event.currentItem ?: return

        // 從隱藏位置獲取目標資訊 (Slot 53)
        val targetInfoItem = inv.getItem(53) ?: return
        val targetInfoStr = targetInfoItem.itemMeta?.displayName ?: return
        if (!targetInfoStr.startsWith("§fTARGET:")) return
        
        val parts = targetInfoStr.replace("§fTARGET:", "").split(":")
        val targetUUID = UUID.fromString(parts[0])
        val speciesID = parts[1]
        val targetPlayer = Bukkit.getPlayer(targetUUID) ?: return
        val species = plugin.getSpeciesRegistry().getSpecies(speciesID) ?: return

        // 獲取當前數值
        val levelItem = inv.getItem(12) ?: return
        var currentLevel = levelItem.itemMeta?.displayName?.filter { it.isDigit() }?.toIntOrNull() ?: 1
        
        val streamingItem = inv.getItem(19) ?: return
        var isStreaming = streamingItem.itemMeta?.displayName?.contains("開啟") == true

        val elementItem = inv.getItem(20) ?: return
        var currentElement = Element.valueOf(elementItem.itemMeta?.displayName?.substringAfter(": §f") ?: species.element.name)

        val tierItem = inv.getItem(21) ?: return
        var currentTier = Tier.valueOf(tierItem.itemMeta?.displayName?.substringAfter(": §f") ?: "D")

        when (slot) {
            10 -> currentLevel = (currentLevel - 10).coerceAtLeast(1)
            11 -> currentLevel = (currentLevel - 1).coerceAtLeast(1)
            13 -> currentLevel = (currentLevel + 1).coerceAtMost(100)
            14 -> currentLevel = (currentLevel + 10).coerceAtMost(100)
            19 -> isStreaming = !isStreaming
            20 -> {
                val elements = Element.entries
                val nextIdx = (elements.indexOf(currentElement) + 1) % elements.size
                currentElement = elements[nextIdx]
            }
            21 -> {
                val tiers = Tier.entries
                val nextIdx = (tiers.indexOf(currentTier) + 1) % tiers.size
                currentTier = tiers[nextIdx]
            }
            49 -> {
                // 確認生成
                val newPet = PetImpl.create(species, targetUUID, currentTier)
                newPet.setLevel(currentLevel)
                newPet.setElement(currentElement)
                newPet.setStreaming(isStreaming)

                plugin.getPetStorage().savePet(newPet).thenAccept {
                    admin.sendMessage("§a已成功通過構造大師為 ${targetPlayer.name} 生成寵物！")
                    targetPlayer.sendMessage("§a管理員贈送了你一隻特別的 ${species.displayName}！")
                    admin.closeInventory()
                }
                return
            }
        }

        // 更新 GUI
        updateConstructionGui(inv, currentLevel, isStreaming, currentElement, currentTier)
    }

    private fun updateConstructionGui(inv: org.bukkit.inventory.Inventory, level: Int, streaming: Boolean, element: Element, tier: Tier) {
        val levelItem = inv.getItem(12)
        val levelMeta = levelItem?.itemMeta
        levelMeta?.setDisplayName("§e當前等級: §f$level")
        levelItem?.itemMeta = levelMeta

        val streamingItem = inv.getItem(19)
        val streamingMeta = streamingItem?.itemMeta
        streamingMeta?.setDisplayName("§b流光狀態: ${if (streaming) "§a開啟" else "§7關閉"}")
        streamingItem?.itemMeta = streamingMeta

        val elementItem = inv.getItem(20)
        val elementMeta = elementItem?.itemMeta
        elementMeta?.setDisplayName("§6元素屬性: §f$element")
        elementItem?.itemMeta = elementMeta

        val tierItem = inv.getItem(21)
        val tierMeta = tierItem?.itemMeta
        tierMeta?.setDisplayName("§e資質階級: §f$tier")
        tierItem?.itemMeta = tierMeta
    }
}

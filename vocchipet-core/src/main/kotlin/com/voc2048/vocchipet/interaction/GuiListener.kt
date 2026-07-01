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

        // 從隱藏位置獲取目標資訊 (Slot 2)
        val targetInfoItem = inv.getItem(2) ?: return
        val targetInfoStr = targetInfoItem.itemMeta?.displayName ?: return
        if (!targetInfoStr.startsWith("§fTARGET:")) return
        
        val parts = targetInfoStr.replace("§fTARGET:", "").split(":")
        val targetUUID = UUID.fromString(parts[0])
        val speciesID = parts[1]
        val targetPlayer = Bukkit.getPlayer(targetUUID) ?: return
        val species = plugin.getSpeciesRegistry().getSpecies(speciesID) ?: return

        // 獲取當前基礎數值
        val levelItem = inv.getItem(5) ?: return
        var currentLevel = levelItem.itemMeta?.displayName?.substringAfter(": §f")?.toIntOrNull() ?: 1
        
        val streamingItem = inv.getItem(0) ?: return
        var isStreaming = streamingItem.itemMeta?.displayName?.contains("開啟") == true

        val elementItem = inv.getItem(1) ?: return
        var currentElement = com.voc2048.vocchipet.api.Element.valueOf(elementItem.itemMeta?.displayName?.substringAfter(": §f") ?: species.element.name)

        // 處理點擊
        when (slot) {
            0 -> isStreaming = !isStreaming
            1 -> {
                val elements = com.voc2048.vocchipet.api.Element.entries
                val nextIdx = (elements.indexOf(currentElement) + 1) % elements.size
                currentElement = elements[nextIdx]
            }
            3 -> currentLevel = (currentLevel - 10).coerceAtLeast(1)
            4 -> currentLevel = (currentLevel - 1).coerceAtLeast(1)
            6 -> currentLevel = (currentLevel + 1).coerceAtMost(100)
            7 -> currentLevel = (currentLevel + 10).coerceAtMost(100)
            8 -> {
                // 確認生成
                val newPet = createPetFromGui(species, targetUUID, inv, currentLevel, isStreaming, currentElement)
                plugin.getPetStorage().savePet(newPet).thenAccept {
                    admin.sendMessage("§a已成功通過構造大師為 ${targetPlayer.name} 生成寵物！")
                    targetPlayer.sendMessage("§a管理員贈送了你一隻特別的 ${species.displayName}！")
                    Bukkit.getScheduler().runTask(plugin, Runnable { admin.closeInventory() })
                }
                return
            }
        }

        // 處理五維點擊 (Row 1-5)
        if (slot in 9..53) {
            val rowStart = (slot / 9) * 9
            val subSlot = slot % 9
            
            if (subSlot == 2) {
                // IT 切換
                val itItem = inv.getItem(slot) ?: return
                val currentTier = Tier.valueOf(itItem.itemMeta?.displayName?.substringAfter(": §f") ?: "D")
                val tiers = Tier.entries
                val nextTier = tiers[(tiers.indexOf(currentTier) + 1) % tiers.size]
                val meta = itItem.itemMeta
                meta?.setDisplayName("§e資質 (IT): §f$nextTier")
                itItem.itemMeta = meta
            } else if (subSlot in listOf(4, 5, 7, 8)) {
                // AT 調整
                val infoItem = inv.getItem(rowStart + 6) ?: return
                var currentAt = infoItem.itemMeta?.displayName?.substringAfter(": §f")?.toIntOrNull() ?: 0
                val delta = when (subSlot) {
                    4 -> -5
                    5 -> -1
                    7 -> 1
                    8 -> 5
                    else -> 0
                }
                currentAt = (currentAt + delta).coerceAtLeast(0)
                val meta = infoItem.itemMeta
                meta?.setDisplayName("§b訓練 (AT): §f$currentAt")
                infoItem.itemMeta = meta
            }
        }

        // 更新基礎 GUI
        val lvItem = inv.getItem(5)
        val lvMeta = lvItem?.itemMeta
        lvMeta?.setDisplayName("§e等級: §f$currentLevel")
        lvItem?.itemMeta = lvMeta

        val sItem = inv.getItem(0)
        val sMeta = sItem?.itemMeta
        sMeta?.setDisplayName("§b流光狀態: ${if (isStreaming) "§a開啟" else "§7關閉"}")
        sItem?.itemMeta = sMeta

        val eItem = inv.getItem(1)
        val eMeta = eItem?.itemMeta
        eMeta?.setDisplayName("§6元素屬性: §f$currentElement")
        eItem?.itemMeta = eMeta
    }

    private fun createPetFromGui(
        species: com.voc2048.vocchipet.api.PetSpecies,
        ownerId: UUID,
        inv: org.bukkit.inventory.Inventory,
        level: Int,
        streaming: Boolean,
        element: com.voc2048.vocchipet.api.Element
    ): PetImpl {
        fun getTier(row: Int) = Tier.valueOf(inv.getItem(row * 9 + 2)?.itemMeta?.displayName?.substringAfter(": §f") ?: "D")
        fun getAt(row: Int) = inv.getItem(row * 9 + 6)?.itemMeta?.displayName?.substringAfter(": §f")?.toIntOrNull() ?: 0

        val baseStats = species.baseStats
        val stats = com.voc2048.vocchipet.api.PetStats(
            hp = baseStats.hp.copy(potential = getTier(1), trained = getAt(1)),
            attack = baseStats.attack.copy(potential = getTier(2), trained = getAt(2)),
            defense = baseStats.defense.copy(potential = getTier(3), trained = getAt(3)),
            speed = baseStats.speed.copy(potential = getTier(4), trained = getAt(4)),
            focus = baseStats.focus.copy(potential = getTier(5), trained = getAt(5)),
            availableTp = 0,
            skills = arrayOfNulls(6)
        )

        return PetImpl(
            uuid = UUID.randomUUID(),
            ownerId = ownerId,
            tamerId = ownerId,
            type = species.id,
            level = level,
            exp = 0,
            affection = 20.0,
            element = element,
            stats = stats,
            streaming = streaming
        )
    }
}

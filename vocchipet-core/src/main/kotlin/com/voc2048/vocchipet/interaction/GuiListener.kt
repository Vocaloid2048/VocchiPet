package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.VocchiPet
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
 */
class GuiListener(
    private val plugin: VocchiPet,
    private val bagGui: PetBagGui,
    private val mainMenuGui: MainMenuGui,
    private val constructionGui: PetConstructionMasterGui
) : Listener {

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
                if (currentPage > 1) bagGui.openBag(player, currentPage - 1)
            } else if (currentItem.type != Material.BLACK_STAINED_GLASS_PANE && currentItem.type != Material.PAPER) {
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

        val targetInfoItem = inv.getItem(2) ?: return
        val targetInfoStr = targetInfoItem.itemMeta?.displayName ?: return
        if (!targetInfoStr.startsWith("§fTARGET:")) return
        
        val parts = targetInfoStr.replace("§fTARGET:", "").split(":")
        val targetUUID = UUID.fromString(parts[0])
        val speciesID = parts[1]
        val targetPlayer = Bukkit.getPlayer(targetUUID) ?: return
        val species = plugin.getSpeciesRegistry().getSpecies(speciesID) ?: return

        val levelItem = inv.getItem(5) ?: return
        var currentLevel = levelItem.itemMeta?.displayName?.substringAfter(": §f")?.toIntOrNull() ?: 1
        
        val subspeciesItem = inv.getItem(0) ?: return
        var isSubspecies = subspeciesItem.itemMeta?.displayName?.contains("開啟") == true

        when (slot) {
            0 -> isSubspecies = !isSubspecies
            3 -> currentLevel = (currentLevel - 10).coerceAtLeast(1)
            4 -> currentLevel = (currentLevel - 1).coerceAtLeast(1)
            6 -> currentLevel = (currentLevel + 1).coerceAtMost(100)
            7 -> currentLevel = (currentLevel + 10).coerceAtMost(100)
            8 -> {
                val newPet = createPetFromGui(species, targetUUID, inv, currentLevel, isSubspecies)
                plugin.getPetStorage().savePet(newPet).thenAccept {
                    admin.sendMessage("§a已成功通過構造大師為 ${targetPlayer.name} 生成寵物！")
                    targetPlayer.sendMessage("§a管理員贈送了你一隻特別的 ${species.displayName}！")
                    Bukkit.getScheduler().runTask(plugin, Runnable { admin.closeInventory() })
                }
                return
            }
        }

        if (slot in 9..53) {
            val rowStart = (slot / 9) * 9
            val subSlot = slot % 9
            
            if (subSlot == 2 || subSlot == 6) {
                val infoItem = inv.getItem(rowStart + 4) ?: return
                var currentTierVal = infoItem.itemMeta?.displayName?.substringAfter(": §f")?.toIntOrNull() ?: 1
                val delta = if (subSlot == 2) -1 else 1
                currentTierVal = (currentTierVal + delta).coerceIn(1, 16)
                
                val meta = infoItem.itemMeta
                meta?.setDisplayName("§e先天階級 (Tier): §f$currentTierVal")
                infoItem.itemMeta = meta
            }
        }

        // 更新基礎 GUI
        val lvMeta = levelItem.itemMeta
        lvMeta?.setDisplayName("§e等級: §f$currentLevel")
        levelItem.itemMeta = lvMeta

        val sMeta = subspeciesItem.itemMeta
        sMeta?.setDisplayName("§b亞種狀態: ${if (isSubspecies) "§a開啟" else "§7關閉"}")
        subspeciesItem.itemMeta = sMeta
    }

    private fun createPetFromGui(
        species: com.voc2048.vocchipet.api.PetSpecies,
        ownerId: UUID,
        inv: org.bukkit.inventory.Inventory,
        level: Int,
        subspecies: Boolean
    ): PetImpl {
        val tiers = Tier.entries.toTypedArray()
        fun getTier(row: Int): Tier {
            val tierVal = inv.getItem(row * 9 + 4)?.itemMeta?.displayName?.substringAfter(": §f")?.toIntOrNull() ?: 1
            return tiers[(tierVal - 1).coerceIn(0, 15)]
        }

        val baseStats = species.baseStats
        val stats = com.voc2048.vocchipet.api.PetStats(
            hp = baseStats.hp.copy(potential = getTier(1)),
            attack = baseStats.attack.copy(potential = getTier(2)),
            defense = baseStats.defense.copy(potential = getTier(3)),
            speed = baseStats.speed.copy(potential = getTier(4)),
            focus = baseStats.focus.copy(potential = getTier(5)),
            skills = arrayOfNulls(4)
        )

        return PetImpl(
            uuid = UUID.randomUUID(),
            ownerId = ownerId,
            tamerId = ownerId,
            type = species.id,
            name = species.displayName,
            level = level,
            exp = 0,
            affection = 20.0,
            stats = stats,
            subspecies = subspecies
        )
    }
}

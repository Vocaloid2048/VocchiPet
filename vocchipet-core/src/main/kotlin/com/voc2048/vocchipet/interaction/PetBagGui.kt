package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.VocchiPet
import com.voc2048.vocchipet.api.Element
import com.voc2048.vocchipet.api.Pet
import com.voc2048.vocchipet.api.Tier
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * 寵物背包 GUI 介面。
 * Pet bag GUI interface.
 */
class PetBagGui(private val plugin: VocchiPet) {

    /**
     * 開啟寵物背包。
     * Opens the pet bag.
     *
     * @param player 目標玩家 / Target player.
     * @param page 頁數 / Page number.
     */
    fun openBag(player: Player, page: Int) {
        plugin.getPetStorage().loadPetsByOwner(player.uniqueId).thenAccept { pets ->
            val inv: Inventory = Bukkit.createInventory(null, 54, "§8寵物背包 (第 $page 頁)")

            val pageSize = 45
            val start = (page - 1) * pageSize
            val end = minOf(start + pageSize, pets.size)

            if (start < pets.size) {
                for (i in start until end) {
                    val pet = pets[i]
                    inv.setItem(i - start, createPetItem(pet))
                }
            }

            // 底部裝飾與導航
            val glass = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
            val glassMeta = glass.itemMeta
            glassMeta?.setDisplayName(" ")
            glass.itemMeta = glassMeta
            for (i in 45..53) {
                inv.setItem(i, glass)
            }

            // 上一頁
            if (page > 1) {
                val prev = ItemStack(Material.ARROW)
                val prevMeta = prev.itemMeta
                prevMeta?.setDisplayName("§f上一頁")
                prev.itemMeta = prevMeta
                inv.setItem(45, prev)
            }

            // 當前頁數顯示
            val info = ItemStack(Material.PAPER)
            val infoMeta = info.itemMeta
            infoMeta?.setDisplayName("§f第 $page 頁")
            infoMeta?.lore = listOf("§7總計寵物: §e${pets.size}")
            info.itemMeta = infoMeta
            inv.setItem(49, info)

            // 下一頁
            if (end < pets.size) {
                val next = ItemStack(Material.ARROW)
                val nextMeta = next.itemMeta
                nextMeta?.setDisplayName("§f下一頁")
                next.itemMeta = nextMeta
                inv.setItem(53, next)
            }

            Bukkit.getScheduler().runTask(plugin, Runnable {
                player.openInventory(inv)
            })
        }
    }

    private fun createPetItem(pet: Pet): ItemStack {
        val species = plugin.getSpeciesRegistry().getSpecies(pet.getType())
        val material = species?.modelItem?.type ?: Material.WOLF_SPAWN_EGG
        
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta?.setDisplayName("§b${species?.displayName ?: pet.getType()} §f(Lv.${pet.getLevel()})")
        
        val stats = pet.getStats()
        val lore = mutableListOf<String>()
        lore.add("§7屬性: ${getElementColor(pet.getElement())}${pet.getElement()}")
        if (pet.isStreaming()) {
            lore.add("§6✦ 流光亞種 (Streaming) ✦")
        }
        lore.add(" ")
        lore.add("§f屬性資質與訓練 (IT/AT):")
        lore.add(formatStatLore("生命", stats.hp))
        lore.add(formatStatLore("攻擊", stats.attack))
        lore.add(formatStatLore("防禦", stats.defense))
        lore.add(formatStatLore("速度", stats.speed))
        lore.add(formatStatLore("專注", stats.focus))
        lore.add(" ")
        lore.add("§7剩餘 TP: §e${stats.availableTp}")
        lore.add("§7好感度: §d${"%.1f".format(pet.getAffection())}")
        lore.add(" ")
        lore.add("§e點擊召喚寵物")
        
        meta?.lore = lore
        item.itemMeta = meta
        return item
    }

    private fun formatStatLore(label: String, component: com.voc2048.vocchipet.api.StatComponent): String {
        val tierColor = getTierColor(component.potential)
        return " §7$label: $tierColor${component.potential} §8| §bAT: ${component.trained}"
    }

    private fun getElementColor(element: Element): String = when (element) {
        Element.FIRE -> "§c"
        Element.WATER -> "§b"
        Element.GRASS -> "§a"
        Element.LIGHT -> "§e"
        Element.DARK -> "§8"
    }

    private fun getTierColor(tier: Tier): String = when (tier) {
        Tier.D -> "§7"
        Tier.C -> "§f"
        Tier.B -> "§a"
        Tier.A -> "§b"
        Tier.S -> "§5"
        Tier.SS -> "§6"
        Tier.SS_PLUS -> "§e"
        Tier.UR -> "§d"
    }
}

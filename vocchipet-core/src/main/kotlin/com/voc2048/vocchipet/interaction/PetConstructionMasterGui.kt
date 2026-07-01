package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.VocchiPet
import com.voc2048.vocchipet.api.PetSpecies
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * 寵物構造大師 GUI，用於管理員自定義生成寵物。
 * Pet Construction Master GUI, used by admins to customize pet generation.
 *
 * @property plugin 插件實例 / Plugin instance.
 */
class PetConstructionMasterGui(private val plugin: VocchiPet) {

    /**
     * 開啟寵物構造大師 GUI。
     * Opens the Pet Construction Master GUI.
     *
     * @param admin 管理員玩家 / Admin player.
     * @param target 目標玩家 / Target player.
     * @param species 寵物種類 / Pet species.
     */
    fun open(admin: Player, target: Player, species: PetSpecies) {
        val inv = Bukkit.createInventory(null, 54, "§0寵物構造大師 | ${species.displayName}")

        // [0-8] 頂部裝飾與基礎設定
        val glass = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
        val glassMeta = glass.itemMeta
        glassMeta?.setDisplayName(" ")
        glass.itemMeta = glassMeta
        for (i in 0..8) inv.setItem(i, glass)
        
        // 基礎屬性調整 (Row 0)
        inv.setItem(0, createButtonItem(Material.NETHER_STAR, "§b流光狀態: §7關閉"))
        inv.setItem(1, createButtonItem(Material.FIRE_CHARGE, "§6元素屬性: §f${species.element}"))
        // Slot 2: Target Info (Hidden)
        inv.setItem(3, createButtonItem(Material.RED_TERRACOTTA, "§cLv -10"))
        inv.setItem(4, createButtonItem(Material.PINK_TERRACOTTA, "§dLv -1"))
        inv.setItem(5, createInfoItem(Material.EXPERIENCE_BOTTLE, "§e等級: §f1"))
        inv.setItem(6, createButtonItem(Material.LIME_TERRACOTTA, "§aLv +1"))
        inv.setItem(7, createButtonItem(Material.GREEN_TERRACOTTA, "§2Lv +10"))
        inv.setItem(8, createButtonItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, "§a§l確認生成並發放", listOf("§7目標玩家: §f${target.name}")))

        // 五維屬性調整 (Row 1-5)
        setupStatRow(inv, 9, "生命 (HP)")
        setupStatRow(inv, 18, "攻擊 (ATK)")
        setupStatRow(inv, 27, "防禦 (DEF)")
        setupStatRow(inv, 36, "速度 (SPD)")
        setupStatRow(inv, 45, "專注 (FCS)")

        // 儲存目標資訊於 Slot 2 (隱藏)
        val targetInfo = ItemStack(Material.PLAYER_HEAD)
        val targetMeta = targetInfo.itemMeta
        targetMeta?.setDisplayName("§fTARGET:${target.uniqueId}:${species.id}")
        targetInfo.itemMeta = targetMeta
        inv.setItem(2, targetInfo)

        admin.openInventory(inv)
    }

    private fun setupStatRow(inv: Inventory, startSlot: Int, label: String) {
        inv.setItem(startSlot, createInfoItem(Material.IRON_BARS, "§f§l$label"))
        
        // IT 調整 (Slot +2)
        inv.setItem(startSlot + 2, createButtonItem(Material.GOLD_INGOT, "§e資質 (IT): §fD", listOf("§7點擊切換 D~UR")))
        
        // AT 調整
        inv.setItem(startSlot + 4, createButtonItem(Material.RED_STAINED_GLASS_PANE, "§c$label AT -5"))
        inv.setItem(startSlot + 5, createButtonItem(Material.PINK_STAINED_GLASS_PANE, "§d$label AT -1"))
        inv.setItem(startSlot + 6, createInfoItem(Material.BOOK, "§b訓練 (AT): §f0"))
        inv.setItem(startSlot + 7, createButtonItem(Material.LIME_STAINED_GLASS_PANE, "§a$label AT +1"))
        inv.setItem(startSlot + 8, createButtonItem(Material.GREEN_STAINED_GLASS_PANE, "§2$label AT +5"))
    }

    private fun createButtonItem(material: Material, name: String, lore: List<String> = emptyList()): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta?.setDisplayName(name)
        meta?.lore = lore
        item.itemMeta = meta
        return item
    }

    private fun createInfoItem(material: Material, name: String, lore: List<String> = emptyList()): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta?.setDisplayName(name)
        meta?.lore = lore
        item.itemMeta = meta
        return item
    }
}

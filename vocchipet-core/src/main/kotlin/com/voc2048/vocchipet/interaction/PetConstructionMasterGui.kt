package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.PetImpl
import com.voc2048.vocchipet.VocchiPet
import com.voc2048.vocchipet.api.Element
import com.voc2048.vocchipet.api.PetSpecies
import com.voc2048.vocchipet.api.Tier
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.UUID

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

        // 初始化預設值 (儲存於 ItemMeta 或標籤中，或者簡單一點，目前先用標題區分狀態是不行的)
        // 實務上我們會需要一個 Session 來追蹤當前編輯狀態，但這裡為了簡化，我們直接生成一個帶有預設值的寵物，並在點擊時更新 GUI。
        // 不過 GUI 點擊處理需要知道這些狀態。我們可以使用 PersistentDataContainer 或簡單的靜態 Map。
        
        // 為了符合任務要求，我們建立一個簡單的 GUI 佈局
        
        // [0-8] 頂部裝飾
        val glass = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
        val glassMeta = glass.itemMeta
        glassMeta?.setDisplayName(" ")
        glass.itemMeta = glassMeta
        for (i in 0..8) inv.setItem(i, glass)

        // [10, 11, 12, 13, 14] 等級調整: -10, -1, [LV], +1, +10
        inv.setItem(10, createButtonItem(Material.RED_TERRACOTTA, "§c等級 -10"))
        inv.setItem(11, createButtonItem(Material.PINK_TERRACOTTA, "§d等級 -1"))
        inv.setItem(12, createInfoItem(Material.EXPERIENCE_BOTTLE, "§e當前等級: §f1", listOf("§7點擊下方按鈕調整 / Adjust level below")))
        inv.setItem(13, createButtonItem(Material.LIME_TERRACOTTA, "§a等級 +1"))
        inv.setItem(14, createButtonItem(Material.GREEN_TERRACOTTA, "§2等級 +10"))

        // [19, 20, 21] 流光、元素、資質
        inv.setItem(19, createButtonItem(Material.NETHER_STAR, "§b流光狀態: §7關閉", listOf("§7點擊切換 / Click to toggle")))
        inv.setItem(20, createButtonItem(Material.FIRE_CHARGE, "§6元素屬性: §f${species.element}", listOf("§7點擊切換屬性 / Click to toggle element")))
        inv.setItem(21, createButtonItem(Material.GOLD_INGOT, "§e資質階級: §fD", listOf("§7點擊切換資質 (D~UR) / Click to toggle tier")))

        // [28-34] 技能配置 (佔位)
        inv.setItem(31, createButtonItem(Material.BLAZE_POWDER, "§6技能配置", listOf("§7功能開發中... / Under development...")))

        // [49] 確認生成
        val confirmItem = ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
        val confirmMeta = confirmItem.itemMeta
        confirmMeta?.setDisplayName("§a§l確認生成並發放")
        confirmMeta?.lore = listOf("§7目標玩家: §f${target.name}", "§7點擊後寵物將直接進入其背包。", "§7Click to give pet to player bag.")
        confirmItem.itemMeta = confirmMeta
        inv.setItem(49, confirmItem)

        // 儲存目標資訊於 GUI 中 (這裡使用一個簡單的隱藏項)
        val targetInfo = ItemStack(Material.PLAYER_HEAD)
        val targetMeta = targetInfo.itemMeta
        targetMeta?.setDisplayName("§fTARGET:${target.uniqueId}:${species.id}")
        targetInfo.itemMeta = targetMeta
        inv.setItem(53, targetInfo)

        admin.openInventory(inv)
    }

    private fun createButtonItem(material: Material, name: String, lore: List<String> = emptyList()): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta?.setDisplayName(name)
        meta?.lore = lore
        item.itemMeta = meta
        return item
    }

    private fun createInfoItem(material: Material, name: String, lore: List<String>): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta?.setDisplayName(name)
        meta?.lore = lore
        item.itemMeta = meta
        return item
    }
}

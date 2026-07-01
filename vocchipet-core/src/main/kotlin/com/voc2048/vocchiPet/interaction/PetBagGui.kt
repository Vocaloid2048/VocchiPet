package com.voc2048.vocchiPet.interaction

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * 寵物背包 GUI 介面。
 * Pet bag GUI interface.
 */
class PetBagGui {

    /**
     * 開啟寵物背包。
     * Opens the pet bag.
     *
     * @param player 目標玩家 / Target player.
     * @param page 頁數 / Page number.
     */
    fun openBag(player: Player, page: Int) {
        val inv: Inventory = Bukkit.createInventory(null, 54, "§8寵物背包 (第 $page 頁)")

        // 模擬寵物道具
        val petItem = ItemStack(Material.WOLF_SPAWN_EGG)
        val meta = petItem.itemMeta
        meta?.setDisplayName("§b測試火龍")
        meta?.lore = listOf(
            "§7屬性: §c火",
            "§7階級: §aD",
            "§7剩餘 TP: §e10"
        )
        petItem.itemMeta = meta
        inv.setItem(0, petItem)

        // 底部頁面控制
        val navItem = ItemStack(Material.ARROW)
        val navMeta = navItem.itemMeta
        navMeta?.setDisplayName("§f下一頁")
        navItem.itemMeta = navMeta
        inv.setItem(53, navItem)

        player.openInventory(inv)
    }
}

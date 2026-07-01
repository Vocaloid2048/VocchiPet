package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.VocchiPet
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * 插件主選單 GUI。
 * Main menu GUI of the plugin.
 */
class MainMenuGui(private val plugin: VocchiPet) {

    /**
     * 開啟主選單。
     * Opens the main menu.
     *
     * @param player 目標玩家 / Target player.
     */
    fun open(player: Player) {
        val inv: Inventory = Bukkit.createInventory(null, 27, "§8VocchiPet 主選單")

        // 寵物背包按鈕
        val bagItem = ItemStack(Material.CHEST)
        val bagMeta = bagItem.itemMeta
        bagMeta?.setDisplayName("§6寵物背包")
        bagMeta?.lore = listOf("§7點擊查看你所擁有的所有寵物。", "§7Click to view all your pets.")
        bagItem.itemMeta = bagMeta
        inv.setItem(11, bagItem)

        // 寵物狀態顯示
        val summonedPet = plugin.getPetManager().getSummonedPet(player.uniqueId)
        val statusItem: ItemStack
        if (summonedPet != null) {
            val species = plugin.getSpeciesRegistry().getSpecies(summonedPet.getType())
            statusItem = ItemStack(species?.modelItem?.type ?: Material.WOLF_SPAWN_EGG)
            val meta = statusItem.itemMeta
            meta?.setDisplayName("§b當前召喚：${species?.displayName ?: summonedPet.getType()}")
            meta?.lore = listOf(
                "§7等級: §f${summonedPet.getLevel()}",
                "§7屬性: §e${summonedPet.getElement()}",
                "§7好感度: §d${"%.1f".format(summonedPet.getAffection())}"
            )
            statusItem.itemMeta = meta
        } else {
            statusItem = ItemStack(Material.BARRIER)
            val meta = statusItem.itemMeta
            meta?.setDisplayName("§c當前尚未召喚寵物")
            meta?.lore = listOf("§7請前往背包選擇寵物進行召喚。", "§7Please go to bag to summon a pet.")
            statusItem.itemMeta = meta
        }
        inv.setItem(13, statusItem)

        // 說明按鈕
        val closeItem = ItemStack(Material.BOOK)
        val closeMeta = closeItem.itemMeta
        closeMeta?.setDisplayName("§f說明指南")
        closeMeta?.lore = listOf("§7查看如何開始你的寵物之旅。", "§7View guide on how to start.")
        closeItem.itemMeta = closeMeta
        inv.setItem(15, closeItem)

        player.openInventory(inv)
    }
}

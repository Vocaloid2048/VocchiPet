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
            val material = when (species?.entityType?.name) {
                "BLAZE" -> Material.BLAZE_SPAWN_EGG
                "TURTLE" -> Material.TURTLE_SPAWN_EGG
                "SNIFFER" -> Material.SNIFFER_SPAWN_EGG
                "BEE" -> Material.BEE_SPAWN_EGG
                "WITHER_SKELETON" -> Material.WITHER_SKELETON_SPAWN_EGG
                else -> Material.WOLF_SPAWN_EGG
            }
            statusItem = ItemStack(material)
            val meta = statusItem.itemMeta
            meta?.setDisplayName("§b當前召喚：${summonedPet.getName()}")
            meta?.lore = listOf(
                "§7種類: §f${species?.displayName ?: summonedPet.getType()}",
                "§7等級: §f${summonedPet.getLevel()}",
                "§7好感度: §d${"%.1f".format(summonedPet.getAffection())}"
            )
            statusItem.itemMeta = meta
            
            // 改名按鈕 (Free Rename Button)
            val renameItem = ItemStack(Material.NAME_TAG)
            val renameMeta = renameItem.itemMeta
            renameMeta?.setDisplayName("§a免費修改暱稱")
            renameMeta?.lore = listOf("§7點擊為你的當前寵物設定一個新名字。", "§7Click to set a new name for your pet.")
            renameItem.itemMeta = renameMeta
            inv.setItem(22, renameItem)
        } else {
            statusItem = ItemStack(Material.BARRIER)
            val meta = statusItem.itemMeta
            meta?.setDisplayName("§c當前尚未召喚寵物")
            meta?.lore = listOf("§7請前往背包選擇寵物進行召喚。", "§7Please go to bag to summon a pet.")
            statusItem.itemMeta = meta
        }
        inv.setItem(13, statusItem)

        // 說明按鈕
        val infoItem = ItemStack(Material.BOOK)
        val infoMeta = infoItem.itemMeta
        infoMeta?.setDisplayName("§f說明指南")
        infoMeta?.lore = listOf("§7查看如何開始你的寵物之旅。", "§7View guide on how to start.")
        infoItem.itemMeta = infoMeta
        inv.setItem(15, infoItem)

        player.openInventory(inv)
    }
}

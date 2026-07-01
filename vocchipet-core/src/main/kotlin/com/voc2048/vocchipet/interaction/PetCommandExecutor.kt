package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.VocchiPet
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * 處理 /vocchipet 指令的執行器。
 * Command executor for handling /vocchipet commands.
 *
 * @property plugin 插件實例 / Plugin instance.
 */
class PetCommandExecutor(private val plugin: VocchiPet) : CommandExecutor {

    /**
     * 執行指令。
     * Executes the command.
     *
     * @param sender 指令發送者 / Command sender.
     * @param command 指令物件 / Command object.
     * @param label 指令標籤 / Command label.
     * @param args 指令參數 / Command arguments.
     * @return 是否執行成功 / Whether the execution was successful.
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§c此指令僅限玩家使用。")
            return true
        }

        val player = sender
        if (args.isEmpty()) {
            player.sendMessage("§e打開主選單...")
            return true
        }

        when (args[0].lowercase()) {
            "admin" -> handleAdminCommand(player, args)
            "bag" -> player.sendMessage("§e打開寵物背包...")
            "summon" -> player.sendMessage("§e召喚寵物...")
            "recall" -> player.sendMessage("§e收回寵物...")
            else -> player.sendMessage("§c未知指令。")
        }
        return true
    }

    private fun handleAdminCommand(player: Player, args: Array<out String>) {
        if (!player.hasPermission("vocchipet.admin")) {
            player.sendMessage("§c無權限。")
            return
        }

        if (args.size < 2) {
            player.sendMessage("§c用法: /vp admin <give|addpage|reload>")
            return
        }

        when (args[1].lowercase()) {
            "give" -> player.sendMessage("§a賦予寵物...")
            "addpage" -> player.sendMessage("§a背包頁數已提升。")
            "reload" -> player.sendMessage("§a插件已重載。")
            else -> player.sendMessage("§c未知管理指令。")
        }
    }
}

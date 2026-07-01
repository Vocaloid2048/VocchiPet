package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.PetImpl
import com.voc2048.vocchipet.VocchiPet
import com.voc2048.vocchipet.api.Tier
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

/**
 * 處理 /vocchipet 指令的執行器。
 * Command executor for handling /vocchipet commands.
 *
 * @property plugin 插件實例 / Plugin instance.
 * @property bagGui 寵物背包 GUI / Pet bag GUI.
 * @property mainMenuGui 主選單 GUI / Main menu GUI.
 */
class PetCommandExecutor(
    private val plugin: VocchiPet,
    private val bagGui: PetBagGui,
    private val mainMenuGui: MainMenuGui
) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§c此指令僅限玩家使用。")
            return true
        }

        val player = sender
        if (args.isEmpty()) {
            mainMenuGui.open(player)
            return true
        }

        when (args[0].lowercase()) {
            "admin" -> handleAdminCommand(player, args)
            "bag" -> bagGui.openBag(player, 1)
            "summon" -> player.sendMessage("§e功能開發中：召喚寵物")
            "recall" -> player.sendMessage("§e功能開發中：收回寵物")
            "home" -> mainMenuGui.open(player)
            else -> player.sendMessage("§c未知指令。使用 /vp 查看幫助。")
        }
        return true
    }

    private fun handleAdminCommand(player: Player, args: Array<out String>) {
        if (!player.hasPermission("vocchipet.admin")) {
            player.sendMessage("§c你沒有權限執行此指令。")
            return
        }

        if (args.size < 2) {
            player.sendMessage("§c用法: /vp admin <give|addpage|reload>")
            return
        }

        when (args[1].lowercase()) {
            "give" -> {
                if (args.size < 5) {
                    player.sendMessage("§c用法: /vp admin give <玩家> <寵物ID> <資質(D~UR)>")
                    return
                }
                val target = Bukkit.getPlayer(args[2])
                if (target == null) {
                    player.sendMessage("§c找不到玩家 ${args[2]}")
                    return
                }
                val speciesId = args[3]
                val species = plugin.getSpeciesRegistry().getSpecies(speciesId)
                if (species == null) {
                    player.sendMessage("§c找不到寵物種類 ${speciesId}")
                    return
                }
                val quality = try {
                    Tier.valueOf(args[4].uppercase())
                } catch (e: Exception) {
                    player.sendMessage("§c無效的資質: ${args[4]}。可選: D, C, B, A, S, SS, SS_PLUS, UR")
                    return
                }

                val newPet = PetImpl.create(species, target.uniqueId, quality)
                plugin.getPetStorage().savePet(newPet).thenAccept {
                    player.sendMessage("§a已成功賦予 ${target.name} 一隻 ${species.displayName} (§f資質: $quality)")
                    target.sendMessage("§a管理員賦予了你一隻 ${species.displayName}！")
                }.exceptionally { ex ->
                    player.sendMessage("§c賦予失敗: ${ex.message}")
                    null
                }
            }
            "addpage" -> {
                if (args.size < 3) {
                    player.sendMessage("§c用法: /vp admin addpage <玩家>")
                    return
                }
                val target = Bukkit.getPlayer(args[2])
                if (target == null) {
                    player.sendMessage("§c找不到玩家 ${args[2]}")
                    return
                }
                player.sendMessage("§a已為 ${target.name} 增加一頁背包上限（功能實作中）。")
            }
            "reload" -> {
                player.sendMessage("§aVocchiPet 設定檔已重載（功能實作中）。")
            }
            else -> player.sendMessage("§c未知管理指令。")
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
        if (args.size == 1) {
            return listOf("admin", "bag", "summon", "recall", "home").filter { it.startsWith(args[0].lowercase()) }
        }
        if (args.size == 2 && args[0].equals("admin", true)) {
            return listOf("give", "addpage", "reload").filter { it.startsWith(args[1].lowercase()) }
        }
        if (args.size == 3 && args[0].equals("admin", true) && args[1].equals("give", true)) {
            return Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[2], true) }
        }
        if (args.size == 4 && args[0].equals("admin", true) && args[1].equals("give", true)) {
            return plugin.getSpeciesRegistry().getAllSpecies().map { it.id }.filter { it.startsWith(args[3].lowercase()) }
        }
        if (args.size == 5 && args[0].equals("admin", true) && args[1].equals("give", true)) {
            return Tier.entries.map { it.name }.filter { it.startsWith(args[4].uppercase()) }
        }
        return emptyList()
    }
}

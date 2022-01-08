package dev.xyze.namecolor.commands

import dev.xyze.namecolor.NameColor
import dev.xyze.namecolor.componentplaceholder.ComponentInfo
import dev.xyze.namecolor.componentplaceholder.PlaceholderHandler
import dev.xyze.namecolor.util.ColorUtil
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class NamePrefixCommand(private val plugin: NameColor) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            when (args.getOrElse(0) { "get" }) {
                "set" -> {
                    if (args.size < 2) {
                        return false
                    }
                    return onSetPlayerPrefix(sender, args.takeLast(args.size - 1).joinToString(" "))
                }
                "reset" -> {
                    if (args.size != 1) {
                        return false
                    }
                    return onResetPlayerPrefix(sender)
                }
                "get" -> {
                    if (args.size != 1) {
                        return false
                    }
                    return onGetPlayerPrefix(sender)
                }
                else -> return false
            }
        } else {
            sender.sendMessage("${ChatColor.DARK_RED}Command must be run as player.")
        }

        return true
    }

    private fun onSetPlayerPrefix(sender: Player, prefix: String): Boolean {
        plugin.playerDataStorage.setPlayerPrefix(sender.uniqueId, prefix)
        sender.spigot().sendMessage(getPrefixInfoComponent(sender, true))

        return true
    }

    private fun onResetPlayerPrefix(sender: Player): Boolean {
        plugin.playerDataStorage.resetPlayerPrefix(sender.uniqueId)
        sender.spigot().sendMessage(getPrefixInfoComponent(sender, true))
        return true
    }

    private fun onGetPlayerPrefix(sender: Player): Boolean {
        sender.spigot().sendMessage(getPrefixInfoComponent(sender))
        return true
    }

    private fun getPrefixInfoComponent(player: Player, hasSetPrefix: Boolean = false): TextComponent {
        val prefix = plugin.playerDataStorage.getPlayerPrefix(player.uniqueId)

        val infoBase = if (hasSetPrefix) "You are now using prefix `$prefix`" else "You are currently using prefix `$prefix`"
        return TextComponent(infoBase)
    }
}
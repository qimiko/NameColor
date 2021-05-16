package dev.xyze.namecolor.commands

import dev.xyze.namecolor.NameColor
import dev.xyze.namecolor.util.ColorUtil
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class NameColorCommand(private val plugin: NameColor) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            when (args.getOrElse(0) { "get" }) {
                "set" -> {
                    if (args.size != 2) {
                        return false
                    }
                    return onSetPlayerColor(sender, args[1])
                }
                "reset" -> {
                    if (args.size != 1) {
                        return false
                    }
                    return onResetPlayerColor(sender)
                }
                "get" -> {
                    if (args.size != 1) {
                        return false
                    }
                    return onGetPlayerColor(sender)
                }
                else -> return false
            }
        } else {
            sender.sendMessage("${ChatColor.DARK_RED}Command must be run as player.")
        }

        return true
    }

    private fun onSetPlayerColor(sender: Player, color: String): Boolean {
        var rColor = color
        if (!color.startsWith("#")) {
            rColor = "#${color}"
        }

        if (ColorUtil.validateColor(rColor)) {
            plugin.playerDataStorage.setPlayerColor(sender.uniqueId, rColor)
            sender.spigot().sendMessage(getColorInfoComponent(sender, true))
        } else {
            sender.sendMessage("${ChatColor.DARK_RED}Incorrect color format!\n${ChatColor.RED}All colors must be a 6-digit hex color.")
        }
        return true
    }

    private fun onResetPlayerColor(sender: Player): Boolean {
        plugin.playerDataStorage.resetPlayerColor(sender.uniqueId)
        sender.spigot().sendMessage(getColorInfoComponent(sender, true))
        return true
    }

    private fun onGetPlayerColor(sender: Player): Boolean {
        sender.spigot().sendMessage(getColorInfoComponent(sender))
        return true
    }

    private fun getColorInfoComponent(player: Player, hasSetColor: Boolean = false): TextComponent {
        val color = plugin.playerDataStorage.getPlayerColor(player.uniqueId)

        val infoPrefix = if (hasSetColor) "You are now using color " else "You are currently using color "
        val infoComp = TextComponent(infoPrefix)
        val colorDisplayComp = TextComponent(color)
        colorDisplayComp.color = ChatColor.of(color)
        colorDisplayComp.clickEvent = ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, color)
        colorDisplayComp.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("Click to copy color!"))
        infoComp.addExtra(colorDisplayComp)

        return infoComp
    }
}
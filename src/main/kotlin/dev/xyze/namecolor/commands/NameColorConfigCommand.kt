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

class NameColorConfigCommand(private val plugin: NameColor) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size == 1) {
            return false
        }

        when (args[0]) {
            "set" -> {
                if (args.size < 3) {
                    return false
                }
                return onSetConfig(sender, args[1], args.takeLast(args.size - 2).joinToString(" "))
            }
            "get" -> {
                if (args.size != 2) {
                    return false
                }
                return onGetConfig(sender, args[1])
            }
            else -> return false
        }
    }

    private fun onSetConfig(sender: CommandSender, key: String, value: String): Boolean {
        val configKey = when (key) {
            "default_color" -> "default-color"
            "format" -> "format"
            else -> null
        }

        if (configKey.isNullOrEmpty()) {
            return false
        }

        if (configKey == "default-color") {
            var rColor = value
            if (!value.startsWith("#")) {
                rColor = "#${value}"
            }

            if (ColorUtil.validateColor(rColor)) {
                plugin.config.set("default-color", rColor)
                sender.spigot().sendMessage(getColorInfoComponent(true))
            } else {
                sender.sendMessage("${ChatColor.DARK_RED}Incorrect color format!\n${ChatColor.RED}All colors must be a 6-digit hex color.")
            }
        } else {
            plugin.config.set(key, value)
            sender.sendMessage("The value of `$key` is now `${plugin.config.getString(configKey)}`.")
        }
        return true
    }

    private fun onGetConfig(sender: CommandSender, key: String): Boolean {
        val configKey = when (key) {
            "default_color" -> "default-color"
            "format" -> "format"
            else -> null
        }

        if (configKey.isNullOrEmpty()) {
            return false
        }

        if (configKey == "default-color") {
            sender.spigot().sendMessage(getColorInfoComponent())
        } else {
            sender.sendMessage("The current value of `$key` is `${plugin.config.getString(configKey)}`.")
        }
        return true
    }

    private fun getColorInfoComponent(hasSetColor: Boolean = false): TextComponent {
        val color = plugin.config.getString("default-color") ?: "#FFFFFF"
        val infoText =
            if (hasSetColor) "Default color is now {nc:color:$color}" else "The current default color is {nc:color:$color}"
        return PlaceholderHandler.replacePlaceholderInString(infoText, ComponentInfo(null, infoText))
    }
}
package dev.xyze.namecolor.commands

import dev.xyze.namecolor.NameColor
import dev.xyze.namecolor.componentplaceholder.ComponentInfo
import dev.xyze.namecolor.componentplaceholder.PlaceholderHandler
import dev.xyze.namecolor.util.DefaultValues
import dev.xyze.namecolor.util.ColorUtil
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class NameColorConfigCommand(private val plugin: NameColor) : CommandExecutor {
    companion object {
        private const val KEY_DEFAULT_COLOR = "default_color"
        private const val KEY_FORMAT = "format"
        private const val KEY_DEFAULT_PREFIX = "default_prefix"
    } 

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size <= 1) {
            return false
        }

        when (args[0]) {
            "set" -> {
                if (args.size < 2) {
                    return false
                }

                if (args.size == 2) {
                    return onResetConfig(sender, args[1])
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

    private fun normalizeConfigKey(key: String) = when (key) {
        KEY_DEFAULT_COLOR -> "default-color"
        KEY_FORMAT -> "format"
        KEY_DEFAULT_PREFIX -> "default-prefix"
        else -> null
    }

    private fun onResetConfig(sender: CommandSender, key: String) : Boolean {
        val defaultValue = when (key) {
            KEY_DEFAULT_COLOR -> DefaultValues.DEFAULT_COLOR
            KEY_FORMAT -> DefaultValues.DEFAULT_FORMAT
            KEY_DEFAULT_PREFIX -> DefaultValues.DEFAULT_PREFIX
            else -> null
        }
        if (defaultValue == null) {
            return false
        }

        return onSetConfig(sender, key, defaultValue)
    }

    private fun onSetConfig(sender: CommandSender, key: String, value: String): Boolean {
        val configKey = this.normalizeConfigKey(key)
        if (configKey.isNullOrEmpty()) {
            return false
        }

        if (key == KEY_DEFAULT_COLOR) {
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
            plugin.config.set(configKey, value)
            sender.sendMessage("The value of `$key` is now `${plugin.config.getString(configKey)}`.")
        }

        plugin.loadConfig()
        return true
    }

    private fun onGetConfig(sender: CommandSender, key: String): Boolean {
        val configKey = this.normalizeConfigKey(key)
        if (configKey.isNullOrEmpty()) {
            return false
        }

        if (key == KEY_DEFAULT_COLOR) {
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
        return PlaceholderHandler.replacePlaceholderInString(this.plugin, infoText, ComponentInfo(null, infoText))
    }
}
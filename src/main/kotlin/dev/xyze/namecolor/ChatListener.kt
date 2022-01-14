package dev.xyze.namecolor

import dev.xyze.namecolor.componentplaceholder.ComponentInfo
import dev.xyze.namecolor.componentplaceholder.PlaceholderHandler
import dev.xyze.namecolor.util.DefaultValues
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener(private val plugin: NameColor) : Listener {
    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val msg = this.plugin.config.getString("format") ?: DefaultValues.DEFAULT_FORMAT
        val formattedMsg =
            PlaceholderHandler.replacePlaceholderInString(this.plugin, msg, ComponentInfo(event.player, event.message))

        event.isCancelled = true
        event.recipients.forEach {
            it.spigot().sendMessage(formattedMsg)
        }
        Bukkit.getConsoleSender().spigot().sendMessage(formattedMsg)
    }
}
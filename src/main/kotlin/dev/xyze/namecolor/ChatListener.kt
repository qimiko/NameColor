package dev.xyze.namecolor

import dev.xyze.namecolor.componentplaceholder.ComponentInfo
import dev.xyze.namecolor.componentplaceholder.PlaceholderHandler
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

object ChatListener : Listener {
    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val msg = Bukkit.getServer().pluginManager.getPlugin("namecolor")?.config?.getString("format")
            ?: "<{nc:player}> {nc:msg}"
        val formattedMsg =
            PlaceholderHandler.replacePlaceholderInString(msg, ComponentInfo(event.player, event.message))

        event.isCancelled = true
        event.recipients.forEach {
            it.spigot().sendMessage(formattedMsg)
        }
        Bukkit.getConsoleSender().spigot().sendMessage(formattedMsg)
    }
}
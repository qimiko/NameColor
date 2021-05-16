package dev.xyze.namecolor.componentplaceholder.segment

import dev.xyze.namecolor.NameColor
import dev.xyze.namecolor.componentplaceholder.ComponentInfo
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.Bukkit

class NameSegment(override val rawText: String) : ComponentSegment {
    override fun toComponent(info: ComponentInfo): TextComponent {
        val playerComp = TextComponent(info.player.displayName)
        playerComp.color = ChatColor.of(
            (Bukkit.getServer().pluginManager.getPlugin("namecolor") as NameColor)
                .playerDataStorage.getPlayerColor(info.player.uniqueId)
        )
        playerComp.clickEvent = ClickEvent(
            ClickEvent.Action.SUGGEST_COMMAND,
            "/tell ${info.player.name}"
        )
        playerComp.hoverEvent = HoverEvent(
            HoverEvent.Action.SHOW_TEXT,
            Text("${info.player.playerListName}\nType: Player\n${info.player.uniqueId}")
        )

        return playerComp
    }
}
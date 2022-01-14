package dev.xyze.namecolor.componentplaceholder.segment

import dev.xyze.namecolor.NameColor
import dev.xyze.namecolor.componentplaceholder.ComponentInfo
import dev.xyze.namecolor.componentplaceholder.PlaceholderHandler
import net.md_5.bungee.api.chat.TextComponent

class BasicTextSegment(override val rawText: String) : ComponentSegment {
    override fun toComponent(plugin: NameColor, info: ComponentInfo): TextComponent {
        var newMsg = rawText
        PlaceholderHandler.Placeholder.values().forEach {
            newMsg = newMsg.replace(
                "\\${PlaceholderHandler.getPlaceholderTag(it)}",
                PlaceholderHandler.getPlaceholderTag(it),
                true
            )
        }
        val fComponent = TextComponent()
        TextComponent.fromLegacyText(newMsg).forEach { fComponent.addExtra(it) }
        return fComponent
    }
}
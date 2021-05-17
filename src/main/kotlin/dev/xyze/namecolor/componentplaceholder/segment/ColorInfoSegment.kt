package dev.xyze.namecolor.componentplaceholder.segment

import dev.xyze.namecolor.componentplaceholder.ComponentInfo
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text

class ColorInfoSegment(override val rawText: String) : ComponentSegment {
    override fun toComponent(info: ComponentInfo): TextComponent {
        var color = rawText.split(":").getOrNull(1)
        if (color.isNullOrEmpty()) {
            color = "#FFFFFF"
        }

        val colorDisplayComp = TextComponent(color)
        colorDisplayComp.color = ChatColor.of(color)
        colorDisplayComp.clickEvent = ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, color)
        colorDisplayComp.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("Click to copy color!"))
        return colorDisplayComp
    }
}
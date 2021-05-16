package dev.xyze.namecolor.componentplaceholder.segment

import dev.xyze.namecolor.componentplaceholder.ComponentInfo
import net.md_5.bungee.api.chat.TextComponent

class MessageSegment(override val rawText: String) : ComponentSegment {
    override fun toComponent(info: ComponentInfo): TextComponent {
        val fComponent = TextComponent()
        TextComponent.fromLegacyText(info.message).forEach { fComponent.addExtra(it) }
        return fComponent
    }
}
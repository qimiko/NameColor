package dev.xyze.namecolor.componentplaceholder.segment

import dev.xyze.namecolor.componentplaceholder.ComponentInfo
import net.md_5.bungee.api.chat.TextComponent

class UnknownSegment(override val rawText: String) : ComponentSegment {
    override fun toComponent(info: ComponentInfo): TextComponent {
        return TextComponent("{np!$rawText}")
    }
}
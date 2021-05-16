package dev.xyze.namecolor.componentplaceholder.segment

import dev.xyze.namecolor.componentplaceholder.ComponentInfo
import net.md_5.bungee.api.chat.TextComponent

interface ComponentSegment {
    val rawText: String

    fun toComponent(info: ComponentInfo): TextComponent
}
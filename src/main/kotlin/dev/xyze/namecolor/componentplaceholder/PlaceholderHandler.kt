package dev.xyze.namecolor.componentplaceholder

import dev.xyze.namecolor.NameColor
import dev.xyze.namecolor.componentplaceholder.segment.*
import net.md_5.bungee.api.chat.TextComponent
import kotlin.reflect.KFunction1

object PlaceholderHandler {
    private const val NAMESPACE = "nc"

    enum class Placeholder(val callback: KFunction1<String, ComponentSegment>) {
        PLAYER(::NameSegment),
        MSG(::MessageSegment),
        COLOR(::ColorInfoSegment),
    }

    fun getPlaceholderTag(placeholder: Placeholder): String {
        return "{$NAMESPACE:${placeholder.name}}"
    }

    fun replacePlaceholderInString(plugin: NameColor, msg: String, info: ComponentInfo): TextComponent {
        val finalComponent = TextComponent()
        placeholderStringToList(msg).forEach {
            finalComponent.addExtra(it.toComponent(plugin, info))
        }

        return finalComponent
    }

    private fun placeholderStringToList(msg: String): List<ComponentSegment> {
        // match to a list of previous, tag
        val matchRegex = Regex("(.*?)(?<!\\\\)\\{${Regex.escape(NAMESPACE)}:([\\S]+)}")
        val matchedList = mutableListOf<ComponentSegment>()
        // keep track of the final value as well
        var maxMatch = 0
        matchRegex.findAll(msg).forEach {
            matchedList.add(BasicTextSegment(it.groupValues[1]))
            try {
                // arguments placed in colons
                matchedList.add(
                    Placeholder.valueOf(
                        it.groupValues[2].uppercase().split(":")[0]
                    ).callback.invoke(it.groupValues[2])
                )
            } catch (e: IllegalArgumentException) {
                matchedList.add(UnknownSegment(it.groupValues[2]))
            }
            // add the final string at the end
            maxMatch = Integer.max(it.range.last, 0)
        }
        matchedList.add(BasicTextSegment(msg.substring(maxMatch + 1, msg.length)))

        return matchedList
    }
}
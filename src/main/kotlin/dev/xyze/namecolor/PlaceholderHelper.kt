package dev.xyze.namecolor

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.lang.Integer.max

object PlaceholderHelper {
    private data class ComponentInformation(val player: Player, val message: String)

    private const val NAMESPACE = "nc"
    private val REGISTERED_PLACEHOLDERS = hashMapOf<String, (ComponentInformation) -> TextComponent>(
        "player" to ::generatePlayerComponent,
        "msg" to ::generateMessageComponent
    )

    fun validateColor(color: String): Boolean {
        val colorMatcher = Regex("^#([a-fA-F0-9]{6})$")
        return color matches colorMatcher
    }

    fun replacePlaceholderInString(msg: String, player: Player, player_msg: String): TextComponent {
        val finalComponent = TextComponent()
        placeholderStringToPair(msg).forEach {
            finalComponent.addExtra(generateMessageComponent(ComponentInformation(player, it.first)))
            if (!it.second.isNullOrBlank()) {
                if (it.second in REGISTERED_PLACEHOLDERS) {
                    finalComponent.addExtra(
                        REGISTERED_PLACEHOLDERS[it.second]?.invoke(
                            ComponentInformation(
                                player,
                                player_msg
                            )
                        )
                    )
                } else {
                    finalComponent.addExtra(TextComponent("!${it.second}!"))
                }
            }
        }

        return finalComponent
    }

    private fun placeholderStringToPair(msg: String): List<Pair<String, String?>> {
        val matchRegex = Regex("(.*?)(?<!\\\\)\\{${Regex.escape(NAMESPACE)}:([\\S]+)}")
        val matchedList = mutableListOf<Pair<String, String?>>()
        var maxMatch = 0
        matchRegex.findAll(msg).forEach {
            matchedList.add(Pair(it.groupValues[1], it.groupValues[2]))
            maxMatch = max(it.range.last, 0)
        }
        matchedList.add(Pair(msg.substring(maxMatch + 1, msg.length), null))

        return matchedList
    }

    private fun generateMessageComponent(info: ComponentInformation): TextComponent {
        var newMsg = info.message
        REGISTERED_PLACEHOLDERS.forEach {
            newMsg = newMsg.replace("\\$NAMESPACE:${it.key}", "$NAMESPACE:${it.key}")
        }
        val fComponent = TextComponent()
        TextComponent.fromLegacyText(newMsg).forEach { fComponent.addExtra(it) }
        return fComponent
    }

    private fun generatePlayerComponent(info: ComponentInformation): TextComponent {
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
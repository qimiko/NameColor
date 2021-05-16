package dev.xyze.namecolor

import org.bukkit.configuration.Configuration
import java.util.*

class PlayerData(private val defaultColor: String) {
    private val storedColors = HashMap<UUID, String>()

    fun getPlayerColor(player: UUID): String {
        return storedColors.getOrDefault(player, defaultColor)
    }

    fun setPlayerColor(player: UUID, color: String) {
        storedColors[player] = color
    }

    fun resetPlayerColor(player: UUID) {
        storedColors.remove(player)
    }

    fun storeInConfig(config: Configuration) {
        config.createSection("players", storedColors)
    }

    fun getFromConfig(config: Configuration) {
        val configSection = config.getConfigurationSection("players")?.getValues(false)

        configSection?.forEach {
            setPlayerColor(UUID.fromString(it.key), it.value as String)
        }
    }
}
package dev.xyze.namecolor

import org.bukkit.configuration.Configuration
import java.util.*

class PlayerData(private val defaultColor: String, private val defaultPrefix: String) {
    private val storedColors = HashMap<UUID, String>()
    private val storedPrefixes = HashMap<UUID, String>()

    fun getPlayerColor(player: UUID): String {
        return storedColors.getOrDefault(player, defaultColor)
    }

    fun getPlayerPrefix(player: UUID): String {
        return storedPrefixes.getOrDefault(player, defaultPrefix)
    }

    fun setPlayerColor(player: UUID, color: String) {
        storedColors[player] = color
    }

    fun setPlayerPrefix(player: UUID, prefix: String) {
        storedPrefixes[player] = prefix
    }

    fun resetPlayerColor(player: UUID) {
        storedColors.remove(player)
    }

    fun resetPlayerPrefix(player: UUID) {
        storedPrefixes.remove(player)
    }

    fun storeInConfig(config: Configuration) {
        config.createSection("players", storedColors)
        config.createSection("prefixes", storedPrefixes)
    }

    fun getFromConfig(config: Configuration) {
        val configSection = config.getConfigurationSection("players")?.getValues(false)
        configSection?.forEach {
            setPlayerColor(UUID.fromString(it.key), it.value as String)
        }

        val prefixSection = config.getConfigurationSection("prefixes")?.getValues(false)
        prefixSection?.forEach {
            setPlayerPrefix(UUID.fromString(it.key), it.value as String)
        }
    }
}
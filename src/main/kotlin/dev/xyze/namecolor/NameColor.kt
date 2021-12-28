package dev.xyze.namecolor

import dev.xyze.namecolor.commands.NameColorCommand
import dev.xyze.namecolor.commands.NameColorConfigCommand
import dev.xyze.namecolor.util.ColorUtil
import dev.xyze.namecolor.util.CommodoreUtil
import me.lucko.commodore.CommodoreProvider
import org.bukkit.plugin.java.JavaPlugin


class NameColor : JavaPlugin() {
    lateinit var playerDataStorage: PlayerData
        private set

    override fun onEnable() {
        this.server.pluginManager.registerEvents(ChatListener, this)
        val ncCommand = this.getCommand("namecolor")!!
        ncCommand.setExecutor(NameColorCommand(this))

        val ncConfigCommand = this.getCommand("ncconfig")!!
        ncConfigCommand.setExecutor(NameColorConfigCommand(this))

        loadConfig()

        if (CommodoreProvider.isSupported()) {
            val commodore = CommodoreProvider.getCommodore(this)
            CommodoreUtil.registerCommandFromFile(commodore, this, "namecolor.commodore")
            CommodoreUtil.registerCommandFromFile(commodore, this, "ncconfig.commodore")
        }
    }

    fun loadConfig() {
        this.saveDefaultConfig()

        var defaultColor = this.config.getString("default-color")

        if (defaultColor.isNullOrBlank() || !ColorUtil.validateColor(defaultColor)) {
            defaultColor = "#FFFFFF"
        }

        val defaultPrefix = this.config.getString("default-prefix") ?: ""

        playerDataStorage = PlayerData(defaultColor, defaultPrefix)

        playerDataStorage.getFromConfig(this.config)
    }

    override fun onDisable() {
        playerDataStorage.storeInConfig(this.config)
        this.saveConfig()
    }
}
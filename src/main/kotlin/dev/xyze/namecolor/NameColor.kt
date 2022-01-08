package dev.xyze.namecolor

import dev.xyze.namecolor.commands.*
import dev.xyze.namecolor.util.*
import me.lucko.commodore.CommodoreProvider
import org.bukkit.plugin.java.JavaPlugin


class NameColor : JavaPlugin() {
    lateinit var playerDataStorage: PlayerData
        private set

    override fun onEnable() {
        this.server.pluginManager.registerEvents(ChatListener, this)
        val ncCommand = this.getCommand("namecolor")!!
        ncCommand.setExecutor(NameColorCommand(this))

        val npCommand = this.getCommand("nameprefix")!!
        npCommand.setExecutor(NamePrefixCommand(this))

        val ncConfigCommand = this.getCommand("ncconfig")!!
        ncConfigCommand.setExecutor(NameColorConfigCommand(this))

        loadConfig()

        if (CommodoreProvider.isSupported()) {
            val commodore = CommodoreProvider.getCommodore(this)
            CommodoreUtil.registerCommandFromFile(commodore, this, "namecolor.commodore")
            CommodoreUtil.registerCommandFromFile(commodore, this, "nameprefix.commodore")
            CommodoreUtil.registerCommandFromFile(commodore, this, "ncconfig.commodore")
        }
    }

    fun loadConfig() {
        this.saveDefaultConfig()

        var defaultColor = this.config.getString("default-color")

        if (defaultColor.isNullOrBlank() || !ColorUtil.validateColor(defaultColor)) {
            defaultColor = DefaultValues.DEFAULT_COLOR
        }

        val defaultPrefix = this.config.getString("default-prefix") ?: DefaultValues.DEFAULT_PREFIX

        playerDataStorage = PlayerData(defaultColor, defaultPrefix)

        playerDataStorage.getFromConfig(this.config)
    }

    override fun onDisable() {
        playerDataStorage.storeInConfig(this.config)
        this.saveConfig()
    }
}
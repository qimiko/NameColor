package dev.xyze.namecolor

import dev.xyze.namecolor.commands.*
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

        this.saveDefaultConfig()

        var defaultColor = this.config.getString("default-color")
        if (defaultColor.isNullOrBlank() || !ColorUtil.validateColor(defaultColor)) {
            defaultColor = "#FFFFFF"
        }
        playerDataStorage = PlayerData(defaultColor)
        playerDataStorage.getFromConfig(this.config)

        if (CommodoreProvider.isSupported()) {
            val commodore = CommodoreProvider.getCommodore(this)
            CommodoreUtil.registerCommandFromFile(commodore, this, ncCommand, "namecolor.commodore")
            CommodoreUtil.registerCommandFromFile(commodore, this, ncConfigCommand, "ncconfig.commodore")
        }
    }

    override fun onDisable() {
        playerDataStorage.storeInConfig(this.config)
        this.saveConfig()
    }
}
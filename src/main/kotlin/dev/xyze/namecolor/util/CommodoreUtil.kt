package dev.xyze.namecolor.util

import com.mojang.brigadier.tree.LiteralCommandNode
import me.lucko.commodore.Commodore
import me.lucko.commodore.file.CommodoreFileFormat
import org.bukkit.plugin.Plugin

object CommodoreUtil {
    fun registerCommandFromFile(
            commodoreInst: Commodore,
            pluginInst: Plugin,
            fileName: String
    ) {
        var commandFile = CommodoreFileFormat.parse<Any>(pluginInst.getResource(fileName));
        commodoreInst.register(commandFile);
    }
}
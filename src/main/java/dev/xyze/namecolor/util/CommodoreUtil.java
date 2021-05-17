package dev.xyze.namecolor.util;

import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.file.CommodoreFileFormat;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class CommodoreUtil {
    public static void registerCommandFromFile(
            final Commodore commodoreInst,
            final Plugin pluginInst,
            final String fileName
    ) throws IOException {
        // this is provided due to kotlin's design of not providing wildcard types
        LiteralCommandNode<?> commandFile = CommodoreFileFormat.parse(pluginInst.getResource(fileName));
        commodoreInst.register(commandFile);
    }
}

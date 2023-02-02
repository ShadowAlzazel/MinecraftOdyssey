package me.shadowalzazel.mcodyssey

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.io.File
import java.io.FileNotFoundException

interface OdysseyAssetManager : Listener {

    fun registerOdysseyStructures(odysseyPlugin: MinecraftOdyssey) {
        odysseyPlugin.run {
            /*
            try {
                val worldFolder = mainWorld!!.worldFolder
                val structureDirectory = worldFolder.path + "/datapacks/OdysseyDataPack/data/minecraftodyssey/structures"

                // Make for loop later
                val stonePillars1File = File("$structureDirectory/stone_pillars/stone_pillars_1.nbt")
                val idescineTreeFile = File("$structureDirectory/idescine/idescine_tree.nbt")

                with(server.structureManager) {
                    registerStructure(NamespacedKey(MinecraftOdyssey.instance, "stone_pillars_1"), loadStructure(stonePillars1File))
                    registerStructure(NamespacedKey(MinecraftOdyssey.instance, "idescine_tree"), loadStructure(idescineTreeFile))

                }

                // Place armor stands with names inv and tags when bottle consume fruit
            }
            catch(ex: FileNotFoundException) {
                logger.info(ex.message)
            }

             */
        }
    }

    // TODO: MAKE METHODS!!

}
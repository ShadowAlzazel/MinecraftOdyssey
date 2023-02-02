package me.shadowalzazel.mcodyssey

import org.bukkit.NamespacedKey
import java.io.File
import java.io.FileNotFoundException

interface OdysseyAssetManager {

    fun registerOdysseyStructures(mcodysseyPlugin: MinecraftOdyssey) {
        mcodysseyPlugin.run {
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
        }
    }





}
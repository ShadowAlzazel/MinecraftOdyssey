package me.shadowalzazel.mcodyssey

import java.io.FileNotFoundException

interface AssetManager {

    fun registerOdysseyStructures(odysseyPlugin: Odyssey) {
        odysseyPlugin.run {
            /*
            try {
                val worldFolder = mainWorld!!.worldFolder
                val structureDirectory = worldFolder.path + "/datapacks/OdysseyDataPack/data/minecraftodyssey/structures"

                // Make for loop later
                val stonePillars1File = File("$structureDirectory/stone_pillars/stone_pillars_1.nbt")
                val idescineTreeFile = File("$structureDirectory/idescine/idescine_tree.nbt")

                with(server.structureManager) {
                    registerStructure(NamespacedKey(Odyssey.instance, "stone_pillars_1"), loadStructure(stonePillars1File))
                    registerStructure(NamespacedKey(Odyssey.instance, "idescine_tree"), loadStructure(idescineTreeFile))

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



    fun registerItems(odysseyPlugin: Odyssey) {
        try {
            // TODO: Get On Init
            val datapackPath = "datapacks/OdysseyDataPack/data/odyssey"

            // CREATE ENUMS FOR EACH ODYSSEY ITEM







        }
        catch(ex: FileNotFoundException) {
            odysseyPlugin.logger.info(ex.message)
        }



    }

    fun registerOccurrences(odysseyPlugin: Odyssey) {





    }



}
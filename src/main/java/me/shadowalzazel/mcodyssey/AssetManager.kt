package me.shadowalzazel.mcodyssey

import java.io.File
import java.io.FileNotFoundException


interface AssetManager {

    fun findMainWorld(odysseyPlugin: Odyssey): Boolean {
        for (world in odysseyPlugin.server.worlds) {
            val folderPath = world.worldFolder
            try {
                val dataPackFolderPath = File("${folderPath}/datapacks")
                odysseyPlugin.mainWorld = world
                return true
            }
            catch (ex: FileNotFoundException) {
                continue
            }
        }

        return false

    }


    fun findOdysseyDatapack(odysseyPlugin: Odyssey): Boolean {

        val worldFilePath = odysseyPlugin.mainWorld!!.worldFolder.path
        val datapackPathString = "/datapacks/OdysseyDataPack"

        return try {
            val datapackMetaFile = File("${worldFilePath + datapackPathString}/pack.mcmeta")
            true
        } catch (ex: FileNotFoundException) {
            odysseyPlugin.logger.info(ex.message)
            false
        }

    }


    fun findOtherDatapacks(odysseyPlugin: Odyssey) {
        val worldFilePath = odysseyPlugin.mainWorld!!.worldFolder.path
        try {

        }
        catch (ex: FileNotFoundException) {
            odysseyPlugin.logger.info(ex.message)
        }
    }

}
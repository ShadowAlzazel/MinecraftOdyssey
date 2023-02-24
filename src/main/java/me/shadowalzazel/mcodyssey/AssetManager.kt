package me.shadowalzazel.mcodyssey

import java.io.File
import java.io.FileNotFoundException


interface AssetManager {

    fun findMainWorld(): Boolean {
        for (world in Odyssey.instance.server.worlds) {
            val folderPath = world.worldFolder
            try {
                val dataPackFolderPath = File("${folderPath}/datapacks")
                Odyssey.instance.mainWorld = world
                return true
            }
            catch (ex: FileNotFoundException) {
                continue
            }
        }
        return false
    }


    fun findOdysseyDatapack(): Boolean {
        val worldFilePath = Odyssey.instance.mainWorld!!.worldFolder.path
        val datapackPathString = "/datapacks/OdysseyDataPack"

        return try {
            val datapackMetaFile = File("${worldFilePath + datapackPathString}/pack.mcmeta")
            true
        } catch (ex: FileNotFoundException) {
            Odyssey.instance.logger.info(ex.message)
            false
        }
    }


    fun findOtherDatapacks() {
        val worldFilePath = Odyssey.instance.mainWorld!!.worldFolder.path
        try {
        }
        catch (ex: FileNotFoundException) {
            Odyssey.instance.logger.info(ex.message)
        }
    }

}
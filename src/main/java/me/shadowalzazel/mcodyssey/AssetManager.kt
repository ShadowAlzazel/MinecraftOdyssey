package me.shadowalzazel.mcodyssey

import java.io.File
import java.io.FileNotFoundException


class AssetManager(val odyssey: Odyssey) {

    /*
    private val hasTerralith: Boolean = odyssey.overworld.isBedWorks
    private val hasIncendium: Boolean = odyssey.overworld.isBedWorks
    val hasContinents: Boolean = odyssey.overworld.isBedWorks

     */

    fun findOdysseyDatapack(): Boolean {
        val worldFilePath = Odyssey.instance.overworld.worldFolder.path
        val datapackPathString = "/datapacks/OdysseyDataPack"

        return try {
            val datapackMetaFile = File("${worldFilePath + datapackPathString}/pack.mcmeta")
            true
        } catch (ex: FileNotFoundException) {
            Odyssey.instance.logger.info(ex.message)
            false
        }
    }
}
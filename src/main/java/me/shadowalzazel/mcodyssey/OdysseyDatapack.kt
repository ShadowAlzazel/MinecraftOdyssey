package me.shadowalzazel.mcodyssey

import java.io.File
import java.io.FileNotFoundException


class OdysseyDatapack(val odyssey: Odyssey) {

    fun findOdysseyDatapack(): Boolean {
        val worldFilePath = Odyssey.instance.overworld.worldFolder.path
        val datapackPathString = "/datapacks/OdysseyDataPack"
        var foundSuccessful = false

        try {
            val datapackMetaFile = File("${worldFilePath + datapackPathString}/pack.mcmeta")
            foundSuccessful = true
        } catch (ex: FileNotFoundException) {
            Odyssey.instance.logger.info(ex.message)
            foundSuccessful = false
        }
        // DO a LOOP 3 times to find pack

        return foundSuccessful
    }
}
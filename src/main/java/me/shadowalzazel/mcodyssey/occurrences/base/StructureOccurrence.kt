package me.shadowalzazel.mcodyssey.occurrences.base

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.generator.structure.Structure
import org.bukkit.generator.structure.StructureType

interface StructureOccurrence {


    private fun locateStructures(someWorld: World, someLocation: Location, matchingStructures: List<Structure>, matchingType: List<StructureType>): List<Structure> {
        val located: MutableList<Structure> = mutableListOf()
        for (t in matchingType) {
            val searchResult = someWorld.locateNearestStructure(someLocation, t, 16 * 48, false)
            if (searchResult?.structure in matchingStructures) located.add(searchResult!!.structure)
        }
        return located
    }







}
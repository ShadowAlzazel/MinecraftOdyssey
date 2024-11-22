package me.shadowalzazel.mcodyssey.util

import org.bukkit.entity.LivingEntity
import org.bukkit.generator.structure.Structure

interface StructureManager {

    fun getBoundedStructures(entity: LivingEntity): List<Structure>? {
        val chunkStructures = entity.location.chunk.structures
        if (chunkStructures.isEmpty()) return null
        val structuresInside = chunkStructures.filter { entity.boundingBox.overlaps(it.boundingBox) }
        if (structuresInside.isEmpty()) return null
        // Add to list to return
        val finalList = mutableListOf<Structure>()
        for (structure in structuresInside) {
            finalList.add(structure.structure)
        }
        // Return null if empty
        return if (finalList.isEmpty()) {
            null
        } else {
            finalList
        }
    }


    fun entityInsideStructure(entity: LivingEntity, structure: Structure): Boolean {
        val boundedStructures = getBoundedStructures(entity) ?: return false
        for (struct in boundedStructures) {
            if (struct == structure) return true
        }
        return false
    }

    fun entityInsideStructures(entity: LivingEntity, structureList: List<Structure>): Boolean {
        val boundedStructures = getBoundedStructures(entity) ?: return false
        for (struct in boundedStructures) {
            if (struct in structureList) return true
        }
        return false
    }


}
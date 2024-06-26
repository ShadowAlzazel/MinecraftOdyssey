package me.shadowalzazel.mcodyssey.occurrences.other

import me.shadowalzazel.mcodyssey.occurrences.base.OccurrenceType
import net.kyori.adventure.text.TextComponent
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.generator.structure.Structure
import org.bukkit.util.StructureSearchResult

open class OldOccurrence(
    val situationName: String,
    val situationType: OccurrenceType,
    private val affectedStructures: List<Structure>,
    private val announcement: TextComponent) {

    private fun locateApplicableStructure(someWorld: World, someLocation: Location): StructureSearchResult? {
        var applicableStructure: StructureSearchResult? = null
        for (someBaseStructure in affectedStructures) {
            applicableStructure = someWorld.locateNearestStructure(someLocation, someBaseStructure.structureType, 16 * 50, false)
            if (applicableStructure != null) { break }
        }
        return applicableStructure
    }

    fun runSituation(someLocation: Location): Boolean {
        val someStructure = locateApplicableStructure(someLocation.world, someLocation)
        someStructure?.let {
            if (checkRequirements(someStructure.location)) {
                situationEffects(someStructure.location)
                return true
            }
        }
        // Fail to locate
        return false
    }

    protected open fun checkRequirements(structureLocation: Location): Boolean {
        return false
    }

    protected open fun situationEffects(structureLocation: Location) {
        structureLocation.getNearbyPlayers(16 * 60.0).forEach{ it.sendMessage(announcement) }
    }


}
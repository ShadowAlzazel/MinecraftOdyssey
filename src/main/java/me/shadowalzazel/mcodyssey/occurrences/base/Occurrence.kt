package me.shadowalzazel.mcodyssey.occurrences.base

import me.shadowalzazel.mcodyssey.seasons.SeasonType
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.generator.structure.Structure
import org.bukkit.generator.structure.StructureType

// The occurrence type dictates what cycle to put it in; which corresponds to rate

open class Occurrence (
    val name: String,
    val oType: OccurrenceType,
    private val negatableByAllay: Boolean,
    private val requirements: List<Requirement>,
    private val criteria: Map<String, List<Condition>>,
    private val persistentActions: List<OccurrenceAction>,
    private val spawningActions: List<OccurrenceAction>,
    private val oneTimeActions: List<OccurrenceAction>
    ) {

    fun persistentHandler(someEntities: List<LivingEntity>) {
        // Used by Task Class for persistent async effects
        // Add to list, Run on branch, if timer ready, apply to bukkit, else continue
        someEntities.forEach {
            multiActionHandler(it, persistentActions)
        }
    }

    fun spawningHandler(someEntities: List<LivingEntity>) {
        // Used By Events
        someEntities.forEach {
            multiActionHandler(it, spawningActions)
        }
    }

    fun oneTimeHandler(someEntities: List<LivingEntity>) {
        // Used By One Time Syncer
        someEntities.forEach {
            multiActionHandler(it, oneTimeActions)
        }
    }

    private fun multiActionHandler(someEntity: LivingEntity, actionList: List<OccurrenceAction>) {
        // FOR all x in occurrenceActions list of obj
        // IF entity passed criteria_name of x apply all entries
        // Maybe Random or Weighted
        actionList.forEach { if (it.criteriaMetBy(someEntity)) it.applyAction(someEntity) }
    }

    private fun OccurrenceAction.criteriaMetBy(someEntity: LivingEntity): Boolean {
        // Allay checked first
        val allayNearby: Boolean = negatableByAllay && someEntity.getNearbyEntities(16.0, 16.0, 16.0).any { it.type == EntityType.ALLAY }
        return !allayNearby && criteria[this.criteriaName]!!.all { it.checkCondition(someEntity) }
    }

    fun requirementHandler(someSeason: SeasonType, someWorld: World) : Boolean {
        // Used to activate and put into activation
        return requirements.all { it.checkRequirement(someSeason, someWorld) }
    }

    fun locateStructures(someWorld: World, someLocation: Location, matchingStructures: List<Structure>, matchingType: List<StructureType>): List<Structure> {
        val located: MutableList<Structure> = mutableListOf()
        for (t in matchingType) {
            val searchResult = someWorld.locateNearestStructure(someLocation, t, 16 * 48, false)
            if (searchResult?.structure in matchingStructures) located.add(searchResult!!.structure)
        }
        return located
    }


    // IDEAS:
    // If new day/ something else
    // If player near village structure
    // If village inhabited
    // not in cooldown
    // Do some OldOccurrence, send rumors to player
    // Ideas: Market Sale, Raid, Nether Invasion
    // If Allay nearby lower chance

    // Make Announcement show village name
    // If structure in list, send name, else add name to list and counters!

}
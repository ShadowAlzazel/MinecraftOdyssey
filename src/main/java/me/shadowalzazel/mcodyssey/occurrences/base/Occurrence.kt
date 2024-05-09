package me.shadowalzazel.mcodyssey.occurrences.base

import me.shadowalzazel.mcodyssey.world_events.utility.EntityConditions
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.generator.structure.Structure
import org.bukkit.generator.structure.StructureType

// The occurrence type dictates what cycle to put it in; which corresponds to rate

open class Occurrence (
    val name: String,
    val mainType: OccurrenceType,
    private val negatableByAllay: Boolean,
    private val requirements: List<Requirement>,
    private val criteria: Map<String, List<EntityConditions>>,
    private val persistentActions: Map<String, List<OccurrenceAction>>,
    private val spawningActions: Map<String, List<OccurrenceAction>>,
    private val oneTimeActions: Map<String, List<OccurrenceAction>>
    ) {

    fun persistentActionHandler(someEntities: List<LivingEntity>) {
        // Used by Task Class for persistent async effects
        // Add to list, Run on branch, if timer ready, apply to bukkit, else continue
        someEntities.forEach { entity ->
            for (aKey in persistentActions.keys) {
                if (criteriaUnlocked(entity, aKey)) {
                    persistentActions[aKey]!!.forEach { it.applyAction(someEntity = entity) }
                }
            }
        }
    }


    fun spawningActionHandler(someEntities: List<LivingEntity>) {
        // Used By Events
        someEntities.forEach { entity ->
            for (aKey in spawningActions.keys) {
                if (criteriaUnlocked(entity, aKey)) {
                    spawningActions[aKey]!!.forEach { it.applyAction(someEntity = entity) }
                }
            }
        }
    }

    fun oneTimeActionHandler(someEntities: List<LivingEntity>) {
        // Used By One Time Syncer
        someEntities.forEach { entity ->
            for (aKey in oneTimeActions.keys) {
                if (criteriaUnlocked(entity, aKey)) {
                    oneTimeActions[aKey]!!.forEach { it.applyAction(someEntity = entity) }
                }
            }
        }
    }

    fun requirementHandler(someWorld: World) : Boolean {
        // Used to activate and put into activation
        return false
    }


    // Probably Expensive to run
    private fun criteriaUnlocked(someEntity: LivingEntity, someKey: String): Boolean {
        // Allay checked first
        // CURRENTLY -> allay affects EVERY EntityConditions and Action
        val allayNearby: Boolean = negatableByAllay && someEntity.getNearbyEntities(16.0, 16.0, 16.0).any { it.type == EntityType.ALLAY }
        return !allayNearby && criteria[someKey]!!.all { it.checkCondition(someEntity) }
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
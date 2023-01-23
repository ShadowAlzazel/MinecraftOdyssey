package me.shadowalzazel.mcodyssey.listeners

import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import kotlin.math.absoluteValue
import kotlin.math.pow

object OdysseySpawningListeners : Listener {

    // Farther away from spawn, stronger mobs
    private fun enhanceDifficultyMobHandler(mobLocation: Location) {

        val planarLocation = mobLocation.clone().also {
            it.toBlockLocation()
            it.y = 0.0
        }

        val zeroLocation = planarLocation.clone().zero()
        val zeroDistance = planarLocation.distance(zeroLocation).absoluteValue

        val healthF = (0.001 * zeroDistance) + (0.001 * zeroDistance).pow(2)

        // table for effectiveness, multiply to get final
        // Zombie -> 0.85%
        // Witch -> 1.2%



    }





    @EventHandler(priority = EventPriority.LOW)
    fun mainSpawningHandler(event: CreatureSpawnEvent) {
        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL) {
            enhanceDifficultyMobHandler(event.location)
        }
    }



}
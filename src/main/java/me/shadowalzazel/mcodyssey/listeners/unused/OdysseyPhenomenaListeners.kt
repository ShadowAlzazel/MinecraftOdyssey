package me.shadowalzazel.mcodyssey.listeners.unused

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.phenomenon.SuenPhenomena
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerBedEnterEvent

@Deprecated(message = "No Longer Using Registry")
object OdysseyPhenomenaListeners : Listener {

    // Function to prevent players from sleeping
    @EventHandler
    fun playerPreventSleep(event: PlayerBedEnterEvent) {
        // Check if ambassador defeated
        // is Odyssey.instance.isEnderDragonDefeated
        if (Odyssey.instance.isLunarPhenomenonActive) {
            val someWorld = event.player.world
            if (someWorld == Odyssey.instance.overworld) {
                event.player.sendMessage("The night prevents you from sleeping.")
                event.isCancelled = true
            }
        }
    }


    // Main function for creature related spawns regarding phenomena
    @EventHandler
    fun mainEntityPhenomenaSpawning(event: CreatureSpawnEvent) {
        if (Odyssey.instance.isLunarPhenomenonActive) {
            val someWorld = event.entity.world
            if (someWorld.environment == World.Environment.NORMAL) {
                when (Odyssey.instance.currentLunarPhenomenon) {
                    // Blue Moon
                    SuenPhenomena.BLUE_MOON -> {
                        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL || event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS) {
                            event.isCancelled = true
                        }
                    }
                    // Blood Moon
                    SuenPhenomena.BLOOD_MOON -> {
                        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL || event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS) {
                            SuenPhenomena.BLOOD_MOON.persistentSpawningActives(event.entity)
                        }
                    }
                    SuenPhenomena.DANCE_OF_THE_BIOLUMINESCENT -> {
                        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL || event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS) {
                            SuenPhenomena.DANCE_OF_THE_BIOLUMINESCENT.persistentSpawningActives(event.entity)
                        }
                    }
                    SuenPhenomena.STARRY_NIGHT -> {
                        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL || event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS) {
                            SuenPhenomena.STARRY_NIGHT.persistentSpawningActives(event.entity)
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }



}
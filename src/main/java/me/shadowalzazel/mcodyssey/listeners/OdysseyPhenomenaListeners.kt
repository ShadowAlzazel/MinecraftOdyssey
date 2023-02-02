package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.phenomenon.SuenPhenomena
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerBedEnterEvent

object OdysseyPhenomenaListeners : Listener {

    // Function to prevent players from sleeping
    @EventHandler
    fun playerPreventSleep(event: PlayerBedEnterEvent) {
        // Check if ambassador defeated
        if (MinecraftOdyssey.instance.isEnderDragonDefeated) {
            if (MinecraftOdyssey.instance.isLunarPhenomenonActive) {
                val someWorld = event.player.world
                if (someWorld == MinecraftOdyssey.instance.mainWorld) {
                    event.player.sendMessage("The night prevents you from sleeping.")
                    event.isCancelled = true
                }
            }
        }
    }


    // Main function for creature related spawns regarding phenomena
    @EventHandler
    fun mainEntityPhenomenaSpawning(event: CreatureSpawnEvent) {
        if (MinecraftOdyssey.instance.isLunarPhenomenonActive) {
            val someWorld = event.entity.world
            if (someWorld.environment == World.Environment.NORMAL) {
                when (MinecraftOdyssey.instance.currentLunarPhenomenon) {
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
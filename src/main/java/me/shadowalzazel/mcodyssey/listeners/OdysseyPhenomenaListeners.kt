package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.mobs.OdysseyMobs
import me.shadowalzazel.mcodyssey.phenomenon.SuenPhenomena
import me.shadowalzazel.mcodyssey.phenomenon.utility.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.World
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Skeleton
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object OdysseyPhenomenaListeners : Listener {

    // Function to prevent players from sleeping
    @EventHandler
    fun playerPreventSleep(event: PlayerBedEnterEvent) {
        // Check if ambassador defeated
        if (MinecraftOdyssey.instance.enderDragonDefeated) {
            if (MinecraftOdyssey.instance.suenPhenomenonActive) {
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
        if (MinecraftOdyssey.instance.suenPhenomenonActive) {
            val someWorld = event.entity.world
            if (someWorld.environment == World.Environment.NORMAL) {
                when (MinecraftOdyssey.instance.currentSuenPhenomenon) {
                    // Blue Moon
                    SuenPhenomena.BLUE_MOON -> {
                        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL || event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS) {
                            event.isCancelled = true
                        }
                    }
                    // Blood Moon
                    SuenPhenomena.BLOOD_MOON -> {
                        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL || event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS) {
                            // TODO: Light Level and Y level
                            SuenPhenomena.BLOOD_MOON.persistentSpawningActives(event.entity)
                        }
                    }
                    SuenPhenomena.DANCE_OF_THE_BIOLUMINESCENT -> {
                        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL || event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS) {
                            // TODO: Light Level and Y level
                            SuenPhenomena.DANCE_OF_THE_BIOLUMINESCENT.persistentSpawningActives(event.entity)
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }



}
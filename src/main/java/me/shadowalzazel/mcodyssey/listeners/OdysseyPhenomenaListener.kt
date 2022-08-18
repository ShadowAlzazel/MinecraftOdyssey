package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.mobs.OdysseyMobs
import me.shadowalzazel.mcodyssey.phenomenon.LunarPhenomena
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

object OdysseyPhenomenaListener : Listener {

    // Function to prevent players from sleeping
    @EventHandler
    fun playerPreventSleep(event: PlayerBedEnterEvent) {
        // Check if ambassador defeated
        if (MinecraftOdyssey.instance.ambassadorDefeated) {
            if (MinecraftOdyssey.instance.lunarPhenomenonActive) {
                val someWorld = event.player.world
                if (someWorld.environment == World.Environment.NORMAL) {
                    event.isCancelled = true
                    event.player.sendMessage("The night prevents you from sleeping.")
                }
            }
        }
    }


    // Main function for creature related spawns regarding phenomena
    @EventHandler
    fun mainEntityPhenomenaSpawning(event: CreatureSpawnEvent) {
        if (MinecraftOdyssey.instance.lunarPhenomenonActive) {
            val someWorld = event.entity.world
            if (someWorld.environment == World.Environment.NORMAL) {
                when (MinecraftOdyssey.instance.currentLunarPhenomenon) {
                    // Blue Moon
                    LunarPhenomena.BLUE_MOON -> {
                        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL || event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS) {
                            event.isCancelled = true
                        }
                    }
                    // Blood Moon
                    LunarPhenomena.BLOOD_MOON -> {
                        // Effects
                        val bloodMoonEffects = listOf(
                            PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300 * 20,1),
                            PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300 * 20,1),
                            PotionEffect(PotionEffectType.HEALTH_BOOST, 300 * 20,4)
                        )
                        // Check Spawn Reason
                        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL || event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS) {
                            // Naming and Tagging
                            val someEntity = event.entity
                            val someName = someEntity.name
                            val newName = "§4Blood Moon $someName"
                            someEntity.customName = newName
                            someEntity.scoreboardTags.add("Blood_Moon_Mob")
                            // Effects and Health
                            someEntity.addPotionEffects(bloodMoonEffects)
                            someEntity.health += 20
                            // Random Upgrade
                            val randomUpgrade = (0..100).random()
                            if (60 < randomUpgrade) {
                                val spawnLocation = someEntity.location.clone()
                                if (spawnLocation.y > 68.0) {
                                    var someMob: LivingEntity? = null
                                    when (someEntity) {
                                        is Zombie -> {
                                            someEntity.remove()
                                            someMob = if (90 < randomUpgrade) OdysseyMobs.SAVAGE.createKnight(someWorld, spawnLocation) else OdysseyMobs.SAVAGE.createMob(someWorld, spawnLocation)
                                        }
                                        is Skeleton -> {
                                            someEntity.remove()
                                            someMob = if (90 < randomUpgrade) OdysseyMobs.VANGUARD.createKnight(someWorld, spawnLocation) else OdysseyMobs.VANGUARD.createMob(someWorld, spawnLocation)
                                        }
                                        else -> {
                                        }
                                    }
                                    someMob?.scoreboardTags?.add("Blood_Moon_Mob")
                                }
                            }
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }


}
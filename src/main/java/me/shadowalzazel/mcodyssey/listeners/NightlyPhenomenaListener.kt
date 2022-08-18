package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.mobs.OdysseyMobs
import me.shadowalzazel.mcodyssey.phenomenon.*
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Skeleton
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object NightlyPhenomenaListener : Listener {

    // Cool Down timers
    private var cooldown : Long = 0
    private val cooldownTimer = 10000 //10000 -> 10 sec

    private var cooldownNightPhenomenon: Long = 0
    private val cooldownNightTimer = 600 * 20 // 10 minutes -> 600 * 20

    @EventHandler
    fun onNightTime(event: PlayerBedEnterEvent) {
        // Check if ambassador defeated
        if (MinecraftOdyssey.instance.ambassadorDefeated) {
            // Check if new day
            val dayElapsed = event.player.world.fullTime - cooldownNightPhenomenon
            if (dayElapsed > cooldownNightTimer) {
                MinecraftOdyssey.instance.nightlyPhenomenonActive = false
                MinecraftOdyssey.instance.currentNightlyPhenomenon = null
            }
            // Check current world time
            val currentWorld = event.player.world
            if (currentWorld.time > 12000) {
                // Check if nightly active
                if (!MinecraftOdyssey.instance.nightlyPhenomenonActive) {
                    // Nightly Event Change
                    val timeElapsedMC = event.player.world.fullTime - cooldownNightPhenomenon
                    if (timeElapsedMC > cooldownNightTimer) {
                        cooldownNightPhenomenon = currentWorld.fullTime
                        // Event Cool down timer
                        val timeElapsed = System.currentTimeMillis() - cooldown
                        if (timeElapsed >= cooldownTimer) {
                            cooldown = System.currentTimeMillis()
                            // Calculate if nightly phenomenon
                            val randomNightlyPhenomenon = NightlyPhenomena.phenomenaList.random()
                            val rolledRate = (0..100).random()
                            val phenomenonActivated: Boolean = randomNightlyPhenomenon.phenomenonActivation(currentWorld, rolledRate)
                            if (phenomenonActivated) {
                                MinecraftOdyssey.instance.nightlyPhenomenonActive = true
                                MinecraftOdyssey.instance.currentNightlyPhenomenon = randomNightlyPhenomenon
                                event.isCancelled = true
                            }
                        }
                    }
                }
                else {
                    event.isCancelled = true
                    event.player.sendMessage("The night prevents you from sleeping.")
                }
            }
        }
    }



    // Main function for creature related spawns regarding phenomena
    @EventHandler
    fun mainEntityPhenomenaSpawning(event: CreatureSpawnEvent) {
        if (MinecraftOdyssey.instance.nightlyPhenomenonActive) {
            val someWorld = event.entity.world
            val timeElapsedWorld = someWorld.fullTime - cooldownNightPhenomenon
            if (someWorld.time > 12000 && timeElapsedWorld <= cooldownNightTimer && someWorld.isBedWorks) {
                when (MinecraftOdyssey.instance.currentNightlyPhenomenon) {
                    // Blue Moon
                    NightlyPhenomena.BLUE_MOON -> {
                        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL || event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS) {
                            event.isCancelled
                        }
                    }
                    // Blood Moon
                    NightlyPhenomena.BLOOD_MOON -> {
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
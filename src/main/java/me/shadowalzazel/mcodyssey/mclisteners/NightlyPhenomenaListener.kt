package me.shadowalzazel.mcodyssey.mclisteners

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
    private val cooldownNightTimer = 6 * 20 // 10 minutes -> 600 * 20

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

    @EventHandler
    fun entitySpawning(event: CreatureSpawnEvent) {
        if (MinecraftOdyssey.instance.nightlyPhenomenonActive) {
            val someWorld = event.entity.world
            val timeElapsedWorld = someWorld.fullTime - cooldownNightPhenomenon
            if (someWorld.time > 12000 && timeElapsedWorld <= cooldownNightTimer && someWorld.isBedWorks) {
                when (MinecraftOdyssey.instance.currentNightlyPhenomenon) {
                    NightlyPhenomena.BLUE_MOON -> {
                        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL || event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS) {
                            event.isCancelled
                        }
                    }
                    NightlyPhenomena.BLOOD_MOON -> {
                        val bloodMoonEffects = listOf(
                            PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300 * 20,1),
                            PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300 * 20,1),
                            PotionEffect(PotionEffectType.HEALTH_BOOST, 300 * 20,4))
                        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL || event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS) {
                            val someEntity = event.entity
                            val someName = someEntity.name
                            someEntity.addPotionEffects(bloodMoonEffects)
                            someEntity.health += 20
                            val newName = "§4Blood Moon $someName"
                            someEntity.customName = newName
                            someEntity.scoreboardTags.add("Blood_Moon_Mob")
                            val randomUpgrade = (0..100).random()
                            if (60 < randomUpgrade) {
                                val spawnLocation = someEntity.location.clone()
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
                    else -> {
                    }
                }
            }
        }
    }


}
package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.mobs.OdysseyMobs
import me.shadowalzazel.mcodyssey.phenomenon.LunarPhenomena
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

    private val bloodMoonEffects = listOf(
        PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300 * 20,1),
        PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300 * 20,1),
        PotionEffect(PotionEffectType.HEALTH_BOOST, 300 * 20,4)
    )


    // Function to prevent players from sleeping
    @EventHandler
    fun playerPreventSleep(event: PlayerBedEnterEvent) {
        // Check if ambassador defeated
        if (MinecraftOdyssey.instance.enderDragonDefeated) {
            println("S")
            if (MinecraftOdyssey.instance.lunarPhenomenonActive) {
                println("Q")
                val someWorld = event.player.world
                if (someWorld.isBedWorks) {
                    event.player.sendMessage("The night prevents you from sleeping.")
                    event.isCancelled = true
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
                        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL || event.spawnReason == CreatureSpawnEvent.SpawnReason.REINFORCEMENTS) {
                            // TODO: Light Level and Y level
                            println(event.location.block.lightFromSky)
                            println(event.entity)
                            bloodMoonPhenomenonSpawning(event.entity)
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }

    /*---------------------------------------------------------*/
    private fun bloodMoonPhenomenonSpawning(someEntity: LivingEntity) {
        someEntity.also {
            // Naming and Tagging
            if (it.customName() != null) { it.customName(Component.text("Blood Moon ${(it.customName() as TextComponent).content()}", TextColor.color(65, 0, 0))) }
            else { it.customName(Component.text("Blood Moon ${it.name}", TextColor.color(65, 0, 0))) }
            it.scoreboardTags.add("Blood_Moon_Mob")
            // Effects and Health
            it.addPotionEffects(bloodMoonEffects)
            it.health += 20
            // Random Upgrade
            val randomUpgrade = (0..100).random()
            if (40 < randomUpgrade) {
                val spawnLocation = it.location.clone()
                val someWorld = it.world
                if (spawnLocation.y > 68.0) {
                    var someMob: LivingEntity? = null
                    when (it) {
                        is Zombie -> {
                            it.remove()
                            someMob = if (90 < randomUpgrade) { OdysseyMobs.SAVAGE.createKnight(someWorld, spawnLocation).first } else { OdysseyMobs.SAVAGE.createMob(someWorld, spawnLocation) }
                        }
                        is Skeleton -> {
                            it.remove()
                            someMob = if (90 < randomUpgrade) { OdysseyMobs.VANGUARD.createKnight(someWorld, spawnLocation).first } else { OdysseyMobs.VANGUARD.createMob(someWorld, spawnLocation) }
                        }
                        else -> {
                        }
                    }
                    someMob?.scoreboardTags?.add("Blood_Moon_Mob")
                }
            }
        }
    }



}
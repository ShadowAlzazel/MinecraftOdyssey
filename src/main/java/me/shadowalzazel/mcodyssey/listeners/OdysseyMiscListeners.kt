package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.bosses.theAmbassador.AmbassadorBoss
import org.bukkit.ChatColor
import org.bukkit.entity.EntityType
import org.bukkit.entity.Snowman
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.world.TimeSkipEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object OdysseyMiscListeners : Listener {

    // Check if Ender Dragon Dies
    @EventHandler
    fun onDefeatEnderDragon(event: EntityDeathEvent) {
        val dragon = event.entity
        val endWorld = event.entity.world
        if (dragon.type == EntityType.ENDER_DRAGON) {
            for (aPlayer in endWorld.players) {
                aPlayer.sendMessage("${ChatColor.DARK_PURPLE}The end has just begun ...")

            }
        }
    }

    // Main function for calling a new boss
    @EventHandler
    fun newBoss(event: TimeSkipEvent) {
        if (!MinecraftOdyssey.instance.activeBoss) {
            val timeElapsed = System.currentTimeMillis() - MinecraftOdyssey.instance.timeSinceBoss
            if (timeElapsed >= 90000000) {
                when ((0..4).random()) {
                    // For all boss RNG
                    0 -> {
                        MinecraftOdyssey.instance.timeSinceBoss = System.currentTimeMillis()
                        MinecraftOdyssey.instance.currentBoss = AmbassadorBoss()
                        MinecraftOdyssey.instance.activeBoss = true
                        val ambassadorBoss = MinecraftOdyssey.instance.currentBoss as AmbassadorBoss
                        ambassadorBoss.createBoss(event.world)
                        println("${event.world.name}Spawned the Ambassador")
                    }
                    else -> {

                    }
                }
            }
        }
    }

    // Misc snow man immunity
    @EventHandler
    fun snowManDamage(event: CreatureSpawnEvent) {
        val snowSpawn = event.spawnReason
        val anEntity = event.entity
        if (snowSpawn == CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN) {
            val anEntityLocationX = anEntity.location.blockX
            val anEntityLocationY = anEntity.location.blockY
            val anEntityLocationZ = anEntity.location.blockZ
            //val anEntityBlock = anEntity.world.getBlockAt(anEntity.location.blockX, anEntity.location.blockY, anEntity.location.blockZ)
            if (anEntity.world.getTemperature(anEntityLocationX, anEntityLocationY, anEntityLocationZ) >= 1) {
                //change to powder snow
                val snowMan = event.entity as Snowman
                if (true) {
                    val snowSkin = PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999999, 1)
                    anEntity.addPotionEffect(snowSkin)
                }
            }
        }
    }





}
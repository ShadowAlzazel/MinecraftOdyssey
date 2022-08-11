package me.shadowalzazel.mcodyssey.mclisteners

import org.bukkit.ChatColor
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDeathEvent
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
                if (true) {
                    val snowSkin = PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999999, 1)
                    anEntity.addPotionEffect(snowSkin)
                }
            }
        }
    }





}
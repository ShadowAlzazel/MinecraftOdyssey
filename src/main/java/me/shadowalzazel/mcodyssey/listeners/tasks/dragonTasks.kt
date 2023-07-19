package me.shadowalzazel.mcodyssey.listeners.tasks

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EntityTags
import org.bukkit.HeightMap
import org.bukkit.Particle
import org.bukkit.entity.AreaEffectCloud
import org.bukkit.entity.DragonFireball
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class DragonLightningStormTask (private val dragon: EnderDragon) : BukkitRunnable() {

    override fun run() {
        if (dragon.isDead) {
            this.cancel()
            return
        }
        if (dragon.scoreboardTags.contains(EntityTags.LEFT_PORTAL)) {
            dragon.scoreboardTags.remove(EntityTags.LEFT_PORTAL)
            this.cancel()
            return
        }
        if (dragon.phase == EnderDragon.Phase.LEAVE_PORTAL) {
            this.cancel()
            return
        }
        dragon.world.spawnParticle(Particle.ELECTRIC_SPARK, dragon.location, 85, 2.5, 1.2, 2.5)


        val ground = dragon.location.clone().toHighestLocation(HeightMap.MOTION_BLOCKING)
        ground.getNearbyEntities(10.0, 10.0, 10.0).forEach {
            if (it is Player) {
                it.world.spawnEntity(it.location.clone(), EntityType.LIGHTNING)
                it.damage(5.0, dragon)
                it.world.spawnParticle(Particle.EXPLOSION_NORMAL, it.location, 1, 0.0, 0.0, 0.0)
                it.world.spawnParticle(Particle.ELECTRIC_SPARK, ground, 25, 0.5, 0.2, 0.5)

            }
        }
    }
}

class LightningCloudTask(private val dragon: EnderDragon, private val cloud: AreaEffectCloud) : BukkitRunnable() {

    override fun run() {
        if (cloud.isDead) {
            this.cancel()
            return
        }

        val ground = cloud.location.clone()
        ground.getNearbyEntities(7.0, 6.0, 7.0).forEach {
            if (it is Player) {
                it.world.spawnEntity(it.location, EntityType.LIGHTNING)
                it.damage(5.0, dragon)
                it.world.spawnParticle(Particle.EXPLOSION_NORMAL, it.location, 1, 0.0, 0.0, 0.0)
                it.world.spawnParticle(Particle.ELECTRIC_SPARK, it.location, 25, 0.5, 0.2, 0.5)
            }
        }
    }

}

class LightningEyeTask(private val dragon: EnderDragon, private val display: ItemDisplay) : BukkitRunnable() {

    override fun run() {
        if (dragon.isDead) {
            this.cancel()
            display.remove()
            return
        }

        val ground = display.location.clone()
        ground.getNearbyEntities(8.5, 6.0, 8.5).forEach {
            if (it is Player) {
                it.world.spawnEntity(it.location, EntityType.LIGHTNING)
                it.damage(5.0, dragon)
                it.world.spawnParticle(Particle.EXPLOSION_NORMAL, it.location, 1, 0.0, 0.0, 0.0)
                it.world.spawnParticle(Particle.ELECTRIC_SPARK, it.location, 25, 0.5, 0.2, 0.5)
                it.addPotionEffect(PotionEffect(
                    PotionEffectType.DARKNESS,
                    3 * 20,
                    0
                ))
            }
        }

    }

}

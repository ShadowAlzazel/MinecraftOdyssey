package me.shadowalzazel.mcodyssey.tasks.dragon_tasks

import org.bukkit.Particle
import org.bukkit.entity.AreaEffectCloud
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class LightningCloudTask(private val dragon: EnderDragon, private val cloud: AreaEffectCloud) : BukkitRunnable() {

    override fun run() {
        if (cloud.isDead) {
            this.cancel()
            return
        }

        val ground = cloud.location.clone()
        ground.getNearbyEntities(7.0, 6.0, 7.0).forEach {
            if (it is Player) {
                it.world.spawnEntity(it.location, EntityType.LIGHTNING_BOLT)
                it.damage(5.0, dragon)
                it.world.spawnParticle(Particle.EXPLOSION, it.location, 1, 0.0, 0.0, 0.0)
                it.world.spawnParticle(Particle.ELECTRIC_SPARK, it.location, 25, 0.5, 0.2, 0.5)
            }
        }
    }
}
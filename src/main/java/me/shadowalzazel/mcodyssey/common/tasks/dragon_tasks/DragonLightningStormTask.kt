package me.shadowalzazel.mcodyssey.common.tasks.dragon_tasks

import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import org.bukkit.HeightMap
import org.bukkit.Particle
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

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
                it.world.spawnEntity(it.location.clone(), EntityType.LIGHTNING_BOLT)
                it.damage(5.0, dragon)
                it.world.spawnParticle(Particle.EXPLOSION, it.location, 1, 0.0, 0.0, 0.0)
                it.world.spawnParticle(Particle.ELECTRIC_SPARK, ground, 25, 0.5, 0.2, 0.5)

            }
        }
    }
}
package me.shadowalzazel.mcodyssey.common.tasks.dragon_tasks

import org.bukkit.Particle
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

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
                it.world.spawnEntity(it.location, EntityType.LIGHTNING_BOLT)
                it.damage(5.0, dragon)
                it.world.spawnParticle(Particle.EXPLOSION, it.location, 1, 0.0, 0.0, 0.0)
                it.world.spawnParticle(Particle.ELECTRIC_SPARK, it.location, 25, 0.5, 0.2, 0.5)
                it.addPotionEffect(
                    PotionEffect(
                    PotionEffectType.DARKNESS,
                    3 * 20,
                    0
                )
                )
            }
        }

    }

}
package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// ABLAZE task
class BlazingTask(private val victim: LivingEntity, private val amplifier: Int, private val maxCount: Int) : BukkitRunnable() {
    private var cooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        victim.also {
            counter += 1
            // Check if in water or not ablaze
            if (EffectTags.ABLAZE !in it.scoreboardTags) { this.cancel() }
            if (it.isInWaterOrRainOrBubbleColumn ) {
                it.removeScoreboardTag(EffectTags.ABLAZE)
                this.cancel()
            }

            // Spawn particles in world
            with(it.world) {
                val someLocation = it.location.clone().add(0.0, 1.0, 0.0)
                val ignitedDust = Material.PUMPKIN.createBlockData()
                spawnParticle(Particle.FALLING_DUST, someLocation, 35, 0.75, 0.25, 0.75, ignitedDust)
                spawnParticle(Particle.SMALL_FLAME, someLocation, 50, 1.05, 0.25, 1.05)
                spawnParticle(Particle.SMOKE, someLocation, 10, 1.25, 0.25, 1.25)
                spawnParticle(Particle.FLAME, someLocation, 35, 0.75, 0.25, 0.75)
                spawnParticle(Particle.LARGE_SMOKE, someLocation, 5, 0.75, 0.25, 0.75)
                spawnParticle(Particle.LAVA, someLocation, 85, 0.75, 0.25, 0.75)
            }

            // Damage
            it.damage(2.0 + (amplifier * 2))

            // Every 1 sec
            val timeElapsed = System.currentTimeMillis() - cooldown
            if (maxCount < counter || it.health <= 0.0 || timeElapsed > maxCount * 1000) {
                it.removeScoreboardTag(EffectTags.ABLAZE)
                this.cancel()
            }
        }
    }

}
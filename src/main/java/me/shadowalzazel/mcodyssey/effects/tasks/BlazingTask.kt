package me.shadowalzazel.mcodyssey.effects.tasks

import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// ABLAZE task
class BlazingTask(private val blazingVictim: LivingEntity, private val blazingFactor: Int, private val blazingCount: Int) : BukkitRunnable() {
    private var dousingCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        blazingVictim.also {
            counter += 1
            // Check if in water or not ablaze
            if (it.isInWaterOrRainOrBubbleColumn || "Ablaze" !in it.scoreboardTags) { this.cancel() }

            // Spawn particles in world
            with(it.world) {
                val someLocation = it.location.clone().add(0.0, 1.0, 0.0)
                val ignitedDust = Material.PUMPKIN.createBlockData()
                spawnParticle(Particle.FALLING_DUST, someLocation, 35, 0.75, 0.25, 0.75, ignitedDust)
                spawnParticle(Particle.SMALL_FLAME, someLocation, 50, 1.05, 0.25, 1.05)
                spawnParticle(Particle.SMOKE_NORMAL, someLocation, 10, 1.25, 0.25, 1.25)
                spawnParticle(Particle.FLAME, someLocation, 35, 0.75, 0.25, 0.75)
                spawnParticle(Particle.SMOKE_LARGE, someLocation, 5, 0.75, 0.25, 0.75)
                spawnParticle(Particle.LAVA, someLocation, 85, 0.75, 0.25, 0.75)
            }

            // Damage
            it.damage(1.0 + (blazingFactor * 0.75))

            // Every 1 sec
            val timeElapsed = System.currentTimeMillis() - dousingCooldown
            if (blazingCount < counter || it.health <= 0.0 || timeElapsed > blazingCount * 1000) {
                if (!it.isDead) { it.scoreboardTags.remove("Ablaze") }
                this.cancel()
            }
        }
    }

}
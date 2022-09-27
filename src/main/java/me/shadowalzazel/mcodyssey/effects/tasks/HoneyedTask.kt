package me.shadowalzazel.mcodyssey.effects.tasks

import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// HONEYED task
class HoneyedTask(private val honeyedVictim: LivingEntity, private val honeyCount: Int) : BukkitRunnable() {
    private var honeyCooldown = System.currentTimeMillis() // TODO: IS this shared??
    private var counter = 0
    override fun run() {
        honeyedVictim.also {
            counter += 1
            // Check if still honeyed
            if ("Honeyed" !in it.scoreboardTags) { this.cancel() }

            // Particles and Sound
            with(it.world) {
                val someLocation = it.location.clone().add(0.0, 0.5, 0.0)
                spawnParticle(Particle.DRIPPING_HONEY, someLocation, 5, 0.35, 1.0, 0.35)
                spawnParticle(Particle.FALLING_HONEY, someLocation, 2, 0.35, 1.0, 0.35)
                spawnParticle(Particle.LANDING_HONEY, someLocation, 2, 0.35, 0.4, 0.35)
                playSound(someLocation, Sound.BLOCK_HONEY_BLOCK_STEP, 1.5F, 0.9F)
            }

            // Sticky
            it.velocity.y = 0.0

            // Timing
            val timeElapsed = System.currentTimeMillis() - honeyCooldown
            if (honeyCount < counter || it.health <= 1.0 || timeElapsed > (honeyCount / 2) * 1000) {
                if (!it.isDead) { it.scoreboardTags.remove("Honeyed") }
                this.cancel()
            }

        }

    }
}
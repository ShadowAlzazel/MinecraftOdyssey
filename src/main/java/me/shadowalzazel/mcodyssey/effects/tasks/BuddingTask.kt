package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// BUDDING task
class BuddingTask(private val victim: LivingEntity, private val factor: Int, private val maxCount: Int) : BukkitRunnable() {
    private var timer = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        victim.also {
            counter += 1
            // Check if no longer decaying
            if (EffectTags.BUDDING !in victim.scoreboardTags) { this.cancel() }

            with(it.world) {
                val location = it.location.clone().add(0.0, 0.5, 0.0)
                // Static
                spawnParticle(Particle.SPORE_BLOSSOM_AIR , location, 70, 0.4, 0.75, 0.4)
                spawnParticle(Particle.GLOW, location, 20, 00.4, 0.75, 0.4)
                spawnParticle(Particle.SCRAPE, location, 20, 0.4, 0.75, 0.4)
                // Directional Particles
                for (x in 1..15) {
                    val offset = location.clone().add((0..10).random() * 0.1, 0.3, (0..10).random() * 0.1)
                    spawnParticle(Particle.GLOW_SQUID_INK, offset, 1, 0.03, 0.03, 0.03)
                    spawnParticle(Particle.SNEEZE, offset, 1, 0.03, 0.03, 0.03)

                }
            }
            // Damage
            it.damage(factor.toDouble() * 0.75)
            // Every 2 sec
            val timeElapsed = System.currentTimeMillis() - timer
            if (counter > maxCount || timeElapsed > (maxCount * 2) * 1000) {
                if (!it.isDead) it.scoreboardTags.remove(EffectTags.BUDDING)
                this.cancel()
            }
        }
    }

}
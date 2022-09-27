package me.shadowalzazel.mcodyssey.effects.tasks

import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// DECAYING task
class DecayingTask(private val decayingVictim: LivingEntity, private val decayingFactor: Int, private val decayingCount: Int) : BukkitRunnable() {
    private var decayCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        decayingVictim.also {
            counter += 1
            // Check if no longer decaying
            if ("Decaying" !in decayingVictim.scoreboardTags) { this.cancel() }

            with(it.world) {
                val someLocation = it.location.clone().add(0.0, 0.5, 0.0)
                // Static
                spawnParticle(Particle.SPORE_BLOSSOM_AIR , someLocation, 45, 0.5, 0.75, 0.5)
                spawnParticle(Particle.GLOW, someLocation, 15, 0.75, 0.8, 0.75)
                spawnParticle(Particle.GLOW_SQUID_INK, someLocation, 15, 0.25, 0.25, 0.25)

                // Directional Particles
                for (x in 1..20) {
                    val randomLocation = someLocation.add((0..10).random() * 0.1, 0.0, (0..10).random() * 0.1)
                    spawnParticle(Particle.SNEEZE, randomLocation, 1, 0.25, 0.25, 0.25)
                    spawnParticle(Particle.SCRAPE, randomLocation, 1, 0.25, 0.4, 0.25)
                }
            }

            // Damage
            it.damage(decayingFactor.toDouble() * 0.75)

            // Every 2 sec
            val timeElapsed = System.currentTimeMillis() - decayCooldown
            if ( counter > decayingCount || timeElapsed > (decayingCount * 2) * 1000) {
                if (!it.isDead) it.scoreboardTags.remove("Decaying")
                this.cancel()
            }
        }
    }

}
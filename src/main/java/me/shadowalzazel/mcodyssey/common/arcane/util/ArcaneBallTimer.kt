package me.shadowalzazel.mcodyssey.common.arcane.util

import org.bukkit.Particle
import org.bukkit.entity.Projectile
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

class ArcaneBallTimer(
    private val magicBall: Projectile,
    private val particle: Particle
) : BukkitRunnable() {

    private val maxTicksAlive = 20 * 20
    private var counter = 0

    override fun run() {
        // Remove if dead or reached maxTicksAlive
        if (magicBall.isDead || counter >= maxTicksAlive) {
            magicBall.remove()
            this.cancel()
            return
        }
        // Jitter/offsets the input value by a small amount
        val jitter: (Double) -> Double = { original ->
            original + Random.nextDouble(-0.03, 0.03)
        }

        // Particles
        repeat(5) {
            val particleLocation = magicBall.location.clone().add(jitter(0.0), jitter(0.0), jitter(0.0))
            magicBall.world.spawnParticle(particle, particleLocation, 1, 0.0, 0.0, 0.0)
        }
        // Add counter
        counter++
    }

}
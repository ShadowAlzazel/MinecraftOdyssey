package me.shadowalzazel.mcodyssey.mclisteners.utility

import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable


// FREEZE Timer
class FreezingTask(freezingEntity: LivingEntity, private val freezeFactor: Int) : BukkitRunnable() {

    private var freezeCooldown = System.currentTimeMillis()
    private val freezingVictim = freezingEntity
    private var counter = 0

    override fun run() {
        println("Effect LOL")
        // some timer
        freezingVictim.freezeTicks = 100
        freezingVictim.damage(freezeFactor.toDouble())
        freezingVictim.world.spawnParticle(Particle.WHITE_ASH, freezingVictim.location, 75, 1.0, 1.0, 1.0)
        freezingVictim.world.spawnParticle(Particle.CRIT_MAGIC, freezingVictim.location, 25, 0.25, 0.5, 0.25)
        freezingVictim.world.spawnParticle(Particle.SNOWBALL, freezingVictim.location, 45, 0.5, 1.0, 0.5)
        freezingVictim.world.spawnParticle(Particle.FALLING_DRIPSTONE_WATER, freezingVictim.location, 25, 0.5, 1.0, 0.5)
        counter += 1
        // MAYBE DO MORE FREEZE TO MAGMA / BLAZE
        val timeElapsed = System.currentTimeMillis() - freezeCooldown
        if (freezeFactor * 3 < counter || freezingVictim.health <= 1.0 || timeElapsed > freezeFactor * 3 * 1000) {
            freezingVictim.freezeTicks = 0
            this.cancel()
        }

    }
}


// Bee Effects
class HoneyedTask(honeyedEntity: LivingEntity, private val honeyFactor: Int) : BukkitRunnable() {

    private var honeyCooldown = System.currentTimeMillis()
    private val honeyedVictim = honeyedEntity
    private var counter = 0

    override fun run() {
        println("Effect Honey")
        // some timer
        honeyedVictim.world.spawnParticle(Particle.DRIPPING_HONEY, honeyedVictim.location, 15, 0.25, 1.0, 0.25)
        honeyedVictim.world.spawnParticle(Particle.FALLING_HONEY, honeyedVictim.location, 8, 0.25, 1.0, 0.25)
        honeyedVictim.world.spawnParticle(Particle.LANDING_HONEY, honeyedVictim.location, 8, 0.25, 0.4, 0.25)
        counter += 1

        val timeElapsed = System.currentTimeMillis() - honeyCooldown
        if (((3 * honeyFactor) + 3) < counter || honeyedVictim.health <= 1.0 || timeElapsed > ((3 * honeyFactor) + 3) * 1000) {
            this.cancel()
        }

    }
}



// DECAYING TOUCH effects
class DecayingTask(decayingEntity: LivingEntity, private val decayingTouchFactor: Int) : BukkitRunnable() {

    private var decayCooldown = System.currentTimeMillis()
    private val decayingVictim = decayingEntity
    private var counter = 0

    // Stacked
    // Apply damage due to damage

    override fun run() {

        decayingVictim.world.spawnParticle(Particle.SPORE_BLOSSOM_AIR , decayingVictim.location, 45, 0.5, 0.75, 0.5)
        decayingVictim.world.spawnParticle(Particle.GLOW, decayingVictim.location, 15, 0.75, 0.8, 0.75)
        decayingVictim.world.spawnParticle(Particle.GLOW_SQUID_INK, decayingVictim.location, 15, 0.25, 0.25, 0.25)
        decayingVictim.world.spawnParticle(Particle.SNEEZE, decayingVictim.location, 45, 0.25, 0.25, 0.25)
        decayingVictim.world.spawnParticle(Particle.SCRAPE, decayingVictim.location, 15, 0.25, 0.4, 0.25)
        decayingVictim.damage(decayingTouchFactor.toDouble())
        counter += 1

        val timeElapsed = System.currentTimeMillis() - decayCooldown
        // 10 sec
        if (5 < counter || decayingVictim.health <= 0.5 || timeElapsed > 10 * 1000) {
            this.cancel()
        }

    }

}
package me.shadowalzazel.mcodyssey.mclisteners.utility

import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

class FreezingTask(freezingEntity: LivingEntity, freezeFactor: Int) : BukkitRunnable() {

    var freezeCooldown = System.currentTimeMillis()
    private val freezingVictim = freezingEntity
    private val freezeFactor = freezeFactor
    private var counter = 0

    override fun run() {
        println("Effect LOL")
        // some timer
        freezingVictim.freezeTicks = 100
        freezingVictim.damage(freezeFactor.toDouble())
        //val freezeColor = Color.fromRGB(168, 225, 255)
        //val freezingDust = Particle.REDSTONE
        //val freezingDustOptions = Particle.DustOptions(freezeColor, 1.0F)
        //val freezingDust = Particle.DustTransition(freezeColor, freezeColor, 1.0F)
        freezingVictim.world.spawnParticle(Particle.WHITE_ASH, freezingVictim.location, 75, 1.0, 1.0, 1.0)
        freezingVictim.world.spawnParticle(Particle.CRIT_MAGIC, freezingVictim.location, 25, 0.25, 0.5, 0.25)
        freezingVictim.world.spawnParticle(Particle.SNOWBALL, freezingVictim.location, 45, 0.5, 1.0, 0.5)
        freezingVictim.world.spawnParticle(Particle.FALLING_DRIPSTONE_WATER, freezingVictim.location, 25, 0.5, 1.0, 0.5)
        counter += 1
        val timeElapsed = System.currentTimeMillis() - freezeCooldown
        if (freezeFactor * 3 < counter || freezingVictim.health <= 1.0 || timeElapsed > freezeFactor * 3 * 1000) {
            freezingVictim.freezeTicks = 0
            this.cancel()
        }

    }
}
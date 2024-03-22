package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// FREEZING task
class FreezingTask(private val freezingVictim: LivingEntity, private val freezeFactor: Int, private val freezingCount: Int) : BukkitRunnable() {
    private var freezeCooldown = System.currentTimeMillis()
    private var counter = 0
    override fun run() {
        freezingVictim.also {
            counter += 1
            // Check if still freezing
            if (EffectTags.FREEZING !in it.scoreboardTags) { this.cancel() }

            // Particles
            with(it.world) {
                val freezingBlock = Material.BLUE_ICE.createBlockData()
                val freezingDust = Material.LAPIS_BLOCK.createBlockData()
                val someLocation = it.location.clone().add(0.0, 0.5, 0.0)
                spawnParticle(Particle.BLOCK_CRACK, someLocation, 20, 0.95, 0.8, 0.95, freezingBlock)
                spawnParticle(Particle.FALLING_DUST, someLocation, 20, 0.75, 0.25, 0.75, freezingDust)
                spawnParticle(Particle.SNOWBALL, someLocation, 20, 0.5, 1.0, 0.5)
                spawnParticle(Particle.FALLING_DRIPSTONE_WATER, someLocation, 20, 0.5, 1.0, 0.5)
            }

            // Damage
            it.freezeTicks = 100
            it.damage(freezeFactor.toDouble())

            // Timing
            val timeElapsed = System.currentTimeMillis() - freezeCooldown
            if (freezingCount < counter || it.health <= 0.5 || timeElapsed > freezingCount * 1000) {
                it.freezeTicks = 0
                if (!it.isDead) { it.scoreboardTags.remove(EffectTags.FREEZING) }
                this.cancel()
            }
        }
    }
}
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
            if (EffectTags.FREEZING !in it.scoreboardTags) {
                this.cancel()
            }
            // Remove if on fire
            if (freezingVictim.fireTicks > 0) {
                it.removeScoreboardTag(EffectTags.FREEZING)
                this.cancel()
            }
            // Particles
            with(it.world) {
                val freezingBlock = Material.BLUE_ICE.createBlockData()
                val location = it.location.clone().add(0.0, 0.5, 0.0)
                spawnParticle(Particle.BLOCK, location, 10, 0.05, 0.2, 0.05, freezingBlock)
                spawnParticle(Particle.SNOWFLAKE, location, 5, 0.05, 0.05, 0.05)
            }

            // Damage
            it.freezeTicks = 100
            it.damage(freezeFactor.toDouble())

            // Timing
            val timeElapsed = System.currentTimeMillis() - freezeCooldown
            if (freezingCount < counter || it.health <= 0.5 || timeElapsed > freezingCount * 1000) {
                it.freezeTicks = 0
                if (!it.isDead) { it.removeScoreboardTag(EffectTags.FREEZING) }
                this.cancel()
            }
        }
    }
}
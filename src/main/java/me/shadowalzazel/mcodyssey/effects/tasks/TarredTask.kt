package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// DOUSED task
class TarredTask(private val dousedVictim: LivingEntity, private val douseCount: Int) : BukkitRunnable() {
    private var dousingCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        dousedVictim.also {
            counter += 1
            // Check if no longer Doused
            if (EffectTags.TARRED !in it.scoreboardTags) { this.cancel() }

            // Check if on fire
            if (it.fireTicks > 0) {
                var dousePower = 0
                // Remove all douses
                for (x in 1..3) {
                    if ("Doused_Factor_$x" in it.scoreboardTags) {
                        it.scoreboardTags.remove("Doused_Factor_$x")
                        dousePower = x
                    }
                }
                // Do ablaze effects
                it.scoreboardTags.remove(EffectTags.TARRED)
                it.addScoreboardTag(EffectTags.ABLAZE)
                it.fireTicks = 20 * ((dousePower * 4) + 4) + 1
                // Run task
                val blazeDouseTask = BlazingTask(it, dousePower, ((dousePower * 4) + 4))
                blazeDouseTask.runTaskTimer(Odyssey.instance, 1, 20)
                this.cancel()
            }

            // Particles
            with(it.world) {
                val dousingBlock = Material.COAL_BLOCK.createBlockData()
                val dousingDust = Material.BLACK_CONCRETE_POWDER.createBlockData()
                val someLocation = it.location.clone().add(0.0, 0.5, 0.0)
                spawnParticle(Particle.BLOCK_CRACK, someLocation, 35, 0.45, 0.25, 0.45, dousingBlock)
                spawnParticle(Particle.FALLING_DUST, someLocation, 35, 0.45, 0.25, 0.45, dousingDust)
                spawnParticle(Particle.SMOKE_NORMAL, someLocation, 15, 0.25, 1.0, 0.25)
            }

            // Timing
            val timeElapsed = System.currentTimeMillis() - dousingCooldown
            if (douseCount < counter || it.health <= 0.50 || timeElapsed > (douseCount) * 1000) {
                if ("Doused" in it.scoreboardTags && !it.isDead) { it.scoreboardTags.remove("Doused") }
                this.cancel()
            }
        }
    }
}
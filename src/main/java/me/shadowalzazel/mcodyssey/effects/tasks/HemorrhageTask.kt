package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// HEMORRHAGE task
class HemorrhageTask(private val victim: LivingEntity, private val factor: Int) : BukkitRunnable() {
    private var timer = System.currentTimeMillis()
    private var counter = 0
    override fun run() {
        victim.also {
            counter += 1
            // Check if still hemorrhaging
            if (EffectTags.HEMORRHAGING !in it.scoreboardTags) { this.cancel() }
            // Particles
            with(it.world) {
                val color = Color.fromRGB(135, 4, 26)
                val bloodDust = Particle.DustOptions(color, 1.0F)
                val bloodBlockBreak = Material.STRIPPED_MANGROVE_LOG.createBlockData()
                val bloodBlockDust = Material.NETHERRACK.createBlockData()
                val location = it.location.clone().add(0.0, 0.35, 0.0)
                spawnParticle(Particle.DUST , location, 45, 0.95, 0.75, 0.95, bloodDust)
                spawnParticle(Particle.BLOCK, location, 65, 0.95, 0.8, 0.95, bloodBlockBreak)
                spawnParticle(Particle.FALLING_DUST, location, 25, 0.75, 0.25, 0.75, bloodBlockDust)
                spawnParticle(Particle.DAMAGE_INDICATOR, location, 25, 0.25, 0.25, 0.25)
            }
            // Damage
            var modifier = 0
            for (x in 1..5) {
                if (victim.scoreboardTags.contains(EffectTags.HEMORRHAGE_MODIFIER + x)) {
                    modifier = x
                }
            }
            it.damage(factor.toDouble() * modifier)
            // Timer
            val timeElapsed = System.currentTimeMillis() - timer
            if (9 < counter || timeElapsed > 9 * 1000) {
                if (!it.isDead) { it.scoreboardTags.remove(EffectTags.HEMORRHAGING) }
                this.cancel()
            }
        }
    }
}
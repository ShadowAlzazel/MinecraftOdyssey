package me.shadowalzazel.mcodyssey.tasks.enchantment_tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.*
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.cos
import kotlin.math.sin

// ARCANE_CELL TASK
class ArcaneCellTask(
    private val victim: LivingEntity,
    private val jailedLocation: Location,
    private val level: Int,
    timeInTicks: Int) : BukkitRunnable()
{
    private var counter = 0
    private var maxCount = timeInTicks.div(5)

    override fun run() {  // Run every 5 ticks
        victim.also {
            if (it.isDead) { this.cancel() }
            if (EffectTags.ARCANE_JAILED !in it.scoreboardTags) { this.cancel() }
            counter += 1
            // Circular Particle Cell
            val arcaneDust = Material.AMETHYST_BLOCK.createBlockData()
            val circumferenceCount = 32
            for (x in 1..circumferenceCount) {
                // Math
                val angle = Math.PI * 2 * (x / (circumferenceCount * 1.0))
                val angularOffset: Pair<Double, Double> = Pair(5.0 * cos(angle), 5 * sin(angle)) // MAYBE ADD HEIGHTMAP? 2
                val particleLocation = jailedLocation.clone().add(angularOffset.first, 0.6, angularOffset.second)
                particleLocation.world.spawnParticle(Particle.FALLING_DUST, particleLocation, 3, 0.0, 0.3, 0.0, arcaneDust)
            }

            // Teleport if outside jail
            val distance = jailedLocation.distance(victim.location)
            if (distance > 5.08) { // Slight offset due to hit box sizes
                it.teleport(jailedLocation)
                it.world.playSound(it.location, Sound.ENTITY_VEX_CHARGE, 1.5F, 0.5F)
            }

            // Timer
            if (counter > maxCount) {
                it.removeScoreboardTag(EffectTags.ARCANE_JAILED)
                this.cancel()
            }
        }

    }
}
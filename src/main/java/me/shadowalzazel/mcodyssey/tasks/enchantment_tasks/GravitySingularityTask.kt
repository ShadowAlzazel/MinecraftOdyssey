package me.shadowalzazel.mcodyssey.tasks.enchantment_tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import org.bukkit.Particle
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// GRAVITY_WELL and SINGULARITY_SHOT
class GravitySingularityTask (
    private val entity: Entity,
    private val attacker: LivingEntity,
    private val modifier: Int,
    private val maxCount: Int) : BukkitRunnable()
{
    private var timer = System.currentTimeMillis()
    private var counter = 0
    private var singularityLocation = entity.location.clone()

    override fun run() {
        entity.also { singularity ->
            counter += 1
            // Check if Gravity Well entity has tag
            if (EffectTags.GRAVITY_WELLED !in singularity.scoreboardTags) { this.cancel() }
            // Check if Moving
            val movableSingularity = singularity.scoreboardTags.contains(EntityTags.FALLING_SINGULARITY) || singularity.scoreboardTags.contains(EntityTags.MOVING_SINGULARITY)
            if (movableSingularity) {
                singularityLocation = singularity.location.clone()
            }
            // Spawn particles and get nearby entities
            with(singularity.world) {
                val someLocation = singularityLocation.clone().add(0.0, 0.25, 0.0)
                spawnParticle(Particle.END_ROD, someLocation, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.CRIT, someLocation, 45, 0.5, 0.40, 0.5)
                spawnParticle(Particle.PORTAL, someLocation, 55, 0.5, 0.4, 0.5)
                spawnParticle(Particle.SONIC_BOOM, someLocation, 2, 0.0, 0.0, 0.0)
            }
            // Pull
            singularity.location.getNearbyLivingEntities(3.25 ).forEach {
                //TODO:  INCLUDE FALLING AND MOVING
                if (!it.scoreboardTags.contains(EntityTags.MOVING_SINGULARITY) && it != attacker) {
                    it.teleport(singularityLocation.clone().add((-3..3).random() * 0.08, 0.1, (-3..3).random() * 0.08))
                    it.damage(0.5 * modifier, attacker)
                }
            }
            // Timer
            val timeElapsed = System.currentTimeMillis() - timer
            val isDead = singularity.isDead
            if (counter > maxCount || isDead || timeElapsed > (maxCount / 2) * 1000) {
                if (!singularity.isDead) { singularity.scoreboardTags.remove(EffectTags.GRAVITY_WELLED) }
                this.cancel()
            }
        }
    }
}
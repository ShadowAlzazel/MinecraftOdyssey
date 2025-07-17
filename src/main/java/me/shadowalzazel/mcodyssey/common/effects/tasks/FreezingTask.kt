package me.shadowalzazel.mcodyssey.common.effects.tasks

import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// FREEZING task
@Suppress("UnstableApiUsage")
class FreezingTask(
    private val entity: LivingEntity,
    private val amplifier: Int,
    private val maxCount: Int) : BukkitRunnable()
{
    private var timer = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        // Check tag
        val noTag = EffectTags.FREEZING !in entity.scoreboardTags
        if (noTag) {
            this.cancel()
            return
        }
        // Remove if on fire
        if (entity.fireTicks > 0) {
            entity.removeScoreboardTag(EffectTags.FREEZING)
            this.cancel()
            return
        }
        // Remove if dead
        if (entity.isDead) {
            entity.removeScoreboardTag(EffectTags.FREEZING)
            this.cancel()
            return
        }
        // Particles
        with(entity.world) {
            val freezingBlock = Material.BLUE_ICE.createBlockData()
            val location = entity.location.clone().add(0.0, 0.5, 0.0)
            spawnParticle(Particle.BLOCK, location, 10, 0.05, 0.2, 0.05, freezingBlock)
            spawnParticle(Particle.SNOWFLAKE, location, 5, 0.05, 0.05, 0.05)
        }
        // Damage
        entity.freezeTicks = 100
        val damageSource = DamageSource.builder(DamageType.FREEZE).build()
        val value = amplifier * 1.0
        entity.damage(value, damageSource)
        // Timing
        val timeElapsed = System.currentTimeMillis() - timer
        if (maxCount < counter || timeElapsed > maxCount * 1000) {
            entity.removeScoreboardTag(EffectTags.FREEZING)
            this.cancel()
        }
    }
}
package me.shadowalzazel.mcodyssey.common.effects.tasks

import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.setIntTag
import org.bukkit.Particle
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

/*
    Aero Erosion = Aerosion

    - On hit, apply a stack of Aerosion. Up to (Allowed Max)
    - Every 1.5 seconds, deal [Stack] * 2.0.

 */

@Suppress("UnstableApiUsage")
class AerosionTask(
    private val entity: LivingEntity,
    private val amplifier: Double,
    private val maxCount: Int) : BukkitRunnable() {
    private var timer = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        // Check tag
        val noTag = EffectTags.AEROSION !in entity.scoreboardTags
        if (noTag) {
            this.cancel()
            return
        }
        // Remove if dead
        if (entity.isDead) {
            entity.removeScoreboardTag(EffectTags.AEROSION)
            this.cancel()
            return
        }
        // Stacks
        val stacks = entity.getIntTag(EntityTags.AEROSION_STACKS) ?: 1
        // Particles
        with(entity.world) {
            val location = entity.location.clone().add(0.0, 0.5, 0.0)
            spawnParticle(Particle.SMALL_GUST , location, 8 * stacks, 0.75, 0.5, 0.75)
        }
        // Damage
        val damageSource = DamageSource.builder(DamageType.WIND_CHARGE).build()
        val value = amplifier * (3.0 * stacks)
        entity.damage(value, damageSource)
        // Reset Velocity
        entity.velocity.multiply(0.0)
        entity.velocity = entity.velocity.clone().multiply(0.0).setY(0).setX(0).setZ(0)
        // Every 1.5 sec
        val timeElapsed = System.currentTimeMillis() - timer
        if (counter > maxCount || timeElapsed > (maxCount * 1.5) * 1000) {
            entity.removeScoreboardTag(EffectTags.AEROSION)
            entity.setIntTag(EntityTags.AEROSION_STACKS, 0)
            this.cancel()
        }
    }

}
package me.shadowalzazel.mcodyssey.common.effects.tasks

import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// ABLAZE task
@Suppress("UnstableApiUsage")
class BlazingTask(private val entity: LivingEntity, private val amplifier: Int, private val maxCount: Int) : BukkitRunnable() {
    private var timer = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        // Check if in water or not ablaze
        if (EffectTags.ABLAZE !in entity.scoreboardTags) this.cancel()
        if (entity.isInWaterOrRainOrBubbleColumn) {
            entity.removeScoreboardTag(EffectTags.ABLAZE)
            this.cancel()
            return
        }
        // Spawn particles in world
        with(entity.world) {
            val location = entity.location.clone().add(0.0, 1.0, 0.0)
            val ignitedDust = Material.PUMPKIN.createBlockData()
            spawnParticle(Particle.FALLING_DUST, location, 35, 0.75, 0.25, 0.75, ignitedDust)
            spawnParticle(Particle.SMALL_FLAME, location, 20, 1.05, 0.25, 1.05)
            spawnParticle(Particle.FLAME, location, 25, 0.75, 0.25, 0.75)
            spawnParticle(Particle.LARGE_SMOKE, location, 5, 0.75, 0.25, 0.75)
            spawnParticle(Particle.LAVA, location, 45, 0.75, 0.25, 0.75)
        }
        // Damage
        val damageSource = DamageSource.builder(DamageType.IN_FIRE).build()
        val value = 2.0 + (amplifier * 2.0)
        entity.damage(value, damageSource)
        // Every 1 sec
        val timeElapsed = System.currentTimeMillis() - timer
        if (maxCount < counter || entity.health <= 0.0 || timeElapsed > maxCount * 1000) {
            entity.removeScoreboardTag(EffectTags.ABLAZE)
            this.cancel()
        }
    }

}
package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.Particle
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// BUDDING task
@Suppress("UnstableApiUsage")
class BuddingTask(private val entity: LivingEntity, private val amplifier: Int, private val maxCount: Int) : BukkitRunnable() {
    private var timer = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        // Check tag
        if (EffectTags.BUDDING !in entity.scoreboardTags) this.cancel()
        if (entity.isDead) {
            entity.removeScoreboardTag(EffectTags.BUDDING)
            this.cancel()
            return
        }
        // Particles
        with(entity.world) {
            val location = entity.location.clone().add(0.0, 0.5, 0.0)
            spawnParticle(Particle.SPORE_BLOSSOM_AIR , location, 70, 0.15, 0.15, 0.15)
        }
        // Damage
        val damageSource = DamageSource.builder(DamageType.MAGIC).build()
        val value = amplifier * 1.0
        entity.damage(value, damageSource)
        // Every 2 sec
        val timeElapsed = System.currentTimeMillis() - timer
        if (counter > maxCount || timeElapsed > (maxCount * 2) * 1000) {
            if (!entity.isDead) entity.removeScoreboardTag(EffectTags.BUDDING)
            this.cancel()
        }
    }

}
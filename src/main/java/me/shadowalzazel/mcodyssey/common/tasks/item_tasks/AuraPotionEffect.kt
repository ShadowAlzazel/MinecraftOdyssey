package me.shadowalzazel.mcodyssey.common.tasks.item_tasks

import me.shadowalzazel.mcodyssey.util.VectorParticles
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.scheduler.BukkitRunnable

class AuraPotionEffect(
    private val entity: Entity,
    private val effects: List<PotionEffect>,
    private val maxCount: Int,
    private val color: Color
) : BukkitRunnable(), VectorParticles {
    private var timer = System.currentTimeMillis()
    private var counter = 0
    override fun run() {
        // Apply effects
        entity.getNearbyEntities(3.0, 3.0, 3.0).forEach {
            if (it !is LivingEntity) return
            it.world.spawnParticle(Particle.ENTITY_EFFECT, it.location, 15, 0.03, 0.1, 0.03, color)
            it.addPotionEffects(effects)
        }
        // Remove if milk bucket?
        spawnLineParticles(Particle.ENTITY_EFFECT, entity.location, entity.location.clone().add(0.0, 3.0, 0.0), 40, color)

        // Sentry
        val timeElapsed = System.currentTimeMillis() - timer
        if (counter > maxCount || entity.isDead || timeElapsed > (maxCount) * 1000) {
            this.cancel()
        }
    }

}
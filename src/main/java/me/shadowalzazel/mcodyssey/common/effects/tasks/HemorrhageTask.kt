package me.shadowalzazel.mcodyssey.common.effects.tasks

import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.getIntTag
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// HEMORRHAGE task
class HemorrhageTask(
    private val entity: LivingEntity,
    private val amplifier: Int,
    private val maxCount: Int) : BukkitRunnable() {
    private var timer = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        // Check tag
        val noTag = EffectTags.HEMORRHAGING !in entity.scoreboardTags
        if (noTag) {
            this.cancel()
            return
        }
        // Remove if dead
        if (entity.isDead) {
            entity.removeScoreboardTag(EffectTags.HEMORRHAGING)
            this.cancel()
            return
        }
        // Particles
        with(entity.world) {
            val color = Color.fromRGB(135, 4, 26)
            val bloodDust = Particle.DustOptions(color, 1.0F)
            val bloodBlockBreak = Material.STRIPPED_MANGROVE_LOG.createBlockData()
            val bloodBlockDust = Material.NETHERRACK.createBlockData()
            val location = entity.location.clone().add(0.0, 0.35, 0.0)
            spawnParticle(Particle.DUST , location, 45, 0.95, 0.75, 0.95, bloodDust)
            spawnParticle(Particle.BLOCK, location, 65, 0.95, 0.8, 0.95, bloodBlockBreak)
            spawnParticle(Particle.FALLING_DUST, location, 25, 0.75, 0.25, 0.75, bloodBlockDust)
        }
        // Damage
        //val modifier = entity.getIntTag(EffectTags.HEMORRHAGE_MODIFIER) ?: 1
        val taskDamage = amplifier.toDouble() * 2.0
        entity.damage(taskDamage)
        // Timing
        val timeElapsed = System.currentTimeMillis() - timer
        if (maxCount < counter || timeElapsed > maxCount * 1000) {
            entity.removeScoreboardTag(EffectTags.HEMORRHAGING)
            this.cancel()
        }
    }
}
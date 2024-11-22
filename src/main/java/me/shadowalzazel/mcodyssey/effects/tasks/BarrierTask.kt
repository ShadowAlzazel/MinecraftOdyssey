package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

class BarrierTask(private val entity: LivingEntity, private val maxCount: Int) : BukkitRunnable() {
    private var timer = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        if (EffectTags.BARRIER !in entity.scoreboardTags) this.cancel()
        entity.world.spawnParticle(Particle.END_ROD, entity.location, 4, 0.1, 0.1, 0.1)
        // Timer
        val timeElapsed = System.currentTimeMillis() - timer
        counter += 1
        if (maxCount < counter || timeElapsed > (maxCount / 2) * 1000) {
            entity.removeScoreboardTag(EffectTags.BARRIER)
            this.cancel()
        }
    }

}
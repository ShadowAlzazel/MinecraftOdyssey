package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// Accursed TASK
class AccursedTask(private val entity: LivingEntity, private val maxCount: Int) : BukkitRunnable() {
    private var timer = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        if (EffectTags.ASPHYXIATE !in entity.scoreboardTags) { this.cancel() }
        val timeElapsed = System.currentTimeMillis() - timer
        if (maxCount < counter || timeElapsed > maxCount * 1000) {
            entity.scoreboardTags.remove(EffectTags.ACCURSED)
            this.cancel()
        }
    }

}
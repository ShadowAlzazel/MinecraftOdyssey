package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

class CorrodingTask(private val thornEntity: LivingEntity, private val thornCount: Int) : BukkitRunnable() {
    private var thornsCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        if (EffectTags.CORRODING !in thornEntity.scoreboardTags) { this.cancel() }
        val timeElapsed = System.currentTimeMillis() - thornsCooldown
        if (thornCount < counter || timeElapsed > thornCount * 1000) {
            thornEntity.scoreboardTags.remove(EffectTags.CORRODING)
            this.cancel()
        }
    }

}
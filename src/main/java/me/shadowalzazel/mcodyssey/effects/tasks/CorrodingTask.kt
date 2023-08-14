package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// THORNS TASK
class CorrodingTask(private val thornEntity: LivingEntity, private val thornCount: Int) : BukkitRunnable() {
    private var thornsCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        if (EffectTags.ASPHYXIATE !in thornEntity.scoreboardTags) { this.cancel() }
        val timeElapsed = System.currentTimeMillis() - thornsCooldown
        if (thornCount < counter || timeElapsed > thornCount * 1000) {
            thornEntity.scoreboardTags.remove(EffectTags.ASPHYXIATE)
            this.cancel()
        }
    }

}
package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.constants.OdysseyEffectTags
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// PUFFY N PRICKLY TASK
class PuffyPricklyTask(private val thornEntity: LivingEntity, private val puffyPricklyCount: Int) : BukkitRunnable() {
    private var puffyPricklyCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        if (OdysseyEffectTags.PUFFY_PRICKLY !in thornEntity.scoreboardTags) { this.cancel() }
        val timeElapsed = System.currentTimeMillis() - puffyPricklyCooldown
        if (puffyPricklyCount < counter || timeElapsed > puffyPricklyCount * 1000) {
            thornEntity.scoreboardTags.remove(OdysseyEffectTags.PUFFY_PRICKLY)
            this.cancel()
        }
    }

}
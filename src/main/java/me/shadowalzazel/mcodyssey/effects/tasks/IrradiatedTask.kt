package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.effects.OdysseyEffectTags
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// PUFFY N PRICKLY TASK
class IrradiatedTask(private val irradiatedEntity: LivingEntity, private val irradiatedCount: Int) : BukkitRunnable() {
    private var puffyPricklyCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        if (OdysseyEffectTags.PUFFY_PRICKLY !in irradiatedEntity.scoreboardTags) { this.cancel() }
        val timeElapsed = System.currentTimeMillis() - puffyPricklyCooldown
        if (irradiatedCount < counter || timeElapsed > irradiatedCount * 1000) {
            irradiatedEntity.scoreboardTags.remove(OdysseyEffectTags.PUFFY_PRICKLY)
            this.cancel()
        }
    }

}
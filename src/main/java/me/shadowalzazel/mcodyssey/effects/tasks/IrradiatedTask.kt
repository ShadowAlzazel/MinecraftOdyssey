package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// PUFFY N PRICKLY TASK
class IrradiatedTask(private val irradiatedEntity: LivingEntity, private val irradiatedCount: Int) : BukkitRunnable() {
    private var puffyPricklyCooldown = System.currentTimeMillis()
    private var counter = 0
    private val healthLimit = irradiatedEntity.health

    override fun run() {
        counter += 1
        if (EffectTags.IRRADIATED !in irradiatedEntity.scoreboardTags) { this.cancel() }
        // Effects
        if (irradiatedEntity.health > healthLimit) { irradiatedEntity.health = healthLimit }
        irradiatedEntity.damage(0.5)

        // Timer
        val timeElapsed = System.currentTimeMillis() - puffyPricklyCooldown
        if (irradiatedCount < counter || timeElapsed > irradiatedCount * 1000) {
            irradiatedEntity.scoreboardTags.remove(EffectTags.IRRADIATED)
            this.cancel()
        }
    }

}
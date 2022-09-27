package me.shadowalzazel.mcodyssey.effects.tasks

import me.shadowalzazel.mcodyssey.effects.OdysseyEffectTags
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// Accursed TASK
class AccursedTask(private val accursedEntity: LivingEntity, private val accursedCounter: Int) : BukkitRunnable() {
    private var accursedCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        if (OdysseyEffectTags.THORNY !in accursedEntity.scoreboardTags) { this.cancel() }
        val timeElapsed = System.currentTimeMillis() - accursedCooldown
        if (accursedCounter < counter || timeElapsed > accursedCounter * 1000) {
            accursedEntity.scoreboardTags.remove(OdysseyEffectTags.THORNY)
            this.cancel()
        }
    }

}
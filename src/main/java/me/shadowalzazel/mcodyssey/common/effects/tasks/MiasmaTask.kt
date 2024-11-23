package me.shadowalzazel.mcodyssey.common.effects.tasks

import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

class MiasmaTask(private val thornEntity: LivingEntity, private val puffyPricklyCount: Int) : BukkitRunnable() {
    private var puffyPricklyCooldown = System.currentTimeMillis()
    private var counter = 0

    override fun run() {
        counter += 1
        if (EffectTags.MIASMA !in thornEntity.scoreboardTags) { this.cancel() }
        val timeElapsed = System.currentTimeMillis() - puffyPricklyCooldown
        if (puffyPricklyCount < counter || timeElapsed > puffyPricklyCount * 1000) {
            thornEntity.scoreboardTags.remove(EffectTags.MIASMA)
            this.cancel()
        }
    }

}
package me.shadowalzazel.mcodyssey.tasks.enchantment_tasks

import me.shadowalzazel.mcodyssey.constants.EffectTags
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// Todo: Redo
// ARCANE_CELL TASK
class ArcaneCellTask(
    private val victim: LivingEntity,
    private val jailedLocation: Location,
    private val cellCount: Int) : BukkitRunnable()
{
    private var counter = 0

    override fun run() {  // Run every 5 ticks
        victim.also {
            if (it.isDead) { this.cancel() }
            if (EffectTags.ARCANE_JAILED !in it.scoreboardTags) { this.cancel() }
            counter += 1
            // TODO: Make circular cell purplish/blueish
            it.teleport(jailedLocation)

            // Timer
            if (counter > cellCount) {
                it.scoreboardTags.remove(EffectTags.ARCANE_JAILED)
                this.cancel()
            }
        }

    }
}
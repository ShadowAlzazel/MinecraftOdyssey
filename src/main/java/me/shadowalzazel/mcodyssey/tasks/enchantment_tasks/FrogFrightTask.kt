package me.shadowalzazel.mcodyssey.tasks.enchantment_tasks

import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

// FROG_FRIGHT TASK
class FrogFrightTask(
    private val victim: LivingEntity,
    private val pullDirection: Vector
) : BukkitRunnable()
{
    override fun run() {
        if (!victim.isDead) {
            victim.velocity = pullDirection.clone().multiply(-1.1)
        }
        this.cancel()
    }
}
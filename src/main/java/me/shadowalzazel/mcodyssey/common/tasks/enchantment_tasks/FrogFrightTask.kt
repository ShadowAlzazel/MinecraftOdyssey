package me.shadowalzazel.mcodyssey.common.tasks.enchantment_tasks

import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class FrogFrightTask(
    private val victim: LivingEntity,
    private val pullDirection: Vector,
    private val level: Int = 1,
) : BukkitRunnable()
{
    override fun run() {
        if (!victim.isDead) {
            victim.velocity = pullDirection.clone().multiply(-1.1)
            victim.damage(level * 1.0)
        }
        this.cancel()
    }
}
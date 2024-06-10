package me.shadowalzazel.mcodyssey.tasks.enchantment_tasks

import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

@Suppress("UnstableApiUsage")
class ConflagrateTask(
    private val victim: LivingEntity,
    private val damage: Double
) : BukkitRunnable()
{
    override fun run() {
        if (!victim.isDead) {
            val source = DamageSource.builder(DamageType.ON_FIRE).build()
            victim.damage(damage, source)
        }
        this.cancel()
    }
}
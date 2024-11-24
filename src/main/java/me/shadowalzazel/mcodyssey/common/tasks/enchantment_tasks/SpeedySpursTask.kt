package me.shadowalzazel.mcodyssey.common.tasks.enchantment_tasks

import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

// SPEEDY_SPURS TASK
// TODO: Change base movement of horse
class SpeedySpursTask(
    private val player: LivingEntity,
    private val mount: LivingEntity,
    private val modifier: Int) : BukkitRunnable()
{
    override fun run() {
        // Checks if mount is player to add effects
        if (player !in mount.passengers) { this.cancel() }
        val speedEffect = PotionEffect(PotionEffectType.SPEED, 10 * 20 , modifier - 1)
        mount.addPotionEffect(speedEffect)
    }
}
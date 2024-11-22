package me.shadowalzazel.mcodyssey.tasks.enchantment_tasks

import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.common.listeners.enchantment_listeners.RangedListeners.cloneAndTag
import org.bukkit.entity.*
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

// BURST_BARRAGE
class BurstBarrageTask(
    private val shooter: LivingEntity,
    private val amount: Int,
    private val velocity: Vector,
    private val projectile: Projectile
) : BukkitRunnable()
{
    private var counter = 0

    override fun run() {
        counter += 1
        // Check if tag removed
        if (EntityTags.IS_BURST_BARRAGING !in shooter.scoreboardTags) { this.cancel() }
        // Spawn projectile and set velocity
        shooter.launchProjectile(projectile.javaClass).also {
            it.cloneAndTag(projectile)
            it.addScoreboardTag(EntityTags.REPLICATED_ARROW)
            // Projectile
            it.velocity = shooter.eyeLocation.direction.clone().normalize().multiply(velocity.length() - (0.05))
        }
        if (counter > amount) {
            shooter.removeScoreboardTag(EntityTags.IS_BURST_BARRAGING)
            this.cancel()
        }
    }
}
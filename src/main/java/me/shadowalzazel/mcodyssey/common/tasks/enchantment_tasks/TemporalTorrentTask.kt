package me.shadowalzazel.mcodyssey.common.tasks.enchantment_tasks

import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import org.bukkit.entity.Projectile
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class TemporalTorrentTask (
    private val level: Int,
    private val initialVelocity: Vector,
    private val projectile: Projectile
) : BukkitRunnable()
{
    private var counter = 0

    // This one has a bounce
    /*
    override fun run() { // Every 4 ticks / 0.2 secs
        counter += 1
        if (!projectile.scoreboardTags.contains(EntityTags.TEMPORAL_TORRENT_ARROW)) return

        if (counter > level) {
            projectile.velocity = initialVelocity.clone().multiply(1.0 + (0.1 * level))
            this.cancel()
        }
        // USE THIS FOR REDIRECT
    }
     */

    override fun run() { // Every 4 ticks / 0.2 secs
        counter += 1
        if (!projectile.scoreboardTags.contains(EntityTags.TEMPORAL_ARROW)) return
        if (projectile.isDead) this.cancel()

        if (counter > level) {
            val newSpeed = initialVelocity.clone().length() * (1.0 + (0.1 * level))
            projectile.velocity = projectile.velocity.multiply(newSpeed)
            this.cancel()
        }
    }
}
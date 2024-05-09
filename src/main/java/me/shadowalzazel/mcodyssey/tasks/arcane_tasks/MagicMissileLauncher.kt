package me.shadowalzazel.mcodyssey.tasks.arcane_tasks

import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class MagicMissileLauncher (
    private val initialVelocity: Vector,
    private val projectile: Projectile,
    private val maxDurationInTicks: Int,
    private val trackingDelayInTicks: Int,
    private val guided: Boolean,
    private val period: Int, // How many times this is ticked
    private val target: Location,
    private val targetEntity: Entity? = null
) : BukkitRunnable()
{
    private var counter = 0

    // ARCANE AUGMENTS (use gems)

    // DIFFERENT MODES (FIRING DIRECTION, TRACKING, SPEED)
    // VLS (Vertical Launch System) go to designated target
    // Auto-Homing (launch and forget)
    // FOX-2 (Cursor Tracking)
    // Delayed Tracking (After a delay track target)

    // Launch modes: VLS, PlayerEye, Set Parabola, SpawnOnTop


    override fun run() {
        // Remove if passed max time
        counter += 1
        if (counter > maxDurationInTicks.div(period)) {
            projectile.remove()
            this.cancel()
            return
        }
        if (projectile.isDead) {
            projectile.remove()
            this.cancel()
            return
        }
        // Track
        if (counter > trackingDelayInTicks.div(period)) {
            val destination = if (guided) {
                getGuidedLocation() ?: target
            } else {
                targetEntity?.location ?: target
            }
            val origin = projectile.location
            val velocity = destination.clone().subtract(origin).toVector().normalize().multiply(initialVelocity.length())
            projectile.velocity = velocity
        }
    }

    private fun getGuidedLocation(): Location? {
        val launcher = projectile.shooter ?: return null
        if (launcher !is Player) return null
        val reach = 48.0
        val result = launcher.rayTraceBlocks(reach, FluidCollisionMode.NEVER) ?: return null
        val location = result.hitPosition.toLocation(launcher.world)
        return location
    }


}
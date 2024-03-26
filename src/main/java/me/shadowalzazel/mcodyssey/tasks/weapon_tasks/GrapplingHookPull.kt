package me.shadowalzazel.mcodyssey.tasks.weapon_tasks

import me.shadowalzazel.mcodyssey.constants.EntityTags
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class GrapplingHookPull(
    private val hooker: LivingEntity,
    private val hook: Projectile,
    private val grapplingHook: ItemStack,
    private val power: Double = 0.4,
    private val maxClose: Double = 2.0
) : BukkitRunnable() {

    // FOR ODM Gear have shift fire the grapples
    override fun run() {
        if (hooker.scoreboardTags.contains(EntityTags.CANCEL_GRAPPLE)) {
            cancelGrapple("Override Cancel")
            return
        }
        // Check if active
        val hookerActive = !hooker.isDead && hooker.isTicking
        if (!hookerActive) { // check if hook is Dead?
            cancelGrapple("Not Active")
            return
        }
        // MAYBE CAN EVEN PULL throughout
        val mainHand = hooker.equipment?.itemInMainHand
        if (mainHand != grapplingHook) {
            cancelGrapple("Not Matching Item [pull]")
            return
        }
        // Cancel with shift
        if (hooker is Player && hooker.isSneaking) {
            cancelGrapple("Toggle Shift")
            return
        }

        // Math and Logic
        val isGrappling = hooker.scoreboardTags.contains(EntityTags.IS_GRAPPLING)
        if (isGrappling) {
            val destination = hook.location
            val origin = hooker.location
            // TINKER CHAIN HOOKS
            // THIS IS AFFECTED BY THE LEVEL/TOOL
            // STORE IN TAGS
            // [LIMITER] controls if you can ignore max centripetal speed
            // Force determines how fast the vector pulls
            // Currently does not cap pull vector -> it stacks
            // Different Hooks have different forces and max velocities
            // Also how close you can be
            // Rope works by counteracting y-vector due to newtons 3rd law
            // Change if power is calculated as a constant [Force] or just add vector
            val pullForce = power
            val pullingVector = destination.clone().subtract(origin).toVector().normalize().multiply(pullForce)
            val newVector = hooker.velocity.clone().add(pullingVector)
            hooker.velocity = newVector
            println("New Velocity: $newVector")
            val distance = destination.distance(origin)
            //val display = origin.world.spawnEntity(origin, EntityType.BLOCK_DISPLAY)
            // Cancel Grapple if too close
            if (distance < maxClose) {
                cancelGrapple("Too Close")
                return
            }
            // !!!!! MAYBE GAIN SOME VELOCITY IN DIRECTION LOOKING AT OR CURRENT DIRECTION
        } else {
            cancelGrapple("Not Grappling")
            return
        }
    }

    private fun cancelGrapple(reason: String) {
        println("Canceled because: $reason")
        hooker.removeScoreboardTag(EntityTags.IS_GRAPPLING)
        this.cancel()
    }

}
package me.shadowalzazel.mcodyssey.listeners.utility

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class BurstBarrageTask(private val someEntity: LivingEntity, private val burstAmount: Int, private val burstVelocity: Vector, private val burstProjectile: Entity) : BukkitRunnable() {
    private var counter = 0
    private var burstCooldown = System.currentTimeMillis()

    override fun run() {
        // some timer
        if ("Burst_Shooting" !in someEntity.scoreboardTags) {
            this.cancel()
        }
        counter += 1
        // get direction
        // normalize
        // multiply by original velocity magnitude
        val someProjectile = someEntity.world.spawnEntity(someEntity.location.clone().add(0.0, 1.5, 0.0), burstProjectile.type)
        // Add aga barrage
        val newVelocity = someEntity.eyeLocation.direction.clone().normalize()
        newVelocity.multiply(burstVelocity.length())


        // Fix for effect arrows
        someProjectile.velocity = newVelocity
        val timeElapsed = System.currentTimeMillis() - burstCooldown
        if (burstAmount < counter || timeElapsed > (0.25 * (burstAmount + 1)) * 1000) {
            someEntity.removeScoreboardTag("Burst_Shooting")
            this.cancel()
        }
    }
}
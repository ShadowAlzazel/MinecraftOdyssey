package me.shadowalzazel.mcodyssey.listeners.tasks

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class BurstBarrageTask(private val someEntity: LivingEntity, private val burstAmount: Int, private val burstVelocity: Vector, private val burstProjectile: Entity) : BukkitRunnable() {
    private var counter = 0
    private var burstCooldown = System.currentTimeMillis()

    override fun run() {
        counter += 1
        // Check if tag removed
        if ("Burst_Shooting" !in someEntity.scoreboardTags) { this.cancel() }

        // Spawn projectile and set velocity
        val someProjectile = someEntity.world.spawnEntity(someEntity.location.clone().add(0.0, 1.5, 0.0), burstProjectile.type)
        val newVelocity = someEntity.eyeLocation.direction.clone().normalize()
        newVelocity.multiply(burstVelocity.length() - 0.1)
        someProjectile.velocity = newVelocity

        // Fix for effect arrows !!!!

        val timeElapsed = System.currentTimeMillis() - burstCooldown
        if (burstAmount < counter || timeElapsed > (0.25 * (burstAmount + 1)) * 1000) {
            someEntity.removeScoreboardTag("Burst_Shooting")
            this.cancel()
        }
    }
}
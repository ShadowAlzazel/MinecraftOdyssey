package me.shadowalzazel.mcodyssey.tasks.weapon_tasks

import org.bukkit.entity.ThrowableProjectile
import org.bukkit.scheduler.BukkitRunnable

class ProjectileDelete(
    private val projectile: ThrowableProjectile,
) : BukkitRunnable() {

    override fun run() {
        projectile.remove()
        this.cancel()
        return
    }
}
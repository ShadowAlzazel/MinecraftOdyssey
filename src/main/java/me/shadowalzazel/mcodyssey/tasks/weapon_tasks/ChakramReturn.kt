package me.shadowalzazel.mcodyssey.tasks.weapon_tasks

import me.shadowalzazel.mcodyssey.constants.EntityTags
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.ThrowableProjectile
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class ChakramReturn(
    private val thrower: LivingEntity,
    private val chakram: ThrowableProjectile,
    private val originalWeapon: ItemStack,
) : BukkitRunnable() {

    override fun run() {
        val throwerActive = !thrower.isDead && thrower.isTicking
        if (chakram.isTicking && throwerActive && !chakram.scoreboardTags.contains(EntityTags.CHAKRAM_HAS_BOUNCED)) {
            val destination = thrower.eyeLocation
            val origin = chakram.location
            val velocity = destination.clone().subtract(origin).toVector().normalize().multiply(2.1)
            chakram.velocity = velocity
        }
        this.cancel()
    }
}
package me.shadowalzazel.mcodyssey.tasks.weapon_tasks

import me.shadowalzazel.mcodyssey.constants.EntityTags
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class GrapplingHookShot(
    private val hooker: LivingEntity,
    private val hook: Projectile,
    val grapplingHook: ItemStack,
) : BukkitRunnable() {

    override fun run() {
        if (!hooker.scoreboardTags.contains(EntityTags.HAS_SHOT_GRAPPLE)) {
            cancelShot("Not shot tag")
            return
        }
        // Check if active
        val hookerActive = !hooker.isDead && hooker.isTicking
        if (!hookerActive || hook.isDead) {
            cancelShot("Not Active")
            return
        }
        // MAYBE CAN EVEN PULL throughout
        val mainHand = hooker.equipment?.itemInMainHand
        if (mainHand != grapplingHook) {
            cancelShot("Item Not Matching [shot]")
            return
        }
        // Remove Has shot grapple if successful grappling
        val isGrappling = hooker.scoreboardTags.contains(EntityTags.IS_GRAPPLING)
        if (isGrappling) {
            cancelShot("Successful grapple")
            return
        }

    }

    private fun cancelShot(reason: String) {
        println("Canceled because: $reason")
        hooker.removeScoreboardTag(EntityTags.HAS_SHOT_GRAPPLE)
        hook.removeScoreboardTag(EntityTags.GRAPPLE_HOOK)
        this.cancel()
        return
    }

}
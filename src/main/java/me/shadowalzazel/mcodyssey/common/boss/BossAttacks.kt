package me.shadowalzazel.mcodyssey.common.boss

import me.shadowalzazel.mcodyssey.common.boss.hog_rider.ShockwaveSmashAttack
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

/**
 * Hurls the source toward a target with a big vertical arc. If the source is
 * riding something (a mount), the *vehicle* is launched so the whole unit flies.
 *
 * [onLand] fires the instant the leaper touches ground again — chain a
 * [ShockwaveSmashAttack] here to get a jump-then-slam combo.
 *
 * Reusable by any boss: `LeapAttack()` for a default ~20-block hop, or tune
 * [leapPower]/[horizontalPull] per boss.
 */
class LeapAttack(
    private val leapPower: Double = 2.0,      // vertical velocity (~2.0 ≈ 20 blocks high)
    private val horizontalPull: Double = 1.5, // how hard it flies toward the target
    private val onLand: BossAttack? = null,
    private val landTimeoutTicks: Int = 120,
) : BossAttack {

    override fun execute(ctx: AttackContext) {
        val target = ctx.targets.firstOrNull() ?: ctx.nearby.randomOrNull() ?: return
        val leaper: Entity = ctx.source.vehicle ?: ctx.source

        val horizontal = target.location.toVector().subtract(leaper.location.toVector()).setY(0.0)
        val direction = if (horizontal.lengthSquared() > 1e-4) horizontal.normalize()
        else leaper.location.direction.setY(0.0).normalize()

        leaper.velocity = direction.multiply(horizontalPull).setY(leapPower)

        //.world.playSound(leaper.location, Sound.ENTITY_HOGLIN_ANGRY, 2.0f, 0.8f)
        //leaper.world.playSound(leaper.location, Sound.ENTITY_RAVAGER_ROAR, 1.5f, 1.2f)

        if (onLand != null) {
            LandingWatcher(ctx.plugin, ctx.source, leaper, landTimeoutTicks) {
                onLand.execute(landingContext(ctx.plugin, ctx.source, leaper))
            }.runTaskTimer(ctx.plugin, 2L, 1L)
        }
    }

    private fun landingContext(plugin: JavaPlugin, source: LivingEntity, leaper: Entity): AttackContext {
        val here = leaper.location
        val nearby = here.getNearbyPlayers(16.0).toList()
        return AttackContext(plugin, source, here.world, here, nearby, nearby)
    }

    /** Waits for the leaper to leave the ground and land again, then fires [onLand]. */
    private class LandingWatcher(
        private val plugin: JavaPlugin,
        private val source: Entity,
        private val leaper: Entity,
        private val timeoutTicks: Int,
        private val onLand: () -> Unit,
    ) : BukkitRunnable() {
        private var airborne = false
        private var ticks = 0

        override fun run() {
            ticks++
            if (!leaper.isValid || !source.isValid) {
                cancel(); return
            }
            if (!leaper.isOnGround) airborne = true
            if ((airborne && leaper.isOnGround) || ticks >= timeoutTicks) {
                onLand()
                cancel()
            }
        }
    }
}
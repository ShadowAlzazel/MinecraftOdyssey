package me.shadowalzazel.mcodyssey.common.boss.hog_rider

import me.shadowalzazel.mcodyssey.common.boss.AttackContext
import me.shadowalzazel.mcodyssey.common.boss.BossAttack
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.cos
import kotlin.math.sin

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

        leaper.world.playSound(leaper.location, Sound.ENTITY_HOGLIN_ANGRY, 2.0f, 0.8f)
        leaper.world.playSound(leaper.location, Sound.ENTITY_RAVAGER_ROAR, 1.5f, 1.2f)
        ctx.nearby.forEach { it.playSound(it.location, Sound.ITEM_GOAT_HORN_SOUND_5, 2.0f, 0.85f) }

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

/**
 * A ground slam that sends a visible ring of "block smash" particles racing
 * outward like a wave. Each player the wavefront passes over is knocked back
 * and damaged exactly once.
 *
 * Fully reusable — drop it on any boss, or hand it to [LeapAttack] as its onLand.
 */
class ShockwaveSmashAttack(
    private val maxRadius: Double = 12.0,
    private val ringSpeed: Double = 0.8,    // blocks the wavefront advances per step
    private val stepTicks: Long = 2L,
    private val band: Double = 1.6,         // how "thick" the damaging front is
    private val damage: Double = 12.0,
    private val knockback: Double = 1.3,
    private val debris: Material = Material.NETHERRACK,
) : BossAttack {

    override fun execute(ctx: AttackContext) {
        ShockwaveTask(
            source = ctx.source,
            center = ctx.origin.clone(),
            maxRadius = maxRadius,
            ringSpeed = ringSpeed,
            band = band,
            damage = damage,
            knockback = knockback,
            debris = debris,
        ).runTaskTimer(ctx.plugin, 0L, stepTicks)
    }

    private class ShockwaveTask(
        private val source: LivingEntity,
        private val center: Location,
        private val maxRadius: Double,
        private val ringSpeed: Double,
        private val band: Double,
        private val damage: Double,
        private val knockback: Double,
        private val debris: Material,
    ) : BukkitRunnable() {

        private val world = center.world
        private val debrisData = debris.createBlockData()
        private val alreadyHit = HashSet<java.util.UUID>()
        private var radius = 1.0
        private var first = true

        override fun run() {
            if (first) {
                first = false
                world.playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 1.4f, 0.7f)
                world.playSound(center, Sound.ITEM_MACE_SMASH_GROUND_HEAVY, 1.6f, 0.9f)
                world.spawnParticle(Particle.EXPLOSION, center, 2)
            }

            drawRing()
            damageFront()

            radius += ringSpeed
            if (radius > maxRadius) cancel()
        }

        private fun drawRing() {
            val points = maxOf(8, (radius * 6).toInt())
            for (i in 0 until points) {
                val angle = 2.0 * Math.PI * i / points
                val x = center.x + radius * cos(angle)
                val z = center.z + radius * sin(angle)
                val at = Location(world, x, center.y + 0.25, z)
                world.spawnParticle(Particle.BLOCK, at, 4, 0.15, 0.1, 0.15, 0.0, debrisData)
                world.spawnParticle(Particle.CLOUD, at, 1, 0.0, 0.0, 0.0, 0.02)
            }
        }

        private fun damageFront() {
            val attacker = source.takeIf { it.isValid }
            center.getNearbyPlayers(maxRadius + band).forEach { player ->
                if (player.uniqueId in alreadyHit) return@forEach
                val dx = player.location.x - center.x
                val dz = player.location.z - center.z
                val flatDistance = kotlin.math.sqrt(dx * dx + dz * dz)
                if (flatDistance in (radius - band)..(radius + band)) {
                    alreadyHit += player.uniqueId
                    knock(player)
                    player.damage(damage, attacker)
                }
            }
        }

        private fun knock(player: Player) {
            val out = player.location.toVector().subtract(center.toVector()).setY(0.0)
            val dir = if (out.lengthSquared() > 1e-4) out.normalize()
            else player.location.direction.setY(0.0).normalize()
            player.velocity = dir.multiply(knockback).setY(0.55)
            player.playSound(player.location, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 0.8f)
        }
    }
}
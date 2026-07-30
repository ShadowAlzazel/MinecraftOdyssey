package me.shadowalzazel.mcodyssey.common.boss.slime_king

import me.shadowalzazel.mcodyssey.common.boss.AttackContext
import me.shadowalzazel.mcodyssey.common.boss.BossAttack
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.cos
import kotlin.math.sin

/**
 * A ground slam that sends a visible ring of "block smash" particles racing
 * outward like a wave. Each player the wavefront passes over is knocked back
 * and damaged exactly once.
 *
 * Fully reusable — drop it on any boss, or hand it to [LeapAttack] as its onLand.
 */
class SlimeShockwaveAttack(
    private val maxRadius: Double = 12.0,
    private val ringSpeed: Double = 1.2,    // blocks the wavefront advances per step
    private val stepTicks: Long = 2L,
    private val band: Double = 1.6,         // how "thick" the damaging front is
    private val damage: Double = 12.0,
    private val knockback: Double = 1.3,
    private val debris: Material = Material.SLIME_BLOCK,
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
        private val blockData = debris.createBlockData()
        private val alreadyHit = HashSet<java.util.UUID>()
        private var radius = 1.0
        private var first = true

        override fun run() {
            if (first) {
                first = false
                world.playSound(center, Sound.ENTITY_SLIME_SQUISH, 1.4f, 0.7f)
                world.playSound(center, Sound.BLOCK_SLIME_BLOCK_PLACE, 1.6f, 0.9f)
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
                world.spawnParticle(Particle.BLOCK, at, 4, 0.15, 0.1, 0.15, 0.0, blockData)
                world.spawnParticle(Particle.ITEM_SLIME, at, 1, 0.0, 0.0, 0.0, 0.02)
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
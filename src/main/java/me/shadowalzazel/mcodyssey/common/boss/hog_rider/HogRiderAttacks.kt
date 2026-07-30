package me.shadowalzazel.mcodyssey.common.boss.hog_rider

import me.shadowalzazel.mcodyssey.common.boss.AttackContext
import me.shadowalzazel.mcodyssey.common.boss.BossAttack
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

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



/**
 * Summons a wave of reinforcements onto valid "seat" blocks arranged in a ring
 * around an arena centre (e.g. a coliseum's diorite-slab stands).
 *
 * Optimised placement: instead of scanning a volume, it walks points around the
 * ring's *circumference* only, probing a couple of blocks vertically at each,
 * and stops the moment it has enough spots. Anything it can't seat is spawned
 * near the boss instead, so the move never silently under-delivers.
 *
 * The attack is deliberately generic — it finds positions and fires FX, but the
 * caller owns *what* gets summoned (via [summon]) and *where the centre is*
 * (via [centerResolver]). That keeps it reusable for any arena boss.
 */
class SummonReinforcementsAttack(
    private val count: IntRange = 3..4,
    private val ringRadius: Double = 25.0,
    private val ringHeight: Int = 11,          // blocks above the centre where seats sit
    private val ringSamples: Int = 48,         // candidate points around the circumference
    private val verticalProbe: Int = 1,        // search this many blocks up/down for a slab
    private val isSeat: (Material) -> Boolean = { it == Material.DIORITE_SLAB },
    private val centerResolver: (LivingEntity) -> Location?,
    private val canTrigger: (LivingEntity) -> Boolean = { true },
    private val summon: (Location) -> Unit,
) : BossAttack {

    override fun execute(ctx: AttackContext) {
        if (!canTrigger(ctx.source)) return

        val needed = count.random()
        val center = centerResolver(ctx.source) ?: ctx.source.location

        val placements = ArrayList<Location>(needed)
        placements += findSeats(center, needed)

        ctx.nearby.forEach { it.playSound(it.location, Sound.ITEM_GOAT_HORN_SOUND_5, 2.0f, 0.85f) }

        // Fallback: fill any shortfall right next to the boss.
        while (placements.size < needed) {
            placements += ctx.source.location.clone().add(
                (-3..3).random().toDouble(), 0.0, (-3..3).random().toDouble(),
            )
        }

        placements.forEach { spot ->
            spawnFx(spot)
            summon(spot)
        }
        ctx.source.world.playSound(ctx.source.location, Sound.EVENT_RAID_HORN, 2.0f, 0.7f)
    }

    /** Walk the ring's circumference, collecting up to [needed] valid seat spots. */
    private fun findSeats(center: Location, needed: Int): List<Location> {
        val world = center.world
        val ringY = center.blockY + ringHeight
        val found = ArrayList<Location>(needed)

        // Random start offset so repeat casts don't always seat on the same slabs.
        val start = (0 until ringSamples).random()
        for (i in 0 until ringSamples) {
            if (found.size >= needed) break // early exit — the whole point of the optimisation
            val step = (start + i) % ringSamples
            val angle = 2.0 * Math.PI * step / ringSamples
            val bx = floor(center.x + ringRadius * cos(angle)).toInt()
            val bz = floor(center.z + ringRadius * sin(angle)).toInt()

            val seat = probeForSeat(world, bx, ringY, bz) ?: continue
            if (found.none { it.blockX == seat.blockX && it.blockZ == seat.blockZ }) {
                val toCenter = center.toVector().subtract(seat.toVector()).setY(0.0)
                if (toCenter.lengthSquared() > 1e-4) seat.direction = toCenter // face inward
                found += seat
            }
        }
        return found
    }

    /** Look a few blocks up/down and out (X/Z) around this column for a seat slab with headroom. */
    private fun probeForSeat(world: World, bx: Int, ringY: Int, bz: Int): Location? {
        val horizontalProbe = 2
        for (dx in -horizontalProbe..horizontalProbe) {
            for (dz in -horizontalProbe..horizontalProbe) {
                val x = bx + dx
                val z = bz + dz
                for (dy in -verticalProbe..verticalProbe) {
                    val y = ringY + dy
                    val slab = world.getBlockAt(x, y, z)
                    val headroom = world.getBlockAt(x, y + 1, z).isPassable &&
                            world.getBlockAt(x, y + 2, z).isPassable
                    if (isSeat(slab.type) && headroom) {
                        // Stand on top of the slab (small settle drop for bottom slabs).
                        return Location(world, x + 0.5, (y + 1).toDouble(), z + 0.5)
                    }
                }
            }
        }
        return null
    }

    private fun spawnFx(loc: Location) {
        loc.world.spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 30, 0.3, 0.6, 0.3, 0.02)
        loc.world.spawnParticle(Particle.LARGE_SMOKE, loc, 15, 0.3, 0.4, 0.3, 0.01)
        loc.world.playSound(loc, Sound.ENTITY_PIGLIN_BRUTE_ANGRY, 1.5f, 0.9f)
    }
}
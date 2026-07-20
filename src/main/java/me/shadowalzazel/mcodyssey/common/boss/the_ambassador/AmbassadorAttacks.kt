package me.shadowalzazel.mcodyssey.common.boss.the_ambassador

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.boss.AttackContext
import me.shadowalzazel.mcodyssey.common.boss.BossAttack
import me.shadowalzazel.mcodyssey.common.tasks.enchantment_tasks.GravitySingularityTask
import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Raider
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import kotlin.collections.forEach
import kotlin.collections.ifEmpty

/* ============================================================================
 *  Reusable attacks. Every one is parameterised with sane defaults, so you can
 *  drop `SkyBombardAttack()` on any boss or `SkyBombardAttack(count = 30)` for
 *  a beefier version elsewhere.
 * ========================================================================== */

class FallingSingularityAttack(
    private val radius: Double = 14.0,
) : BossAttack {
    override fun execute(ctx: AttackContext) {
        ctx.origin.getNearbyPlayers(radius).forEach { player ->
            val onTop = player.location.clone().add(0.0, 16.0, 0.0)

            val fallingSingularity: ArmorStand = (player.world.spawnEntity(onTop, EntityType.ARMOR_STAND) as ArmorStand).apply {
                isSilent = true
                isInvisible = true
                isInvulnerable = true
                isVisible = false
                addScoreboardTag(EffectTags.GRAVITY_WELLED)
                addScoreboardTag(EntityTags.FALLING_SINGULARITY)
                addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 300, 0))
            }
            val task = GravitySingularityTask(
                fallingSingularity,
                ctx.source,
                8,
                2 * 20 // 0.5 * 2 -> 1 second * 20
            )
            task.runTaskTimer(Odyssey.instance, 0, 10)

            // Remove falling singularity
            Odyssey.instance.server.scheduler.runTaskLater(
                Odyssey.instance,
                Runnable {
                    fallingSingularity.remove()
                },
                400L // 20 seconds = 400 ticks
            )
        }
    }
}

/** Rains detonating fireworks down onto [AttackContext.origin]. */
class SkyBombardAttack(
    private val count: Int = 13,
    private val spread: Int = 7,
    private val height: IntRange = 35..45,
) : BossAttack {
    override fun execute(ctx: AttackContext) {
        repeat(count) {
            val loc = ctx.origin.clone().add(
                (-spread..spread).random().toDouble(),
                height.random().toDouble(),
                (-spread..spread).random().toDouble(),
            )
            BossFx.superFirework(ctx.world, loc)
        }
    }
}

/** A vortex that damages and flings everyone near [AttackContext.origin] skyward. */
class GravityLaunchAttack(
    private val radius: Double = 7.5,
    private val damage: Double = 18.0,
) : BossAttack {
    override fun execute(ctx: AttackContext) {
        ctx.origin.getNearbyPlayers(radius).forEach { player ->
            player.damage(damage, ctx.source)
            player.addPotionEffects(
                listOf(
                    PotionEffect(PotionEffectType.LEVITATION, 20 * 8, 0),
                    PotionEffect(PotionEffectType.WEAKNESS, 20 * 5, 1),
                    PotionEffect(PotionEffectType.DARKNESS, 20, 0),
                ),
            )
            with(player) {
                playSound(this, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.3f, 1.1f)
                playSound(this, Sound.ITEM_TRIDENT_THUNDER, 2.5f, 0.8f)
                playSound(this, Sound.BLOCK_BEACON_POWER_SELECT, 1.5f, 0.3f)
                playSound(this, Sound.ITEM_TRIDENT_RIPTIDE_3, 1.0f, 1.0f)
                spawnParticle(Particle.ELECTRIC_SPARK, location, 95, 2.5, 0.5, 2.5)
                spawnParticle(Particle.END_ROD, location, 65, 2.0, 1.0, 2.0)
            }
        }
    }
}

/**
 * Drops an invisible "singularity" above each target that drags nearby players
 * toward it for a short lifetime. The pull itself is a reusable [GravityFallTask].
 */
class GravityAnchorAttack(
    private val spawnHeight: Double = 15.0,
    private val pullRadius: Double = 10.0,
    private val pullStrength: Double = 0.30,
    private val lifetimeTicks: Long = 20L * 16,
) : BossAttack {
    override fun execute(ctx: AttackContext) {
        val victims = ctx.targets.ifEmpty { ctx.nearby }
        victims.forEach { player ->
            val anchor = BossFx.spawnGravityAnchor(ctx.world, player.location.clone().add(0.0, spawnHeight, 0.0))
            GravityFallTask(anchor, pullRadius, pullStrength).runTaskTimer(ctx.plugin, 0L, 2L)
            ctx.plugin.server.scheduler.runTaskLater(ctx.plugin, Runnable { anchor.remove() }, lifetimeTicks)
        }
    }
}

/**
 * Yanks a player to the boss and buries them in levitation + slowness.
 * Exposed as a plain function too, so a task or another boss can reuse it.
 */
class VoidPullAttack(private val damage: Double = 10.0) : BossAttack {
    override fun execute(ctx: AttackContext) = ctx.targets.forEach { pull(ctx.source, it, damage) }

    companion object {
        fun pull(source: LivingEntity, player: Player, damage: Double = 10.0) = with(player) {
            teleport(source.location)
            addPotionEffects(
                listOf(
                    PotionEffect(PotionEffectType.LEVITATION, 20 * 10, 1),
                    PotionEffect(PotionEffectType.SLOWNESS, 20 * 10, 2),
                ),
            )
            damage(damage, source)
            BossFx.abductionFx(this)
        }
    }
}

/**
 * Grabs a target, mounts the boss on them, and spawns decoy copies + homing
 * arrows from every bystander. The brief "ride" is handled by [HijackControlTask].
 *
 * NOTE: the original AmbassadorHijackTasks body wasn't available, so the ride
 * behaviour here is a faithful reconstruction — tune duration/damage to taste.
 */
class HijackAttack(
    private val decoyRadius: Double = 22.0,
    private val rideTicks: Long = 20L * 3,
) : BossAttack {
    override fun execute(ctx: AttackContext) {
        val target = ctx.targets.firstOrNull() ?: return
        with(target) {
            teleport(ctx.source.location)
            ctx.source.addPassenger(this)
            damage(2.0, ctx.source)
            playSound(this, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 1.0f)
            playSound(this, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0f, 1.1f)
        }
        target.world.getNearbyPlayers(target.location, decoyRadius)
            .filter { it != target }
            .forEach { bystander ->
                val arrow = bystander.world.spawnEntity(bystander.location, EntityType.ARROW) as Arrow
                val dir = target.location.clone().subtract(arrow.location).toVector().normalize()
                arrow.velocity = dir.clone().multiply(3.14)
                bystander.swingMainHand()
                BossFx.abductionFx(bystander)
                BossFx.spawnDecoy(ctx.source, bystander.location)
            }
        HijackControlTask(ctx.source, target).runTaskLater(ctx.plugin, rideTicks)
    }
}

/* ============================================================================
 *  Reusable tasks
 * ========================================================================== */

/** Continuously drags players within [radius] toward [anchor]. Reuse anywhere. */
class GravityFallTask(
    private val anchor: Entity,
    private val radius: Double,
    private val strength: Double,
) : BukkitRunnable() {
    override fun run() {
        if (anchor.isDead || !anchor.isValid) {
            cancel()
            return
        }
        anchor.location.getNearbyPlayers(radius).forEach { player ->
            val pull = anchor.location.toVector().subtract(player.location.toVector())
            val distance = pull.length().coerceAtLeast(0.5)
            val scaled = (radius / distance).coerceAtMost(2.0)
            player.velocity = player.velocity.add(pull.normalize().multiply(strength * scaled))
            player.world.spawnParticle(Particle.PORTAL, player.location, 6, 0.3, 0.3, 0.3)
        }
    }
}

/** Ends a hijack: dismount, fling the rider off, and leave a parting mark. */
private class HijackControlTask(
    private val source: Entity,
    private val target: Player,
) : BukkitRunnable() {
    override fun run() {
        source.eject()
        if (target.isValid) {
            target.damage(6.0, source)
            target.velocity = target.location.direction.multiply(-1.5).setY(0.8)
            BossFx.abductionFx(target)
        }
    }
}

/* ============================================================================
 *  Shared visual/entity helpers
 * ========================================================================== */

object BossFx {

    private val FIREWORK_COLORS = listOf(Color.BLUE, Color.RED, Color.YELLOW, Color.FUCHSIA, Color.AQUA)

    fun superFirework(world: World, at: Location): Firework =
        (world.spawnEntity(at, EntityType.FIREWORK_ROCKET) as Firework).apply {
            addScoreboardTag(EntityTags.SUPER_FIREWORK)
            fireworkMeta = fireworkMeta.clone().also { meta ->
                meta.addEffect(
                    FireworkEffect.builder()
                        .with(FireworkEffect.Type.BALL_LARGE)
                        .withColor(FIREWORK_COLORS.random())
                        .withFade(FIREWORK_COLORS.random())
                        .trail(true)
                        .flicker(true)
                        .build(),
                )
                meta.power = 120
            }
            ticksToDetonate = 40
            velocity = velocity.setY(-1.918) // drive it back toward the ground
        }

    /** Invisible, invulnerable anchor used as a gravity source. */
    fun spawnGravityAnchor(world: World, at: Location): ArmorStand =
        (world.spawnEntity(at, EntityType.ARMOR_STAND) as ArmorStand).apply {
            isSilent = true
            isInvisible = true
            isInvulnerable = true
            isVisible = false
            setGravity(false)
            addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 300, 0))
        }

    /** A short-lived, harmless copy of the boss for visual chaos. */
    fun spawnDecoy(source: LivingEntity, at: Location): LivingEntity =
        (source.world.spawnEntity(at, source.type) as LivingEntity).apply {
            customName(source.customName())
            isCustomNameVisible = true
            addPotionEffects(
                listOf(
                    PotionEffect(PotionEffectType.REGENERATION, 20 * 300, 0),
                    PotionEffect(PotionEffectType.SLOWNESS, 20 * 300, 1),
                ),
            )
            if (this is Raider) isCanJoinRaid = false
        }

    fun abductionFx(player: Player) = with(player) {
        playSound(this, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 1.0f)
        playSound(this, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0f, 1.1f)
        playSound(this, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.8f, 0.9f)
        spawnParticle(Particle.DAMAGE_INDICATOR, location, 43, 1.5, 0.5, 1.5)
        spawnParticle(Particle.PORTAL, location, 42, 2.5, 0.5, 2.5)
        spawnParticle(Particle.END_ROD, location, 35, 2.0, 1.0, 2.0)
        spawnParticle(Particle.WITCH, location, 25, 1.0, 1.0, 1.0)
    }
}
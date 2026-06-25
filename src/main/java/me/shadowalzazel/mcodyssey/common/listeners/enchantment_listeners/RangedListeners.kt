package me.shadowalzazel.mcodyssey.common.listeners.enchantment_listeners

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.PotionContents
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.tasks.enchantment_tasks.*
import me.shadowalzazel.mcodyssey.common.enchantments.EnchantmentManager
import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.removeTag
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.setIntTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.*
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

@Suppress("UnstableApiUsage")
object RangedListeners : Listener, EnchantmentManager {

    // ──────────────────────────────────────────────────────────────────────────────
    // ──────────────────────────────── CONTEXTS ────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    private val currentOverchargeTasks = mutableMapOf<UUID, OverchargeTask>()

    private class DamageMods {
        var flat: Float = 0.0f         // raw base added before percent
        var percent: Float = 0.0f      // summed, applied as (1 + percent)
        var postPercent: Float = 1.0f  // multiplied last (e.g. magic)
    }

    private data class BowShotContext(
        val event: EntityShootBowEvent,
        val shooter: LivingEntity,
        val projectile: Entity,
        val bow: ItemStack,
        val level: Int,
    )

    private data class ProjectileDamageContext(
        val event: EntityDamageByEntityEvent,
        val projectile: Projectile,
        val shooter: LivingEntity,
        val victim: LivingEntity,
        val mods: DamageMods,
    )

    private data class ProjectileHitContext(
        val event: ProjectileHitEvent,
        val projectile: Projectile,
        val victim: LivingEntity,
    )

    private data class ProjectileDeathContext(
        val event: EntityDeathEvent,
        val projectile: Projectile,
        val victim: LivingEntity,
    )

    private data class BowReadyContext(
        val player: Player,
        val bow: ItemStack,
        val level: Int,
    )

    // ──────────────────────────────────────────────────────────────────────────────
    // ──────────────────────────────── MAPPINGS ────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    // Fired on shoot. Side effects on the projectile/shooter; iteration order is
    // the bow's enchantment order (the map is lookup only), so "priority in order"
    // is preserved.
    private val bowShotEnchantmentsMap: Map<String, (BowShotContext) -> Unit> = mapOf(
        "alchemy_artillery" to { c -> alchemyArtilleryShoot(c.projectile, c.level) },
        "ballistics"        to { c -> ballisticsEnchantmentShoot(c.projectile, c.level) },
        "ambush"            to { c -> ambushEnchantmentShoot(c.projectile, c.level) },
        "bola_shot"         to { c -> bolaShotEnchantmentShoot(c.projectile, c.level) },
        "burst_barrage"     to { c -> burstBarrageEnchantmentShoot(c.projectile, c.shooter, c.level) },
        "chain_reaction"    to { c -> chainReactionEnchantmentShoot(c.projectile, c.level) },
        "cluster_shot"      to { c -> clusterShotEnchantmentShoot(c.projectile, c.level) },
        "deadeye"           to { c -> deadeyeEnchantmentShoot(c.projectile, c.level) },
        "death_from_above"  to { c -> deathFromAboveEnchantmentShoot(c.projectile, c.level) },
        "dynamo"            to { c -> dynamoEnchantmentShoot(c.projectile, c.level) },
        "double_tap"        to { c -> doubleTapEnchantmentShoot(c.projectile, c.shooter) },
        "entanglement"      to { c -> entanglementEnchantmentShoot(c.projectile, c.level) },
        "fan_fire"          to { c -> fanFireEnchantmentShoot(c.projectile, c.shooter, c.level) },
        "lucky_draw"        to { c -> luckyDrawEnchantmentShoot(c.event, c.level) },
        "luxpose"           to { c -> luxposeEnchantmentShoot(c.projectile, c.level) },
        "gale"              to { c -> galeEnchantmentShoot(c.shooter, c.projectile, c.level) },
        "overcharge"        to { c -> overchargeEnchantmentShoot(c.projectile, c.shooter, c.bow) },
        "perpetual"         to { c -> perpetualProjectileEnchantmentShoot(c.projectile, c.level) },
        "rain_of_arrows"    to { c -> rainOfArrowsEnchantmentShoot(c.projectile, c.level) },
        "ricochet"          to { c -> ricochetEnchantmentShoot(c.projectile, c.level) },
        "sharpshooter"      to { c -> sharpshooterEnchantmentShoot(c.projectile, c.level) },
        "single_out"        to { c -> singleOutEnchantmentShoot(c.projectile, c.level) },
        "singularity_shot"  to { c -> singularityShotEnchantmentShoot(c.projectile, c.level, c.shooter) },
        "steady_aim"        to { c -> steadyAim(c.projectile, c.level, c.shooter) },
        "rend"              to { c -> rendEnchantmentShoot(c.projectile, c.level) },
        "temporal"          to { c -> temporalTorrentEnchantmentShoot(c.projectile, c.level) },
        "vulnerocity"       to { c -> vulnerocityEnchantmentShoot(c.projectile, c.level) },
    )

    // On-hit damage.
    // (Base Damage + flat) * (1 + percent) * postPercent
    // Entries below are currently FLAT (behaviour-identical to the old `event.damage += …`).
    // To convert one to a percent modifier: change the enchant fn to return a fraction,
    // then swap `c.mods.flat` -> `c.mods.percent` on its line.
    private val projectileDamageEnchantmentsMap: Map<String, (ProjectileDamageContext) -> Unit> = mapOf(
        EntityTags.AMBUSH_ARROW           to { c -> c.mods.percent += ambushEnchantmentHit(c.projectile, c.victim) },
        EntityTags.BALLISTICS_ARROW       to { c -> c.mods.percent += ballisticsEnchantmentHit(c.projectile) },
        EntityTags.DEADEYE_ARROW          to { c -> c.mods.percent += deadeyeEnchantmentHit(c.projectile, c.victim) },
        EntityTags.DEATH_FROM_ABOVE_ARROW to { c -> c.mods.percent += deathFromAboveEnchantmentHit(c.projectile, c.victim, c.shooter) },
        EntityTags.LUXPOSE_ARROW          to { c -> c.mods.percent += luxposeEnchantmentHit(c.projectile, c.victim) },
        EntityTags.OVERCHARGE_ARROW       to { c -> c.mods.percent += overchargeEnchantmentHit(c.projectile) },
        EntityTags.RICOCHET_ARROW         to { c -> c.mods.percent += ricochetEnchantmentEntityHit(c.projectile) },
        EntityTags.SHARPSHOOTER_ARROW     to { c -> c.mods.percent += sharpshooterEnchantmentHit(c.projectile) },
        EntityTags.SINGLE_OUT_ARROW       to { c -> c.mods.percent += singleOutEnchantmentHit(c.projectile, c.victim) },

        // side effects only — don't touch mods
        EntityTags.DYNAMO_ARROW           to { c -> dynamoEnchantmentHit(c.projectile, c.victim) },
        EntityTags.SOUL_REND_ARROW        to { c -> soulRendEnchantmentHit(c.shooter, c.projectile, c.victim) },
    )

    // On-hit, entity only. Side effects.
    private val projectileHitEnchantmentsMap: Map<String, (ProjectileHitContext) -> Unit> = mapOf(
        EntityTags.BOLA_SHOT_ARROW        to { c -> bolaShotEnchantmentHit(c.projectile, c.victim) },
        EntityTags.CHAIN_REACTION_ARROW   to { c -> chainReactionEnchantmentHit(c.projectile, c.victim) },
        EntityTags.CLUSTER_SHOT_ARROW     to { c -> clusterShotEnchantmentHit(c.projectile, c.victim) },
        EntityTags.ENTANGLEMENT_ARROW     to { c -> entanglementEnchantmentHit(c.projectile, c.victim) },
        EntityTags.VULNEROCITY_ARROW      to { c -> vulnerocityEnchantmentHit(c.projectile, c.victim) },
        EntityTags.RAIN_OF_ARROWS_ARROW   to { c -> rainOfArrowsEnchantmentHit(c.projectile, c.victim) },
    )

    // On kill. Side effects.
    private val projectileDeathEnchantmentsMap: Map<String, (ProjectileDeathContext) -> Unit> = mapOf(
        "THERMO_TEST" to { c -> dynamoEnchantmentKill(c.projectile, c.victim) }, // FIXME: looks like a placeholder tag
    )

    // On arrow ready.
    private val bowReadyEnchantmentsMap: Map<String, (BowReadyContext) -> Unit> = mapOf(
        "overcharge" to { c -> overchargeEnchantmentLoad(c.player, c.bow, c.level) },
    )

    private fun <C> dispatchEnchants(
        item: ItemStack,
        map: Map<String, (C) -> Unit>,
        makeContext: (level: Int) -> C,
    ) {
        for ((enchant, level) in item.enchantments) {
            val handler = map[enchant.getNameId()] ?: continue
            handler(makeContext(level))
        }
    }

    private fun <C> dispatchTags(
        tags: Collection<String>,
        map: Map<String, (C) -> Unit>,
        context: C,
    ) {
        for (tag in tags) map[tag]?.invoke(context)
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // ──────────────────────────────── HANDLERS ────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    // Main function for enchantments relating to shooting bows
    @EventHandler
    fun mainBowShotHandler(event: EntityShootBowEvent) {
        if (event.bow?.hasItemMeta() == false) return
        val projectile = event.projectile
        if (projectile.scoreboardTags.contains(EntityTags.REPLICATED_ARROW)) return
        val shooter = event.entity
        val bow = event.bow ?: return

        dispatchEnchants(bow, bowShotEnchantmentsMap) { level ->
            BowShotContext(event, shooter, projectile, bow, level)
        }
    }

    // Main function for enchantments relating to projectile damage
    @EventHandler
    fun mainProjectileDamageHandler(event: EntityDamageByEntityEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.PROJECTILE) return
        if (event.damager !is Projectile) return
        if (event.entity !is LivingEntity) return
        val projectile = event.damager as Projectile
        if (projectile.shooter == null) return
        val shooter = projectile.shooter as LivingEntity
        val victim = event.entity as LivingEntity

        val mods = DamageMods()
        dispatchTags(
            projectile.scoreboardTags.toSet(), // defensive copy
            projectileDamageEnchantmentsMap,
            ProjectileDamageContext(event, projectile, shooter, victim, mods),
        )

        // (Base Damage + flat) * (1 + percent) * postPercent
        event.damage = (event.damage + mods.flat) * (1 + mods.percent) * mods.postPercent
        if (event.damage < 0.0) event.damage = 0.0
    }

    // Main function for enchantments relating to projectile hits
    @EventHandler
    fun mainProjectileHitHandler(event: ProjectileHitEvent) {
        val projectile: Projectile = event.entity
        if (event.hitEntity is LivingEntity) {
            val victim = event.hitEntity as LivingEntity
            if (projectile.scoreboardTags.isEmpty()) return
            dispatchTags(
                projectile.scoreboardTags.toSet(), // Prevent async problems
                projectileHitEnchantmentsMap,
                ProjectileHitContext(event, projectile, victim),
            )
        } else if (event.hitBlock != null) {
            if (projectile.scoreboardTags.contains(EntityTags.RICOCHET_ARROW)) {
                ricochetEnchantmentBlockHit(projectile, event.hitBlockFace!!.direction)
            }
        }
    }

    // Main function for enchantments relating to entity deaths
    @EventHandler
    fun mainProjectileDeathHandler(event: EntityDeathEvent) {
        if (event.entity.killer !is LivingEntity) return
        if (event.damageSource.damageType != DamageType.ARROW) return
        val projectile = event.damageSource.directEntity ?: return
        if (projectile !is Projectile) return
        val victim: LivingEntity = event.entity

        dispatchTags(
            projectile.scoreboardTags.toSet(), // Prevent async problems
            projectileDeathEnchantmentsMap,
            ProjectileDeathContext(event, projectile, victim),
        )
    }

    // Main function for enchantments relating to loading crossbows
    @EventHandler
    fun crossbowLoadingHandler(event: EntityLoadCrossbowEvent) {
        // Currently a no-op: crossbow enchant handling is not implemented yet.
        // NOTE: the original guard returned when the crossbow *had* meta (inverted),
        // and the body was commented out — so nothing ran in either branch.
        // When you wire this up, the guard should be `if (!event.crossbow.hasItemMeta()) return`.
    }

    // Main function for readying arrows
    @EventHandler
    fun bowReadyHandler(event: PlayerReadyArrowEvent) {
        if (!event.bow.hasItemMeta()) return
        dispatchEnchants(event.bow, bowReadyEnchantmentsMap) { level ->
            BowReadyContext(event.player, event.bow, level)
        }
    }

    // Helper function to clone projectiles and their tags and to omit provided ones
    internal fun Projectile.cloneAndTag(projectile: Projectile, omitList: List<String> = listOf()) {
        // CAN HAVE SET SHOOT ON to launch event again but need to fix bugs
        if (this is AbstractArrow && projectile is AbstractArrow) {
            itemStack = projectile.itemStack
            isPersistent = false
            fireTicks = projectile.fireTicks
            pickupStatus = AbstractArrow.PickupStatus.DISALLOWED

        }
        if (this is Arrow && projectile is Arrow) {
            itemStack = projectile.itemStack
            for (effect in projectile.customEffects) {
                addCustomEffect(effect, true)
            }
            basePotionType = projectile.basePotionType
            pierceLevel = maxOf(projectile.pierceLevel - 1, 0)
        }
        if (this is ThrownPotion) {
            item = (projectile as ThrownPotion).item
        }
        if (this is Firework) {
            fireworkMeta = (projectile as Firework).fireworkMeta
            //setGravity(false)
        }
        // Tags
        for (tag in projectile.scoreboardTags) {
            if (tag in omitList) continue
            //if (tag == EntityTags.ORIGINAL_ARROW)
            this.addScoreboardTag(tag)
        }
        // Stats
        fireTicks = projectile.fireTicks
        setGravity(projectile.hasGravity()) // Perpetual
        // Shooter
        shooter = projectile.shooter
        setHasLeftShooter(false)
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // ─────────────────────────────── ENCHANTMENTS ─────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    private fun alchemyArtilleryShoot(projectile: Entity, level: Int) {
        val durationScale = 1 + (0.2 * level)
        val speedScale = 1 + (0.1 * level)

        // Extension functions
        fun PotionEffect.scaled() = PotionEffect(type, (duration * durationScale).toInt(), amplifier)
        fun List<PotionEffect>.scaleAll() = map { it.scaled() }

        // Get contents
        val contents = when (projectile) {
            is Arrow ->  {
                val entityPotionContents = projectile.getData(DataComponentTypes.POTION_CONTENTS)
                val itemPotionContents = projectile.itemStack.getData(DataComponentTypes.POTION_CONTENTS)
                entityPotionContents ?: itemPotionContents ?: return
            }
            is ThrownPotion -> {
                val entityPotionContents = projectile.getData(DataComponentTypes.POTION_CONTENTS)
                val itemPotionContents = projectile.item.getData(DataComponentTypes.POTION_CONTENTS)
                entityPotionContents ?: itemPotionContents ?: return
            }
            else -> {
                return
            }
        }

        val scaledEffects = contents.allEffects().scaleAll()

        val newContents = PotionContents.potionContents()
            .apply {
                contents.potion()?.let { potion(it) }
                contents.customColor()?.let { customColor(it) }
                scaledEffects.forEach { addCustomEffect(it) }
            }
            .build()

        // Build contents
        when(projectile) {
            is Arrow -> {
                projectile.itemStack.setData(DataComponentTypes.POTION_CONTENTS, newContents)
            }
            is ThrownPotion -> {
                projectile.item.setData(DataComponentTypes.POTION_CONTENTS, newContents)
            }
        }

        projectile.velocity = projectile.velocity.multiply(speedScale)
    }

    private fun dynamoEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.DYNAMO_ARROW)
            setIntTag(EntityTags.DYNAMO_MODIFIER, level)
        }
    }

    private fun dynamoEnchantmentHit(projectile: Projectile, victim: LivingEntity) {
        if (victim.scoreboardTags.contains(EntityTags.DYNAMO_ARROW)) return
        val modifier = projectile.getIntTag(EntityTags.DYNAMO_MODIFIER) ?: return
        val arrowSpeed = projectile.velocity.length()

        if (arrowSpeed > 0) {
            victim.world.spawnParticle(Particle.ELECTRIC_SPARK, victim.location, 10 * modifier, 0.75, 0.5, 0.75)
            val dynamoDamage = (0.4 * modifier) * arrowSpeed
            val damageSource = DamageSource.builder(DamageType.LIGHTNING_BOLT).build()
            victim.damage(dynamoDamage, damageSource) // Create Damage Source
        }
        return
    }

    private fun oldDynamoEnchantmentKill(projectile: Projectile, victim: LivingEntity) {
        if (victim.scoreboardTags.contains(EntityTags.DYNAMO_ARROW)) return
        val modifier = projectile.getIntTag(EntityTags.DYNAMO_MODIFIER) ?: return
        if (victim.fireTicks > 10) {
            victim.world.spawnParticle(Particle.ELECTRIC_SPARK, victim.location, 10, 0.15, 0.15, 0.15)
            val dynamoDamage = ((victim.fireTicks / 20) + 1) * (modifier * 0.4)
            val nearby = victim.location.getNearbyLivingEntities(3.0).filter { it != victim }
            if (nearby.isNotEmpty()) {
                nearby.forEach {
                    it.world.spawnParticle(Particle.ELECTRIC_SPARK, it.location, 15, 0.25, 0.25, 0.25)
                    val damageSource = DamageSource.builder(DamageType.LIGHTNING_BOLT).build()
                    it.damage(dynamoDamage, damageSource) // Create Damage Source
                }
            }
        }
        return
    }

    private fun oldDdynamoEnchantmentHit(projectile: Projectile, victim: LivingEntity): Double {
        if (victim.scoreboardTags.contains(EntityTags.DYNAMO_ARROW)) return 0.0
        val modifier = projectile.getIntTag(EntityTags.DYNAMO_MODIFIER) ?: return 0.0
        if (victim.fireTicks > 0) {
            victim.world.spawnParticle(Particle.LAVA, victim.location, 10, 0.15, 0.15, 0.15)
            return modifier * 1.0
        }
        return 0.0
    }

    private fun dynamoEnchantmentKill(projectile: Projectile, victim: LivingEntity) {
        if (victim.scoreboardTags.contains(EntityTags.DYNAMO_ARROW)) return
        val modifier = projectile.getIntTag(EntityTags.DYNAMO_MODIFIER) ?: return
        if (victim.fireTicks > 10) {
            victim.world.spawnParticle(Particle.ELECTRIC_SPARK, victim.location, 10, 0.15, 0.15, 0.15)
            val dynamoDamage = ((victim.fireTicks / 20) + 1) * (modifier * 0.4)
            val nearby = victim.location.getNearbyLivingEntities(3.0).filter { it != victim }
            if (nearby.isNotEmpty()) {
                nearby.forEach {
                    it.world.spawnParticle(Particle.ELECTRIC_SPARK, it.location, 15, 0.25, 0.25, 0.25)
                    val damageSource = DamageSource.builder(DamageType.LIGHTNING_BOLT).build()
                    it.damage(dynamoDamage, damageSource) // Create Damage Source
                }
            }
        }
        return
    }

    private fun ambushEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.AMBUSH_ARROW)
            setIntTag(EntityTags.AMBUSH_MODIFIER, level)
        }
    }

    private fun ambushEnchantmentHit(projectile: Projectile, victim: LivingEntity): Float {
        if (victim.scoreboardTags.contains(EntityTags.AMBUSH_MARKED)) return 0.0F
        val modifier = projectile.getIntTag(EntityTags.AMBUSH_MODIFIER) ?: return 0.0F
        victim.world.spawnParticle(Particle.VAULT_CONNECTION, victim.location, 10, 0.15, 0.15, 0.15)
        victim.addScoreboardTag(EntityTags.AMBUSH_MARKED)
        return modifier * 0.25F
    }

    private fun ballisticsEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.BALLISTICS_ARROW)
            setIntTag(EntityTags.BALLISTICS_MODIFIER, level)
        }
    }

    private fun ballisticsEnchantmentHit(projectile: Projectile): Float {
        val modifier = projectile.getIntTag(EntityTags.BALLISTICS_MODIFIER) ?: return 0.0F
        return modifier * 0.10F
    }

    private fun bolaShotEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.BOLA_SHOT_ARROW)
            setIntTag(EntityTags.BOLA_SHOT_MODIFIER, level)
        }
    }

    private fun bolaShotEnchantmentHit(projectile: Projectile, victim: LivingEntity) {
        if (victim.location.block.type != Material.AIR) return
        // Get modifier
        val modifier = projectile.getIntTag(EntityTags.BOLA_SHOT_MODIFIER) ?: return
        victim.location.block.type = Material.COBWEB
        victim.addPotionEffects(listOf(
            PotionEffect(PotionEffectType.SLOWNESS, 20 * (3 + modifier), 0),
            PotionEffect(PotionEffectType.MINING_FATIGUE, 20 * (3 + modifier), 0)))
    }

    private fun burstBarrageEnchantmentShoot(projectile: Entity, shooter: LivingEntity, level: Int) {
        if (!shooter.scoreboardTags.contains(EntityTags.IS_BURST_BARRAGING) && !projectile.scoreboardTags.contains(EntityTags.REPLICATED_ARROW) && projectile is Projectile)  {
            projectile.run {
                if (this is Arrow) {
                    pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                }
                if (this is SpectralArrow) {
                    pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                }
                if (this is AbstractArrow) {
                    pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                }
            }
            shooter.addScoreboardTag(EntityTags.IS_BURST_BARRAGING)
            val initialVelocity = projectile.velocity.clone()
            // Tasks
            val task = BurstBarrageTask(shooter, level, initialVelocity, projectile)
            task.runTaskTimer(Odyssey.instance, 1, 1)
        }

    }

    private fun chainReactionEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.CHAIN_REACTION_ARROW)
            setIntTag(EntityTags.CHAIN_REACTION_MODIFIER, level + 2)
            addScoreboardTag(EntityTags.ORIGINAL_ARROW)
        }
    }

    private fun chainReactionEnchantmentHit(projectile: Projectile, victim: LivingEntity) {
        if (projectile.scoreboardTags.contains(EntityTags.CHAIN_REACTION_SPAWNED)) return
        val modifier = projectile.getIntTag(EntityTags.CHAIN_REACTION_MODIFIER) ?: return
        if (modifier <= 1) return
        // Get Entities
        val lastHitID = projectile.getIntTag(EntityTags.CHAIN_REACTION_LAST_HIT) ?: 0
        val origin = projectile.location.clone()
        val nearby = victim.location.getNearbyLivingEntities(12.0).filter {
            (it != projectile.shooter && it != victim && it.entityId != lastHitID)
        }
        // Get Nearest
        if (nearby.isEmpty()) return
        var closest = nearby.first()
        var distance = closest.location.distance(origin)
        val speed = 2.22
        if (nearby.size < 10) {
            for (entity in nearby) {
                if (entity == closest) continue
                val entityDistance = entity.location.distance(origin)
                if (entityDistance < distance) {
                    distance = entityDistance
                    closest = entity
                }
            }
        }
        // Velocity
        val destination = closest.location.clone()
        val velocity = destination.clone().subtract(origin).toVector().normalize().multiply(speed)
        // Prevent infinite spawns
        if (!projectile.scoreboardTags.contains(EntityTags.ORIGINAL_ARROW)) {
            projectile.addScoreboardTag(EntityTags.CHAIN_REACTION_SPAWNED)
        }
        (projectile.world.spawnEntity(origin, projectile.type) as Projectile).also {
            it.velocity = velocity
            it.addScoreboardTag(EntityTags.CHAIN_REACTION_ARROW)
            it.setIntTag(EntityTags.CHAIN_REACTION_LAST_HIT, victim.entityId)
            it.setIntTag(EntityTags.CHAIN_REACTION_MODIFIER, modifier - 1)
            it.cloneAndTag(projectile, listOf(EntityTags.ORIGINAL_ARROW))
        }
    }

    private fun clusterShotEnchantmentShoot(projectile: Entity, level: Int) {
        projectile.run {
            addScoreboardTag(EntityTags.CLUSTER_SHOT_ARROW)
            addScoreboardTag(EntityTags.ORIGINAL_ARROW)
            setIntTag(EntityTags.CLUSTER_SHOT_MODIFIER, level)
            if (this is Arrow) {
                pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
            if (this is SpectralArrow) {
                pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
            if (this is AbstractArrow) {
                pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            }
        }
    }

    // Shoots in a radial
    private fun clusterShotEnchantmentHit(projectile: Projectile, victim: LivingEntity) {
        if (!projectile.scoreboardTags.contains(EntityTags.ORIGINAL_ARROW)) return
        val modifier = projectile.getIntTag(EntityTags.CLUSTER_SHOT_MODIFIER) ?: return
        // Radius
        val radiusAmount = (modifier * 4)
        for (x in 1..radiusAmount) { // 4/8/12/16
            // Math
            val angle = Math.PI * 2 * (x / (radiusAmount * 1.0))
            val angularCoordinates: Pair<Double, Double> = Pair(5 * cos(angle), 5 * sin(angle))
            val origin = victim.eyeLocation.clone()
            val destination = origin.clone().add(angularCoordinates.first, 0.1, angularCoordinates.second)
            val newVelocity = destination.subtract(origin).toVector().normalize().multiply(1.77)
            val spawnLocation = origin.clone().add(0.0, 0.5, 0.0)
            (projectile.world.spawnEntity(spawnLocation, projectile.type) as Projectile).also {
                // Projectile
                it.velocity = newVelocity
                it.shooter = projectile.shooter
                it.cloneAndTag(projectile, listOf(EntityTags.CLUSTER_SHOT_ARROW))
            }
        }
    }

    private fun deadeyeEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.DEADEYE_ARROW)
            setIntTag(EntityTags.DEADEYE_MODIFIER, level)
        }
    }

    private fun deadeyeEnchantmentHit(projectile: Entity, victim: LivingEntity): Float {
        val modifier = projectile.getIntTag(EntityTags.DEADEYE_MODIFIER) ?: return 0.0F
        val eyeDistance = projectile.location.distance(victim.eyeLocation)
        if (eyeDistance <= 0.8) {
            return modifier * 0.2F
        }
        return 0.0F
    }

    private fun deathFromAboveEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.DEATH_FROM_ABOVE_ARROW)
            setIntTag(EntityTags.DEATH_FROM_ABOVE_MODIFIER, level)
        }
    }

    private fun deathFromAboveEnchantmentHit(projectile: Projectile, victim: LivingEntity, shooter: LivingEntity): Float {
        val modifier = projectile.getIntTag(EntityTags.DEATH_FROM_ABOVE_MODIFIER) ?: return 0.0F
        val height = modifier * 5
        if (victim.location.distance(shooter.location) >= height) {
            return 0.2F * modifier / height
        }
        return 0.0F
    }

    private fun doubleTapEnchantmentShoot(projectile: Entity, shooter: LivingEntity) {
        if (projectile !is Projectile) return
        if (projectile.scoreboardTags.contains(EntityTags.DOUBLE_TAP_SHOT)) return
        (shooter.world.spawnEntity(shooter.eyeLocation, projectile.type) as Projectile).also {
            it.cloneAndTag(projectile)
            it.addScoreboardTag(EntityTags.DOUBLE_TAP_SHOT)
            //val speed = projectile.velocity.clone().length() * 0.95
            //it.velocity = shooter.eyeLocation.direction.clone().normalize().multiply(speed)
            it.velocity = projectile.velocity.clone().multiply(0.95)
        }

    }

    // Quantum Entanglement does TP (legendary affix)
    private fun entanglementEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.ENTANGLEMENT_ARROW)
            setIntTag(EntityTags.ENTANGLEMENT_MODIFIER, level)
        }
    }

    private fun entanglementEnchantmentHit(projectile: Projectile, victim: LivingEntity) {
        val modifier = projectile.getIntTag(EntityTags.ENTANGLEMENT_MODIFIER) ?: return
        victim.addScoreboardTag(EntityTags.ENTANGLED)
        val nearby = victim.location.getNearbyLivingEntities(10.0).filter {
            it.scoreboardTags.contains(EntityTags.ENTANGLED) && it != victim
        }
        if (nearby.isEmpty()) return
        val other = nearby.first()
        // Math
        val origin1 = victim.eyeLocation.clone()
        val destination1 = other.eyeLocation.clone()
        val origin2 = other.eyeLocation.clone()
        val destination2 = victim.eyeLocation.clone()
        val distance = origin1.distance(origin2)
        val velocity1 = destination1.subtract(origin1).toVector().normalize().multiply(0.5 + (0.125 * distance))
        val velocity2 = destination2.subtract(origin2).toVector().normalize().multiply(0.5 + (0.125 * distance))
        victim.velocity = velocity1
        victim.scoreboardTags.remove(EntityTags.ENTANGLED)
        other.velocity = velocity2
        other.scoreboardTags.remove(EntityTags.ENTANGLED)
    }

    private fun fanFireEnchantmentShoot(projectile: Entity, shooter: LivingEntity, level: Int) {
        if (projectile !is Projectile) return
        if (projectile.scoreboardTags.contains(EntityTags.FAN_FIRE_SHOT)) return
        // Math
        val vector1 = shooter.eyeLocation.direction
        val getVector2 = { destination: Location, orig: Location -> destination.clone().subtract(orig).toVector() }
        val nearby = shooter.world.getNearbyLivingEntities(shooter.location, 8.0).filter {
            it != shooter && vector1.angle(getVector2(it.eyeLocation, shooter.eyeLocation)) < 1.74533
        }
        if (nearby.isEmpty()) return
        val counter = minOf(nearby.size, level)
        val origin = shooter.eyeLocation.clone()
        // Run shoot
        for (x in 1..counter) {
            val target = nearby[x - 1]
            (shooter.world.spawnEntity(shooter.eyeLocation, projectile.type) as Projectile).also {
                it.cloneAndTag(projectile)
                it.scoreboardTags.add(EntityTags.FAN_FIRE_SHOT)
                // Velocity
                val speed = projectile.velocity.length() * 0.5
                val destination = target.eyeLocation.clone()
                val velocity = destination.subtract(origin).toVector().normalize().multiply(speed)
                it.velocity = velocity
            }
        }
    }

    private fun galeEnchantmentShoot(shooter: LivingEntity, projectile: Entity, level: Int) {
        if (projectile.velocity.length() <= 2.2) return
        shooter.world.playSound(shooter.location, Sound.ENTITY_WIND_CHARGE_WIND_BURST, 2.5F, 1.5F)
        val task = GaleWindTask(shooter, level)
        task.runTaskLater(Odyssey.instance, 5)
    }

    private fun luckyDrawEnchantmentShoot(event: EntityShootBowEvent, level: Int) {
        val luckyDraw = (level * 10) + 7 > (0..100).random()
        val projectile = event.projectile as? Projectile ?: return
        val shooter = projectile.shooter as? LivingEntity ?: return
        if (luckyDraw) {
            val item = event.consumable?.clone() ?: return
            if (shooter is Player) shooter.inventory.addItem(item)
        }
    }

    private fun luxposeEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.LUXPOSE_ARROW)
            setIntTag(EntityTags.LUXPOSE_MODIFIER, level)
        }
    }

    private fun luxposeEnchantmentHit(projectile: Projectile, victim: LivingEntity): Float {
        if (!victim.isGlowing) return 0.0F
        val modifier = projectile.getIntTag(EntityTags.LUXPOSE_MODIFIER) ?: return 0.0F
        return modifier * 0.1F
    }

    private fun overchargeEnchantmentLoad(player: Player, bow: ItemStack, level: Int) {
        if (player.scoreboardTags.contains(EntityTags.OVERCHARGING)) return
        player.scoreboardTags.add(EntityTags.OVERCHARGING)
        // Prevent Spam
        player.setIntTag(EntityTags.OVERCHARGE_MODIFIER, 0)
        // Task
        val task = OverchargeTask(player, bow, level)
        val delay = (20 * 2)
        if (currentOverchargeTasks[player.uniqueId] != null) {
            currentOverchargeTasks[player.uniqueId]?.cancel()
        }
        currentOverchargeTasks[player.uniqueId] = task
        task.runTaskTimer(Odyssey.instance, delay.toLong(), 10) // Run Effects every other
    }

    private fun overchargeEnchantmentShoot(projectile: Entity, shooter: LivingEntity, bow: ItemStack) {
        if (!shooter.scoreboardTags.contains(EntityTags.OVERCHARGING)) return
        val modifier = shooter.getIntTag(EntityTags.OVERCHARGE_MODIFIER) ?: return
        // Remove all responsible tags
        shooter.removeScoreboardTag(EntityTags.OVERCHARGING)
        shooter.removeTag(EntityTags.OVERCHARGE_MODIFIER)
        if (modifier == 0) return
        // Get task
        val task = currentOverchargeTasks[shooter.uniqueId] ?: return
        val taskBow = task.bow
        task.cancel()
        if (taskBow != bow) return

        with(projectile) {
            addScoreboardTag(EntityTags.OVERCHARGE_ARROW)
            setIntTag(EntityTags.OVERCHARGE_MODIFIER, modifier)
            velocity = projectile.velocity.multiply(1 + (modifier * 0.2))
        }
    }

    private fun overchargeEnchantmentHit(projectile: Projectile): Float {
        val modifier = projectile.getIntTag(EntityTags.OVERCHARGE_MODIFIER) ?: return 0.0F
        return modifier * 0.3F
    }

    private fun perpetualProjectileEnchantmentShoot(projectile: Entity, level: Int) {
        projectile.run {
            addScoreboardTag(EntityTags.PERPETUAL_ARROW)
            setIntTag(EntityTags.PERPETUAL_MODIFIER, level)
            setGravity(false)
            isPersistent = false
        }
    }

    private fun rainOfArrowsEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.RAIN_OF_ARROWS_ARROW)
            setIntTag(EntityTags.RAIN_OF_ARROWS_MODIFIER, level)
        }
    }

    private fun rainOfArrowsEnchantmentHit(projectile: Projectile, victim: LivingEntity) {
        //if (!projectile.scoreboardTags.contains(EntityTags.ORIGINAL_ARROW)) return
        val modifier = projectile.getIntTag(EntityTags.RAIN_OF_ARROWS_MODIFIER) ?: return

        val arrowCount = modifier * 6
        val strikeLocation = victim.location.clone()

        // 1s delay before arrows launch
        Odyssey.instance.server.scheduler.runTaskLater(Odyssey.instance, Runnable {

            // Warning particles at ground target during the delay (spawned immediately, visible before arrows fall)
            strikeLocation.world?.spawnParticle(Particle.ELECTRIC_SPARK, strikeLocation, 30, 0.6, 0.1, 0.6, 0.05)

            for (x in 1..arrowCount) {
                // Spread arrows evenly in a radial pattern
                val angle = Math.PI * 2 * (x / (arrowCount * 1.0))
                val spreadRadius = 0.6 // Tight spread at launch so they fan out naturally
                val lateralX = spreadRadius * cos(angle)
                val lateralZ = spreadRadius * sin(angle)

                // Launch from slightly above victim location so arrows clear the target
                val spawnLocation = strikeLocation.clone().add(lateralX, 1.0, lateralZ)

                // Upward velocity with slight outward spread — goes up 6-10 blocks then arcs down
                val upwardVelocity = org.bukkit.util.Vector(
                    lateralX * 0.25,  // slight outward drift
                    2.8,              // ~8 block apex
                    lateralZ * 0.25
                )

                (projectile.world.spawnEntity(spawnLocation, projectile.type) as? Projectile)?.also {
                    it.velocity = upwardVelocity
                    it.shooter = projectile.shooter
                    it.cloneAndTag(projectile, listOf(EntityTags.RAIN_OF_ARROWS_ARROW))
                }
            }
        }, 20L) // 20 ticks = 1 second
    }

    private fun ricochetEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.RICOCHET_ARROW)
            setIntTag(EntityTags.RICOCHET_BOUNCE, 0)
            setIntTag(EntityTags.RICOCHET_MODIFIER, level)
        }
    }

    private fun ricochetEnchantmentEntityHit(projectile: Projectile): Float {
        val modifier = projectile.getIntTag(EntityTags.RICOCHET_BOUNCE) ?: return 0.0F
        return modifier * 0.25F
    }

    private fun ricochetEnchantmentBlockHit(projectile: Projectile, normalVector: Vector) {
        val bounce = projectile.getIntTag(EntityTags.RICOCHET_BOUNCE) ?: return
        val modifier = projectile.getIntTag(EntityTags.RICOCHET_MODIFIER) ?: return
        // Check if it can bounce more
        if (bounce < modifier) {
            projectile.setIntTag(EntityTags.RICOCHET_BOUNCE, bounce + 1)
        }
        else {
            return
        }
        // Spawn New Arrow
        (projectile.world.spawnEntity(projectile.location.clone(), projectile.type) as Projectile).also {
            it.cloneAndTag(projectile)
            it.setIntTag(EntityTags.RICOCHET_BOUNCE, bounce + 1)
            it.setIntTag(EntityTags.RICOCHET_MODIFIER, modifier)
            // Math
            val normal = normalVector.clone().normalize()
            val input = projectile.velocity.clone().normalize()
            it.velocity = input.subtract(normal.multiply(input.dot(normal) * 2.0)).normalize().multiply(max(projectile.velocity.length() - 0.3, 0.0))
        }
        projectile.remove()
    }

    private fun sharpshooterEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            if (velocity.length() <= 2.6) return
            addScoreboardTag(EntityTags.SHARPSHOOTER_ARROW)
            setIntTag(EntityTags.SHARPSHOOTER_MODIFIER, level)
            velocity = velocity.multiply(1 + (0.1 * level))
        }
    }

    private fun sharpshooterEnchantmentHit(projectile: Entity): Float {
        val modifier = projectile.getIntTag(EntityTags.SHARPSHOOTER_MODIFIER) ?: return 0.0F
        return modifier * 0.05F
    }

    private fun singleOutEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.SINGLE_OUT_ARROW)
            setIntTag(EntityTags.SINGLE_OUT_MODIFIER, level)
        }
    }

    private fun singleOutEnchantmentHit(projectile: Entity, victim: LivingEntity): Float {
        val modifier = projectile.getIntTag(EntityTags.SINGLE_OUT_MODIFIER) ?: return 0.0F
        val nearbyEntities = victim.location.world.getNearbyLivingEntities(victim.location, 12.0).filter { it != victim }
        val isIsolated = nearbyEntities.isEmpty()
        if (!isIsolated) return 0.0F
        return modifier * 0.2F
    }

    private fun singularityShotEnchantmentShoot(projectile: Entity, level: Int, shooter: LivingEntity) {
        with(projectile) {
            // Task
            addScoreboardTag(EffectTags.GRAVITY_WELLED)
            addScoreboardTag(EntityTags.MOVING_SINGULARITY)
            val modifier = (level * 1) + 1
            val maxCount = (level * 2) * 2
            val singularityShotTask = GravitySingularityTask(projectile, shooter, modifier, maxCount)
            singularityShotTask.runTaskTimer(Odyssey.instance, 0, 10)
            velocity.multiply(1.0 - (0.1 * modifier))
        }
    }

    private fun steadyAim(projectile: Entity, level: Int, shooter: LivingEntity) {
        with(projectile) {
            val eyeDirection = shooter.eyeLocation.direction.clone().multiply(level * 0.825)
            val speed = velocity.length()
            val aimVector = eyeDirection.add(velocity).normalize().multiply(speed)
            velocity = aimVector
        }
    }

    private fun rendEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.SOUL_REND_ARROW)
            setIntTag(EntityTags.SOUL_REND_MODIFIER, level)
        }
    }

    private fun soulRendEnchantmentHit(shooter: LivingEntity, projectile: Projectile, victim: LivingEntity) {
        val modifier = projectile.getIntTag(EntityTags.SOUL_REND_MODIFIER) ?: return
        victim.setIntTag(EntityTags.SOUL_RENDED_BY, shooter.entityId)
        victim.setIntTag(EntityTags.SOUL_REND_MODIFIER, modifier)
        victim.world.spawnParticle(Particle.SCULK_SOUL, victim.location, 5, 0.15, 0.15, 0.15)
    }

    private fun soulRendEnchantmentActivate(activator: LivingEntity) {
        activator.location.getNearbyLivingEntities(32.0).forEach {
            val isSoulRended = it.getIntTag(EntityTags.SOUL_RENDED_BY) == activator.entityId
            if (isSoulRended) {
                val modifier = it.getIntTag(EntityTags.SOUL_REND_MODIFIER)!!
                // Damage
                val soulRendDamage = it.arrowsInBody * (modifier * 1.0)
                it.damage(soulRendDamage, activator)
                it.arrowsInBody = 0
                it.removeTag(EntityTags.SOUL_RENDED_BY)
                it.removeTag(EntityTags.SOUL_REND_MODIFIER)
                it.world.spawnParticle(Particle.SOUL, it.location, 15, 0.05, 0.35, 0.05)
                it.world.spawnParticle(Particle.SCULK_SOUL, it.location, 15, 0.25, 0.35, 0.25)
            }
        }
    }

    private fun temporalTorrentEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            if (projectile !is Projectile) return
            addScoreboardTag(EntityTags.TEMPORAL_ARROW)
            setIntTag(EntityTags.TORRENT_MODIFIER, level)
            // Task
            val task = TemporalTorrentTask(level, projectile.velocity.clone(), projectile)
            projectile.velocity = projectile.velocity.clone().multiply(1.0 - (0.1 * level))
            task.runTaskTimer(Odyssey.instance, 0, 4) // Every 4 ticks / 0.2 secs
        }
    }

    private fun vulnerocityEnchantmentShoot(projectile: Entity, level: Int) {
        with(projectile) {
            addScoreboardTag(EntityTags.VULNEROCITY_ARROW)
            setIntTag(EntityTags.VULNEROCITY_MODIFIER, level)
        }
    }

    private fun vulnerocityEnchantmentHit(projectile: Entity, victim: LivingEntity) {
        val modifier = projectile.getIntTag(EntityTags.VULNEROCITY_MODIFIER) ?: return
        victim.noDamageTicks -= modifier * 2
    }

}
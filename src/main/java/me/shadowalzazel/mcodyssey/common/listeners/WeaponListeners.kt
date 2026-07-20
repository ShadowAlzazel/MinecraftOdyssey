package me.shadowalzazel.mcodyssey.common.listeners

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.tasks.weapon_tasks.*
import me.shadowalzazel.mcodyssey.common.combat.WeaponProjectileHandler
import me.shadowalzazel.mcodyssey.common.enchantments.EnchantmentManager
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.setIntTag
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps.MIN_RANGE_MAP
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps.SWEEP_MAP
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import java.util.*


object WeaponListeners : Listener, WeaponProjectileHandler, EnchantmentManager {

    private val markedVoidTargets = mutableMapOf<UUID, Entity>()
    private val currentGrappleShotTasks = mutableMapOf<UUID, GrapplingHookShot>()
    private val currentGrapplePullTasks = mutableMapOf<UUID, GrapplingHookPull>()

    // ──────────────────────────────────────────────────────────────────────────────
    // ──────────────────────────────── CONTEXTS ────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    private const val VANILLA_CRIT_MULTIPLIER = 1.5
    private const val DEFAULT_CRIT_BONUS = 0.5  //  vanilla 50%

    private class WeaponContext(
        val event: EntityDamageByEntityEvent,
        val player: Player,
        val victim: LivingEntity,
        val mainWeapon: ItemStack,
        val offHandWeapon: ItemStack,
        val isTwoHanding: Boolean, // empty off-hand
        val hasShield: Boolean,
        val isMounted: Boolean,
        val isSneaking: Boolean,
        val isCrit: Boolean,
        val fullAttack: Boolean,  // attackCooldown > 0.99
        val attackPower: Double,   // min(attackCooldown, 1.0) — swing charge
    ) {
        var critBonus: Double = DEFAULT_CRIT_BONUS   // weapons may override
    }

    private data class ThrowableSpec(
        val tag: String,                 // scoreboard tag stamped on the projectile
        val speed: Double,
        val cooldownTicks: Int,
        val gravity: Boolean = true,
        val dualThrowable: Boolean = false,         // also fire the off-hand copy
        val consume: (ItemStack, Player) -> Unit,   // durability vs stack
        val onSpawn: ((Snowball, ItemStack, Player) -> Unit)? = null,  // e.g. chakram return
    )

    private val THROWABLES: Map<String, ThrowableSpec> = mapOf(
        "kunai" to ThrowableSpec(
            tag = EntityTags.THROWN_KUNAI,
            speed = 1.6,
            cooldownTicks = (1 * 20) + 10,
            consume = { weapon, player -> weapon.damage(1, player) },
        ),
        "shuriken" to ThrowableSpec(
            tag = EntityTags.THROWN_SHURIKEN,
            speed = 2.6,
            cooldownTicks = 4,
            consume = { item, _ -> item.subtract(1) },
        ),
        "chakram" to ThrowableSpec(
            tag = EntityTags.THROWN_CHAKRAM,
            speed = 2.1,
            gravity = false,
            dualThrowable = true,
            cooldownTicks = 3 * 20,
            consume = { weapon, player -> weapon.damage(1, player) },
            onSpawn = { projectile, weapon, player ->
                ChakramReturn(player, projectile, weapon).runTaskLater(Odyssey.instance, 15)
                ProjectileDelete(projectile).runTaskLater(Odyssey.instance, 20 * 10)
            },
        ),
    )

    // ──────────────────────────────────────────────────────────────────────────────
    // ───────────────────────────────── EFFECTS ────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    /** Cone/circle AOE around the contact point. deg = arc width, radius optional. */
    private fun WeaponContext.sweep(
        deg: Double,
        multiplier: Double = 1.0,
        radius: Double? = null) {
        val rads = Math.toRadians(deg)
        val dmg = event.damage * multiplier
        if (radius != null) doWeaponAOESweep(player, victim, dmg, rads, radius)
        else doWeaponAOESweep(player, victim, dmg, rads)
    }

    // weaponType (ItemDataTags.TOOL_TYPE) -> bonus effect.
    // `it` is the WeaponContext: conditions + event.damage + settable critBonus.
    private val weaponEffects: Map<String, (WeaponContext) -> Unit> = mapOf(
        // --- circle sweepers (hit around the player) ---
        // "sickle"     to { it.sweep(deg = 100.0, radius = 2.0) },
        // "dagger"     to { it.sweep(deg = 80.0,  radius = 1.75) },
        "chakram"       to { it.sweep(deg = 175.0, radius = 1.5) },

        // --- cone sweepers ---
        "scythe"        to { it.sweep(deg = 120.0, radius = 3.0) },
        // "zweihander" to { it.sweep(deg = 120.0, radius = 2.0) },
        // "glaive"     to { it.sweep(deg = 65.0) },   // near contact
        // "poleaxe"    to { it.sweep(deg = 25.0) },   // near contact

        // --- conditional bonuses ---
        "katana"        to { it.critBonus = 0.6 },
        "longaxe"       to { it.critBonus = 0.75 },
    )

    private fun applyWeaponEffects(
        event: EntityDamageByEntityEvent,
        player: Player,
        victim: LivingEntity,
        mainWeapon: ItemStack,
        weaponType: String?,
    ) {
        val offHand = player.equipment.itemInOffHand
        val ctx = WeaponContext(
            event = event,
            player = player,
            victim = victim,
            mainWeapon = mainWeapon,
            offHandWeapon = offHand,
            isTwoHanding = offHand.type == Material.AIR,
            hasShield = offHand.type == Material.SHIELD,
            isMounted = player.vehicle?.passengers?.contains(player) == true,
            isSneaking = player.isSneaking,
            isCrit = event.isCritical,
            fullAttack = player.attackCooldown > 0.99,
            attackPower = player.attackCooldown.toDouble()
        )

        if (weaponType != null) applyDamageAttributes(ctx, weaponType)   // damage-type layer
        weaponEffects[weaponType]?.invoke(ctx)    // per-weapon bonus

        // Rebase vanilla 1.5x crit onto our desired multiplier (runs AFTER overrides)
        if (ctx.isCrit) {
            event.damage *= (1.0 + ctx.critBonus) / VANILLA_CRIT_MULTIPLIER
        }
    }

    /**
     * Damage-type attributes layered onto the weapon's base hit. Each is driven by
     * its own map keyed on weapon type; a missing entry means no effect.
     *
     *   Piercing    – converts up to `pierce` damage into TRUE damage, capped at the
     *                 target's armor. Effectively "ignores this much armor".
     *   Lacerating  – flat bonus blunted point-for-point by armor: max(lac - armor, 0).
     *                 Strong vs light targets, useless vs heavy.
     *   Bludgeoning – flat bonus that GROWS with armor: min(blunt, armor / 2).
     *                 Weak vs light targets, strong vs heavy.
     *
     * Lacerate + bludgeon feed back into event_damage so later crit math scales them.
     * Piercing is dealt straight to health and removed from event_damage so armor
     * can't soften it. All three scale with attack charge.
     */
    private fun applyDamageAttributes(ctx: WeaponContext, weaponType: String) {
        val victim = ctx.victim
        if (victim.isDead) return
        val armor = victim.getAttribute(Attribute.ARMOR)?.value ?: 0.0
        val power = ctx.attackPower

        // Piercing → true damage, bypasses armor
        val pierce = WeaponMaps.PIERCE_TRUE_DAMAGE_MAP[weaponType]?.let { minOf(armor, it) } ?: 0.0
        val trueDamage = pierce * power
        if (trueDamage > 0.0) {
            victim.health = maxOf(0.0, victim.health - trueDamage)
            ctx.event.damage -= trueDamage
        }

        // Lacerating + bludgeoning → normal bonus damage
        val lacerate = WeaponMaps.LACERATE_DAMAGE_MAP[weaponType]?.let { maxOf(it - armor, 0.0) } ?: 0.0
        val bludgeon = WeaponMaps.BLUNT_DAMAGE_MAP[weaponType]?.let { minOf(it, armor / 2.0) } ?: 0.0
        ctx.event.damage += power * (lacerate + bludgeon)

        // cleaving (WeaponMaps.CLEAVE_MAP) — computed in the old code but never applied; reserved.
    }

    private fun applyMinRangeFalloff(
        event: EntityDamageByEntityEvent,
        player: Player,
        victim: LivingEntity,
        weaponType: String?,
    ) {
        val minRange = MIN_RANGE_MAP[weaponType] ?: return
        val closest = minOf(
            player.eyeLocation.distance(victim.location),
            player.eyeLocation.distance(victim.eyeLocation),
        )
        if (closest < minRange) {
            event.damage *= closest / minRange   // == 1 - (minRange - closest) /minRange
        }
    }

    private fun throwWeapon(player: Player, weapon: ItemStack, spec: ThrowableSpec) {
        if (player.getCooldown(weapon.type) > 0) return

        val projectile = (player.world.spawnEntity(player.eyeLocation, EntityType.SNOWBALL) as Snowball).also {
            it.item = weapon
            it.setIntTag(EntityTags.THROWABLE_DAMAGE, getWeaponAttack(weapon).toInt())
            it.addScoreboardTag(spec.tag)
            it.velocity = player.eyeLocation.direction.clone().normalize().multiply(spec.speed)
            it.setGravity(spec.gravity)
            it.shooter = player
            it.setHasLeftShooter(false)
        }

        player.setCooldown(weapon.type, spec.cooldownTicks)
        spec.consume(weapon, player)
        spec.onSpawn?.invoke(projectile, weapon, player)
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // ──────────────────────────────── HANDLERS ────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    @EventHandler(priority = EventPriority.LOWEST)
    fun mainWeaponDamageHandler(event: EntityDamageByEntityEvent) {
        // Phase 0 — only real player melee hits on a living target
        val player = event.damager as? Player ?: return
        val victim = event.entity as? LivingEntity ?: return
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK &&
            event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return
        if (event.damage <= 0.0) return  // shielded / already-cancelled hit

        val mainWeapon = player.equipment.itemInMainHand
        val weaponType = mainWeapon.getStringTag(ItemDataTags.TOOL_TYPE)

        // Phase 1 — base normalisation (applies to AOE-spawned hits too)
        if (weaponType != null) {
            event.damage = maxOf(0.0, event.damage - 1.0)  // attribute +1 base bug
        }
        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            event.damage += SWEEP_MAP[weaponType] ?: 0.0
        }

        val wasThrowable = victim.removeScoreboardTag(EntityTags.THROWABLE_ATTACK_HIT)

        // Phase 2 — stop AOE-spawned hits from recursing into bonuses / range
        if (victim.removeScoreboardTag(EntityTags.HIT_BY_AOE_SWEEP)) return

        // Phase 3 — per-weapon bonus effects + crit
        applyWeaponEffects(event, player, victim, mainWeapon, weaponType)

        // Phase 4 — range falloff (direct hits only)
        if (!wasThrowable) applyMinRangeFalloff(event, player, victim, weaponType)

        event.damage = maxOf(0.0, event.damage)
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    fun mainWeaponInteractionHandler(event: PlayerInteractEvent) {
        if (event.action.isLeftClick) {
            leftClickHandler(event)
        }
        else if (event.action.isRightClick) {
            rightClickHandler(event)
        }
    }

    private fun rightClickHandler(event: PlayerInteractEvent) {
        val player = event.player
        val mainWeapon = player.equipment.itemInMainHand
        val offHand = player.equipment.itemInOffHand

        // Off-hand melee dual-wield
        val offHandType = offHand.getStringTag(ItemDataTags.TOOL_TYPE)
        if (offHandType in WeaponMaps.DUAL_WIELDABLE) {
            val target = getRayTraceTarget(player, offHandType!!)
            if (target is LivingEntity) dualWieldAttack(player, target) else player.swingOffHand()
        }

        // Main-hand throwable
        val mainType = mainWeapon.getStringTag(ItemDataTags.TOOL_TYPE)
        THROWABLES[mainType]?.let { spec ->
            throwWeapon(player, mainWeapon, spec)
            if (spec.dualThrowable && offHandType == mainType) {
                throwWeapon(player, offHand, spec)   // off-hand copy
            }
        }
    }

    private fun leftClickHandler(event: PlayerInteractEvent) {
        val player = event.player
        val mainWeapon = player.equipment.itemInMainHand
        // Sentries
        // DEPRECATED
    }


    // ──────────────────────────────────────────────────────────────────────────────
    // ────────────────────────────── WEAPON EFFECTS ────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    fun voidKunaiHandler(event: PlayerSwapHandItemsEvent) {
        if (!event.mainHandItem.hasItemMeta()) return
        val mainHand = event.mainHandItem
        if (!mainHand.itemMeta.hasCustomModelData()) return
        val player = event.player
        // Kunai
        if (mainHand.getItemNameFromData() == "void_linked_kunai") {
            val target = markedVoidTargets[player.uniqueId] ?: return
            // Create task to run AFTER event
            val voidLinkedChunk = target.chunk // First fire load
            voidLinkedChunk.load() // VERY TEMP!!!!!!!!
            val task = VoidLinkedKunaiAttack(player, mainHand, target)
            task.runTask(Odyssey.instance)
        }

    }

    /*-----------------------------------------------------------------------------------------------*/
    // Handler for projectile based throwable weapons
    @EventHandler(priority = EventPriority.LOWEST)
    fun mainWeaponProjectileHitHandler(event: ProjectileHitEvent) {
        val projectile: Projectile = event.entity
        if (event.hitEntity is LivingEntity) {
            if (projectile.scoreboardTags.isEmpty()) return
            val tags = projectile.scoreboardTags.toSet() // Prevent async problems
            for (tag in tags) {
                when (tag) {
                    EntityTags.THROWN_KUNAI -> {
                        kunaiHitEntityHandler(event)
                    }
                    EntityTags.THROWN_CHAKRAM -> {
                        chakramHitEntityHandler(event)
                    }
                    EntityTags.EXPLOSIVE_ARROW -> {
                        explosiveArrowHitHandler(event)
                    }
                    EntityTags.THROWN_SHURIKEN -> {
                        shurikenHitEntityHandler(event)
                    }
                }
            }
        }
        else if (event.hitBlock != null) {
            if (projectile.scoreboardTags.isEmpty()) return
            val tags = projectile.scoreboardTags.toSet() // Prevent async problems
            for (tag in tags) {
                when (tag) {
                    EntityTags.THROWN_CHAKRAM -> {
                        chakramHitBlockHandler(event)
                    }
                    EntityTags.EXPLOSIVE_ARROW -> {
                        explosiveArrowHitHandler(event)
                    }
                    EntityTags.GRAPPLE_HOOK -> {
                        grapplingHookHitHandler(event)
                    }
                }
            }
        }
    }

    // For thrown kunai hitting target
    private fun kunaiHitEntityHandler(event: ProjectileHitEvent) {
        val projectile: Projectile = event.entity
        if (projectile !is ThrowableProjectile) return
        val damage = projectile.getIntTag(EntityTags.THROWABLE_DAMAGE) ?: return
        val victim = event.hitEntity ?: return
        if (victim !is LivingEntity) return
        // Damage hit entity
        victim.addScoreboardTag(EntityTags.THROWABLE_ATTACK_HIT)
        if (projectile.shooter != null) {
            val thrower = projectile.shooter ?: return
            victim.addScoreboardTag(EntityTags.THROWABLE_ATTACK_HIT)
            // Match same weapon
            if (thrower is HumanEntity && thrower.equipment.itemInMainHand.getItemNameId() == projectile.item.getItemNameId()) {
                thrower.attack(victim)
                victim.damage(damage * 1.0, createEntityDamageSource(thrower, projectile, DamageType.PLAYER_ATTACK))

            } else {
                victim.damage(damage * 1.0, thrower as LivingEntity)
            }
        } else {
            victim.damage(damage * 1.0)
        }
    }

    // For thrown chakram hitting target
    private fun chakramHitEntityHandler(event: ProjectileHitEvent) {
        val projectile: Projectile = event.entity
        if (projectile !is ThrowableProjectile) return
        val damage = projectile.getIntTag(EntityTags.THROWABLE_DAMAGE) ?: return
        val target = event.hitEntity ?: return
        if (target !is LivingEntity) return
        val targetIsThrower = target == projectile.shooter
        var returnChakram = false
        // Damage if not owner
        if (projectile.shooter != null && !targetIsThrower) {
            val thrower = projectile.shooter ?: return
            if (thrower !is LivingEntity) return
            target.addScoreboardTag(EntityTags.THROWABLE_ATTACK_HIT)
            thrower.attack(target)
            target.damage(damage * 1.0, createEntityDamageSource(thrower, projectile, DamageType.PLAYER_ATTACK))
            returnChakram = true
        }
        // If hit owner, reset Cooldown
        else if (targetIsThrower) {
            if (target is HumanEntity) {
                target.setCooldown(projectile.item.type, 0)
            }
            event.isCancelled = true
            projectile.remove()
            return
        }
        else {
            target.addScoreboardTag(EntityTags.THROWABLE_ATTACK_HIT)
            target.damage(damage * 1.0)
        }
        // Respawn Chakram
        if (returnChakram) {
            respawnChakramProjectile(projectile, damage)
            /*
            event.isCancelled = true // EITHER DO THIS OR SPAWN NEW ONE???
            projectile.velocity = projectile.velocity.multiply(-1)
            projectile.setHasLeftShooter(false)
             */
        }
    }

    private fun shurikenHitEntityHandler(event: ProjectileHitEvent) {
        val projectile: Projectile = event.entity
        if (projectile !is ThrowableProjectile) return
        val damage = projectile.getIntTag(EntityTags.THROWABLE_DAMAGE) ?: return
        val victim = event.hitEntity ?: return
        if (victim !is LivingEntity) return
        // Damage hit entity
        victim.addScoreboardTag(EntityTags.THROWABLE_ATTACK_HIT)
        if (projectile.shooter != null) {
            val thrower = projectile.shooter ?: return
            victim.addScoreboardTag(EntityTags.THROWABLE_ATTACK_HIT)
            victim.damage(damage * 1.0, thrower as LivingEntity)
        } else {
            victim.damage(damage * 1.0)
        }
    }

    private fun chakramHitBlockHandler(event: ProjectileHitEvent) {
        val projectile: Projectile = event.entity
        if (projectile !is ThrowableProjectile) return
        if (projectile.shooter !is LivingEntity) return
        // Prevent multi bounce
        if (projectile.scoreboardTags.contains(EntityTags.CHAKRAM_HAS_BOUNCED)) return
        //val block = event.hitBlock ?: return
        // Check Damage
        val damage = projectile.getIntTag(EntityTags.THROWABLE_DAMAGE) ?: return
        // Respawn
        respawnChakramProjectile(projectile, damage)
    }

    private fun respawnChakramProjectile(projectile: ThrowableProjectile, damage: Int) {
        val thrower = projectile.shooter as LivingEntity
        val weapon = projectile.item
        (thrower.world.spawnEntity(projectile.location, EntityType.SNOWBALL) as Snowball).also {
            // Set Item
            it.item = weapon
            // Tags
            it.setIntTag(EntityTags.THROWABLE_DAMAGE, damage)
            it.addScoreboardTag(EntityTags.THROWN_CHAKRAM)
            it.addScoreboardTag(EntityTags.CHAKRAM_HAS_BOUNCED)
            // Velocity (MAYBE SPEED IS STORED IN COMPONENT??)
            it.velocity = projectile.velocity.multiply(-0.7)
            it.setGravity(false)
            it.shooter = thrower
            it.setHasLeftShooter(false)
            val deleteTask = ProjectileDelete(it)
            deleteTask.runTaskLater(Odyssey.instance, 20 * 10)
        }
    }



    /*-----------------------------------------------------------------------------------------------*/
    // Event for detecting when entity shoots bow

    @EventHandler(priority = EventPriority.LOWEST)
    fun bowShootHandler(event: EntityShootBowEvent) {
        val bow = event.bow ?: return
        // Projectile takes priority
        if (event.consumable != null) {
            val consumable = event.consumable!!
            if (consumable.getItemNameFromData() == "explosive_arrow") {
                explosiveArrowShootHandler(event)
            }
        }
        // Match Shoot
        when(val itemName = bow.getItemNameFromData()) {
            "crossbolter" -> crossbolterShooting(event)
            "auto_crossbow" -> autoCrossbowShooting(event)
            "warped_bow" -> return // Has +2 damage but -10% accuracy
            "tinkered_musket" -> tinkeredMusketShooting(event) // Shoot projectiles at 400% speed
            "tinkered_bow" -> return // Has 50% greater accuracy
            "alchemical_driver" -> alchemicalWeaponShooting(event, itemName) // Launches potions
            "alchemical_diffuser" -> alchemicalWeaponShooting(event, itemName) // Sprays a mist with the potion effect
            "alchemical_bolter" -> alchemicalWeaponShooting(event, itemName) // Shoots arrows of tipped with the effect
            "chain_hook" -> grapplingHookShooting(event)
        }
    }

    // Event for detecting when entity loads a crossbow
    @EventHandler
    fun crossbowLoadHandler(event: EntityLoadCrossbowEvent) {
        val crossbow = event.crossbow
        if (!crossbow.hasItemMeta()) return
        when(val itemName = crossbow.getItemNameFromData()) {
            "crossbolter" -> crossbolterLoading(event)
            "compact_crossbow" -> compactCrossbowLoading(event)
            "tinkered_musket" -> tinkeredMusketLoading(event) // Requires x2 reload (gunpowder -> iron both have to be in hand)
            "alchemical_driver" -> event.isCancelled = alchemicalWeaponLoading(event.entity, crossbow, itemName)
            "alchemical_diffuser" -> event.isCancelled = alchemicalWeaponLoading(event.entity, crossbow, itemName)
            "alchemical_bolter" -> event.isCancelled = alchemicalWeaponLoading(event.entity, crossbow, itemName)
        }
    }


    /*-----------------------------------------------------------------------------------------------*/
    // GRAPPLING HOOK (cross hook)
    private fun grapplingHookShooting(event: EntityShootBowEvent, power: Double = 0.2) {
        //println("CALLED SHOOT")
        if (event.entity.scoreboardTags.contains(EntityTags.HAS_SHOT_GRAPPLE)) {
            event.entity.removeScoreboardTag(EntityTags.HAS_SHOT_GRAPPLE)
            event.entity.removeScoreboardTag(EntityTags.IS_GRAPPLING)
            return
        }
        val grapplingHook = event.bow ?: return
        val projectile = event.projectile
        if (projectile !is Projectile) return
        // Maybe use same technique as Overcharge
        val hookerId = event.entity.uniqueId
        val task = GrapplingHookShot(event.entity, projectile, grapplingHook)
        if (currentGrappleShotTasks[hookerId] != null) {
            currentGrappleShotTasks[hookerId]?.cancel()
        }
        event.entity.addScoreboardTag(EntityTags.HAS_SHOT_GRAPPLE)
        projectile.addScoreboardTag(EntityTags.GRAPPLE_HOOK)
        projectile.velocity = projectile.velocity.multiply(2.0)
        currentGrappleShotTasks[hookerId] = task
        task.runTaskTimer(Odyssey.instance, 1, 1)
        //println("FINISHED SHOOT")
    }

    private fun grapplingHookHitHandler(event: ProjectileHitEvent) {
        //println("CALLED PULL")
        val projectile = event.entity
        val hooker = projectile.shooter
        if (hooker !is LivingEntity) return
        if (!hooker.scoreboardTags.contains(EntityTags.HAS_SHOT_GRAPPLE)) return
        // Remove Has Shot Grapple via TASK (WIP)
        val hookerId = hooker.uniqueId
        if (currentGrappleShotTasks[hookerId] == null) {
            return
        }
        val mainHand = hooker.equipment?.itemInMainHand ?: return
        if (currentGrappleShotTasks[hookerId]?.grapplingHook != mainHand) {
            return
        }
        val task = GrapplingHookPull(hooker, projectile, mainHand)
        if (currentGrapplePullTasks[hookerId] != null) {
            currentGrapplePullTasks[hookerId]?.cancel()
        }
        // Max distance for pull
        val maxPullDistance = 50 // Get From Tags later
        val distance = projectile.location.distance(hooker.location)
        if (distance > maxPullDistance) return

        hooker.addScoreboardTag(EntityTags.IS_GRAPPLING)
        currentGrapplePullTasks[hookerId] = task
        task.runTaskTimer(Odyssey.instance, 1, 1)
        //println("FINISHED PULL CALL")
    }


}
package me.shadowalzazel.mcodyssey.common.listeners

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.tasks.weapon_tasks.*
import me.shadowalzazel.mcodyssey.common.combat.WeaponCombatHandler
import me.shadowalzazel.mcodyssey.common.combat.WeaponProjectileHandler
import me.shadowalzazel.mcodyssey.common.enchantments.EnchantmentManager
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.setIntTag
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps.MIN_RANGE_MAP
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps.REACH_MAP
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps.SWEEP_MAP
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

// --------------------------------- NOTES --------------------------------
// Mounted Bonus i.e. Cavalry Charges
// Add Armor Stats through Lore
// Scepter -> checks if offhand is a tome of _enchantment_, does spell.
// Can craft weapons without recipe, but with recipe, it is greater quality
// DeprecatedWeapon that can ignore I-frames
// RUNE STONES vs ENCHANTMENTS
// Entities have custom dual and range wield mechanics!!!!!
// Make crit and still combos !!
// SWEEP PARTICLES!
// Rabbit Hide -> Sheath
// PARRY
// IF hand raised
// weapon can parry
// If attack right after parry
// DO damage,

// ZWEIHANDER
// left click stab
// right click short dash and AOE

// CAN THROW KUNAI/ SNOWBALLS THAT DO DMG
// If throw snowball
// if kunai
// add tag
// if tag is kunai, and hits do +5 damage

// MINATO enchant
// If throw kunai
// it has tag
// If surface normalized
// spawn armor stand with kunai
// can tp if throw another
// Offhand DeprecatedWeapon

@Suppress("UnstableApiUsage")
object WeaponListeners : Listener, WeaponCombatHandler, WeaponProjectileHandler, EnchantmentManager {

    private val markedVoidTargets = mutableMapOf<UUID, Entity>()
    private val currentGrappleShotTasks = mutableMapOf<UUID, GrapplingHookShot>()
    private val currentGrapplePullTasks = mutableMapOf<UUID, GrapplingHookPull>()

    @EventHandler(priority = EventPriority.LOWEST)
    fun mainWeaponDamageHandler(event: EntityDamageByEntityEvent) {
        // Check if event damager and damaged is living entity
        if (event.damager !is Player) return
        if (event.entity !is LivingEntity) return
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK &&
            event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return
        if (event.damage <= 0.0) return // Prevent going through shields
        val player = event.damager as Player
        val victim = event.entity as LivingEntity
        // Further checks
        val mainWeapon = player.equipment.itemInMainHand
        //val offHandWeapon = player.equipment.itemInOffHand
        // Get weapon type
        val mainWeaponType = mainWeapon.getStringTag(ItemDataTags.TOOL_TYPE)
        //val mainWeaponMaterial = mainWeapon.getStringTag(ItemDataTags.MATERIAL_TYPE)
        // Sweep damage should not? call other bonuses?
        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            event.damage += SWEEP_MAP[mainWeaponType] ?: 0.0
        }
        // For throwable weapon damage
        val wasThrowable = victim.scoreboardTags.contains(EntityTags.THROWABLE_ATTACK_HIT)
        if (wasThrowable) {
            victim.removeScoreboardTag(EntityTags.THROWABLE_ATTACK_HIT)
        }
        if (event.damage > 1.0 && !wasThrowable) { event.damage -= 1.0 } // Reduce Weapon Damage by 1 to match attribute display
        // Called Before
        weaponBonusAttributesHandler(event, mainWeaponType)
        // Prevent Recursive AOE calls
        if (victim.scoreboardTags.contains(EntityTags.HIT_BY_AOE_SWEEP)) {
            victim.scoreboardTags.remove(EntityTags.HIT_BY_AOE_SWEEP)
            return
        }
        weaponBonusEffectsHandler(event)
        // Reduce damage for min range / cancel for max range
        if (!wasThrowable) {
            val minRange = MIN_RANGE_MAP[mainWeaponType]
            if (minRange != null) {
                val closestDistance = minOf(player.eyeLocation.distance(victim.location), player.eyeLocation.distance(victim.eyeLocation))
                if (closestDistance < minRange) {
                    val rangePower = 1 - ((minRange - closestDistance) / minRange)
                    event.damage *= rangePower
                }
            }
        }
        // Make sure not to get negative event damage
        event.damage = maxOf(0.0, event.damage)
    }

    /*-----------------------------------------------------------------------------------------------*/

    @EventHandler(priority = EventPriority.HIGHEST)
    fun mainWeaponInteractionHandler(event: PlayerInteractEvent) {
        if (event.action.isLeftClick) {
            leftClickHandler(event)
        }
        else if (event.action.isRightClick) {
            rightClickHandler(event)
        }
    }

    private fun leftClickHandler(event: PlayerInteractEvent) {
        val player = event.player
        val mainWeapon = player.equipment.itemInMainHand
        // Sentries
        if (!mainWeapon.hasItemMeta()) return
        if (!mainWeapon.itemMeta!!.hasCustomModelData()) return
        val weaponType = mainWeapon.getStringTag(ItemDataTags.TOOL_TYPE) ?: return
        val reach = REACH_MAP[weaponType] ?: return
        if (reach < 4.0) return
        // Run

    }

    // For dual wielding weapons or special attacks
    private fun rightClickHandler(event: PlayerInteractEvent) {
        val player = event.player
        val mainWeapon = player.equipment.itemInMainHand
        val offHandWeapon = player.equipment.itemInOffHand
        // Offhand Dual Weldable
        when(val offWeaponType = offHandWeapon.getStringTag(ItemDataTags.TOOL_TYPE)) {
            "dagger", "sickle", "chakram", "cutlass" -> {
                val entity = getRayTraceTarget(player, offWeaponType)
                if (entity is LivingEntity) { // Attack with offhand
                    dualWieldAttack(player, entity)
                } else {
                    player.swingOffHand()
                }
            }
        }
        // Throwable
        when(mainWeapon.getStringTag(ItemDataTags.TOOL_TYPE)) {
            "kunai" -> {
                kunaiThrowableHandler(event)
            }
            "chakram" -> {
                chakramThrowableHandler(event)
            }
            "shuriken" -> {
                shurikenThrowableHandler(event)
            }
        }
    }


    /*-----------------------------------------------------------------------------------------------*/
    // Right Click Throwable

    private fun kunaiThrowableHandler(event: PlayerInteractEvent) {
        val player = event.player
        val weapon = player.equipment.itemInMainHand
        if (player.getCooldown(weapon.type) > 0) return
        // Spawn Kunai
        (player.world.spawnEntity(player.eyeLocation, EntityType.SNOWBALL) as Snowball).also {
            it.item = weapon
            val damage = getWeaponAttack(weapon)
            it.setIntTag(EntityTags.THROWABLE_DAMAGE, damage.toInt())
            it.addScoreboardTag(EntityTags.THROWN_KUNAI)
            // Velocity
            it.velocity = player.eyeLocation.direction.clone().normalize().multiply(1.6)
            it.shooter = player
            it.setHasLeftShooter(false)
        }
        player.setCooldown(weapon.type, (1 * 20) + 10)
        weapon.damage(1, player)
    }

    private fun chakramThrowableHandler(event: PlayerInteractEvent) {
        val player = event.player
        val mainWeapon = player.equipment.itemInMainHand // Is chakram
        chakramWeaponThrown(mainWeapon, player)
        // Check if dual wielding chakram
        val offhand = player.equipment.itemInOffHand
        if (offhand.getStringTag(ItemDataTags.TOOL_TYPE) == "chakram") {
            chakramWeaponThrown(offhand, player)
        }

    }

    private fun chakramWeaponThrown(weapon: ItemStack, player: Player) {
        if (player.getCooldown(weapon.type) > 0) return
        // Spawn Chakram
        val throwable = (player.world.spawnEntity(player.eyeLocation, EntityType.SNOWBALL) as Snowball).also {
            it.item = weapon
            val damage = getWeaponAttack(weapon)
            it.setIntTag(EntityTags.THROWABLE_DAMAGE, damage.toInt())
            it.addScoreboardTag(EntityTags.THROWN_CHAKRAM)
            // Velocity (MAYBE SPEED IS STORED IN COMPONENT??)
            it.velocity = player.eyeLocation.direction.clone().normalize().multiply(2.1)
            it.setGravity(false)
            it.shooter = player
            it.setHasLeftShooter(false)
        }
        player.setCooldown(weapon.type, 3 * 20)
        // Return Task
        val task = ChakramReturn(player, throwable, weapon)
        weapon.damage(1, player)
        val deleteTask = ProjectileDelete(throwable)
        task.runTaskLater(Odyssey.instance, 15)
        deleteTask.runTaskLater(Odyssey.instance, 20 * 10)
    }


    private fun shurikenThrowableHandler(event: PlayerInteractEvent) {
        val player = event.player
        val throwable = player.equipment.itemInMainHand
        if (player.getCooldown(throwable.type) > 0) return
        // Spawn Shuriken
        (player.world.spawnEntity(player.eyeLocation, EntityType.SNOWBALL) as Snowball).also {
            it.item = throwable
            val damage = getWeaponAttack(throwable)
            it.setIntTag(EntityTags.THROWABLE_DAMAGE, damage.toInt())
            it.addScoreboardTag(EntityTags.THROWN_SHURIKEN)
            // Velocity
            it.velocity = player.eyeLocation.direction.clone().normalize().multiply(2.6)
            it.shooter = player
            it.setHasLeftShooter(false)
        }
        player.setCooldown(throwable.type, 4)
        throwable.subtract(1)
    }

    /*-----------------------------------------------------------------------------------------------*/
    @EventHandler(priority = EventPriority.LOWEST)
    fun voidKunaiHandler(event: PlayerSwapHandItemsEvent) {
        if (!event.mainHandItem.hasItemMeta()) return
        val mainHand = event.mainHandItem
        if (!mainHand.itemMeta.hasCustomModelData()) return
        val player = event.player
        // Kunai
        if (mainHand.getItemIdentifier() == "void_linked_kunai") {
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
    @Suppress("UnstableApiUsage")
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
            if (thrower is HumanEntity && thrower.equipment.itemInMainHand.getItemKeyTag() == projectile.item.getItemKeyTag()) {
                //thrower.attack(victim)
                victim.damage(damage * 1.0, createPlayerDamageSource(thrower))

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
            target.damage(damage * 1.0, createPlayerDamageSource(thrower))
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

    /*-----------------------------------------------------------------------------------------------*/
    // Event for detecting when entity shoots bow

    @EventHandler(priority = EventPriority.LOWEST)
    fun bowShootHandler(event: EntityShootBowEvent) {
        val bow = event.bow ?: return
        // Projectile takes priority
        if (event.consumable != null) {
            val consumable = event.consumable!!
            if (consumable.getItemIdentifier() == "explosive_arrow") {
                explosiveArrowShootHandler(event)
            }
        }
        // Match Shoot
        when(val itemName = bow.getItemIdentifier()) {
            "auto_crossbow" -> autoCrossbowShooting(event)
            "warped_bow" -> return // Has +2 damage but -10% accuracy
            "tinkered_musket" -> return // Shoot projectiles at 400% speed
            "tinkered_bow" -> return // Has 50% greater accuracy
            "alchemical_driver" -> alchemicalWeaponShooting(event, itemName) // Launches potions
            "alchemical_diffuser" -> alchemicalWeaponShooting(event, itemName) // Sprays a mist with the potion effect
            "alchemical_bolter" -> alchemicalWeaponShooting(event, itemName) // Shoots arrows of tipped with the effect
            "grappling_hook_mk1" -> grapplingHookShooting(event)
        }
    }

    // Event for detecting when entity loads a crossbow
    @EventHandler
    fun crossbowLoadHandler(event: EntityLoadCrossbowEvent) {
        val crossbow = event.crossbow
        if (!crossbow.hasItemMeta()) return
        when(val itemName = crossbow.getItemIdentifier()) {
            "compact_crossbow" -> compactCrossbowLoading(event)
            "tinkered_musket" -> return // Requires x2 reload (gunpowder -> iron both have to be in hand)
            "alchemical_driver" -> event.isCancelled = alchemicalWeaponLoading(event.entity, crossbow, itemName)
            "alchemical_diffuser" -> event.isCancelled = alchemicalWeaponLoading(event.entity, crossbow, itemName)
            "alchemical_bolter" -> event.isCancelled = alchemicalWeaponLoading(event.entity, crossbow, itemName)
        }
    }


    /*-----------------------------------------------------------------------------------------------*/
    // GRAPPLING HOOK (cross hook)
    private fun grapplingHookShooting(event: EntityShootBowEvent, power: Double = 0.2) {
        println("CALLED SHOOT")
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
        println("FINISHED SHOOT")
    }

    private fun grapplingHookHitHandler(event: ProjectileHitEvent) {
        println("CALLED PULL")
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
        println("FINISHED PULL CALL")
    }


}
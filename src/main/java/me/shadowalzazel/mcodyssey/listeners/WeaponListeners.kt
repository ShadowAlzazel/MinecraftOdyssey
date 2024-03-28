package me.shadowalzazel.mcodyssey.listeners

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.alchemy.utility.ThickPotion
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.EntityTags.setIntTag
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.addTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.getUUIDTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasOdysseyItemTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.removeTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.setIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.setUUIDTag
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.BLUDGEON_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.CLEAVE_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.LACERATE_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.MAX_RANGE_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.MIN_RANGE_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.PIERCE_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.REACH_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.SWEEP_MAP
import me.shadowalzazel.mcodyssey.tasks.weapon_tasks.*
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.enchantments.Enchantment
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
import org.bukkit.inventory.meta.CrossbowMeta
import org.bukkit.inventory.meta.PotionMeta
import java.util.*
import kotlin.math.pow

// --------------------------------- NOTES --------------------------------
// TODO: Mounted Bonus i.e. Cavalry Charges
// TODO: Add Armor Stats through Lore
// TODO: Scepter -> checks if offhand is a tome of _enchantment_, does spell.
// Can craft weapons without recipe, but with recipe, it is greater quality
// DeprecatedWeapon that can ignore I-frames
// RUNE STONES vs ENCHANTMENTS
// Entities have custom dual and range wield mechanics!!!!!
// Make crit and still combos !!
// SWEEP PARTICLES!
// Rabbit Hide -> Sheath
// PARRY
// IF hand raised
// IF weapon can parry
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

object WeaponListeners : Listener {

    private val markedVoidTargets = mutableMapOf<UUID, Entity>()
    private val currentGrappleShotTasks = mutableMapOf<UUID, GrapplingHookShot>()
    private val currentGrapplePullTasks = mutableMapOf<UUID, GrapplingHookPull>()

    @EventHandler(priority = EventPriority.LOWEST)
    fun mainWeaponDamageHandler(event: EntityDamageByEntityEvent) {
        // Check if event damager and damaged is living entity
        if (event.damager !is Player) return
        if (event.entity !is LivingEntity) return
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return
        val player = event.damager as Player
        val victim = event.entity as LivingEntity

        // Prevent Recursive
        if (victim.scoreboardTags.contains(EntityTags.MELEE_AOE_HIT)) {
            victim.scoreboardTags.remove(EntityTags.MELEE_AOE_HIT)
            return
        }
        if (!player.equipment.itemInMainHand.hasItemMeta()) return
        if (!player.equipment.itemInMainHand.itemMeta!!.hasCustomModelData()) return
        val mainWeapon = player.equipment.itemInMainHand
        val offHandWeapon = player.equipment.itemInOffHand
        val model = mainWeapon.itemMeta.customModelData
        if (event.damage > 1.0) { event.damage -= 1.0 } // Reduce Weapon Damage by 1 to match attribute display

        // Min and Max Range
        val wasThrowable = victim.scoreboardTags.contains(EntityTags.THROWABLE_ATTACK_HIT)
        if (wasThrowable) {
            victim.removeScoreboardTag(EntityTags.THROWABLE_ATTACK_HIT)
        }
        else { // If not throwable check range
            if (MAX_RANGE_MAP.containsKey(model)) {
                val r = MAX_RANGE_MAP[model]!!
                if (victim !in player.getNearbyEntities(r, r, r)) {
                    event.isCancelled = true
                    return
                }
            }
            if (MIN_RANGE_MAP.containsKey(model)) {
                val r = MIN_RANGE_MAP[model]!!
                if (victim in player.getNearbyEntities(r, r, r)) {
                    event.isCancelled = true
                    return
                }
            }
        }

        // Conditions
        val twoHanded = offHandWeapon.type == Material.AIR
        val shieldInOff = offHandWeapon.type == Material.SHIELD
        val isMounted = (player.vehicle != null) && (player in player.vehicle!!.passengers)
        val isCrit = event.isCritical
        val fullAttack = player.attackCooldown > 0.99

        when (model) {
            ItemModels.SICKLE, ItemModels.SOUL_STEEL_SICKLE -> {
                if (offHandWeapon.itemMeta?.hasCustomModelData() == true && offHandWeapon.itemMeta.customModelData == ItemModels.SICKLE) {
                    victim.shieldBlockingDelay = 20
                }
            }
            ItemModels.KATANA, ItemModels.SOUL_STEEL_KATANA -> {
                val sayaInOff = offHandWeapon.type == Material.RABBIT_HIDE
                if (isCrit && (twoHanded || sayaInOff)) {
                    event.damage += 3
                    weaponSweep(victim, player, SWEEP_MAP[model]!!, event.damage)
                } else if (!twoHanded && !sayaInOff) {
                    val minimumDamage = minOf(event.damage, 3.0)
                    event.damage -= minimumDamage
                }
            }
            ItemModels.CLAYMORE, ItemModels.SOUL_STEEL_CLAYMORE -> {
                if (!twoHanded) {
                    val minimumDamage = minOf(event.damage, 6.0)
                    event.damage -= minimumDamage
                } else if (twoHanded) {
                    val sweepDamage = if (isCrit) {
                        event.damage
                    } else {
                        event.damage - 3.0
                    }
                    weaponSweep(victim, player, SWEEP_MAP[model]!!, sweepDamage)
                }

            }
            ItemModels.SABER, ItemModels.SOUL_STEEL_SABER -> {
                if (isMounted && isCrit) {
                    event.damage += 3
                }
            }
            ItemModels.CHAKRAM, ItemModels.SOUL_STEEL_CHAKRAM -> {
                val sweepDamage = if (isCrit) { event.damage + 1.0 } else { maxOf(event.damage, 2.0) }
                weaponSweep(victim, player, SWEEP_MAP[model]!!, sweepDamage)
            }
            ItemModels.HALBERD, ItemModels.SOUL_STEEL_HALBERD -> {
                if (!twoHanded && !shieldInOff) {
                    val minimumDamage = minOf(event.damage, 6.0)
                    event.damage -= minimumDamage
                }
            }
            ItemModels.SCYTHE, ItemModels.SOUL_STEEL_SCYTHE -> {
                if (fullAttack && twoHanded) {
                    weaponSweep(victim, player, SWEEP_MAP[model]!!, maxOf(0.0, event.damage - 4.0))
                }
            }
            ItemModels.LANCE, ItemModels.SOUL_STEEL_LANCE -> {
                if (isMounted && fullAttack) {
                    event.damage += 14.0
                }
            }
            ItemModels.WARHAMMER, ItemModels.SOUL_STEEL_WARHAMMER -> {
                if (twoHanded) {
                    victim.shieldBlockingDelay = 60
                }
            }
            ItemModels.LONG_AXE, ItemModels.SOUL_STEEL_LONG_AXE -> {
                if (twoHanded && isCrit) {
                    event.damage += (event.damage / 1.5) * 0.75 // 50% -> 75% Crit
                } else if (!twoHanded) {
                    event.damage = maxOf(event.damage * 0.5, 0.0)
                }
            }
            ItemModels.BAMBOO_STAFF, ItemModels.WOODEN_STAFF, ItemModels.BONE_STAFF, ItemModels.BLAZE_ROD_STAFF -> {
                if (isCrit) {
                    weaponSweep(victim, player, SWEEP_MAP[model]!!, event.damage + 2)
                } else {
                    weaponSweep(victim, player, SWEEP_MAP[model]!!, maxOf(event.damage - 1, 1.0))
                }

            }
            ItemModels.VOID_LINKED_KUNAI -> {
                markedVoidTargets[player.uniqueId] = victim // Right now saves to player, change to save per item
                println("Player ${player.uniqueId} Marked ${victim.uniqueId} ")
            }
        }
        // -----------------------------------------------------------------------------

        val attackPower = player.attackCooldown
        val extraDamages = weaponStatsHandler(model, victim, event.damage)
        val physicalDamage = extraDamages.first * attackPower
        val trueDamage = if (fullAttack) {
            maxOf(minOf(extraDamages.second, event.damage), 0.0)
        } else {
            0.0
        }
        event.damage -= trueDamage
        event.damage += physicalDamage

        if (victim.health < trueDamage) {
            victim.health = 0.0
        } else {
            victim.health -= trueDamage
        }
        event.damage = maxOf(0.0, event.damage)
        //println("Cooldown Charge: " + player.attackCooldown)
        //println("Cooldown Period: " + player.cooldownPeriod)
        //println("Final Damage: " + event.finalDamage)
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
            if (thrower is HumanEntity) {
                thrower.attack(victim)
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
            returnChakram = true
        }
        // If hit owner, reset Cooldown
        else if (targetIsThrower) {
            if (target is HumanEntity) {
                target.setCooldown(projectile.item.type, 0)
            }
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
        val block = event.hitBlock ?: return
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
        }
    }


    /*-----------------------------------------------------------------------------------------------*/
    @EventHandler(priority = EventPriority.LOWEST)
    fun weaponHandSwapHandler(event: PlayerSwapHandItemsEvent) {
        if (event.mainHandItem == null) return
        if (!event.mainHandItem!!.hasItemMeta()) return
        val mainHand = event.mainHandItem!! // ITEM TO BE IN MAINHAND AFTER SWAP
        val player = event.player

        // Kunai MOVE TO FUN LATER
        if (mainHand.itemMeta.customModelData == ItemModels.VOID_LINKED_KUNAI) {
            println("Void Swap Target: ${markedVoidTargets[player.uniqueId]?.uniqueId}")
            val target = markedVoidTargets[player.uniqueId] ?: return
            // Create task to run AFTER event
            val voidLinkedChunk = target.chunk // First fire load
            voidLinkedChunk.load() // VERY TEMP!!!!!!!!
            val task = VoidLinkedKunaiAttack(player, mainHand, target)
            task.runTask(Odyssey.instance)
        }

    }

    private fun leftClickHandler(event: PlayerInteractEvent) {
        val player = event.player
        val mainWeapon = player.equipment.itemInMainHand
        // Sentries
        if (!mainWeapon.hasItemMeta()) return
        if (!mainWeapon.itemMeta!!.hasCustomModelData()) return
        val model = mainWeapon.itemMeta!!.customModelData
        if (REACH_MAP[model] == null) return

        //val entity = getReachedTarget(player, REACH_MAP[model])
        val entity = getRayTraceTarget(player, model)
        if (entity is LivingEntity) {
            player.attack(entity)
        }
    }


    private fun rightClickHandler(event: PlayerInteractEvent) {
        val player = event.player
        val mainWeapon = player.equipment.itemInMainHand
        val offHandWeapon = player.equipment.itemInOffHand

        // Off Hand Dual Weldable
        val fullAttack = player.attackCooldown > 0.5
        if (offHandWeapon.itemMeta?.hasCustomModelData() == true && fullAttack) {
            when(val model = offHandWeapon.itemMeta!!.customModelData) {
                ItemModels.DAGGER, ItemModels.SICKLE, ItemModels.CHAKRAM, ItemModels.CUTLASS -> {
                    val entity = getRayTraceTarget(player, model)
                    if (entity is LivingEntity) {
                        dualWieldHandler(player, entity)
                    }
                    else {
                        player.swingOffHand()
                    }
                }
            }
        }
        // Throwable
        if (mainWeapon.itemMeta?.hasCustomModelData() == true) {
            if (!mainWeapon.hasOdysseyItemTag()) return
            val model = mainWeapon.itemMeta.customModelData
            // Get from components or Model
            val isKunai = (model == ItemModels.KUNAI || model == ItemModels.VOID_LINKED_KUNAI) || (mainWeapon.hasTag(ItemTags.IS_KUNAI))
            if (isKunai) {
                kunaiThrowableHandler(event)
                return
            }
            val isChakram = (model == ItemModels.CHAKRAM) || (mainWeapon.hasTag(ItemTags.IS_CHAKRAM))
            if (isChakram) {
                chakramThrowableHandler(event)
                return
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Right Click Throwables

    private fun kunaiThrowableHandler(event: PlayerInteractEvent) {
        val player = event.player
        val mainWeapon = player.equipment.itemInMainHand
        // Get CD
        if (player.getCooldown(mainWeapon.type) > 0) return
        // Spawn Kunai
        (player.world.spawnEntity(player.eyeLocation, EntityType.SNOWBALL) as Snowball).also {
            // Set Item
            it.item = mainWeapon
            // Tags
            var damage = 1.0
            val attackModifiers = mainWeapon.itemMeta.attributeModifiers?.get(Attribute.GENERIC_ATTACK_DAMAGE)
            if (attackModifiers != null) {
                for (modifier in attackModifiers) { // FIX LATER
                    damage += modifier.amount
                }
            }
            it.setIntTag(EntityTags.THROWABLE_DAMAGE, damage.toInt())
            it.addScoreboardTag(EntityTags.THROWN_KUNAI)
            // Velocity
            it.velocity = player.eyeLocation.direction.clone().normalize().multiply(1.6)
            it.shooter = player
            it.setHasLeftShooter(false)
        }
        player.setCooldown(mainWeapon.type, 1 * 20)
        mainWeapon.damage(1, player)
    }

    private fun chakramThrowableHandler(event: PlayerInteractEvent) {
        val player = event.player
        val mainWeapon = player.equipment.itemInMainHand
        // Get CD
        if (player.getCooldown(mainWeapon.type) > 0) return
        // Spawn Chakram
        val throwable = (player.world.spawnEntity(player.eyeLocation, EntityType.SNOWBALL) as Snowball).also {
            // Set Item
            it.item = mainWeapon
            // Tags
            var damage = 1.0
            val attackModifiers = mainWeapon.itemMeta.attributeModifiers?.get(Attribute.GENERIC_ATTACK_DAMAGE)
            if (attackModifiers != null) {
                for (modifier in attackModifiers) { // FIX LATER
                    damage += modifier.amount
                }
            }
            it.setIntTag(EntityTags.THROWABLE_DAMAGE, damage.toInt())
            it.addScoreboardTag(EntityTags.THROWN_CHAKRAM)
            // Velocity (MAYBE SPEED IS STORED IN COMPONENT??)
            it.velocity = player.eyeLocation.direction.clone().normalize().multiply(2.1)
            it.setGravity(false)
            it.shooter = player
            it.setHasLeftShooter(false)
        }
        player.setCooldown(mainWeapon.type, 6 * 20)
        // Return Task
        val task = ChakramReturn(player, throwable, mainWeapon)
        task.runTaskLater(Odyssey.instance, 15)
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun getRayTraceTarget(player: Player, model: Int): Entity? {
        val reach = REACH_MAP[model] ?: return null
        val result = player.rayTraceEntities(reach.toInt()) ?: return null
        val target = result.hitEntity ?: return null
        val distance = player.eyeLocation.distance(target.location)
        if (reach < distance) { return null }
        return target
    }

    // TODO: If Crouching more damage
    // Function for critical hits that sweep
    private fun weaponSweep(victim: LivingEntity, attacker: LivingEntity, radius: Double, damage: Double) {
        val midpoint = attacker.location.clone().set(
            ((victim.location.x + attacker.location.x) / 2),
            ((victim.location.y + attacker.location.y) / 2),
            ((victim.location.z + attacker.location.z) / 2)
        )

        victim.scoreboardTags.add(EntityTags.MELEE_AOE_HIT)
        val enemyList = midpoint.getNearbyEntities(radius, radius, radius).filter {
            it != victim && it != attacker
        }
        for (entity in enemyList) {
            if (entity is LivingEntity && !entity.scoreboardTags.contains(EntityTags.MELEE_AOE_HIT)) {
                entity.scoreboardTags.add(EntityTags.MELEE_AOE_HIT)
                entity.damage(damage, attacker)
                entity.world.spawnParticle(
                    Particle.SWEEP_ATTACK,
                    entity.location.clone().add(0.0, 1.75, 0.0),
                    1,
                    0.05,
                    0.03,
                    0.05
                )
            }
        }
        attacker.world.playSound(victim.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 2.75F, 0.5F)
    }

    private fun dualWieldHandler(player: Player, enemy: LivingEntity) {
        with(player.equipment) {
            val mainHand = itemInMainHand.clone()
            val offHand = itemInOffHand.clone()
            setItemInOffHand(mainHand)
            setItemInMainHand(offHand)
            player.attack(enemy)
            setItemInMainHand(mainHand)
            setItemInOffHand(offHand)
        }
        player.resetCooldown()
        player.swingOffHand()
    }

    // Stat handler
    private fun weaponStatsHandler(model: Int, victim: LivingEntity, damage: Double): Pair<Double, Double> {
        if (victim.isDead) { return Pair(0.0, 0.0) }
        if (victim.getAttribute(Attribute.GENERIC_ARMOR)?.value == null) { return Pair(0.0, 0.0) }

        val armor = victim.getAttribute(Attribute.GENERIC_ARMOR)!!.value
        val cleavingDamage = CLEAVE_MAP[model] ?: 0.0
        val bludgeoningDamage = BLUDGEON_MAP[model]?.let { minOf(it, armor)} ?: 0.0
        val laceratingDamage = LACERATE_MAP[model]?.let { if (armor <= 1.5) { it } else { 0.0 } } ?: 0.0
        //val laceratingDamage = if (armor <= 1.5) { LACERATE_MAP[model] } else { 0.0 } ?: 0.0
        val piercingDamage = PIERCE_MAP[model]?.let { minOf(armor, it) } ?: 0.0

        val bonusDamage = if (damage >= bludgeoningDamage + laceratingDamage) {
            bludgeoningDamage + laceratingDamage
        } else {
            0.0
        }

       return Pair(bonusDamage, piercingDamage)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Event for detecting when entity shoots bow

    @EventHandler(priority = EventPriority.LOWEST)
    fun bowShootHandler(event: EntityShootBowEvent) {
        val bow = event.bow ?: return
        // Projectile takes priority
        if (event.consumable != null) {
            val consumable = event.consumable!!
            // CHANE TO COMPONENT TAG SEARCH
            if (consumable.hasItemMeta() && consumable.itemMeta.hasCustomModelData()) {
                if (consumable.itemMeta.customModelData == ItemModels.EXPLOSIVE_ARROW) {
                    explosiveArrowShootHandler(event)
                }
            }
        }

        // Match
        if (bow.hasItemMeta() && bow.itemMeta.hasCustomModelData()) {
            when(bow.itemMeta.customModelData) {
                ItemModels.AUTO_CROSSBOW -> {
                    autoCrossbowHandler(event)
                }
                ItemModels.ALCHEMICAL_BOLTER -> {
                    alchemicalBolterShootHandler(event)
                }
                ItemModels.GRAPPLING_HOOK -> {
                    grapplingHookShootHandler(event)
                }
            }
        }
        /*
        println("------------------------")
        println("Crossbow: ${event.bow}.")
        println("Arrow: ${event.projectile}.")
        println("Consumes: ${event.shouldConsumeItem()}")
         */
    }

    // Event for detecting when entity loads a crossbow
    @EventHandler
    fun crossbowLoadHandler(event: EntityLoadCrossbowEvent) {
        // Checks
        val crossbow = event.crossbow ?: return
        if (!crossbow.hasItemMeta()) return
        if (!crossbow.itemMeta.hasCustomModelData()) return

        when(crossbow.itemMeta.customModelData) {
            ItemModels.COMPACT_CROSSBOW -> {
                compactCrossbowHandler(event)
            }
            ItemModels.ALCHEMICAL_BOLTER -> {
                event.isCancelled = alchemicalBolterLoadingHandler(event.entity, crossbow)
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // EXPLOSIVE ARROW
    private fun explosiveArrowShootHandler(event: EntityShootBowEvent) {
        event.projectile.also {
            it.addScoreboardTag(EntityTags.EXPLOSIVE_ARROW)
        }
    }

    @Suppress("UnstableApiUsage")
    private fun explosiveArrowHitHandler(event: ProjectileHitEvent) {
        val projectile = event.entity
        val location = projectile.location
        location.world.spawnParticle(Particle.EXPLOSION_LARGE, location, 1, 0.0, 0.04, 0.0)
        location.world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.8F, 1.2F)
        for (entity in location.getNearbyLivingEntities(2.5)) {
            // indirect distance square
            val distance = entity.location.distance(location)
            val power = (maxOf(2.5 - distance, 0.0)).pow(2.0) + (maxOf(2.5 - distance, 0.0)).times(1) + 1.25
            val damageSource = DamageSource.builder(DamageType.EXPLOSION).build()
            entity.damage(power, damageSource) // Create Damage Source
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // GRAPPLING HOOK (cross hook)
    private fun grapplingHookShootHandler(event: EntityShootBowEvent) {
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
        hooker.addScoreboardTag(EntityTags.IS_GRAPPLING)
        currentGrapplePullTasks[hookerId] = task
        task.runTaskTimer(Odyssey.instance, 1, 1)
        println("FINISHED PULL")
    }

    /*-----------------------------------------------------------------------------------------------*/
    // AUTO CROSSBOW

    private fun autoCrossbowHandler(event: EntityShootBowEvent) {
        val crossbow = event.bow ?: return
        // Offhand
        val offhand = event.entity.equipment?.itemInOffHand ?: return
        //if (!offhand.hasItemMeta()) return
        // Auto Crossbow
        if (crossbow.itemMeta is CrossbowMeta) {
            // Check if match
            val crossbowMeta = crossbow.itemMeta as CrossbowMeta
            if (crossbowMeta.chargedProjectiles.isEmpty()) return
            val projectileMeta = crossbowMeta.chargedProjectiles[0].itemMeta
            val loadedItems = mutableListOf<ItemStack>()
            for (item in crossbowMeta.chargedProjectiles) {
                loadedItems.add(item.clone())
            }
            // Matches
            val isArrow = offhand.type == crossbowMeta.chargedProjectiles[0].type && projectileMeta !is PotionMeta && offhand.type != Material.FIREWORK_ROCKET
            if (offhand.itemMeta == projectileMeta || isArrow) {
                if (!crossbow.hasTag(ItemTags.AUTO_LOADER_LOADING)) {
                    offhand.subtract(1)
                    val task = LoadAutoCrossbow(event.entity, crossbow, loadedItems)
                    task.runTaskLater(Odyssey.instance, 1)
                }
                crossbow.addTag(ItemTags.AUTO_LOADER_LOADING)
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // ALCHEMICAL BOLTER

    private var LOADED_ALCHEMICAL_AMMO = mutableMapOf<UUID, ItemStack?>() // CURRENTLY DOES NOT SAVE AFTER SERVER SHUTDOWN

    private fun alchemicalBolterLoadingHandler(loader: LivingEntity, crossbow: ItemStack): Boolean {
        // CHANGE TO GET HAND
        val offhandIsPotion = loader.equipment!!.itemInOffHand.type in listOf(
            Material.SPLASH_POTION, Material.LINGERING_POTION)
        val offhandIsAmmo = loader.equipment!!.itemInOffHand == ThickPotion.createThickPotion() // change to component comparison
        if (!offhandIsPotion && !offhandIsAmmo) {
            return false
        }

        val potionOffHand = loader.equipment!!.itemInOffHand
        return if (!crossbow.hasTag(ItemTags.ALCHEMY_ARTILLERY_LOADED)) {
            val newUUID = UUID.randomUUID()
            val oldUUID =  UUID.fromString(crossbow.getUUIDTag())
            crossbow.addTag(ItemTags.ALCHEMY_ARTILLERY_LOADED)
            crossbow.setUUIDTag(newUUID)
            // Load Item to UUID
            // STORE IN COMPONENTS FOR 1.20.5 !!
            val oldPotion = LOADED_ALCHEMICAL_AMMO[oldUUID]
            if (offhandIsAmmo && oldPotion != null) {
                LOADED_ALCHEMICAL_AMMO[newUUID] = oldPotion.clone()
                LOADED_ALCHEMICAL_AMMO[oldUUID] = null
                crossbow.addTag(ItemTags.ALCHEMY_COPY_STORED)
            } else {
                LOADED_ALCHEMICAL_AMMO[newUUID] = potionOffHand.clone()
            }

            // Load counter into tag/component
            val multiCounter = if (crossbow.itemMeta.hasEnchant(Enchantment.MULTISHOT)) 3 else 1
            crossbow.setIntTag(ItemTags.ALCHEMICAL_AMMO_COUNT, multiCounter)
            loader.equipment!!.itemInOffHand.subtract()
            // Maybe need to load twice?? 1 the potion, then the ammo?
            false
        } else {
            true
        }
    }

    private fun alchemicalBolterShootHandler(event: EntityShootBowEvent) {
        // Sentries
        val crossbow = event.bow ?: return
        if (crossbow.type != Material.CROSSBOW) return
        if (!crossbow.hasTag(ItemTags.ALCHEMY_ARTILLERY_LOADED)) return
        // Potions
        val shooter = event.entity
        val projectile = event.projectile
        var lastShot = false
        var thrownPotion: ThrownPotion? = null
        val bowUUID = UUID.fromString(crossbow.getUUIDTag())
        val count = crossbow.getIntTag(ItemTags.ALCHEMICAL_AMMO_COUNT) ?: 1
        if (count >= 1) {
            // Change to store in component projectiles 1.20.5
            // Spawn potion with item
            thrownPotion = (shooter.world.spawnEntity(projectile.location, EntityType.SPLASH_POTION) as ThrownPotion).also {
                it.item = LOADED_ALCHEMICAL_AMMO[bowUUID] ?: ItemStack(Material.SPLASH_POTION, 1)
                it.velocity = projectile.velocity.clone().multiply(0.6)
                it.shooter = shooter
            }
            // Multishot compatibility
            crossbow.setIntTag(ItemTags.ALCHEMICAL_AMMO_COUNT, count - 1)
            if (count - 1 == 0) {
                lastShot = true
            }
        }
        if (lastShot) {
            crossbow.removeTag(ItemTags.ALCHEMY_ARTILLERY_LOADED)
            // Do not remove saved potion if ammo
            if (crossbow.hasTag(ItemTags.ALCHEMY_COPY_STORED)) {
                crossbow.removeTag(ItemTags.ALCHEMY_COPY_STORED)
            } /*
            else {
                LOADED_ALCHEMICAL_AMMO[bowUUID] = null
            }
            */
        }
        // Shoot if potion made
        if (thrownPotion != null) {
            event.projectile = thrownPotion
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // COMPACT CROSSBOW

    private fun compactCrossbowHandler(event: EntityLoadCrossbowEvent) {
        val player = event.entity
        if (player !is Player) return

        if (player.inventory.itemInMainHand.type != Material.CROSSBOW) return
        val otherBow = if (player.inventory.itemInOffHand.type == Material.CROSSBOW) {
            player.inventory.itemInOffHand
        }
        else {
            return
        }
        if (!otherBow.hasItemMeta()) return
        val otherBowMeta = otherBow.itemMeta as CrossbowMeta
        if (!otherBowMeta.hasCustomModelData()) return
        if (otherBowMeta.hasChargedProjectiles()) return
        val bowMeta = event.crossbow!!.itemMeta as CrossbowMeta
        if (bowMeta.hasChargedProjectiles()) return
        // Load
        val loadedItem = ItemStack(Material.ARROW, 1)
        otherBowMeta.addChargedProjectile(loadedItem)
        otherBow.itemMeta = otherBowMeta
    }

}
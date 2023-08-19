package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.BLUDGEON_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.CLEAVE_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.LACERATE_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.MAX_RANGE_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.MIN_RANGE_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.PIERCE_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.REACH_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.SWEEP_MAP
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent

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

    @EventHandler(priority = EventPriority.HIGHEST)
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

        // Range
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

        // Conditions
        val emptyOff = offHandWeapon.type == Material.AIR
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
                if (isCrit && (emptyOff || sayaInOff)) {
                    event.damage += 3
                    weaponSweep(victim, player, SWEEP_MAP[model]!!, event.damage)
                } else if (!emptyOff && !sayaInOff) {
                    val minimumDamage = minOf(event.damage, 3.0)
                    event.damage -= minimumDamage
                }
            }
            ItemModels.CLAYMORE, ItemModels.SOUL_STEEL_CLAYMORE -> {
                if (!emptyOff) {
                    val minimumDamage = minOf(event.damage, 6.0)
                    event.damage -= minimumDamage
                } else if (emptyOff) {
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
                if (!emptyOff && !shieldInOff) {
                    val minimumDamage = minOf(event.damage, 6.0)
                    event.damage -= minimumDamage
                }
            }
            ItemModels.SCYTHE, ItemModels.SOUL_STEEL_SCYTHE -> {
                if (fullAttack && emptyOff) {
                    weaponSweep(victim, player, SWEEP_MAP[model]!!, maxOf(0.0, event.damage - 4.0))
                }
            }
            ItemModels.LANCE, ItemModels.SOUL_STEEL_LANCE -> {
                if (isMounted && fullAttack) {
                    event.damage += 14.0
                }
            }
            ItemModels.WARHAMMER, ItemModels.SOUL_STEEL_WARHAMMER -> {
                if (emptyOff) {
                    victim.shieldBlockingDelay = 60
                }
            }
            ItemModels.LONG_AXE, ItemModels.SOUL_STEEL_LONG_AXE -> {
                if (!emptyOff) {
                    val minimumDamage = minOf(event.damage, 6.0)
                    event.damage -= minimumDamage
                }
            }
            ItemModels.BAMBOO_STAFF, ItemModels.WOODEN_STAFF, ItemModels.BONE_STAFF, ItemModels.BLAZE_ROD_STAFF -> {
                if (event.isCritical) {
                    weaponSweep(victim, player, SWEEP_MAP[model]!!, event.damage + 2)
                } else {
                    weaponSweep(victim, player, SWEEP_MAP[model]!!, maxOf(event.damage - 1, 1.0))
                }

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

    /*
    @EventHandler(priority = EventPriority.HIGH)
    fun mainInteractEntityHandler(event: PlayerInteractEntityEvent) {
        //println("Interact")
    }
    @EventHandler(priority = EventPriority.HIGH)
    fun mainInteractAtEntityHandler(event: PlayerInteractAtEntityEvent) {
        //println("Interact At Entity")
    }
    */

    private fun leftClickHandler(event: PlayerInteractEvent) {
        val player = event.player
        val mainWeapon = player.equipment.itemInMainHand

        if (!mainWeapon.hasItemMeta()) return
        if (!mainWeapon.itemMeta!!.hasCustomModelData()) return
        val model = mainWeapon.itemMeta!!.customModelData
        if (REACH_MAP[model] == null) return
        // Sentries Passed

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

        // ----------- Off Hand ------------
        val fullAttack = player.attackCooldown > 0.5
        if (offHandWeapon.itemMeta?.hasCustomModelData() == true && fullAttack) {
            when(val model = offHandWeapon.itemMeta!!.customModelData) {
                ItemModels.DAGGER, ItemModels.SICKLE, ItemModels.CHAKRAM -> {
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
    }

    /*-----------------------------------------------------------------------------------------------*/
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

}
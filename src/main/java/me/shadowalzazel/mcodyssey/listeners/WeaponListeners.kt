package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.BLUDGEON_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.CLEAVE_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.LACERATE_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.PIERCE_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.REACH_MAP
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.SWEEP_MAP

import org.bukkit.Material
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
import kotlin.math.min

// --------------------------------- NOTES --------------------------------
// TODO: Mounted Bonus i.e. Cavalry Charges
// TODO: Add Armor Stats through Lore
// TODO: Scepter -> checks if offhand is a tome of _enchantment_, does spell.

// Can craft weapons without recipe, but with recipe, it is greater quality
// DeprecatedWeapon that can ignore I-frames

// RUNE STONES vs ENCHANTMENTS

object WeaponListeners : Listener {

    // Entities have custom dual and range wield mechanics!!!!!

    // Helper function for reach
    private fun getReachedTarget(somePlayer: Player, reachStat: Double?): Entity? {
        val reachedEntity: Entity? = reachStat?.run {
            val targetInfo = somePlayer.getTargetEntityInfo(reachStat.toInt() + 2, false)
            var targetEntity = targetInfo?.entity
            if (targetEntity != null) {
                val rangeReach = somePlayer.eyeLocation.distance(targetEntity.location.clone())
                if (reachStat < rangeReach) {
                    targetEntity = null
                }
            }
            targetEntity
        }
        return reachedEntity
    }

    // TODO: If Crouching more damage

    // Function for critical hits that sweep
    private fun weaponSweep(victim: LivingEntity, attacker: LivingEntity, radius: Double, damage: Double) {
        val midpoint = attacker.location.clone().set(
            ((victim.location.x + attacker.location.x) / 2),
            ((victim.location.y + attacker.location.y) / 2),
            ((victim.location.z + attacker.location.z) / 2)
        )

        victim.scoreboardTags.add(EntityTags.COMBOED)
        val enemyList = midpoint.getNearbyEntities(radius, radius, radius).filter {
            it != victim && it != attacker
        }
        for (entity in enemyList) {
            if (entity is LivingEntity && !entity.scoreboardTags.contains(EntityTags.COMBOED)) {
                // TODO: Use New Attack Functions
                entity.scoreboardTags.add(EntityTags.COMBOED)
                entity.damage(damage, attacker)
                // Attack? instead of damage? method
            }
        }
    }

    // Stat handler
    private fun weaponStatsHandler(mapNum: Int, victim: LivingEntity, damage: Double): Pair<Double, Double> {
        return if (!victim.isDead && victim.getAttribute(Attribute.GENERIC_ARMOR)?.value != null) {
            // Armor Point
            val armorPoints = victim.getAttribute(Attribute.GENERIC_ARMOR)!!.value

            // Piercing ignores some armor
            // Bludgeon is true damage
            // Cleaving more damage if wearing armor
            // lacerate


            // Cleaving
            val cleaveDamage = if (CLEAVE_MAP[mapNum] != null) {
                CLEAVE_MAP[mapNum]
            } else {
                0.0
            }

            // Bludgeon
            val bludgeoningDamage = if (BLUDGEON_MAP[mapNum] != null) {
                min(BLUDGEON_MAP[mapNum]!! + (armorPoints * 0.2), armorPoints)
            } else {
                0.0
            }
            // Lacerate
            val laceratingDamage = if (LACERATE_MAP[mapNum] != null) {
                if (armorPoints <= 1.5) {
                    LACERATE_MAP[mapNum]!!
                } else {
                    0.0
                }
            } else {
                0.0
            }
            // Pierce
            val trueDamage = if (PIERCE_MAP[mapNum] != null) {
                min(armorPoints, PIERCE_MAP[mapNum]!!)
            } else {
                0.0
            }

            // Extra damage, True Damage
            val bonusDamage = if (damage >= bludgeoningDamage + laceratingDamage) {
                bludgeoningDamage + laceratingDamage
            } else {
                0.0
            }

            Pair(bonusDamage, trueDamage)
        } else {
            Pair(0.0, 0.0)
        }

    }

    // TODO: !!!!!!!!
    // PARRY
    // IF hand raised
    // IF weapon can parry
    // If attack right after parry
    // DO damage,

    // ----------------------------------------------------------------------------------------------------


    // Main function regarding interactions
    @EventHandler(priority = EventPriority.HIGHEST)
    fun mainWeaponInteractionHandler(event: PlayerInteractEvent) {
        // Right click and left click combos

        // Player and weapons
        val player = event.player
        val mainWeapon = player.equipment.itemInMainHand
        val offHandWeapon = player.equipment.itemInOffHand

        // Left Click
        if (event.action.isLeftClick && mainWeapon.itemMeta?.hasCustomModelData() == true && REACH_MAP[mainWeapon.itemMeta.customModelData] != null) {
            getReachedTarget(player, REACH_MAP[mainWeapon.itemMeta.customModelData])
            // Get If entity reached
            val entity = getReachedTarget(player, REACH_MAP[mainWeapon.itemMeta.customModelData])
            if (entity is LivingEntity) {
                player.attack(entity)
            }
        }
        // Right Click
        else if (event.action.isRightClick) {
            // Main DeprecatedWeapon
            if (mainWeapon.itemMeta?.hasCustomModelData() == true) {
                when (mainWeapon.itemMeta.customModelData) {
                    // Staff AOE
                    ItemModels.WOODEN_STAFF, ItemModels.BONE_STAFF, ItemModels.BAMBOO_STAFF, ItemModels.BLAZE_ROD_STAFF -> {
                        // Empty Hand
                        if (player.equipment.itemInOffHand.type == Material.AIR) {
                            // TODO: Fix
                            val nearbyEnemies = player.getNearbyEntities(1.5, 1.5, 1.5).also { it.remove(player) }
                            for (enemy in nearbyEnemies) {
                                player.attack(enemy)
                                player.swingMainHand()
                            }
                        }
                    }
                    // Warhammer
                    ItemModels.WARHAMMER -> {
                        // SUPER ATTACK?
                        if (player.equipment.itemInOffHand.type == Material.AIR) {
                            //  Step Forward
                            val movingVector = player.eyeLocation.direction.clone().normalize().setY(0.0)
                            // Get swing arc
                            val midPointSwing = player.eyeLocation.direction.clone().add(movingVector.multiply(2.0)).toLocation(player.world)
                            val nearbyEnemies = midPointSwing.getNearbyEntities(2.5, 1.0, 2.5).also { it.remove(player) }
                            for (enemy in nearbyEnemies) {
                                player.attack(enemy)
                            }
                            //if hit
                            if (nearbyEnemies.isNotEmpty() && !player.isFlying) {
                                // Particles and Sounds
                                player.world.playSound(player.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.5F, 2.75F)
                                println("X")
                                player.velocity = movingVector.clone().multiply(0.35)
                            }

                        }
                    }
                    ItemModels.ZWEIHANDER -> {
                        // Empty Hand
                        if (player.equipment.itemInOffHand.type == Material.AIR) {
                            //  Step Forward
                            val movingVector = player.eyeLocation.direction.clone().normalize().setY(0.0)
                            // Get swing arc
                            val midPointSwing = player.eyeLocation.direction.clone().add(movingVector.multiply(2.0)).toLocation(player.world)
                            val nearbyEnemies = midPointSwing.getNearbyEntities(2.5, 1.0, 2.5).also { it.remove(player) }
                            for (enemy in nearbyEnemies) {
                                player.attack(enemy)
                            }
                            //if hit
                            if (nearbyEnemies.isNotEmpty() && !player.isFlying) {
                                // Particles and Sounds
                                player.world.playSound(player.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.5F, 2.75F)
                                println("X")
                                player.velocity = movingVector.clone().multiply(0.35)
                            }

                        }
                    }
                }
            }

            // CAN THROW KUNAI/ SNOWBALLS THAT DO DMG
            // If throw snowball
            // if kunai
            // add tag
            // if tag is kunai, and hits do +5 damage


            // MINATO enchant
            // If throw kunai
            // If has tag
            // If surface normalized
            // spawn armor stand with kunai
            // can tp if throw another
            //

            // Offhand DeprecatedWeapon
            if (offHandWeapon.itemMeta?.hasCustomModelData() == true) {
                when (offHandWeapon.itemMeta.customModelData) {
                    // Dagger
                    ItemModels.DAGGER -> {
                        // Left click offhand
                        val reachedEntity = getReachedTarget(player, REACH_MAP[offHandWeapon.itemMeta.customModelData])
                        if (reachedEntity is LivingEntity) {
                            player.swingOffHand()
                            with(player.equipment) {
                                val mainHand = itemInMainHand.clone()
                                val offHand = itemInOffHand.clone()
                                setItemInOffHand(mainHand)
                                setItemInMainHand(offHand)
                                player.attack(reachedEntity)
                                setItemInMainHand(mainHand)
                                setItemInOffHand(offHand)
                            }
                        }
                    }
                }
            }
        }
    }

    //
    @EventHandler(priority = EventPriority.HIGHEST)
    fun mainWeaponDamageHandler(event: EntityDamageByEntityEvent) {
        // Check if event damager and damaged is living entity
        if (event.damager is Player && event.entity is LivingEntity && event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            val player = event.damager as Player
            val victim = event.entity as LivingEntity
            // Check if entity recently comboed to stop recursive call
            with(victim.scoreboardTags) {
                if (contains("Weapon_Comboed")) {
                    remove("Weapon_Comboed")
                    return
                }
            }

            // Check if active item has custom model data
            if (player.equipment.itemInMainHand.itemMeta?.hasCustomModelData() == true) {
                val mainWeapon = player.equipment.itemInMainHand
                val offHandWeapon = player.equipment.itemInOffHand

                // Make crit and still combos !!
                when (val weaponData = mainWeapon.itemMeta.customModelData) {
                    ItemModels.DAGGER, ItemModels.SICKLE, ItemModels.CHAKRAM -> {
                        if (victim !in player.getNearbyEntities(1.9, 1.9, 1.9)) {
                            event.isCancelled = true
                            return
                        }
                    }
                    ItemModels.SPEAR -> {
                        if (victim in player.getNearbyEntities(1.25, 1.25, 1.25)) {
                            event.isCancelled = true
                            return
                        }
                    }
                    ItemModels.HALBERD -> {
                        if (victim in player.getNearbyEntities(1.5, 1.5, 1.5) || (offHandWeapon.type != Material.AIR && offHandWeapon.type != Material.SHIELD)) {
                            event.isCancelled = true
                            return
                        }
                    }
                    ItemModels.BAMBOO_STAFF, ItemModels.WOODEN_STAFF, ItemModels.BONE_STAFF, ItemModels.BLAZE_ROD_STAFF -> {
                        if (event.isCritical) {
                            weaponSweep(victim, player, SWEEP_MAP[weaponData]!!, event.damage + 2)
                        } else {
                            weaponSweep(victim, player, SWEEP_MAP[weaponData]!!, event.damage - 1)
                        }

                    }
                    ItemModels.LONG_AXE -> {
                        if (offHandWeapon.type != Material.AIR) {
                            val minimumDamage = minOf(event.damage, 5.0)
                            event.damage -= minimumDamage
                        }
                    }
                    ItemModels.KATANA, ItemModels.SOUL_STEEL_KATANA -> {
                        // Rabbit Hide -> Sheath
                        if (event.isCritical && (offHandWeapon.type == Material.AIR || offHandWeapon.type == Material.RABBIT_HIDE)) {
                            weaponSweep(victim, player, SWEEP_MAP[weaponData]!!, event.damage)
                        } else if (offHandWeapon.type != Material.AIR && offHandWeapon.type != Material.RABBIT_HIDE) {
                            val minimumDamage = minOf(event.damage, 3.0)
                            event.damage -= minimumDamage
                        }
                        // Piercing?
                    }
                    ItemModels.CLAYMORE -> {
                        if (offHandWeapon.type != Material.AIR) {
                            val minimumDamage = minOf(event.damage, 5.0)
                            event.damage -= minimumDamage
                        } else if (offHandWeapon.type == Material.AIR) {
                            val sweepDamage = if (event.isCritical) {
                                event.damage
                            } else {
                                event.damage - 3.0
                            }
                            weaponSweep(victim, player, SWEEP_MAP[weaponData]!!, sweepDamage)
                        }
                        // SWEEP PARTICLES!!!
                    }
                    ItemModels.ZWEIHANDER -> {
                        // Stab Or Custom Sweep
                        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
                            event.isCancelled = true
                        }
                    }

                    // ZWEIHANDER
                    // leftclick stab
                    // righclick short dash and AOE

                }
                val extraDamages = weaponStatsHandler(mainWeapon.itemMeta.customModelData, victim, event.damage)
                event.damage -= extraDamages.second
                event.damage += extraDamages.first
                if (victim.health < extraDamages.second) {
                    victim.health -= extraDamages.second - victim.health
                } else {
                    victim.health -= extraDamages.second
                }

            }
        }
    }



}
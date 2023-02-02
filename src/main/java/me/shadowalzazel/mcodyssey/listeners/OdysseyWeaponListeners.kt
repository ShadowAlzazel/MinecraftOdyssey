package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import me.shadowalzazel.mcodyssey.constants.OdysseyWeaponAttributes.BLUDGEON_MAP
import me.shadowalzazel.mcodyssey.constants.OdysseyWeaponAttributes.CLEAVE_MAP
import me.shadowalzazel.mcodyssey.constants.OdysseyWeaponAttributes.LACERATE_MAP
import me.shadowalzazel.mcodyssey.constants.OdysseyWeaponAttributes.PIERCE_MAP
import me.shadowalzazel.mcodyssey.constants.OdysseyWeaponAttributes.REACH_MAP
import me.shadowalzazel.mcodyssey.constants.OdysseyWeaponAttributes.SWEEP_MAP

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


// TODO: Mounted Bonus i.e. Cavalry Charges
// TODO: Add Armor Stats through Lore
// TODO: Scepter -> checks if offhand is a tome of _enchantment_, does spell.
object OdysseyWeaponListeners : Listener {

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
    private fun doWeaponSweep(someVictim: LivingEntity, someDamager: LivingEntity, radius: Double, eventDamage: Double) {
        val midpoint = someDamager.location.clone().set(
            ((someVictim.location.x + someDamager.location.x) / 2),
            ((someVictim.location.y + someDamager.location.y) / 2),
            ((someVictim.location.z + someDamager.location.z) / 2)
        )

        someVictim.scoreboardTags.add("Weapon_Comboed")
        val comboEntities = midpoint.getNearbyEntities(radius, radius, radius).filter {
            !(it == someVictim || it == someDamager)
        }
        for (entity in comboEntities) {
            if (entity is LivingEntity && !entity.scoreboardTags.contains("Weapon_Comboed")) {
                entity.scoreboardTags.add("Weapon_Comboed")
                entity.damage(eventDamage, someDamager)
                // Attack? instead of damage? method
            }
        }
    }

    // Stat handler
    private fun weaponStatsHandler(weaponData: Int, someVictim: LivingEntity, oldDamage: Double): Pair<Double, Double> {
        return if (!someVictim.isDead && someVictim.getAttribute(Attribute.GENERIC_ARMOR)?.value != null) {
            // Armor Point
            val armorPoints = someVictim.getAttribute(Attribute.GENERIC_ARMOR)!!.value

            // Piercing ignores some armor
            // Bludgeon is true damage
            // Cleaving more damage if wearing armor
            // lacerate


            // Cleaving
            val cleaveDamage = if (CLEAVE_MAP[weaponData] != null) {
                CLEAVE_MAP[weaponData]
            } else {
                0.0
            }

            // Bludgeon
            val bludgeoningDamage = if (BLUDGEON_MAP[weaponData] != null) {
                min(BLUDGEON_MAP[weaponData]!! + (armorPoints * 0.2), armorPoints)
            } else {
                0.0
            }
            // Lacerate
            val laceratingDamage = if (LACERATE_MAP[weaponData] != null) {
                if (armorPoints <= 1.5) {
                    LACERATE_MAP[weaponData]!!
                } else {
                    0.0
                }
            } else {
                0.0
            }
            // Pierce
            val trueDamage = if (PIERCE_MAP[weaponData] != null) {
                min(armorPoints, PIERCE_MAP[weaponData]!!)
            } else {
                0.0
            }

            // Extra damage, True Damage
            val bonusDamage = if (oldDamage >= bludgeoningDamage + laceratingDamage) {
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
        val somePlayer = event.player
        val mainWeapon = somePlayer.equipment.itemInMainHand
        val offHandWeapon = somePlayer.equipment.itemInOffHand

        // Left Click
        if (event.action.isLeftClick && mainWeapon.itemMeta?.hasCustomModelData() == true && REACH_MAP[mainWeapon.itemMeta.customModelData] != null) {
            getReachedTarget(somePlayer, REACH_MAP[mainWeapon.itemMeta.customModelData])
            // Get If entity reached
            val reachedEntity = getReachedTarget(somePlayer, REACH_MAP[mainWeapon.itemMeta.customModelData])
            if (reachedEntity is LivingEntity) {
                somePlayer.attack(reachedEntity)
            }
        }
        // Right Click
        else if (event.action.isRightClick) {
            // Main Weapon
            if (mainWeapon.itemMeta?.hasCustomModelData() == true) {
                when (mainWeapon.itemMeta.customModelData) {
                    // Staff AOE
                    OdysseyItemModels.WOODEN_STAFF, OdysseyItemModels.BONE_STAFF, OdysseyItemModels.BAMBOO_STAFF, OdysseyItemModels.BLAZE_ROD_STAFF -> {
                        // Empty Hand
                        if (somePlayer.equipment.itemInOffHand.type == Material.AIR) {
                            // TODO: Fix
                            val nearbyEnemies = somePlayer.getNearbyEntities(1.5, 1.5, 1.5).also { it.remove(somePlayer) }
                            for (enemy in nearbyEnemies) {
                                somePlayer.attack(enemy)
                                somePlayer.swingMainHand()
                            }
                        }
                    }
                    // Warhammer
                    OdysseyItemModels.IRON_WARHAMMER -> {
                        // SUPER ATTACK?
                        if (somePlayer.equipment.itemInOffHand.type == Material.AIR) {
                            //  Step Forward
                            val movingVector = somePlayer.eyeLocation.direction.clone().normalize().setY(0.0)
                            // Get swing arc
                            val midPointSwing = somePlayer.eyeLocation.direction.clone().add(movingVector.multiply(2.0)).toLocation(somePlayer.world)
                            val nearbyEnemies = midPointSwing.getNearbyEntities(2.5, 1.0, 2.5).also { it.remove(somePlayer) }
                            for (enemy in nearbyEnemies) {
                                somePlayer.attack(enemy)
                            }
                            //if hit
                            if (nearbyEnemies.isNotEmpty() && !somePlayer.isFlying) {
                                // Particles and Sounds
                                somePlayer.world.playSound(somePlayer.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.5F, 2.75F)
                                println("X")
                                somePlayer.velocity = movingVector.clone().multiply(0.35)
                            }

                        }
                    }
                    OdysseyItemModels.NETHERITE_ZWEIHANDER -> {
                        // Empty Hand
                        if (somePlayer.equipment.itemInOffHand.type == Material.AIR) {
                            //  Step Forward
                            val movingVector = somePlayer.eyeLocation.direction.clone().normalize().setY(0.0)
                            // Get swing arc
                            val midPointSwing = somePlayer.eyeLocation.direction.clone().add(movingVector.multiply(2.0)).toLocation(somePlayer.world)
                            val nearbyEnemies = midPointSwing.getNearbyEntities(2.5, 1.0, 2.5).also { it.remove(somePlayer) }
                            for (enemy in nearbyEnemies) {
                                somePlayer.attack(enemy)
                            }
                            //if hit
                            if (nearbyEnemies.isNotEmpty() && !somePlayer.isFlying) {
                                // Particles and Sounds
                                somePlayer.world.playSound(somePlayer.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.5F, 2.75F)
                                println("X")
                                somePlayer.velocity = movingVector.clone().multiply(0.35)
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

            // Offhand Weapon
            if (offHandWeapon.itemMeta?.hasCustomModelData() == true) {
                when (offHandWeapon.itemMeta.customModelData) {
                    // Dagger
                    OdysseyItemModels.DIAMOND_DAGGER -> {
                        // Left click offhand
                        val reachedEntity = getReachedTarget(somePlayer, REACH_MAP[offHandWeapon.itemMeta.customModelData])
                        if (reachedEntity is LivingEntity) {
                            somePlayer.swingOffHand()
                            with(somePlayer.equipment) {
                                val mainHand = itemInMainHand.clone()
                                val offHand = itemInOffHand.clone()
                                setItemInOffHand(mainHand)
                                setItemInMainHand(offHand)
                                somePlayer.attack(reachedEntity)
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
            val someDamager = event.damager as Player
            val someVictim = event.entity as LivingEntity
            // Check if entity recently comboed to stop recursive call
            with(someVictim.scoreboardTags) {
                if (contains("Weapon_Comboed")) {
                    remove("Weapon_Comboed")
                    return
                }
            }

            // Check if active item has custom model data
            if (someDamager.equipment.itemInMainHand.itemMeta?.hasCustomModelData() == true) {
                val someWeapon = someDamager.equipment.itemInMainHand
                val someOffHand = someDamager.equipment.itemInOffHand

                // Make crit and still combos !!
                when (val weaponData = someWeapon.itemMeta.customModelData) {
                    OdysseyItemModels.DIAMOND_DAGGER -> {
                        if (someVictim !in someDamager.getNearbyEntities(1.9, 1.9, 1.9)) {
                            event.isCancelled = true
                            return
                        }
                    }
                    OdysseyItemModels.WOODEN_SPEAR -> {
                        if (someVictim in someDamager.getNearbyEntities(1.25, 1.25, 1.25)) {
                            event.isCancelled = true
                            return
                        }
                    }
                    OdysseyItemModels.IRON_HALBERD -> {
                        if (someVictim in someDamager.getNearbyEntities(1.5, 1.5, 1.5) || (someOffHand.type != Material.AIR && someOffHand.type != Material.SHIELD)) {
                            event.isCancelled = true
                            return
                        }
                    }
                    OdysseyItemModels.BAMBOO_STAFF, OdysseyItemModels.WOODEN_STAFF, OdysseyItemModels.BONE_STAFF, OdysseyItemModels.BLAZE_ROD_STAFF -> {
                        if (event.isCritical) {
                            doWeaponSweep(someVictim, someDamager, SWEEP_MAP[weaponData]!!, event.damage + 2)
                        } else {
                            doWeaponSweep(someVictim, someDamager, SWEEP_MAP[weaponData]!!, event.damage - 1)
                        }

                    }
                    OdysseyItemModels.DIAMOND_LONG_AXE -> {
                        if (someOffHand.type != Material.AIR) {
                            val minimumDamage = minOf(event.damage, 5.0)
                            event.damage -= minimumDamage
                        }
                    }
                    OdysseyItemModels.DIAMOND_KATANA, OdysseyItemModels.SOUL_STEEL_KATANA -> {
                        // Rabbit Hide -> Sheath
                        if (event.isCritical && (someOffHand.type == Material.AIR || someOffHand.type == Material.RABBIT_HIDE)) {
                            doWeaponSweep(someVictim, someDamager, SWEEP_MAP[weaponData]!!, event.damage)
                        } else if (someOffHand.type != Material.AIR && someOffHand.type != Material.RABBIT_HIDE) {
                            val minimumDamage = minOf(event.damage, 3.0)
                            event.damage -= minimumDamage
                        }
                        // Piercing?
                    }
                    OdysseyItemModels.DIAMOND_CLAYMORE -> {
                        if (someOffHand.type != Material.AIR) {
                            val minimumDamage = minOf(event.damage, 5.0)
                            event.damage -= minimumDamage
                        } else if (someOffHand.type == Material.AIR) {
                            val sweepDamage = if (event.isCritical) {
                                event.damage
                            } else {
                                event.damage - 3.0
                            }
                            doWeaponSweep(someVictim, someDamager, SWEEP_MAP[weaponData]!!, sweepDamage)
                        }
                        // SWEEP PARTICLES!!!
                    }
                    OdysseyItemModels.NETHERITE_ZWEIHANDER -> {
                        // Stab Or Custom Sweep
                        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
                            event.isCancelled = true
                        }
                    }

                    // ZWEIHANDER
                    // leftclick stab
                    // righclick short dash and AOE

                }
                val extraDamages = weaponStatsHandler(someWeapon.itemMeta.customModelData, someVictim, event.damage)
                event.damage -= extraDamages.second
                event.damage += extraDamages.first
                if (someVictim.health < extraDamages.second) {
                    someVictim.health -= extraDamages.second - someVictim.health
                } else {
                    someVictim.health -= extraDamages.second
                }

            }
        }
    }


}
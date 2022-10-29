package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.WeaponStats.bludgeonMap
import me.shadowalzazel.mcodyssey.constants.WeaponStats.lacerateMap
import me.shadowalzazel.mcodyssey.constants.WeaponStats.pierceMap
import me.shadowalzazel.mcodyssey.constants.WeaponStats.reachMap
import me.shadowalzazel.mcodyssey.constants.WeaponStats.sweepMap

import org.bukkit.Material
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
import kotlin.math.max
import kotlin.math.min


object OdysseyWeaponListeners : Listener {

    // Entities have custom dual and range wield mechanics!!!!!

    // Helper function for reach
    private fun reachFunction(somePlayer: Player, reachStat: Double?): Entity? {
        val reachedEntity: Entity? = reachStat?.run {
            val targetInfo = somePlayer.getTargetEntityInfo(reachStat.toInt() + 2, false)
            var targetEntity = targetInfo?.entity
            if (targetEntity != null) {
                val rangeReach = somePlayer.eyeLocation.distance(targetEntity.location.clone())
                //println(rangeReach)
                // sees if target entity out of range
                if (reachStat < rangeReach) { targetEntity = null }
            }
            targetEntity
        }
        return reachedEntity
    }

    // TODO: If Crouching more damage

    // Function for critical hits that sweep
    private fun sweepComboFunction(someVictim: LivingEntity, someDamager: LivingEntity, radius: Double, eventDamage: Double) {
        val midpoint = someDamager.location.clone().set(
            ((someVictim.location.x + someDamager.location.x) / 2),
            ((someVictim.location.y + someDamager.location.y) / 2),
            ((someVictim.location.z + someDamager.location.z) / 2))

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
    private fun armorCalculations(weaponData: Int, someVictim: LivingEntity, oldDamage: Double): Pair<Double, Double> {
        return if (!someVictim.isDead && someVictim.getAttribute(Attribute.GENERIC_ARMOR)?.value != null) {
            // Armor Point
            val armorPoints = someVictim.getAttribute(Attribute.GENERIC_ARMOR)!!.value

            // Bludgeon
            val bludgeoningDamage = if (bludgeonMap[weaponData] != null) {  min(bludgeonMap[weaponData]!! + (armorPoints * 0.2), armorPoints) }  else { 0.0 }
            // Lacerate
            val laceratingDamage = if (lacerateMap[weaponData] != null) {
                if (armorPoints <= 1.5) { lacerateMap[weaponData]!! } else { 0.0 }
            }  else { 0.0 }
            // Pierce
            val piercingDamage = if (pierceMap[weaponData] != null) { min(armorPoints, pierceMap[weaponData]!!) }  else { 0.0 }

            // Extra damage, True Damage
            val extraAppliedDamage = if (oldDamage >= bludgeoningDamage + laceratingDamage) { bludgeoningDamage + laceratingDamage } else { 0.0 }

            Pair(extraAppliedDamage, piercingDamage)
        } else {
            Pair(0.0, 0.0)
        }

    }


    // ----------------------------------------------------------------------------------------------------


    // Main function regarding interactions
    @EventHandler(priority = EventPriority.HIGH)
    fun mainWeaponInteractionHandler(event: PlayerInteractEvent) {
        // Right click and left click combos
        val somePlayer = event.player
        if (somePlayer.equipment.itemInMainHand.itemMeta?.hasCustomModelData() == true) {
            val mainWeapon = somePlayer.equipment.itemInMainHand
            val offHand = somePlayer.equipment.itemInOffHand
            // Main Damage Left Click
            if (event.action.isLeftClick) {
                reachFunction(somePlayer, reachMap[mainWeapon.itemMeta.customModelData])
                // Get If entity reached
                val reachedEntity = reachFunction(somePlayer, reachMap[mainWeapon.itemMeta.customModelData])
                if (reachedEntity is LivingEntity) {
                    somePlayer.attack(reachedEntity)
                }
            }
            // Right click bonus actions
            if (event.action.isRightClick) {
                // Off-hand weapon
                when (offHand.itemMeta.customModelData) {
                    // Dagger
                    ItemModels.DIAMOND_DAGGER -> {
                        // Left click offhand
                        val reachedEntity = reachFunction(somePlayer, reachMap[mainWeapon.itemMeta.customModelData])
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
                // Main-hand weapon
                when (mainWeapon.itemMeta.customModelData) {
                    // Staff AOE
                    ItemModels.WOODEN_STAFF, ItemModels.BONE_STAFF, ItemModels.BAMBOO_STAFF, ItemModels.BLAZE_ROD_STAFF -> {
                        if (somePlayer.equipment.itemInOffHand.type == Material.AIR) {
                            val nearbyEnemies = somePlayer.getNearbyEntities(1.5, 1.5, 1.5).also { it.remove(somePlayer) }
                            for (enemy in nearbyEnemies) {
                                somePlayer.attack(enemy)
                                somePlayer.swingMainHand()
                            }
                        }
                    }
                    // Warhammer
                    ItemModels.IRON_WARHAMMER -> {
                        // SUPER ATTACK?

                    }
                }
            }
        }
    }

    //
    @EventHandler(priority = EventPriority.HIGH)
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
                    ItemModels.DIAMOND_DAGGER -> {
                        if (someVictim !in someDamager.getNearbyEntities(1.9, 1.9, 1.9)) {
                            event.isCancelled = true
                            return
                        }
                    }
                    ItemModels.WOODEN_SPEAR -> {
                        if (someVictim in someDamager.getNearbyEntities(1.25, 1.25, 1.25)) {
                            event.isCancelled = true
                            return
                        }
                    }
                    ItemModels.IRON_HALBERD -> {
                        if (someVictim in someDamager.getNearbyEntities(1.5, 1.5, 1.5) || (someOffHand.type != Material.AIR && someOffHand.type != Material.SHIELD)) {
                            event.isCancelled = true
                            return
                        }
                    }
                    ItemModels.BAMBOO_STAFF, ItemModels.WOODEN_STAFF, ItemModels.BONE_STAFF, ItemModels.BLAZE_ROD_STAFF -> {
                        if (event.isCritical) { sweepComboFunction(someVictim, someDamager, sweepMap[weaponData]!!, event.damage + 2) }
                        else { sweepComboFunction(someVictim, someDamager, sweepMap[weaponData]!!, event.damage - 1) }

                    }
                    ItemModels.GOLDEN_SABER -> {
                    }

                    ItemModels.DIAMOND_KATANA, ItemModels.SOUL_STEEL_KATANA -> {
                        // Rabbit Hide -> Sheath
                        if (event.isCritical && (someOffHand.type == Material.AIR || someOffHand.type == Material.RABBIT_HIDE)) {
                            sweepComboFunction(someVictim, someDamager, sweepMap[weaponData]!!, event.damage)
                        }
                        else if (someOffHand.type != Material.AIR && someOffHand.type != Material.RABBIT_HIDE) {
                            event.damage -= 3.0
                        }
                        // Piercing?
                    }
                    ItemModels.DIAMOND_CLAYMORE -> {
                        if (someOffHand.type != Material.AIR) {
                            event.damage -= 5.0
                        }
                        else if (someOffHand.type == Material.AIR) {
                            val sweepDamage = if (event.isCritical) { event.damage } else { event.damage - 3.0 }
                            sweepComboFunction(someVictim, someDamager, sweepMap[weaponData]!!, sweepDamage)
                        }
                            // SWEEP PARTICLES!!!
                    }
                }
                val extraDamages = armorCalculations(someWeapon.itemMeta.customModelData, someVictim, event.damage)
                event.damage -= extraDamages.second
                event.damage += extraDamages.first
                if (someVictim.health < extraDamages.second) {
                    someVictim.health -= extraDamages.second - someVictim.health
                }
                else {
                    someVictim.health -= extraDamages.second
                }

            }
        }
    }


}
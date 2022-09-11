package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.assets.ItemModels
import me.shadowalzazel.mcodyssey.assets.WeaponStats.weaponReachMap
import org.bukkit.Location
import org.bukkit.Material

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent


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

    // Function for critical hits that sweep
    private fun sweepCombo(someVictim: LivingEntity, someDamager: LivingEntity, radius: Double, centerLocation: Location, eventDamage: Double) {
        someVictim.scoreboardTags.add("Weapon_Comboed")
        val comboEntities = centerLocation.getNearbyEntities(radius, radius, radius).also {
            it.remove(someVictim)
            it.remove(someDamager)
        }
        for (entity in comboEntities) {
            if (entity is LivingEntity && !entity.scoreboardTags.contains("Weapon_Comboed")) {
                entity.scoreboardTags.add("Weapon_Comboed")
                entity.damage(eventDamage, someDamager)
                // Attack? instead of damage? method
            }
        }
    }

    // TODO: Weapon Piercing, Cleaving, Bludgeoning Damage

    // Main function regarding interactions
    @EventHandler(priority = EventPriority.HIGH)
    fun mainWeaponInteractionHandler(event: PlayerInteractEvent) {
        // Right click and left click combos
        val somePlayer = event.player
        if (somePlayer.equipment.itemInMainHand.itemMeta?.hasCustomModelData() == true) {
            val mainWeapon = somePlayer.equipment.itemInMainHand
            // Main Damage Click
            if (event.action.isLeftClick) {
                reachFunction(somePlayer, weaponReachMap[mainWeapon.itemMeta.customModelData])
                // Get If entity reached
                val reachedEntity = reachFunction(somePlayer, weaponReachMap[mainWeapon.itemMeta.customModelData])
                if (reachedEntity is LivingEntity) {
                    somePlayer.attack(reachedEntity)
                }
            }
            // Right click actions can be custom combos
            if (event.action.isRightClick) {
                when (mainWeapon.itemMeta.customModelData) {
                    // Dagger
                    ItemModels.DIAMOND_DAGGER -> {
                        // Check if dual wielding daggers
                        val dualWieldingDaggers: Boolean = somePlayer.equipment.itemInOffHand.let {
                            if (it.hasItemMeta()) {
                                if (it.itemMeta.hasCustomModelData()) { it.itemMeta.customModelData == ItemModels.DIAMOND_DAGGER }
                                else { false }
                            }
                            else { false }
                        }
                        if (dualWieldingDaggers) {
                            val reachedEntity = reachFunction(somePlayer, weaponReachMap[mainWeapon.itemMeta.customModelData])
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
                when (someWeapon.itemMeta.customModelData) {
                    ItemModels.DIAMOND_DAGGER -> {
                        if (someVictim !in someDamager.getNearbyEntities(1.8, 1.8, 1.8)) {
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
                        if (someVictim in someDamager.getNearbyEntities(1.5, 1.5, 1.5) || someOffHand.type != Material.AIR) {
                            event.isCancelled = true
                            return
                        }
                    }
                    ItemModels.BAMBOO_STAFF, ItemModels.WOODEN_STAFF, ItemModels.BONE_STAFF, ItemModels.BLAZE_ROD_STAFF -> {
                        if (event.isCritical) { sweepCombo(someVictim, someDamager, 1.75, someDamager.location, event.damage + 2) }
                        else { sweepCombo(someVictim, someDamager, 1.75, someDamager.location, event.damage - 1) }

                    }
                    ItemModels.GOLDEN_SABER -> {
                    }

                    ItemModels.DIAMOND_KATANA, ItemModels.SOUL_STEEL_KATANA -> {
                        // Rabbit Hide -> Sheath
                        if (event.isCritical && someOffHand.type == Material.AIR || someOffHand.type == Material.RABBIT_HIDE) {
                            val dLocation = someDamager.location
                            val eLocation = someVictim.location
                            val midLocation = someDamager.location.clone().set(((dLocation.x + eLocation.x) / 2), ((dLocation.y + eLocation.y) / 2), ((dLocation.z + eLocation.z) / 2))
                            sweepCombo(someVictim, someDamager, 0.75, midLocation, event.damage)
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
                        else if (someDamager.equipment.itemInOffHand.type == Material.AIR) {
                            val sweepDamage = if (event.isCritical) { event.damage } else { event.damage - 3.0 }
                            val dLocation = someDamager.location
                            val eLocation = someVictim.location
                            val midLocation = someDamager.location.clone().set(((dLocation.x + eLocation.x) / 2), ((dLocation.y + eLocation.y) / 2), ((dLocation.z + eLocation.z) / 2))
                            sweepCombo(someVictim, someDamager, 1.25, midLocation, sweepDamage)
                        }
                            // SWEEP PARTICLES!!!
                    }
                }
            }
        }
    }


}
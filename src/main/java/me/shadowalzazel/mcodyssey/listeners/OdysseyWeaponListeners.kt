package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.resources.CustomModels
import me.shadowalzazel.mcodyssey.resources.WeaponStats.weaponReachMap

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
            // Right click actions per model
            if (event.action.isRightClick) {
                when (mainWeapon.itemMeta.customModelData) {
                    // Dagger
                    CustomModels.DIAMOND_DAGGER -> {
                        // Check if dual wielding daggers
                        val dualWieldingDaggers: Boolean = somePlayer.equipment.itemInOffHand.let {
                            if (it.hasItemMeta()) {
                                if (it.itemMeta.hasCustomModelData()) {
                                    it.itemMeta.customModelData == CustomModels.DIAMOND_DAGGER
                                }
                                else {
                                    false
                                }
                            }
                            else {
                                false
                            }
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
                if (contains("Comboed")) {
                    remove("Comboed")
                    return
                }
            }

            // Check if active item has custom model data
            if (someDamager.equipment.itemInMainHand.itemMeta?.hasCustomModelData() == true) {
                val someWeapon = someDamager.equipment.itemInMainHand
                // Make crit and still combos !!
                when (someWeapon.itemMeta.customModelData) {
                    CustomModels.DIAMOND_DAGGER -> {
                        if (someVictim !in someDamager.getNearbyEntities(1.65, 1.65, 1.65)) {
                            event.isCancelled = true
                            return
                        }
                    }
                    CustomModels.WOODEN_SPEAR -> {
                        if (someVictim in someDamager.getNearbyEntities(1.25, 1.25, 1.25)) {
                            event.isCancelled = true
                            return
                        }
                    }
                    CustomModels.BAMBOO_STAFF, CustomModels.WOODEN_STAFF, CustomModels.BONE_STAFF, CustomModels.BLAZE_ROD_STAFF -> {
                        if (event.isCritical || someDamager.velocity.length() < 0.05) {
                            someVictim.scoreboardTags.add("Comboed")
                            val comboEntities = someDamager.getNearbyEntities(1.75, 1.75, 1.75).also {
                                it.remove(someVictim)
                                it.remove(someDamager)
                            }
                            for (entity in comboEntities) {
                                if (entity is LivingEntity && !entity.scoreboardTags.contains("Comboed")) {
                                    entity.scoreboardTags.add("Comboed")
                                    someDamager.attack(entity)
                                }
                            }
                        }
                    }
                    CustomModels.DIAMOND_KATANA -> {
                        if (event.isCritical || someDamager.velocity.length() < 0.05) {
                            val dLocation = someDamager.location
                            val eLocation = someVictim.location
                            val midLocation = someDamager.location.clone().set(((dLocation.x + eLocation.x) / 2), ((dLocation.y + eLocation.y) / 2), ((dLocation.z + eLocation.z) / 2))
                            val comboEntities = midLocation.getNearbyEntities(1.0, 1.0, 1.0).also {
                                it.remove(someVictim)
                                it.remove(someDamager)
                            }
                            for (entity in comboEntities) {
                                if (entity is LivingEntity && !entity.scoreboardTags.contains("Comboed")) {
                                    entity.scoreboardTags.add("Comboed")
                                    someDamager.attack(entity)
                                }
                            }
                        }
                    }
                    CustomModels.DIAMOND_CLAYMORE -> {
                        if (event.isCritical || someDamager.velocity.length() < 0.05) {
                            val dLocation = someDamager.location
                            val eLocation = someVictim.location
                            val midLocation = someDamager.location.clone().set(((dLocation.x + eLocation.x) / 2), ((dLocation.y + eLocation.y) / 2), ((dLocation.z + eLocation.z) / 2))
                            val comboEntities = midLocation.getNearbyEntities(1.5, 1.0, 1.5).also {
                                it.remove(someVictim)
                                it.remove(someDamager)
                            }
                            for (entity in comboEntities) {
                                if (entity is LivingEntity && !entity.scoreboardTags.contains("Comboed")) {
                                    entity.scoreboardTags.add("Comboed")
                                    someDamager.attack(entity)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    // Enchantment




}
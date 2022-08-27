package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.resources.CustomModels
import me.shadowalzazel.mcodyssey.resources.WeaponStats.weaponReachMap

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.util.RayTraceResult


object OdysseyWeaponListeners : Listener {

    // Entities have custom dual and range wield mechanics!!!!!

    // Main function regarding interactions
    @EventHandler
    fun mainWeaponInteractionHandler(event: PlayerInteractEvent) {
        val somePlayer = event.player
        if (somePlayer.equipment.itemInMainHand.itemMeta?.hasCustomModelData() == true) {
            val mainWeapon = somePlayer.equipment.itemInMainHand
            val reachStat = weaponReachMap[mainWeapon.itemMeta.customModelData]

            // Get Reach
            val rayTraceEntity: RayTraceResult? = reachStat?.let {
                val eyeLocation = somePlayer.eyeLocation.clone()
                val vL = eyeLocation.add(eyeLocation.direction.clone().normalize().multiply(1.0))
                somePlayer.world.rayTraceEntities(vL, eyeLocation.direction, it)
            }
            if (rayTraceEntity?.hitEntity is LivingEntity) {
                somePlayer.attack(rayTraceEntity.hitEntity!!)
            }
        }
    }


    //
    @EventHandler
    fun mainWeaponDamageHandler(event: EntityDamageByEntityEvent) {
        // Check if event damager and damaged is living entity
        if (event.damager is Player && event.entity is LivingEntity && event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            val someDamager = event.damager as Player
            val someVictim = event.entity as LivingEntity
            // Check if active item has custom model data
            if (someDamager.equipment.itemInMainHand.itemMeta?.hasCustomModelData() == true) {
                val someWeapon = someDamager.equipment.itemInMainHand
                when (someWeapon.itemMeta.customModelData) {
                    CustomModels.DIAMOND_DAGGER -> {
                        if (someVictim !in someDamager.getNearbyEntities(1.25, 1.25, 1.25)) {
                            event.isCancelled = true
                            return
                        }
                        if (someDamager.equipment.itemInOffHand.itemMeta?.hasCustomModelData() == true) {
                            if (someDamager.equipment.itemInOffHand.itemMeta.customModelData == CustomModels.DIAMOND_DAGGER) {
                                println("X")
                                someDamager.swingOffHand()

                            }
                        }
                    }
                    CustomModels.BAMBOO_STAFF -> {
                        for (entity in someDamager.getNearbyEntities(1.75, 1.75, 1.75)) {
                            if (entity is LivingEntity) {
                                println("Q")
                                someDamager.attack(entity)
                            }
                        }
                    }
                    CustomModels.DIAMOND_KATANA -> {
                        //if (someVictim !in someDamager.getNearbyEntities(3.5, 3.5, 3.5)) { event.isCancelled = true }

                    }
                }
            }
        }
    }


    // Enchantment




}
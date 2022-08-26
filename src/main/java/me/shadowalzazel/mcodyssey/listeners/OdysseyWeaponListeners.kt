package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.models.CustomModels

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.util.RayTraceResult


object OdysseyWeaponListeners : Listener {

    private val weaponReachMap = mapOf(CustomModels.STONE_SPEAR to 6.0, CustomModels.IRON_CLAYMORE to 4.67)


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

                // Dagger Check
                if (someWeapon.itemMeta.customModelData == CustomModels.DIAMOND_DAGGER) {
                    if (someDamager.equipment.itemInOffHand.itemMeta?.hasCustomModelData() == true) {
                        if (someDamager.equipment.itemInOffHand.itemMeta.customModelData == CustomModels.DIAMOND_DAGGER) {
                            println("X")

                        }
                    }
                }
            }
        }
    }




}
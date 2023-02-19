package me.shadowalzazel.mcodyssey.listeners.enchantment_listeners

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

object MiscListeners : Listener {

    private var voidJumpCooldown = mutableMapOf<UUID, Long>()

    // MIRROR_FORCE enchantment effects
    @EventHandler
    fun mirrorForceEnchantment(event: ProjectileHitEvent) {
        if (event.hitEntity != null) {
            if (event.hitEntity is LivingEntity) {
                val blockingEntity = event.hitEntity as LivingEntity
                if (blockingEntity.equipment != null ) {
                    if (blockingEntity.equipment!!.itemInOffHand.hasItemMeta() ) {
                        val someShield = blockingEntity.equipment!!.itemInOffHand
                        if (someShield.itemMeta.hasEnchant(OdysseyEnchantments.MIRROR_FORCE)) {
                            if (blockingEntity.isHandRaised) {
                                if (blockingEntity.handRaised == EquipmentSlot.OFF_HAND) {
                                    println("Blocked!")
                                    //cooldown is part of the factor

                                    //event.entity.velocity = event.entity.location.subtract(blockingEntity.location).toVector()
                                    event.entity.velocity.multiply(-2.0)
                                    //direction
                                    // Use normal

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ---------------------------------------------- VOID_JUMP -------------------------------------------

    private fun voidJumpConditionsMet(eventPlayer: Player) : Boolean {
        // Check if player has chestplate and not in spectator
        if (eventPlayer.equipment.chestplate != null && eventPlayer.equipment.chestplate.hasItemMeta() && eventPlayer.gameMode != GameMode.SPECTATOR) {
            val someElytra = eventPlayer.equipment.chestplate
            // Check if player has enchantment
            if (someElytra.itemMeta.hasEnchant(OdysseyEnchantments.VOID_JUMP)) {
                // Check Speed
                val someSpeed = eventPlayer.velocity.clone().length()
                if (someSpeed > 0.125) {
                    if (eventPlayer.gameMode != GameMode.SPECTATOR && !eventPlayer.isDead) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun voidJumpParticles(someLocation: Location) {
        with(someLocation.world) {
            spawnParticle(Particle.FLASH, someLocation, 15, 0.0, 0.0, 0.0)
            spawnParticle(Particle.SONIC_BOOM, someLocation, 5, 0.0, 0.0, 0.0)
            spawnParticle(Particle.PORTAL, someLocation, 85, 1.5, 1.5, 1.5)
            playSound(someLocation, Sound.BLOCK_BEACON_DEACTIVATE, 2.5F, 2.5F)
        }
    }

    // VOID_JUMP Ender pearl handler
    @EventHandler
    fun voidJumpHandler(event: ProjectileLaunchEvent) {
        // Check if shot pearl and shooter is player
        if (event.entity is EnderPearl && event.entity.shooter is Player) {
            // Requirement Sentry
            if (!voidJumpConditionsMet(event.entity.shooter as Player)) {
                return
            }

            // Apply
            with((event.entity.shooter as Player)) {
                // Cooldown
                if (!voidJumpCooldown.containsKey(uniqueId)) { voidJumpCooldown[uniqueId] = 0L }
                val timeElapsed: Long = System.currentTimeMillis() - voidJumpCooldown[uniqueId]!!
                // Time sentry
                if (timeElapsed < 1.0 * 1000) {
                    return@voidJumpHandler
                }
                voidJumpCooldown[uniqueId] = System.currentTimeMillis()

                // Get vector
                val enchantmentLevel = equipment.chestplate.getEnchantmentLevel(OdysseyEnchantments.VOID_JUMP)
                val unitVector = velocity.clone().normalize()
                val jumpLocation = location.clone().add(
                    unitVector.clone().multiply((enchantmentLevel * 5.0) + 5)
                )

                // Apply teleport, new velocity, and particles
                voidJumpParticles(location)
                teleport(jumpLocation)
                voidJumpParticles(jumpLocation)
                velocity = unitVector.clone().multiply(1)
                
                // Inventory
                inventory.also {
                    if (it.itemInMainHand.type == Material.ENDER_PEARL) {
                        if (it.itemInMainHand.amount == 1) {
                            it.setItemInMainHand(ItemStack(Material.AIR, 1))
                        }
                        else {
                            it.setItemInMainHand(ItemStack(Material.ENDER_PEARL, it.itemInMainHand.amount - 1))
                        }
                    }
                    else if (it.itemInOffHand.type == Material.ENDER_PEARL) {
                        if (it.itemInOffHand.amount == 1) {
                            it.setItemInOffHand(ItemStack(Material.AIR, 1))
                        }
                        else {
                            it.setItemInOffHand(ItemStack(Material.ENDER_PEARL, it.itemInOffHand.amount - 1))
                        }
                    }
                }
            }
            // Remove pearl
            event.entity.remove()
        }
    }


    // Temp main hand make for all later
    @EventHandler
    fun hookShotEnchantment(event: ProjectileHitEvent) {
        if (event.entity is FishHook) {
            val someHook = event.entity as FishHook
            if (someHook.shooter is LivingEntity) {
                val someThrower = someHook.shooter as LivingEntity
                if (someThrower.equipment != null) {
                    if (someThrower.equipment!!.itemInMainHand.hasItemMeta()) {
                        val someFishingRod = someThrower.equipment!!.itemInMainHand
                        if (someFishingRod.itemMeta.hasEnchant(OdysseyEnchantments.HOOK_SHOT)) {
                            val hookFactor = someFishingRod.itemMeta.getEnchantLevel(OdysseyEnchantments.HOOK_SHOT)
                            if (event.hitBlock != null) {
                                val someBlock = event.hitBlock
                                val someInvisibleStand: ArmorStand = someThrower.world.spawnEntity(someHook.location, EntityType.ARMOR_STAND) as ArmorStand
                                someInvisibleStand.isVisible = false
                                someInvisibleStand.setCanMove(false)
                                someInvisibleStand.isMarker = true
                                someHook.hookedEntity = someInvisibleStand
                                someHook.setGravity(false)
                                println("P")
                            }
                        }
                    }
                }
            }
        }
    }


}
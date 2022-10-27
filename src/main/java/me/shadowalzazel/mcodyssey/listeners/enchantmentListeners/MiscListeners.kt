package me.shadowalzazel.mcodyssey.listeners.enchantmentListeners

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.GameMode
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

    private var playersWarpDriveCooldown = mutableMapOf<UUID, Long>()

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


    // WARP_DRIVE enchantment effects
    @EventHandler
    fun warpDriveEnchantment(event: ProjectileLaunchEvent) {
        if (event.entity is EnderPearl) {
            if (event.entity.shooter is Player) {
                val somePlayer = event.entity.shooter as Player
                var removePearl = false
                if (somePlayer.equipment.chestplate != null) {
                    if (somePlayer.equipment.chestplate.hasItemMeta()) {
                        val someElytra = somePlayer.equipment.chestplate
                        if (someElytra.itemMeta.hasEnchant(OdysseyEnchantments.WARP_JUMP)) {
                            val someSpeed = somePlayer.velocity.clone().length()
                            if (!somePlayer.isDead && someSpeed > 0.125) {
                                if (somePlayer.gameMode != GameMode.SPECTATOR) {
                                    val warpFactor = someElytra.itemMeta.getEnchantLevel(OdysseyEnchantments.WARP_JUMP)
                                    if (!playersWarpDriveCooldown.containsKey(somePlayer.uniqueId)) {
                                        playersWarpDriveCooldown[somePlayer.uniqueId] = 0L
                                    }
                                    val timeElapsed: Long = System.currentTimeMillis() - playersWarpDriveCooldown[somePlayer.uniqueId]!!
                                    if (timeElapsed >= 1.0 * 1000) {
                                        // Timer
                                        playersWarpDriveCooldown[somePlayer.uniqueId] = System.currentTimeMillis()
                                        // Vector Math
                                        var someVector = somePlayer.velocity.clone()
                                        val unitVector = someVector.clone().normalize()
                                        someVector = unitVector.clone().multiply((warpFactor * 5.0) + 5)
                                        val someWarpLocation = somePlayer.location.clone()
                                        someWarpLocation.add(someVector)
                                        // Effects and Teleport
                                        somePlayer.world.spawnParticle(Particle.FLASH, somePlayer.location, 15, 0.0, 0.0, 0.0)
                                        somePlayer.world.spawnParticle(Particle.SONIC_BOOM, somePlayer.location, 5, 0.0, 0.0, 0.0)
                                        somePlayer.world.spawnParticle(Particle.PORTAL, somePlayer.location, 85, 1.5, 1.5, 1.5)
                                        somePlayer.playSound(somePlayer.location, Sound.BLOCK_BEACON_DEACTIVATE, 2.5F, 2.5F)
                                        somePlayer.teleport(someWarpLocation)
                                        somePlayer.world.spawnParticle(Particle.FLASH, somePlayer.location, 15, 0.0, 0.0, 0.0)
                                        somePlayer.world.spawnParticle(Particle.SONIC_BOOM, somePlayer.location, 5, 0.0, 0.0, 0.0)
                                        somePlayer.world.spawnParticle(Particle.PORTAL, somePlayer.location, 85, 1.5, 1.5, 1.5)
                                        somePlayer.playSound(somePlayer.location, Sound.BLOCK_BEACON_DEACTIVATE, 2.5F, 2.5F)
                                        // Post Teleport
                                        somePlayer.velocity = unitVector.clone().multiply(1)
                                        println(someVector)
                                        // Remove Pearl
                                        removePearl = true
                                        println("X")
                                    }
                                }
                            }
                        }
                    }
                }
                if (removePearl)  {
                    event.entity.remove()
                    if (somePlayer.inventory.itemInMainHand.type == Material.ENDER_PEARL) {
                        val someItemStack = somePlayer.inventory.itemInMainHand
                        val someAmount = someItemStack.amount
                        if (someAmount == 1) {
                            somePlayer.inventory.setItemInMainHand(ItemStack(Material.AIR, 1))
                        }
                        else {
                            somePlayer.inventory.setItemInMainHand(ItemStack(Material.ENDER_PEARL, someAmount - 1))
                        }
                    }
                    else if (somePlayer.inventory.itemInOffHand.type == Material.ENDER_PEARL) {
                        val someItemStack = somePlayer.inventory.itemInOffHand
                        val someAmount = someItemStack.amount
                        if (someAmount == 1) {
                            somePlayer.inventory.setItemInOffHand(ItemStack(Material.AIR, 1))
                        }
                        else {
                            somePlayer.inventory.setItemInOffHand(ItemStack(Material.ENDER_PEARL, someAmount - 1))
                        }
                    }
                }
            }
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
package me.shadowalzazel.mcodyssey.common.listeners.enchantment_listeners

import me.shadowalzazel.mcodyssey.util.AttackHelper
import me.shadowalzazel.mcodyssey.util.EnchantmentsManager
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.inventory.ItemStack

object OtherListeners : Listener, EnchantmentsManager, AttackHelper {

    /*-----------------------------------------------------------------------------------------------*/
    private fun getActiveRod(player: Player): ItemStack? {
        val mainHand = if (player.inventory.itemInMainHand.type == Material.FISHING_ROD) {
            player.inventory.itemInMainHand
        }
        else {
            player.inventory.itemInOffHand
        }
        return if (mainHand != player.activeItem) {
            null
        } else {
            mainHand
        }
    }


    /*-----------------------------------------------------------------------------------------------*/
    @EventHandler
    fun fishingHandler(event: PlayerFishEvent) {
        when(event.state) {
            PlayerFishEvent.State.CAUGHT_FISH -> {
                caughtFishHandler(event)
            }
            PlayerFishEvent.State.CAUGHT_ENTITY -> {
                caughtEntityHandler(event)
            }
            PlayerFishEvent.State.FISHING -> {
                castLineHandler(event)
            }
            else -> {
            }
        }
    }

    private fun caughtEntityHandler(event: PlayerFishEvent) {
        if (event.caught == null) return
        if (event.caught !is LivingEntity) return
        val caught = event.caught as LivingEntity
        val player = event.player
        val rod = getActiveRod(event.player) ?: return
        for (enchant in rod.enchantments) {
            when (enchant.key.getNameId()) {
                "bomb_ob" -> {
                    bombObEnchantment(caught, enchant.value)
                }
                "yank" -> {
                    yankEnchantment(caught, enchant.value)
                }
                "hook_shot" -> {
                    hookShotEnchantment(player, caught, enchant.value)
                }
            }
        }
    }

    private fun caughtFishHandler(event: PlayerFishEvent) {
        if (event.caught == null) { return }
        if (event.caught!!.type != EntityType.ITEM) { return }
        // Get main rod
        val rod = getActiveRod(event.player) ?: return
        for (enchant in rod.enchantments) {
            when (enchant.key.getNameId()) {
                "scourer" -> {
                    val item = event.caught as Item
                    // Pulls to not override
                    val goodPulls = listOf(
                        Material.COD, Material.SALMON, Material.TROPICAL_FISH, Material.PUFFERFISH,
                        Material.ENCHANTED_BOOK, Material.NAME_TAG, Material.NAUTILUS_SHELL, Material.SADDLE)
                    // Gems
                    val gems = listOf(
                        ItemStack(Material.EMERALD, (1..(enchant.value)).random()),
                        ItemStack(Material.DIAMOND, (1..(enchant.value)).random()),
                    )
                    //item.itemStack = gems.random()
                    if (item.itemStack.type !in goodPulls) {
                        item.itemStack = gems.random()
                    }
                }
                "wisdom_of_the_deep" -> {
                    event.expToDrop *= (0.75 + (0.75 * enchant.value)).toInt()
                }
            }
        }
    }

    private fun castLineHandler(event: PlayerFishEvent) {
        val rod = getActiveRod(event.player) ?: return
        for (enchant in rod.enchantments) {
            when (enchant.key.getNameId()) {
                "lengthy_line" -> {
                    event.hook.velocity = event.hook.velocity.multiply(1 + (0.5 * enchant.value))
                }
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun hookShotEnchantment(player: Player, entity: LivingEntity, level: Int) {
        val distance = player.location.distance(entity.location)
        val speed = distance * (0.1 * level)
        val destination = entity.eyeLocation.clone()
        val unitVector = destination.subtract(player.location).toVector().normalize()
        val velocity = unitVector.multiply(speed)
        player.velocity = velocity
    }

    private fun bombObEnchantment(victim: LivingEntity, level: Int) {
        val location = victim.location
        explosionHandler(location.getNearbyLivingEntities(level * 0.75), location, level * 0.75)
    }

    private fun yankEnchantment(victim: LivingEntity, level: Int) {
        victim.velocity = victim.velocity.multiply(1 + (0.4 * level))
    }

    /*-----------------------------------------------------------------------------------------------*/

    @EventHandler
    fun voidJumpHandler(event: ProjectileLaunchEvent) {
        if (event.entity !is EnderPearl) return
        if (event.entity.shooter !is Player) return
        val player = event.entity.shooter!! as Player
        if (player.equipment.chestplate.type != Material.ELYTRA) return
        val elytra = player.equipment.chestplate
        if (!elytra.hasItemMeta()) return
        if (!elytra.hasEnchantment("void_jump")) return
        val activeItem = player.activeItem
        if (activeItem.type != Material.ENDER_PEARL) return
        if (player.getCooldown(activeItem.type) > 0) return
        if (player.velocity.length() < 2.5) return
        if (!player.isFlying) return
        // Passed all checks
        val jumpLevel = elytra.enchantments[getNamedEnchantment("void_jump")] ?: return
        // Math and TP
        val unitVector = player.eyeLocation.direction.clone().normalize()
        val jumpLocation = player.location.clone().add(unitVector.clone().multiply((jumpLevel * 5.0) + 5))
        voidJumpParticles(player.location)
        player.teleport(jumpLocation)
        voidJumpParticles(jumpLocation)
        // Cooldown
        player.setCooldown(activeItem.type, 4 * 20)
        activeItem.subtract()
        event.entity.remove()
    }

    private fun voidJumpParticles(location: Location) {
        with(location.world) {
            spawnParticle(Particle.SONIC_BOOM, location, 5, 0.0, 0.0, 0.0)
            spawnParticle(Particle.PORTAL, location, 35, 1.5, 1.5, 1.5)
            playSound(location, Sound.BLOCK_BEACON_DEACTIVATE, 2.5F, 2.5F)
        }
    }

    /*-----------------------------------------------------------------------------------------------*/

    // MIRROR_FORCE enchantment effects
    /*
    @EventHandler
    fun mirrorForceEnchantment(event: ProjectileHitEvent) {
        if (event.hitEntity != null) {
            if (event.hitEntity is LivingEntity) {
                val blockingEntity = event.hitEntity as LivingEntity
                if (blockingEntity.equipment != null ) {
                    if (blockingEntity.equipment!!.itemInOffHand.hasItemMeta() ) {
                        val someShield = blockingEntity.equipment!!.itemInOffHand
                        if (someShield.itemMeta.hasEnchant(OdysseyEnchantments.MIRROR_FORCE.toBukkit())) {
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

     */

}
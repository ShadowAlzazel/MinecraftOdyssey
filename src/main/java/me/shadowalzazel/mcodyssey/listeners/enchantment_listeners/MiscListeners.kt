package me.shadowalzazel.mcodyssey.listeners.enchantment_listeners

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.items.Ingredients
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.*

object MiscListeners : Listener {

    private var voidJumpCooldown = mutableMapOf<UUID, Long>()

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    @EventHandler
    fun fishingHandler(event: PlayerFishEvent) {
        when(event.state) {
            PlayerFishEvent.State.CAUGHT_FISH -> {
                caughtFish(event)
            }
            PlayerFishEvent.State.CAUGHT_ENTITY -> {
                caughtEntity(event)
            }
            PlayerFishEvent.State.FISHING -> {
                castLine(event)
            }
            else -> {

            }
        }

    }

    private fun caughtEntity(event: PlayerFishEvent) {
        if (event.caught == null) { return }
        val rod = if (event.player.inventory.itemInMainHand.type == Material.FISHING_ROD) {
            event.player.inventory.itemInMainHand
        }
        else {
            event.player.inventory.itemInOffHand
        }
        for (enchant in rod.enchantments) {
            // Continue if not OdysseyEnchant
            val gildedEnchant = MeleeListeners.findOdysseyEnchant(enchant.key) ?: continue
            // When match
            when (gildedEnchant) {
                OdysseyEnchantments.BOMB_OB -> {
                    if (event.caught!! is LivingEntity) {
                        bombObEnchantment(event.caught!! as LivingEntity, enchant.value)
                    }
                }
                OdysseyEnchantments.YANK -> {
                    if (event.caught!! is LivingEntity) {
                        yankEnchantment(event.caught!! as LivingEntity, enchant.value)
                    }
                }
            }
        }
    }

    private fun caughtFish(event: PlayerFishEvent) {
        if (event.caught == null) { return }
        if (event.caught!!.type != EntityType.DROPPED_ITEM) { return }
        //
        val rod = if (event.player.inventory.itemInMainHand.type == Material.FISHING_ROD) {
            event.player.inventory.itemInMainHand
        }
        else {
            event.player.inventory.itemInOffHand
        }
        //
        for (enchant in rod.enchantments) {
            // Continue if not OdysseyEnchant
            val gildedEnchant = MeleeListeners.findOdysseyEnchant(enchant.key) ?: continue
            // When match
            when (gildedEnchant) {
                OdysseyEnchantments.O_SHINY -> {
                    val item = event.caught as Item

                    val goodPulls = listOf(
                        Material.COD, Material.SALMON, Material.TROPICAL_FISH, Material.PUFFERFISH,
                        Material.ENCHANTED_BOOK, Material.NAME_TAG, Material.NAUTILUS_SHELL, Material.SADDLE)

                    val gems = listOf(
                        ItemStack(Material.EMERALD, (1..(enchant.value)).random()),
                        ItemStack(Material.DIAMOND, (1..(enchant.value)).random()),
                        Ingredients.JADE.createItemStack((1..(enchant.value)).random()),
                        Ingredients.RUBY.createItemStack((1..(enchant.value)).random()),
                        Ingredients.KUNZITE.createItemStack((1..(enchant.value)).random()),
                        Ingredients.ALEXANDRITE.createItemStack((1..(enchant.value)).random()),
                    )

                    //item.itemStack = gems.random()
                    if (item.itemStack.type !in goodPulls) {
                        item.itemStack = gems.random()
                    }

                }
                OdysseyEnchantments.WISE_BAIT -> {
                    event.expToDrop *= (1 + (0.5 * enchant.value)).toInt()
                }
            }
        }
    }

    private fun castLine(event: PlayerFishEvent) {
        val rod = if (event.player.inventory.itemInMainHand.type == Material.FISHING_ROD) {
            event.player.inventory.itemInMainHand
        }
        else {
            event.player.inventory.itemInOffHand
        }
        for (enchant in rod.enchantments) {
            // Continue if not OdysseyEnchant
            val gildedEnchant = MeleeListeners.findOdysseyEnchant(enchant.key) ?: continue
            // When match
            when (gildedEnchant) {
                OdysseyEnchantments.LENGTHY_LINE -> {
                    println(event.hook.velocity)
                    event.hook.velocity = event.hook.velocity.multiply(1 + (0.5 * enchant.value))
                    event.hook.velocity
                }
            }
        }
    }

    private fun bombObEnchantment(victim: LivingEntity, enchantmentStrength: Int) {
        val randomColors = listOf(Color.GREEN, Color.LIME, Color.OLIVE, Color.AQUA, Color.BLUE)
        with(victim.world) {
            // Particles
            spawnParticle(Particle.FLASH, victim.location, 2, 0.2, 0.2, 0.2)
            spawnParticle(Particle.SCRAPE, victim.location, 25, 1.5, 1.0, 1.5)
            // Fireball
            (spawnEntity(victim.location, EntityType.FIREBALL) as Fireball).also {
                it.setIsIncendiary(false)
                it.yield = 0.0F
                it.direction = Vector(0.0, -3.0, 0.0)
            }
            // Firework
            (spawnEntity(victim.location, EntityType.FIREWORK) as Firework).also {
                val newMeta = it.fireworkMeta
                newMeta.power = enchantmentStrength * 30
                newMeta.addEffect(
                    FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(randomColors.random())
                        .withFade(randomColors.random()).trail(true).flicker(true).build()
                )
                it.fireworkMeta = newMeta
                it.velocity = Vector(0.0, -3.0, 0.0)
                it.ticksToDetonate = 1
            }
        }
    }

    private fun yankEnchantment(victim: LivingEntity, enchantmentStrength: Int) {
        println(victim.velocity)
        victim.velocity = victim.velocity.multiply(1 + (0.35 * enchantmentStrength))
        println(victim.velocity)
    }


    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/


    // MIRROR_FORCE enchantment effects
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

    // ---------------------------------------------- VOID_JUMP -------------------------------------------

    private fun voidJumpConditionsMet(player: Player) : Boolean {
        // Check if player has chestplate and not in spectator
        if (player.equipment.chestplate != null && player.equipment.chestplate.hasItemMeta() && player.gameMode != GameMode.SPECTATOR) {
            val elytra = player.equipment.chestplate
            // Check if player has enchantment
            if (elytra.itemMeta.hasEnchant(OdysseyEnchantments.VOID_JUMP.toBukkit())) {
                // Check Speed
                val speed = player.velocity.clone().length()
                if (speed > 0.125) {
                    if (player.gameMode != GameMode.SPECTATOR && !player.isDead) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun voidJumpParticles(location: Location) {
        with(location.world) {
            spawnParticle(Particle.FLASH, location, 15, 0.0, 0.0, 0.0)
            spawnParticle(Particle.SONIC_BOOM, location, 5, 0.0, 0.0, 0.0)
            spawnParticle(Particle.PORTAL, location, 85, 1.5, 1.5, 1.5)
            playSound(location, Sound.BLOCK_BEACON_DEACTIVATE, 2.5F, 2.5F)
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
            with(event.entity.shooter as Player) {
                // Cooldown
                if (!voidJumpCooldown.containsKey(uniqueId)) { voidJumpCooldown[uniqueId] = 0L }
                val timeElapsed: Long = System.currentTimeMillis() - voidJumpCooldown[uniqueId]!!
                // Time sentry
                if (timeElapsed < 1.0 * 1000) {
                    return@voidJumpHandler
                }
                voidJumpCooldown[uniqueId] = System.currentTimeMillis()

                // Get vector
                val enchantmentLevel = equipment.chestplate.getEnchantmentLevel(OdysseyEnchantments.VOID_JUMP.toBukkit())
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
                        if (someFishingRod.itemMeta.hasEnchant(OdysseyEnchantments.HOOK_SHOT.toBukkit())) {
                            val hookFactor = someFishingRod.itemMeta.getEnchantLevel(OdysseyEnchantments.HOOK_SHOT.toBukkit())
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
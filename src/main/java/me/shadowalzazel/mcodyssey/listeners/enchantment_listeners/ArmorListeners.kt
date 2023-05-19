package me.shadowalzazel.mcodyssey.listeners.enchantment_listeners

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.listeners.tasks.SpeedySpursTask
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

object ArmorListeners : Listener {

    // Internal Cool downs
    private var fruitfulFareCooldown = mutableMapOf<UUID, Long>()
    private var potionBarrierCooldown = mutableMapOf<UUID, Long>()

    // Main function for enchantments relating to entity damage for armor
    @EventHandler
    fun mainArmorDamageHandler(event: EntityDamageByEntityEvent) {
        if (event.damager !is LivingEntity) {
            return
        }
        if (event.entity !is LivingEntity) {
            return
        }
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return
        }

        // Make thorns bug new enchant apply ranged effects
        val attacker = event.damager as LivingEntity
        val defender = event.entity as LivingEntity
        // --------------------------------------------------------------
        // Check if helmet item has lore
        if (defender.equipment?.helmet?.hasItemMeta() == true) {
            val helmet = defender.equipment?.helmet
            // Loop for all enchants
            for (enchant in helmet!!.enchantments) {
                // Check when
                when (enchant.key) {

                }
            }
        }
        // --------------------------------------------------------------
        // Check if chestplate item has lore
        if (defender.equipment?.chestplate?.hasItemMeta() == true) {
            val chestplate = defender.equipment?.chestplate
            // Loop for all enchants
            for (enchant in chestplate!!.enchantments) {
                // Check when
                when (enchant.key) {
                    OdysseyEnchantments.VENGEFUL -> {
                        vengefulEnchantment(attacker, enchant.value)
                    }
                    OdysseyEnchantments.BEASTLY_BRAWLER -> {
                        beastlyBrawlerEnchantment(defender, enchant.value)
                    }
                }
            }
        }
        // --------------------------------------------------------------
        // Check if legging item has lore
        if (defender.equipment?.leggings?.hasItemMeta() == true) {
            val leggings = defender.equipment?.leggings
            // Loop for all enchants
            for (enchant in leggings!!.enchantments) {
                // Check when
                when (enchant.key) {
                    OdysseyEnchantments.COWARDICE -> {
                        cowardiceEnchantment(attacker, defender, enchant.value)
                    }
                    OdysseyEnchantments.SPOREFUL -> {
                        sporefulEnchantment(defender, enchant.value)
                    }
                    OdysseyEnchantments.SQUIDIFY -> {
                        squidifyEnchantment(defender, enchant.value)
                    }
                }
            }
        }
        // --------------------------------------------------------------
        // Check if boot item has lore
        if (defender.equipment?.boots?.hasItemMeta() == true) {
            val boots = defender.equipment?.boots
            // Loop for all enchants
            for (enchant in boots!!.enchantments) {
                // Check when
                when (enchant.key) {

                }
            }
        }
    }

    // Main function for enchantments relating to consuming items
    @EventHandler
    fun mainArmorConsumingHandler(event: PlayerItemConsumeEvent) {
        val player = event.player
        // --------------------------------------------------------------
        // Check if chestplate item has lore
        if (player.equipment.chestplate?.hasItemMeta() == true) {
            val chestplate = player.equipment.chestplate
            // Loop for all enchants
            for (enchant in chestplate!!.enchantments) {
                // Check when
                when (enchant.key) {
                    OdysseyEnchantments.FRUITFUL_FARE -> {
                        if (!fruitfulFareCooldown.containsKey(player.uniqueId)) {
                            fruitfulFareCooldown[player.uniqueId] = 0L
                        }
                        val timeElapsed: Long =
                            System.currentTimeMillis() - fruitfulFareCooldown[player.uniqueId]!!
                        if (timeElapsed > 8 * 1000) {
                            fruitfulFareCooldown[player.uniqueId] = System.currentTimeMillis()
                            fruitfulFareEnchantment(player, event.item, enchant.value)
                        }
                    }
                    // Add if eat rotten or raw heal + other
                    OdysseyEnchantments.POTION_BARRIER -> {
                        if (!potionBarrierCooldown.containsKey(player.uniqueId)) {
                            potionBarrierCooldown[player.uniqueId] = 0L
                        }
                        val timeElapsed: Long =
                            System.currentTimeMillis() - potionBarrierCooldown[player.uniqueId]!!
                        if (timeElapsed > 12 * 1000) {
                            potionBarrierCooldown[player.uniqueId] = System.currentTimeMillis()
                            potionBarrierEnchantment(player, event.item, enchant.value)
                        }
                    }

                }
            }
        }

    }

    @EventHandler
    fun mainArmorHitHandler(event: EntityDamageEvent) {
        if (event.cause == EntityDamageEvent.DamageCause.FALL) {
            val defender = event.entity as LivingEntity
            // --------------------------------------------------------------
            // Check if boot item has lore
            if (defender.equipment?.boots?.hasItemMeta() == true) {
                val someBoots = defender.equipment?.boots
                // Loop for all enchants
                for (enchant in someBoots!!.enchantments) {
                    // Check when
                    when (enchant.key) {
                        OdysseyEnchantments.DEVASTATING_DROP -> {
                            devastatingDrop(defender, event.damage, enchant.value)
                        }
                    }
                }
            }
        }

        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.cause == EntityDamageEvent.DamageCause.PROJECTILE) {
            // DODGE AN ATTACK WITH UNTOUCHABLE
            // If CAN SEE AND WITHIN 3 BLOCKS DODGE MELLE
            // IF LINE OF SIGHT projectile
            // DODGE
            // COOLDOWN


            // IF SOMETHING
            // IDK


        }

    }


    // Some function regarding vehicles and armor
    @EventHandler
    fun miscArmorVehicleHandler(event: VehicleEnterEvent) { // Move Horse Armor Enchant Here
        if (event.entered is LivingEntity && event.vehicle is LivingEntity) {
            val rider = event.entered as LivingEntity
            val mount = event.vehicle as LivingEntity
            // --------------------------------------------------------------
            // Check if boot item has lore
            if (rider.equipment?.boots?.hasItemMeta() == true) {
                val boots = rider.equipment?.boots
                // Loop for all enchants
                for (enchant in boots!!.enchantments) {
                    // Check when
                    when (enchant.key) {
                        OdysseyEnchantments.SPEEDY_SPURS -> {
                            speedySpursEnchantment(rider, mount, enchant.value)
                        }
                    }
                }
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    // ------------------------------- BEASTLY_BRAWLER ------------------------------------
    // TODO: Add Temp Attribute Attack Damage instead of Potion
    private fun beastlyBrawlerEnchantment(
        defender: LivingEntity,
        enchantmentStrength: Int
    ) {
        if (defender.location.getNearbyLivingEntities(4.0).size > 5) {
            defender.addPotionEffect(
                PotionEffect(
                    PotionEffectType.INCREASE_DAMAGE,
                    6 * 20,
                    enchantmentStrength - 1
                )
            )
        }
    }

    // ------------------------------- COWARDICE ------------------------------------
    private fun cowardiceEnchantment(
        attacker: LivingEntity,
        defender: LivingEntity,
        enchantmentStrength: Int
    ) {
        defender.addPotionEffect(
            PotionEffect(
                PotionEffectType.SPEED,
                6 * 20,
                enchantmentStrength
            )
        )
        // Movement Math
        if (attacker.location.distance(defender.location) <= 5.0) {
            defender.location.clone().subtract(attacker.location).toVector().normalize().multiply(1.6)
        }
    }

    // ------------------------------- DEVASTATING_DROP ------------------------------------
    private fun devastatingDrop(
        dropper: LivingEntity,
        receivedDamage: Double,
        enchantmentStrength: Int
    ) {
        dropper.location.getNearbyLivingEntities(4.0).forEach {
            if (it != dropper) {
                it.damage(receivedDamage * (0.4 * enchantmentStrength))
            }
        }
    }

    // ------------------------------- FRUITFUL_FARE ------------------------------------
    private fun fruitfulFareEnchantment(
        player: Player,
        food: ItemStack,
        enchantmentStrength: Int
    ) {
        // list of materials that can not be consumed
        val notFood = listOf(
            Material.POTION,
            Material.ROTTEN_FLESH,
            Material.POISONOUS_POTATO,
            Material.SPIDER_EYE,
            Material.PUFFERFISH
        )
        if (food.type !in notFood) { // Currently non-forgiving
            // Check Health
            val currentHealth = player.health
            if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value < currentHealth + (1 + enchantmentStrength)) {
                player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
            } else {
                player.health += (1 + enchantmentStrength)
            }
            // Particles
            with(player.world) {
                spawnParticle(Particle.HEART, player.location, 35, 0.5, 0.5, 0.5)
                spawnParticle(Particle.VILLAGER_HAPPY, player.location, 35, 0.5, 0.5, 0.5)
                spawnParticle(Particle.COMPOSTER, player.location, 35, 0.5, 0.5, 0.5)
                playSound(player.location, Sound.ENTITY_STRIDER_HAPPY, 1.5F, 0.5F)
                playSound(player.location, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1.5F, 0.5F)
                playSound(player.location, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.5F, 0.8F)
            }
        }
    }

    // ------------------------------- POTION_BARRIER ------------------------------------
    private fun potionBarrierEnchantment(
        player: Player,
        potion: ItemStack,
        enchantmentStrength: Int
    ) {
        // Check if potion
        if (potion.type != Material.POTION) {
            return
        }
        if (!potion.itemMeta.hasCustomModelData() || potion.itemMeta.customModelData == ItemModels.SQUARE_BOTTLE) {
            // Effects
            player.addPotionEffect(
                PotionEffect(
                    PotionEffectType.DAMAGE_RESISTANCE,
                    ((enchantmentStrength * 2) + 2) * 20,
                    1
                )
            )
            // Particles
            with(player.world) {
                spawnParticle(Particle.SCRAPE, player.location, 35, 0.5, 0.5, 0.5)
                spawnParticle(Particle.ELECTRIC_SPARK, player.location, 35, 0.5, 0.5, 0.5)
                spawnParticle(Particle.COMPOSTER, player.location, 35, 0.5, 0.5, 0.5)
                playSound(player.location, Sound.ITEM_SHIELD_BLOCK, 1.5F, 0.5F)
                playSound(player.location, Sound.BLOCK_DEEPSLATE_BREAK, 1.5F, 0.5F)
                playSound(player.location, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.5F, 0.8F)
            }
        }
    }

    // ------------------------------- SPEEDY_SPURS ------------------------------------
    private fun speedySpursEnchantment(rider: LivingEntity, mount: LivingEntity, enchantmentStrength: Int) {
        val someSpeedySpursTask = SpeedySpursTask(rider, mount, enchantmentStrength)
        someSpeedySpursTask.runTaskTimer(Odyssey.instance, 0, 10 * 20)
    }

    // ------------------------------- SPOREFUL ------------------------------------
    private fun sporefulEnchantment(defender: LivingEntity, enchantmentStrength: Int) {
        // List effects
        defender.world.getNearbyLivingEntities(defender.location, enchantmentStrength.toDouble() * 0.75)
            .forEach {
                if (it != defender) {
                    it.addPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.POISON, ((enchantmentStrength * 2) + 2) * 20, 0),
                            PotionEffect(PotionEffectType.CONFUSION, ((enchantmentStrength * 2) + 2) * 20, 1),
                            PotionEffect(PotionEffectType.SLOW, 20, 0)
                        )
                    )
                }
            }
        // TODO: Make purple-ish
        // Particles
        with(defender.world) {
            spawnParticle(Particle.PORTAL, defender.location, 65, 0.5, 0.5, 0.5)
            spawnParticle(Particle.GLOW, defender.location, 45, 0.5, 0.5, 0.5)
            spawnParticle(Particle.SCRAPE, defender.location, 65, 0.15, 0.15, 0.15)
            spawnParticle(Particle.FALLING_SPORE_BLOSSOM, defender.location, 85, 1.0, 0.5, 1.0)
        }
    }

    // ------------------------------- SQUIDIFY ------------------------------------
    private fun squidifyEnchantment(defender: LivingEntity, enchantmentStrength: Int) {
        defender.world.getNearbyLivingEntities(defender.location, enchantmentStrength.toDouble() * 0.75)
            .forEach {
                if (it != defender) {
                    it.addPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.BLINDNESS, (enchantmentStrength * 2) + 2, 1),
                            PotionEffect(PotionEffectType.SLOW, enchantmentStrength * 20, 2)
                        )
                    )
                }
            }
        // Particles
        with(defender.world) {
            spawnParticle(Particle.ASH, defender.location, 95, 1.5, 0.5, 1.5)
            spawnParticle(Particle.SPELL_MOB_AMBIENT, defender.location, 55, 0.75, 0.5, 0.75)
            spawnParticle(Particle.SQUID_INK, defender.location, 85, 0.75, 0.5, 0.75)
            spawnParticle(Particle.SMOKE_LARGE, defender.location, 85, 1.0, 0.5, 1.0)
        }
    }

    // ------------------------------- VENGEFUL ------------------------------------
    private fun vengefulEnchantment(attacker: LivingEntity, enchantmentStrength: Int) {
        attacker.addScoreboardTag(EntityTags.MARKED_FOR_VENGEANCE)
        attacker.addScoreboardTag(EntityTags.VENGEFUL_MODIFIER + enchantmentStrength)
        // Add Particles Timer?
    }


}
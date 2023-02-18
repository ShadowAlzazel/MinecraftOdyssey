package me.shadowalzazel.mcodyssey.listeners.enchantment_listeners

import me.shadowalzazel.mcodyssey.Odyssey
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
        // Check if event damager and defender is living entity
        if (event.damager is LivingEntity && event.entity is LivingEntity && event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) { // Make thorns bug new enchant apply ranged effects
            val someDamager = event.damager as LivingEntity
            val someDefender = event.entity as LivingEntity
            // --------------------------------------------------------------
            // Check if helmet item has lore
            if (someDefender.equipment?.helmet?.hasItemMeta() == true) {
                val someHelmet = someDefender.equipment?.helmet
                // Loop for all enchants
                for (enchant in someHelmet!!.enchantments) {
                    // Check when
                    when (enchant.key) {

                    }
                }
            }
            // --------------------------------------------------------------
            // Check if chestplate item has lore
            if (someDefender.equipment?.chestplate?.hasItemMeta() == true) {
                val someChestplate = someDefender.equipment?.chestplate
                // Loop for all enchants
                for (enchant in someChestplate!!.enchantments) {
                    // Check when
                    when (enchant.key) {
                        OdysseyEnchantments.VENGEFUL -> {
                            vengefulEnchantment(someDamager, enchant.value)
                        }
                        OdysseyEnchantments.BEASTLY_BRAWLER -> {
                            beastlyBrawlerEnchantment(someDefender, enchant.value)
                        }
                    }
                }
            }
            // --------------------------------------------------------------
            // Check if legging item has lore
            if (someDefender.equipment?.leggings?.hasItemMeta() == true) {
                val someLeggings = someDefender.equipment?.leggings
                // Loop for all enchants
                for (enchant in someLeggings!!.enchantments) {
                    // Check when
                    when (enchant.key) {
                        OdysseyEnchantments.COWARDICE -> {
                            cowardiceEnchantment(someDamager, someDefender, enchant.value)
                        }
                        OdysseyEnchantments.SPOREFUL -> {
                            sporefulEnchantment(someDefender, enchant.value)
                        }
                        OdysseyEnchantments.SQUIDIFY -> {
                            squidifyEnchantment(someDefender, enchant.value)
                        }
                    }
                }
            }
            // --------------------------------------------------------------
            // Check if boot item has lore
            if (someDefender.equipment?.boots?.hasItemMeta() == true) {
                val someBoots = someDefender.equipment?.boots
                // Loop for all enchants
                for (enchant in someBoots!!.enchantments) {
                    // Check when
                    when (enchant.key) {

                    }
                }
            }
        }
    }

    // Main function for enchantments relating to consuming items
    @EventHandler
    fun mainArmorConsumingHandler(event: PlayerItemConsumeEvent) {
        val someConsumer = event.player
        // --------------------------------------------------------------
        // Check if chestplate item has lore
        if (someConsumer.equipment.chestplate?.hasItemMeta() == true) {
            val someChestplate = someConsumer.equipment.chestplate
            // Loop for all enchants
            for (enchant in someChestplate!!.enchantments) {
                // Check when
                when (enchant.key) {
                    OdysseyEnchantments.FRUITFUL_FARE -> {
                        if (!fruitfulFareCooldown.containsKey(someConsumer.uniqueId)) {
                            fruitfulFareCooldown[someConsumer.uniqueId] = 0L
                        }
                        val timeElapsed: Long =
                            System.currentTimeMillis() - fruitfulFareCooldown[someConsumer.uniqueId]!!
                        if (timeElapsed > 8 * 1000) {
                            fruitfulFareCooldown[someConsumer.uniqueId] = System.currentTimeMillis()
                            fruitfulFareEnchantment(someConsumer, event.item, enchant.value)
                        }
                    }
                    // Add if eat rotten or raw heal + other
                    OdysseyEnchantments.POTION_BARRIER -> {
                        if (!potionBarrierCooldown.containsKey(someConsumer.uniqueId)) {
                            potionBarrierCooldown[someConsumer.uniqueId] = 0L
                        }
                        val timeElapsed: Long =
                            System.currentTimeMillis() - potionBarrierCooldown[someConsumer.uniqueId]!!
                        if (timeElapsed > 12 * 1000) {
                            potionBarrierCooldown[someConsumer.uniqueId] = System.currentTimeMillis()
                            potionBarrierEnchantment(someConsumer, event.item, enchant.value)
                        }
                    }

                }
            }
        }

    }

    @EventHandler
    fun mainArmorHitHandler(event: EntityDamageEvent) {
        if (event.cause == EntityDamageEvent.DamageCause.FALL) {
            val someDefender = event.entity as LivingEntity
            // --------------------------------------------------------------
            // Check if boot item has lore
            if (someDefender.equipment?.boots?.hasItemMeta() == true) {
                val someBoots = someDefender.equipment?.boots
                // Loop for all enchants
                for (enchant in someBoots!!.enchantments) {
                    // Check when
                    when (enchant.key) {
                        OdysseyEnchantments.DEVASTATING_DROP -> {
                            devastatingDrop(someDefender, event.damage, enchant.value)
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
            val someRider = event.entered as LivingEntity
            val someMount = event.vehicle as LivingEntity
            // --------------------------------------------------------------
            // Check if boot item has lore
            if (someRider.equipment?.boots?.hasItemMeta() == true) {
                val someBoots = someRider.equipment?.boots
                // Loop for all enchants
                for (enchant in someBoots!!.enchantments) {
                    // Check when
                    when (enchant.key) {
                        OdysseyEnchantments.SPEEDY_SPURS -> {
                            speedySpursEnchantment(someRider, someMount, enchant.value)
                        }
                    }
                }
            }
        }
    }

    /*----------------------------------------------------------------------------------*/

    // BEASTLY_BRAWLER Enchantment Function
    private fun beastlyBrawlerEnchantment(eventDefender: LivingEntity, enchantmentStrength: Int) {
        // Effects
        if (eventDefender.location.getNearbyLivingEntities(4.0).size > 5) {
            eventDefender.addPotionEffect(
                PotionEffect(
                    PotionEffectType.INCREASE_DAMAGE,
                    6 * 20,
                    enchantmentStrength - 1
                )
            )
        }
    }

    // COWARDICE Enchantment Function
    private fun cowardiceEnchantment(
        eventDamager: LivingEntity,
        eventDefender: LivingEntity,
        enchantmentStrength: Int
    ) {
        // Effects
        eventDefender.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 6 * 20, enchantmentStrength))
        // Movement Math
        if (eventDamager.location.distance(eventDefender.location) <= 5.0) {
            eventDefender.location.clone().subtract(eventDamager.location).toVector().normalize().multiply(1.6)
        }
    }


    private fun devastatingDrop(eventDefender: LivingEntity, eventDamage: Double, enchantmentStrength: Int) {
        eventDefender.location.getNearbyLivingEntities(4.0).forEach {
            if (it != eventDefender) {
                it.damage(eventDamage * (0.4 * enchantmentStrength))
            }
        }
    }


    // FRUITFUL_FARE Enchantment Function
    private fun fruitfulFareEnchantment(eventConsumer: Player, eventItem: ItemStack, enchantmentStrength: Int) {
        // list of materials that can not be consumed
        val notFood = listOf(
            Material.POTION,
            Material.ROTTEN_FLESH,
            Material.POISONOUS_POTATO,
            Material.SPIDER_EYE,
            Material.PUFFERFISH
        )
        if (eventItem.type !in notFood) { // Currently non-forgiving
            // Check Health
            val currentHealth = eventConsumer.health
            if (eventConsumer.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value < currentHealth + (1 + enchantmentStrength)) {
                eventConsumer.health = eventConsumer.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
            } else {
                eventConsumer.health += (1 + enchantmentStrength)
            }
            // Particles
            with(eventConsumer.world) {
                spawnParticle(Particle.HEART, eventConsumer.location, 35, 0.5, 0.5, 0.5)
                spawnParticle(Particle.VILLAGER_HAPPY, eventConsumer.location, 35, 0.5, 0.5, 0.5)
                spawnParticle(Particle.COMPOSTER, eventConsumer.location, 35, 0.5, 0.5, 0.5)
                playSound(eventConsumer.location, Sound.ENTITY_STRIDER_HAPPY, 1.5F, 0.5F)
                playSound(eventConsumer.location, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1.5F, 0.5F)
                playSound(eventConsumer.location, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.5F, 0.8F)
            }
        }
    }

    // POTION_BARRIER Enchantment Function
    private fun potionBarrierEnchantment(eventConsumer: Player, eventItem: ItemStack, enchantmentStrength: Int) {
        // Check if potion
        if (eventItem.type == Material.POTION) { // Currently non-forgiving
            // Effects
            eventConsumer.addPotionEffect(
                PotionEffect(
                    PotionEffectType.DAMAGE_RESISTANCE,
                    ((enchantmentStrength * 2) + 2) * 20,
                    1
                )
            )
            // Particles
            with(eventConsumer.world) {
                spawnParticle(Particle.SCRAPE, eventConsumer.location, 35, 0.5, 0.5, 0.5)
                spawnParticle(Particle.ELECTRIC_SPARK, eventConsumer.location, 35, 0.5, 0.5, 0.5)
                spawnParticle(Particle.COMPOSTER, eventConsumer.location, 35, 0.5, 0.5, 0.5)
                playSound(eventConsumer.location, Sound.ITEM_SHIELD_BLOCK, 1.5F, 0.5F)
                playSound(eventConsumer.location, Sound.BLOCK_DEEPSLATE_BREAK, 1.5F, 0.5F)
                playSound(eventConsumer.location, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.5F, 0.8F)
            }
        }
    }

    // TODO: Item if in hand, create aura?
    // Enchantment: Things around get chilling? flame?

    // SPEEDY_SPURS enchantment effects
    private fun speedySpursEnchantment(eventRider: LivingEntity, eventMount: LivingEntity, enchantmentStrength: Int) {
        // Tasks
        val someSpeedySpursTask = SpeedySpursTask(eventRider, eventMount, enchantmentStrength)
        someSpeedySpursTask.runTaskTimer(Odyssey.instance, 0, 10 * 20)
    }

    // SPOREFUL Enchantment Function
    private fun sporefulEnchantment(eventDefender: LivingEntity, enchantmentStrength: Int) {
        // List effects
        eventDefender.world.getNearbyLivingEntities(eventDefender.location, enchantmentStrength.toDouble() * 0.75)
            .forEach {
                if (it != eventDefender) {
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
        with(eventDefender.world) {
            spawnParticle(Particle.PORTAL, eventDefender.location, 65, 0.5, 0.5, 0.5)
            spawnParticle(Particle.GLOW, eventDefender.location, 45, 0.5, 0.5, 0.5)
            spawnParticle(Particle.SCRAPE, eventDefender.location, 65, 0.15, 0.15, 0.15)
            spawnParticle(Particle.FALLING_SPORE_BLOSSOM, eventDefender.location, 85, 1.0, 0.5, 1.0)
        }
    }

    // SQUIDIFY Enchantment Function
    private fun squidifyEnchantment(eventDefender: LivingEntity, enchantmentStrength: Int) {
        // List effects
        eventDefender.world.getNearbyLivingEntities(eventDefender.location, enchantmentStrength.toDouble() * 0.75)
            .forEach {
                if (it != eventDefender) {
                    it.addPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.BLINDNESS, (enchantmentStrength * 2) + 2, 1),
                            PotionEffect(PotionEffectType.SLOW, enchantmentStrength * 20, 2)
                        )
                    )
                }
            }

        // Particles
        with(eventDefender.world) {
            spawnParticle(Particle.ASH, eventDefender.location, 95, 1.5, 0.5, 1.5)
            spawnParticle(Particle.SPELL_MOB_AMBIENT, eventDefender.location, 55, 0.75, 0.5, 0.75)
            spawnParticle(Particle.SQUID_INK, eventDefender.location, 85, 0.75, 0.5, 0.75)
            spawnParticle(Particle.SMOKE_LARGE, eventDefender.location, 85, 1.0, 0.5, 1.0)
        }
    }

    // VENGEFUL Enchantment Function
    private fun vengefulEnchantment(eventDamager: LivingEntity, enchantmentStrength: Int) {
        // Mark
        eventDamager.addScoreboardTag("Vengeance_Marked")
        eventDamager.addScoreboardTag("Vengeance_Marked_$enchantmentStrength")
        // Add Particles Timer?
    }


}
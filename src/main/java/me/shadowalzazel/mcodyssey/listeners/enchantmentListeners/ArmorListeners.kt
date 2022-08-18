package me.shadowalzazel.mcodyssey.listeners.enchantmentListeners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.effects.SpeedySpursTask
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
                            vengefulEnchantment(someDamager, someDefender, someChestplate)
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
                            cowardiceEnchantment(event, someDefender, someLeggings)
                        }
                        OdysseyEnchantments.SPOREFUL -> {
                            sporefulEnchantment(someDefender, someLeggings)
                        }
                        OdysseyEnchantments.SQUIDIFY -> {
                            squidifyEnchantment(someDefender, someLeggings)
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
                        if (!fruitfulFareCooldown.containsKey(someConsumer.uniqueId)) { fruitfulFareCooldown[someConsumer.uniqueId] = 0L }
                        val timeElapsed: Long = System.currentTimeMillis() - fruitfulFareCooldown[someConsumer.uniqueId]!!
                        if (timeElapsed > 8 * 1000) {
                            fruitfulFareCooldown[someConsumer.uniqueId] = System.currentTimeMillis()
                            fruitfulFareEnchantment(someConsumer, event.item, someChestplate)
                        }
                    }
                    // Add if eat rotten or raw heal + other
                    OdysseyEnchantments.POTION_BARRIER -> {
                        if (!potionBarrierCooldown.containsKey(someConsumer.uniqueId)) { potionBarrierCooldown[someConsumer.uniqueId] = 0L }
                        val timeElapsed: Long = System.currentTimeMillis() - potionBarrierCooldown[someConsumer.uniqueId]!!
                        if (timeElapsed > 12 * 1000) {
                            potionBarrierCooldown[someConsumer.uniqueId] = System.currentTimeMillis()
                            potionBarrierEnchantment(someConsumer, event.item, someChestplate)
                        }
                    }

                }
            }
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
                            speedySpursEnchantment(someRider, someMount, someBoots)
                        }
                    }
                }
            }
        }
    }

    /*----------------------------------------------------------------------------------*/

    // COWARDICE Enchantment Function
    private fun cowardiceEnchantment(event: EntityDamageByEntityEvent, eventDefender: LivingEntity, defenderArmor: ItemStack) {
        // Get enchantment Strength
        val enchantmentStrength = defenderArmor.itemMeta.getEnchantLevel(OdysseyEnchantments.COWARDICE)
        // Effects
        val cowardiceEffect = PotionEffect(PotionEffectType.SPEED, 6 * 20, enchantmentStrength)
        eventDefender.addPotionEffect(cowardiceEffect)
        // Movement Math
        val nearbyEnemies = eventDefender.world.getNearbyLivingEntities(eventDefender.location, 2.5)
        if (event.damager in nearbyEnemies) {
            eventDefender.velocity = eventDefender.location.clone().add(0.0, 0.35, 0.0).subtract(event.damager.location).toVector().normalize().multiply(1.0)
        }
    }

    // FRUITFUL_FARE Enchantment Function
    private fun fruitfulFareEnchantment(eventConsumer: Player, eventItem: ItemStack, eventArmor: ItemStack) {
        // Get enchantment Strength
        val enchantmentStrength = eventArmor.itemMeta.getEnchantLevel(OdysseyEnchantments.FRUITFUL_FARE)
        // list of materials that can be consumed
        val notFood = listOf(Material.POTION, Material.ROTTEN_FLESH, Material.POISONOUS_POTATO, Material.SPIDER_EYE, Material.PUFFERFISH)
        if (eventItem.type !in notFood) { // Currently non-forgiving
            // Check Health
            val currentHealth = eventConsumer.health
            if (eventConsumer.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value < currentHealth + (1 + enchantmentStrength)) {
                eventConsumer.health = eventConsumer.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
            }
            else {
                eventConsumer.health += (1 + enchantmentStrength)
            }
            // Particles
            eventConsumer.world.spawnParticle(Particle.HEART, eventConsumer.location, 35, 0.5, 0.5, 0.5)
            eventConsumer.world.spawnParticle(Particle.VILLAGER_HAPPY, eventConsumer.location, 35, 0.5, 0.5, 0.5)
            eventConsumer.world.spawnParticle(Particle.COMPOSTER, eventConsumer.location, 35, 0.5, 0.5, 0.5)
            eventConsumer.world.playSound(eventConsumer.location, Sound.ENTITY_STRIDER_HAPPY, 1.5F, 0.5F)
            eventConsumer.world.playSound(eventConsumer.location, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1.5F, 0.5F)
            eventConsumer.world.playSound(eventConsumer.location, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.5F, 0.8F)
        }
    }

    // POTION_BARRIER Enchantment Function
    private fun potionBarrierEnchantment(eventConsumer: Player, eventItem: ItemStack, eventArmor: ItemStack) {
        // Get enchantment Strength
        val enchantmentStrength = eventArmor.itemMeta.getEnchantLevel(OdysseyEnchantments.POTION_BARRIER)
        // Check if potion
        if (eventItem.type == Material.POTION) { // Currently non-forgiving
            // Effects
            val potionBarrierEffect = PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, ((enchantmentStrength * 2) + 2) * 20, 1)
            eventConsumer.addPotionEffect(potionBarrierEffect)
            // Particles
            eventConsumer.world.spawnParticle(Particle.SCRAPE, eventConsumer.location, 35, 0.5, 0.5, 0.5)
            eventConsumer.world.spawnParticle(Particle.ELECTRIC_SPARK, eventConsumer.location, 35, 0.5, 0.5, 0.5)
            eventConsumer.world.spawnParticle(Particle.COMPOSTER, eventConsumer.location, 35, 0.5, 0.5, 0.5)
            eventConsumer.world.playSound(eventConsumer.location, Sound.ITEM_SHIELD_BLOCK, 1.5F, 0.5F)
            eventConsumer.world.playSound(eventConsumer.location, Sound.BLOCK_DEEPSLATE_BREAK, 1.5F, 0.5F)
            eventConsumer.world.playSound(eventConsumer.location, Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 1.5F, 0.8F)
        }
    }

    // SPEEDY_SPURS enchantment effects
    private fun speedySpursEnchantment(eventRider: LivingEntity, eventMount: LivingEntity, eventBoots: ItemStack) {
        // Get enchantment Strength
        val enchantmentStrength = eventBoots.itemMeta.getEnchantLevel(OdysseyEnchantments.SPEEDY_SPURS)
        // Tasks
        val someSpeedySpursTask = SpeedySpursTask(eventRider, eventMount, enchantmentStrength)
        someSpeedySpursTask.runTaskTimer(MinecraftOdyssey.instance, 0, 10 * 20)
    }

    // SPOREFUL Enchantment Function
    private fun sporefulEnchantment(eventDefender: LivingEntity, defenderArmor: ItemStack) {
        // Get enchantment Strength
        val enchantmentStrength = defenderArmor.itemMeta.getEnchantLevel(OdysseyEnchantments.SPOREFUL)
        // Remove from list
        val nearbyEnemies = eventDefender.world.getNearbyLivingEntities(eventDefender.location, enchantmentStrength.toDouble() * 0.75)
        nearbyEnemies.remove(eventDefender)
        // Effects
        val sporefulEffects = listOf(
            PotionEffect(PotionEffectType.POISON, ((enchantmentStrength * 2) + 2) * 20, 0),
            PotionEffect(PotionEffectType.CONFUSION, ((enchantmentStrength * 2) + 2) * 20, 1),
            PotionEffect(PotionEffectType.SLOW, 20, 0)
        )
        for (threat in nearbyEnemies) {
            threat.addPotionEffects(sporefulEffects)
        }
        // Make purple-ish
        // Particles
        eventDefender.world.spawnParticle(Particle.GLOW_SQUID_INK, eventDefender.location, 65, 0.5, 0.5, 0.5)
        eventDefender.world.spawnParticle(Particle.GLOW, eventDefender.location, 45, 0.5, 0.5, 0.5)
        eventDefender.world.spawnParticle(Particle.WARPED_SPORE, eventDefender.location, 95, 0.75, 0.5, 0.75)
        eventDefender.world.spawnParticle(Particle.SNEEZE, eventDefender.location, 65, 0.15, 0.15, 0.15)
        eventDefender.world.spawnParticle(Particle.FALLING_SPORE_BLOSSOM, eventDefender.location, 85, 1.0, 0.5, 1.0)

    }

    // SQUIDIFY Enchantment Function
    private fun squidifyEnchantment(eventDefender: LivingEntity, defenderArmor: ItemStack) {
        // Get enchantment Strength
        val enchantmentStrength = defenderArmor.itemMeta.getEnchantLevel(OdysseyEnchantments.SQUIDIFY)
        // Remove from list
        val nearbyEnemies = eventDefender.world.getNearbyLivingEntities(eventDefender.location, enchantmentStrength.toDouble() * 0.75)
        nearbyEnemies.remove(eventDefender)
        // Effects
        val squidifyEffects = listOf(
            PotionEffect(PotionEffectType.BLINDNESS, (enchantmentStrength * 2) + 2, 1),
            PotionEffect(PotionEffectType.SLOW, enchantmentStrength * 20, 2)
        )
        for (threat in nearbyEnemies) {
            threat.addPotionEffects(squidifyEffects)
        }
        // Particles
        eventDefender.world.spawnParticle(Particle.ASH, eventDefender.location, 95, 1.5, 0.5, 1.5)
        eventDefender.world.spawnParticle(Particle.SPELL_MOB_AMBIENT, eventDefender.location, 55, 0.75, 0.5, 0.75)
        eventDefender.world.spawnParticle(Particle.SQUID_INK, eventDefender.location, 85, 0.75, 0.5, 0.75)
        eventDefender.world.spawnParticle(Particle.SMOKE_LARGE, eventDefender.location, 85, 1.0, 0.5, 1.0)
    }

    // VENGEFUL Enchantment Function
    private fun vengefulEnchantment(eventDamager: LivingEntity, eventDefender: LivingEntity, defenderArmor: ItemStack) {
        // Get enchantment Strength
        val enchantmentStrength = defenderArmor.itemMeta.getEnchantLevel(OdysseyEnchantments.VENGEFUL)
        // Effects
        val vengefulEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 5, 0)
        eventDefender.addPotionEffect(vengefulEffect)
        // Mark
        eventDamager.addScoreboardTag("Vengeance_Marked")
        eventDamager.addScoreboardTag("Vengeance_Marked_$enchantmentStrength")
        // Add Particles Timer?
    }


}
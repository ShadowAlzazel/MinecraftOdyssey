package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.OdysseyUUIDs.ODYSSEY_ENHANCED_MOB_HEALTH_UUID
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.items.OdysseyBooks
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.WanderingTrader
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import kotlin.math.absoluteValue
import kotlin.math.pow

object OdysseySpawningListeners : Listener {

    // TODO: Scarecrow Mob pumpkin and stand
    // weeping angels

    private fun gildedMobHandler(eventEntity: LivingEntity) {

        val gildedPrefixes = setOf("Deadly", "Magnificent", "Terrorizing", "Potent", "Dominant", "Forceful", "Mighty", "Great", "Cruel", "Dangerous", "Savage", "Lethal", "Fatal")

        eventEntity.apply {
            val gildedAffix = OdysseyEnchantments.meleeSet.random()

            // Add Item or enchant
            equipment!!.also {
                if (it.itemInMainHand.hasItemMeta()) { it.itemInMainHand.addUnsafeEnchantment(gildedAffix, gildedAffix.maxLevel) }
                else { it.setItemInMainHand(OdysseyBooks.GILDED_BOOK.createGildedBook(gildedAffix, gildedAffix.startLevel)) }
                it.itemInMainHandDropChance = 0.35F
            }

            // TODO: Add prefixes and affixes to enchantments
            @Suppress("DEPRECATION")
            customName((Component.text("${gildedPrefixes.random()} ")).append(name()).append(Component.text(" of ${gildedAffix.name}")).color(TextColor.color(255, 170, 0)))
            isCustomNameVisible = true

            //val newHealthModifier = AttributeModifier(healthUUID, healthMap[healthUUID]!!.first, healthMap[healthUUID]!!.second, AttributeModifier.Operation.ADD_NUMBER)



        }
    }

    // Farther away from spawn, stronger mobs
    private fun enhanceDifficultyMobHandler(eventEntity: LivingEntity) {

        // Find the XY distance from zero
        val distanceFromZero = eventEntity.location.clone().let {
            it.toBlockLocation()
            it.y = 0.0
            it.distance(it.clone().zero()).absoluteValue
        }

        // Formula
        val enhancedHealthRaw = (0.00125 * distanceFromZero) + (0.000125 * distanceFromZero).pow(2)

        // Get Modifier and Add
        val enhancedHealthModifier = AttributeModifier(ODYSSEY_ENHANCED_MOB_HEALTH_UUID, "odyssey_enhanced_mob_health", enhancedHealthRaw, AttributeModifier.Operation.ADD_NUMBER)
        val mobHealth = eventEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!
        mobHealth.addModifier(enhancedHealthModifier)


        // table for effectiveness, multiply to get final
        // Zombie -> 0.85%
        // Witch -> 1.2%

    }

    @EventHandler(priority = EventPriority.LOW)
    fun mainSpawningHandler(event: CreatureSpawnEvent) {
        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL) {

            // Rolls and spawns a gilded mob
            val rollGilded = (1 > (0..100).random())
            if (rollGilded) {
                gildedMobHandler(event.entity)
                return
            }



            // Specific Spawn Handlers
            when (event.entity) {
                is WanderingTrader -> {

                }
                else -> {

                }
            }

        }
    }



}
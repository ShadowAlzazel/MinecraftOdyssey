package me.shadowalzazel.mcodyssey.listeners.unused

import me.shadowalzazel.mcodyssey.constants.AttributeIDs.ODYSSEY_ENHANCED_MOB_HEALTH_UUID
import me.shadowalzazel.mcodyssey.constants.AttributeIDs.ODYSSEY_GILDED_MOB_HEALTH_UUID
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.items.Arcane
import me.shadowalzazel.mcodyssey.items.Arcane.createGildedBook
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
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

    private val dangerPrefixes = setOf(
        "Deadly",
        "Magnificent",
        "Terrorizing",
        "Potent",
        "Dominant",
        "Forceful",
        "Mighty",
        "Great",
        "Cruel",
        "Dangerous",
        "Savage",
        "Lethal",
        "Fatal")

    private fun gildedMobHandler(eventEntity: LivingEntity) {

        eventEntity.apply {
            val gildedEnchant = OdysseyEnchantments.MELEE_SET.random()

            // Add Item or enchant
            equipment!!.also {
                if (it.itemInMainHand.type != Material.AIR) { it.itemInMainHand.addUnsafeEnchantment(gildedEnchant.toBukkit(), gildedEnchant.maximumLevel) }
                else { it.setItemInMainHand(Arcane.GILDED_BOOK.createGildedBook(gildedEnchant, gildedEnchant.toBukkit().startLevel)) }
                it.itemInMainHandDropChance = 0.35F
            }

            // TODO: Add prefixes and affixes to enchantments
            @Suppress("DEPRECATION")
            customName((Component.text("${dangerPrefixes.random()} ")).append(name()).append(Component.text(" of ${gildedEnchant.enchantName}")).color(TextColor.color(255, 170, 0)))
            isCustomNameVisible = true

            // Gilded Health Modifier
            val gildedHealthModifier = AttributeModifier(ODYSSEY_GILDED_MOB_HEALTH_UUID, "odyssey_gilded_mob_health", health * 2.5, AttributeModifier.Operation.ADD_NUMBER)
            getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.addModifier(gildedHealthModifier)

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
        val scaleDist = 1.0 / 800.0
        val dif = (scaleDist * distanceFromZero) + (scaleDist * distanceFromZero).pow(2)

        // Get Modifier and Add
        val enhancedHealthModifier = AttributeModifier(ODYSSEY_ENHANCED_MOB_HEALTH_UUID, "odyssey_enhanced_mob_health", dif, AttributeModifier.Operation.ADD_NUMBER)
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
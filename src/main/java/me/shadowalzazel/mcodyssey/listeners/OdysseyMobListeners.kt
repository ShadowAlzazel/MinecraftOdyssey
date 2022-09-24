package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.items.OdysseyBooks
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.WanderingTrader
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object OdysseyMobListeners : Listener {


    private fun createGildedMob(eventEntity: LivingEntity) {

        val gildedPrefixes = setOf("Deadly", "Magnificent", "Terrorizing", "Potent", "Dominant", "Forceful", "Mighty", "Great", "Cruel", "Dangerous", "Savage", "Lethal", "Fatal")

        eventEntity.apply {
            val gildedAffix = OdysseyEnchantments.meleeSet.random()

            // Add Item or enchant
            equipment!!.also {
                if (it.itemInMainHand.enchantments.isNotEmpty()) { it.itemInMainHand.addUnsafeEnchantment(gildedAffix, gildedAffix.maxLevel) }
                else { it.setItemInMainHand(OdysseyBooks.GILDED_BOOK.createGildedBook(gildedAffix, gildedAffix.maxLevel)) }
                it.itemInMainHandDropChance = 0.35F
            }

            // TODO: Add prefixes and affixes to enchantments
            @Suppress("DEPRECATION")
            customName((Component.text("${gildedPrefixes.random()} ")).append(name()).append(Component.text(" of ${gildedAffix.name}")).color(TextColor.color(255, 170, 0)))
            isCustomNameVisible = true

            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300 * 20,3, true),
                PotionEffect(PotionEffectType.SPEED, 300 * 20,1, true),
                PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300 * 20,1, true),
                PotionEffect(PotionEffectType.HEALTH_BOOST, 300 * 20, (health / 2).toInt()))
            )
            health *= 2.9
        }
    }




    @EventHandler(priority = EventPriority.LOW)
    fun mainSpawningHandler(event: CreatureSpawnEvent) {
        if (event.spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL) {
            val rollGilded = (1 > (0..100).random())
            when (event.entity) {
                is WanderingTrader -> {

                }

            }

            if (rollGilded) {
                println("X")
                println(event.entity.location)
                createGildedMob(event.entity)
            }
        }
    }

}
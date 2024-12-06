package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object PetListener : Listener, AttributeManager, DataTagManager {
    @EventHandler
    fun eatingPetFoods(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked.type != EntityType.WOLF) return
        if (event.player.inventory.itemInMainHand.type == Material.AIR) return
        if (event.rightClicked !is LivingEntity) return
        val mainHand = event.player.inventory.itemInMainHand
        val entity = event.rightClicked as LivingEntity
        // Make Permanent
        when (mainHand.getItemKeyTag()) {
            "dog_spinach" -> {
                entity.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 10 * 20, 0))
            }
            "dog_sizzle_crisp" -> {
                entity.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE,12 * 60 * 20, 0))
            }
            "dog_milk_bone" -> {
                entity.addHealthAttribute(10.0)
            }
        }
    }
}
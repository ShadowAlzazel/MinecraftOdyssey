package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


object FoodListeners : Listener, DataTagManager {

    // Main Function for food related consumption events
    @EventHandler
    fun eatingFood(event: PlayerItemConsumeEvent) {
        // Buffed Golden Apples
        if (event.item.type == Material.ENCHANTED_GOLDEN_APPLE) {
            event.player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 20 * 20, 3))
            event.player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 120 * 20, 4))
        }
    }
}
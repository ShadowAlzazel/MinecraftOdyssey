package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.ItemDataTags.getOdysseyTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.hasOdysseyItemTag
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


object FoodListeners : Listener {

    // Main Function for food related consumption events
    @EventHandler
    fun eatingFood(event: PlayerItemConsumeEvent) {
        // Buffed Golden Apples
        if (event.item.type == Material.ENCHANTED_GOLDEN_APPLE) {
            event.player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 20 * 20, 3))
            event.player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 120 * 20, 4))
        }
        // Sentries
        if (!event.item.hasOdysseyItemTag()) return
        if (!event.item.itemMeta.hasCustomModelData()) return

        when (event.item.getOdysseyTag()!!) {
            "beetroot_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 6 * 20, 0))
            }
            "pumpkin_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.HASTE, 6 * 20, 0))
            }
            "honey_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 6 * 20, 0))
            }
            "apple_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, 6 * 20, 0))
            }
            "berry_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 6 * 20, 0))
            }
            "glow_berry_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 6 * 20, 0))
            }
            "melon_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 6 * 20, 0))
            }
            "sugar_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 6 * 20, 0))
            }
            "golden_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 15 * 20, 1))
            }
            "chorus_cookie" -> {
                event.player.teleport(event.player.location.clone().add(((0..50).random().div(10.0)), 8.0, ((0..50).random().div(10.0))))
            }
            "spider_eye_boba" -> {
                event.player.foodLevel += 1
                event.player.saturation += 2F       
            }
            "fish_n_chips" -> {
                event.player.saturation += 1F
            }
            "bacon" -> {
                event.player.saturation -= 1F
            }
        }
    }
}
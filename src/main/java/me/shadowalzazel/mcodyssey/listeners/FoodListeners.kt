package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.ItemTags.getOdysseyTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasOdysseyTag
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


object FoodListeners : Listener {

    // Main Function for food related consumption events
    @EventHandler
    fun eatingOdysseyFood(event: PlayerItemConsumeEvent) {
        if (!event.item.hasOdysseyTag()) {
            return
            //println(event.item.itemMeta.persistentDataContainer[NamespacedKey(Odyssey.instance, "item"), PersistentDataType.STRING])
        }
        if (!event.item.itemMeta.hasCustomModelData()) {
            return
        }

        when (event.item.getOdysseyTag()!!) {
            "beetroot_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 4 * 20, 0))
            }
            "pumpkin_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 5 * 20, 0))
            }
            "honey_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 4 * 20, 0))
            }
            "apple_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 4 * 20, 0))
            }
            "berry_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2 * 20, 0))
            }
            "glow_berry_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 2 * 20, 0))
            }
            "melon_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 3 * 20, 0))
            }
            "sugar_cookie" -> {
                event.player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 5 * 20, 0))
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
package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.listeners.enchantment_listeners.MiscListeners
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.inventory.ItemStack

object FishingListeners : Listener {

    @EventHandler
    fun fishingHandler(event: PlayerFishEvent) {
        when(event.state) {
            PlayerFishEvent.State.BITE -> {
                // START MINIGAME
                // create class object for minigame

            }
            PlayerFishEvent.State.CAUGHT_FISH -> {
                caughtFish(event)
            }
            PlayerFishEvent.State.CAUGHT_ENTITY -> {
                caughtEntity(event)
            }
            PlayerFishEvent.State.FISHING -> {
                castLine(event)
            }
            else -> {

            }
        }

    }

    // When bob touches entity
    private fun caughtEntity(event: PlayerFishEvent) {
        if (event.caught == null) { return }
        val rod = if (event.player.inventory.itemInMainHand.type == Material.FISHING_ROD) {
            event.player.inventory.itemInMainHand
        }
        else {
            event.player.inventory.itemInOffHand
        }
    }

    // When click and get item
    private fun caughtFish(event: PlayerFishEvent) {
        if (event.caught == null) { return }
        if (event.caught!!.type != EntityType.DROPPED_ITEM) { return }
        //
        val rod = if (event.player.inventory.itemInMainHand.type == Material.FISHING_ROD) {
            event.player.inventory.itemInMainHand
        }
        else {
            event.player.inventory.itemInOffHand
        }
    }

    // When sending the line
    private fun castLine(event: PlayerFishEvent) {
        val rod = if (event.player.inventory.itemInMainHand.type == Material.FISHING_ROD) {
            event.player.inventory.itemInMainHand
        }
        else {
            event.player.inventory.itemInOffHand
        }

    }

}
package me.shadowalzazel.mcodyssey.listeners

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent

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
        if (event.caught!!.type != EntityType.ITEM) { return }
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
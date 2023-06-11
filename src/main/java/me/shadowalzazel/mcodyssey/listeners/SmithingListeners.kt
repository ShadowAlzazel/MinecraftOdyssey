package me.shadowalzazel.mcodyssey.listeners

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.event.inventory.SmithItemEvent
import org.bukkit.inventory.SmithingTransformRecipe

object SmithingListeners : Listener {

    // TODO: Handler upgrading tools using 1.20 smithing table!
    // Handle weapon quality

    @EventHandler
    fun smithingHandler(event: PrepareSmithingEvent) {
        val q = event.inventory.recipe
        println(q)
        if (event.inventory.recipe == null) { return }
        println("PASS")


    }



}
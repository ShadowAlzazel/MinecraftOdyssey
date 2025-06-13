package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.common.arcane.ArcaneEquipmentManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent

@Suppress("UnstableApiUsage")
object ArcaneListeners: Listener, ArcaneEquipmentManager, DataTagManager {

    @EventHandler(priority = EventPriority.LOW)
    fun mainWeaponInteractionHandler(event: PlayerInteractEvent) {
        if (event.action.isLeftClick) {
            leftClickHandler(event)
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun mainConsumingHandler(event: PlayerItemConsumeEvent) {
        val player = event.player
        val equipment = player.equipment ?: return
        val mainhand = equipment.itemInMainHand
        val itemName = mainhand.getItemNameId()
        // Detect magic items
        when (itemName) {
            "scroll" -> arcanePenWithScrollCastingHandler(player)
        }

    }

    private fun leftClickHandler(event: PlayerInteractEvent) {
        val player = event.player
        val equipment = player.equipment ?: return
        val mainhand = equipment.itemInMainHand
        val itemName = mainhand.getItemNameId()
        // Sentries Passed
        when (itemName) {
            "arcane_wand" -> arcaneWandHandler(player)
            "arcane_blade" -> arcaneBladeHandler(event)
            "arcane_scepter" -> arcaneScepterHandler(player)
        }

    }



}
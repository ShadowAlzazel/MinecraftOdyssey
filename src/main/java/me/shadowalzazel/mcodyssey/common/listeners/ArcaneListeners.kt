package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.common.arcane.ArcaneEquipmentManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack

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
            "scroll" -> scrollConsumingHandler(event)
            "spell_scroll" -> {
                // Scroll damage
                val scroll = mainhand.clone()
                scroll.damage(1, player)
                event.replacement = scroll
                // do spell
                spellScrollCastingHandler(player, mainhand)
            }
        }

    }

    @EventHandler(priority = EventPriority.LOW)
    fun dispenserHandler(event: BlockDispenseEvent) {
        val dispensedItem = event.item
        val itemName = dispensedItem.getItemNameId()
        if (itemName == "spell_scroll") {
            // Triggered for block
            val direction = event.velocity
            event.item = ItemStack(Material.AIR)
        }

    }


    private fun leftClickHandler(event: PlayerInteractEvent) {
        val player = event.player
        val equipment = player.equipment ?: return
        val mainhand = equipment.itemInMainHand
        val itemName = mainhand.getItemNameId()
        // Sentries Passed
        when (itemName) {
            // Tools
            "arcane_wand" -> arcaneWandHandler(player)
            "arcane_blade" -> arcaneBladeHandler(event)
            "arcane_scepter" -> arcaneScepterHandler(player)
            // Others
            "arcane_stylus" -> arcaneStylusDrawingHandler(event)
        }

    }



}
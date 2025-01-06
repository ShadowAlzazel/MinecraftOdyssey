package me.shadowalzazel.mcodyssey.common.listeners

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.common.combat.ArcaneEquipmentManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

@Suppress("UnstableApiUsage")
object ArcaneListeners: Listener, ArcaneEquipmentManager, DataTagManager {

    @EventHandler(priority = EventPriority.LOW)
    fun mainWeaponInteractionHandler(event: PlayerInteractEvent) {
        if (event.action.isLeftClick) {
            leftClickHandler(event)
        }
    }

    private fun leftClickHandler(event: PlayerInteractEvent) {
        val player = event.player
        val offHand = player.equipment.itemInOffHand
        //if (player.hasCooldown(offHand.type)) return
        //val model = offHand.getData(DataComponentTypes.ITEM_MODEL) ?: return
        val itemName = offHand.getItemIdentifier() ?: return
        //if (ARCANE_RANGES[itemName] == null) return
        val book = player.equipment.itemInMainHand
        if (book.type == Material.AIR) return
        val bookEnchantments = book.getData(DataComponentTypes.STORED_ENCHANTMENTS)
        // Sentries Passed
        when (itemName) {
            "arcane_wand" -> arcaneWandUseHandler(event)
            "arcane_blade" -> arcaneBladeHandler(event)
            "arcane_scepter" -> arcaneScepterHandler(event)
        }

    }



}
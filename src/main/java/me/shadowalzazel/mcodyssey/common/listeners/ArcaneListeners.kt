package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.util.ArcaneEquipmentManager
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps.ARCANE_RANGES
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
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
        if (!offHand.hasItemMeta()) return
        if (!offHand.itemMeta!!.hasCustomModelData()) return
        if (!offHand.hasOdysseyItemTag()) return
        if (player.hasCooldown(offHand.type)) return
        val model = offHand.itemMeta!!.itemModel?.key ?: return
        if (ARCANE_RANGES[model] == null) return
        val mainHandBook = player.equipment.itemInOffHand
        if (!mainHandBook.hasItemMeta()) return
        // if (!mainHandBook.itemMeta!!.hasCustomModelData()) return // Can only use volumes ??????
        // Sentries Passed
        val itemTag = offHand.getOdysseyTag() ?: return
        when (itemTag) {
            "arcane_wand" -> arcaneWandHandler(event)
            "arcane_blade" -> arcaneBladeHandler(event)
            "arcane_scepter" -> arcaneScepterHandler(event)
            "warping_wand" -> warpingWandHandler(event)
        }

    }



}
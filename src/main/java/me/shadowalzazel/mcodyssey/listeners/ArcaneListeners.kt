package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.arcane.ArcaneEquipmentManager
import me.shadowalzazel.mcodyssey.constants.WeaponMaps.ARCANE_RANGES
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.getOdysseyTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.hasOdysseyItemTag
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
object ArcaneListeners: Listener, ArcaneEquipmentManager {

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
        val model = offHand.itemMeta!!.customModelData
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
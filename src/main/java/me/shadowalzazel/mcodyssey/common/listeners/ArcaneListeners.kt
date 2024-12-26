package me.shadowalzazel.mcodyssey.common.listeners

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.util.ArcaneEquipmentManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps.ARCANE_RANGES
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
        if (player.hasCooldown(offHand.type)) return
        val model = offHand.getData(DataComponentTypes.ITEM_MODEL) ?: return
        val itemName = offHand.getItemIdentifier() ?: return
        //val model = offHand.itemMeta!!.itemModel?.key ?: return
        if (ARCANE_RANGES[itemName] == null) return
        val mainHandBook = player.equipment.itemInOffHand
        if (mainHandBook.type == Material.AIR) return
        val bookEnchantments = mainHandBook.getData(DataComponentTypes.STORED_ENCHANTMENTS) ?: return
        // if (!mainHandBook.itemMeta!!.hasCustomModelData()) return // Can only use volumes ??????
        // Sentries Passed
        val itemTag = offHand.getItemIdTag() ?: return
        when (itemTag) {
            "arcane_wand" -> arcaneWandHandler(event)
            "arcane_blade" -> arcaneBladeHandler(event)
            "arcane_scepter" -> arcaneScepterHandler(event)
            "warping_wand" -> warpingWandHandler(event)
        }

    }



}
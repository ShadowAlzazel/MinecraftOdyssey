package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.ItemTags.hasOdysseyTag
import me.shadowalzazel.mcodyssey.rune_writing.RunewriterManager
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.ItemStack

object RunesherdListeners : Listener, RunewriterManager {

    @EventHandler
    fun runesherdSmithingHandler(event: PrepareSmithingEvent) {
        val recipe = event.inventory.recipe ?: return
        if (event.inventory.inputMineral == null) return
        if (event.inventory.inputEquipment == null) return
        if (event.inventory.inputTemplate == null) return
        // Variables
        val mineral = event.inventory.inputMineral!!
        val equipment = event.inventory.inputEquipment!!.clone()
        val template = event.inventory.inputTemplate!!
        // Avoid Conflict with other smithing using (Brick)
        if (!template.itemMeta.hasCustomModelData()) return
        if (!equipment.hasItemMeta()) return
        if (recipe.result.type != Material.BRICK) return // ??
        if (event.result?.type == Material.BRICK) {
            event.result = ItemStack(Material.AIR)
        }
        //
        // Var for items
        val runesherd = if (template.type == Material.BRICK) template else return
        // Checks
        if (!runesherd.hasRunesherdTag()) return
        if (!runesherd.hasOdysseyTag()) return



    }

}
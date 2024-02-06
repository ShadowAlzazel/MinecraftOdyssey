package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.ItemTags.hasOdysseyTag
import me.shadowalzazel.mcodyssey.items.Runesherds.addRuneAugmentTag
import me.shadowalzazel.mcodyssey.rune_writing.RunesherdManager
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.block.BlastFurnace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.FurnaceBurnEvent
import org.bukkit.event.inventory.FurnaceSmeltEvent
import org.bukkit.event.inventory.FurnaceStartSmeltEvent
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object RunesherdListeners : Listener, RunesherdManager {

    @EventHandler
    fun runesherdSmithingHandler(event: PrepareSmithingEvent) {
        // Variables, return if any null
        val recipe = event.inventory.recipe ?: return
        val mineral = event.inventory.inputMineral ?: return
        val equipment = event.inventory.inputEquipment?.clone() ?: return
        val template = event.inventory.inputTemplate ?: return
        val runesherd = if (template.type == Material.BRICK) {
            template
        } else {
            return
        }

        val result = event.result ?: return
        // Check types
        if (mineral.type != Material.DIAMOND) return
        if (template.type != Material.BRICK) return
        if (recipe.result.type != Material.BRICK) return
        // Check meta
        if (!runesherd.itemMeta.hasCustomModelData()) return
        // Avoid Conflict with other smithing using (Brick)
        if (result.type == Material.BRICK) {
            event.result = ItemStack(Material.AIR)
        }
        // Checks
        if (!runesherd.hasRunesherdTag()) return
        if (!runesherd.hasOdysseyTag()) return

        // Run
        val item = addRunesherdToSmithingItem(runesherd, equipment) ?: return
        event.inventory.result = item
        event.result = item

        return
    }

    // Orb in off hand that can hold up to 3 runesherds
    // Converts any slot to offhand

    @EventHandler
    fun runewareKilnFiringHandler(event: FurnaceStartSmeltEvent) {
        // Get matching
        if (event.block.type != Material.BLAST_FURNACE) return
        val kiln = event.block
        val input = event.source
        if (input.type != Material.CLAY_BALL) return
        val recipe = event.recipe
        if (recipe.result.type != Material.BRICK) return
        // Get Structure, slow down if not configured right
        val location = kiln.location.clone().toCenterLocation()
        if (location.clone().add(0.0, -1.0, 0.0).block.type != Material.CAMPFIRE) {
            event.totalCookTime += 30 * 20
        }
        var missingBlocks = -1
        val kilnStructureBlocks = listOf(Material.MUD_BRICKS, Material.PACKED_MUD)
        if (location.clone().add(0.0, 1.0, 0.0).block.type !in kilnStructureBlocks) {
            missingBlocks += 1
        }
        if (location.clone().add(1.0, 0.0, 0.0).block.type !in kilnStructureBlocks) {
            missingBlocks += 1
        }
        if (location.clone().add(0.0, 0.0, 1.0).block.type !in kilnStructureBlocks) {
            missingBlocks += 1
        }
        if (location.clone().add(-1.0, 0.0, 0.0).block.type !in kilnStructureBlocks) {
            missingBlocks += 1
        }
        if (location.clone().add(0.0, 0.0, -1.0).block.type !in kilnStructureBlocks) {
            missingBlocks += 1
        }
        event.totalCookTime += ((maxOf(missingBlocks, 0) * 10) * 20)
        // Slowdown if not charcoal
        /*
        if (kiln.inventory.fuel?.type != Material.CHARCOAL) {
            event.totalCookTime += 20 * 20
        }
         */
        println("Cooking Time: ${event.totalCookTime}")

    }

    @EventHandler
    fun runewareKilnFinishFiringHandler(event: FurnaceSmeltEvent) {
        // Get matching vars
        if (event.block.type != Material.BLAST_FURNACE) return
        val kiln = event.block
        val input = event.source
        if (input.type != Material.CLAY_BALL) return
        val recipe = event.recipe ?: return
        if (recipe.result.type != Material.BRICK) return
        if (event.result.type != Material.BRICK) return
        // Tag Check
        if (!input.hasRunewareTag()) return
        println("PASSED T1")
        val runeware = event.result.clone()
        // Transfer to offhand
        val attributeMap = input.itemMeta.attributeModifiers ?: return
        val newList = mutableListOf<Pair<Attribute, AttributeModifier>>()
        attributeMap.forEach { attributeKey, modifier ->
            newList.add(
                Pair(
                    attributeKey,
                    AttributeModifier(
                        modifier.uniqueId,
                        modifier.name,
                        modifier.amount,
                        modifier.operation,
                        EquipmentSlot.OFF_HAND)))
        }
        runeware.itemMeta = runeware.itemMeta.also {
            for (pair in newList) { it.addAttributeModifier(pair.first, pair.second) }
        }
        runeware.addRuneAugmentTag()
        runeware.setRuneAugmentCount(3)
        //
        event.result = runeware
    }


}
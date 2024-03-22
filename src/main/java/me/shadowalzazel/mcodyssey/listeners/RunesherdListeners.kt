package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasOdysseyItemTag
import me.shadowalzazel.mcodyssey.items.Runesherds
import me.shadowalzazel.mcodyssey.items.Runesherds.createLootSherdStack
import me.shadowalzazel.mcodyssey.items.Runesherds.createRuneware
import me.shadowalzazel.mcodyssey.items.Runesherds.createSherdStack
import me.shadowalzazel.mcodyssey.rune_writing.RunesherdManager
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDropItemEvent
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
        if (!runesherd.itemMeta.hasCustomModelData()) {
            event.result = ItemStack(Material.AIR)
            return
        }
        // Avoid Conflict with other smithing using (Brick)
        if (result.type == Material.BRICK) {
            event.result = ItemStack(Material.AIR)
        }
        // Checks
        if (!runesherd.hasRunesherdTag()) return
        if (!runesherd.hasOdysseyItemTag()) return

        // Run
        val item = addRunesherdToSmithingItem(runesherd, equipment) ?: return
        item.also {
            it.amount = 1
        }
        event.inventory.result = item
        event.result = item
        return
    }


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
        // Cancel if more than 1
        if (event.result.amount > 1) {
            event.isCancelled = true
            return
        }

        val runeware = when (input.itemMeta.customModelData) {
            ItemModels.FRAGMENTED_ORB -> {
                Runesherds.GLAZED_RUNE_ORB.createRuneware(1)
            }
            ItemModels.CLAY_TOTEM -> {
                Runesherds.GLAZED_RUNE_TOTEM.createRuneware(1)
            }
            else -> {
                Runesherds.GLAZED_RUNE_ORB.createRuneware(1)
            }
        }
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
                        modifier.amount * 1.25, // Increase by 25%
                        modifier.operation,
                        EquipmentSlot.OFF_HAND)))
        }
        runeware.itemMeta = runeware.itemMeta.also {
            for (pair in newList) { it.addAttributeModifier(pair.first, pair.second) }
        }
        runeware.addRuneAugmentTag()
        runeware.setRuneAugmentCount(3)
        // Set Result
        event.result = runeware
    }

    @EventHandler
    fun changeRunicRuinLootTable(event: BlockDropItemEvent) {
        if (event.block.type != Material.SUSPICIOUS_GRAVEL
            && event.block.type != Material.SUSPICIOUS_SAND) return
        if (event.items.size != 1) return
        val item = event.items[0]
        if (item.itemStack.type != Material.BRICK) return
        if (!item.itemStack.itemMeta.hasCustomModelData()) return
        if (item.itemStack.itemMeta.customModelData != ItemModels.UNKNOWN_RUNESHERD) return
        // Change to runesherd
        val runesherdList = listOf(Runesherds.FINESSE_RUNESHERD, Runesherds.FORCE_RUNESHERD, Runesherds.ASSAULT_RUNESHERD,
            Runesherds.GUARD_RUNESHERD, Runesherds.SWIFT_RUNESHERD, Runesherds.VITALITY_RUNESHERD, Runesherds.STEADFAST_RUNESHERD)

        // RNG
        if ((0..100).random() > 70) {
            item.itemStack = runesherdList.random().createSherdStack(1)
        }
        else {
            item.itemStack = runesherdList.random().createLootSherdStack(1) // Do more random slots
        }
    }

}
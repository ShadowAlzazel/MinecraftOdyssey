@file:Suppress("UnstableApiUsage")

package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.common.items.custom.Glyphsherds
import me.shadowalzazel.mcodyssey.common.items.custom.Glyphsherds.createLootSherdStack
import me.shadowalzazel.mcodyssey.common.items.custom.Glyphsherds.createGlyphicItem
import me.shadowalzazel.mcodyssey.common.items.custom.Glyphsherds.createPresetSherdStack
import me.shadowalzazel.mcodyssey.common.items.custom.Glyphsherds.runesherdRuinsList
import me.shadowalzazel.mcodyssey.util.GlyphManager
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.FurnaceSmeltEvent
import org.bukkit.event.inventory.FurnaceStartSmeltEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

object GlyphListeners : Listener, GlyphManager {

    @EventHandler
    fun glyphSmithingHandler(event: PrepareSmithingEvent) {
        // Variables, return if any null
        val recipe = event.inventory.recipe ?: return
        val mineral = event.inventory.inputMineral ?: return
        val equipment = event.inventory.inputEquipment?.clone() ?: return
        val template = event.inventory.inputTemplate ?: return
        val runesherd = if (template.hasRunesherdTag() || template.getRuneIdentifier() != null) {
            template
        } else {
            return
        }

        val result = event.result ?: return
        // Check types
        if (mineral.type != Material.DIAMOND) return
        //if (template.type != Material.BRICK || template.type !=) return
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
        // Run
        val item = addRunesherdToItemStack(runesherd, equipment) ?: return
        item.also {
            it.amount = 1
        }
        event.inventory.result = item
        event.result = item
        return
    }

    @EventHandler
    fun glyphKilnFiringHandler(event: FurnaceStartSmeltEvent) {
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
            missingBlocks = 0
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
    }

    @EventHandler
    fun glyphKilnFinishFiringHandler(event: FurnaceSmeltEvent) {
        // Get matching vars
        if (event.block.type != Material.BLAST_FURNACE) return
        val kiln = event.block
        val input = event.source
        if (input.type != Material.CLAY_BALL) return
        val recipe = event.recipe ?: return
        if (recipe.result.type != Material.BRICK) return
        if (event.result.type != Material.BRICK) return
        // Tag Check
        if (!input.hasGlyphwareTag()) return
        // Cancel if more than 1
        if (event.result.amount > 1) {
            event.isCancelled = true
            return
        }
        val itemId = input.getRuneIdentifier()
        val glyphicItem = when (itemId) {
            "clay_totem" -> { Glyphsherds.GLAZED_TOTEM.createGlyphicItem(1) }
            "clay_orb"-> { Glyphsherds.GLAZED_ORB.createGlyphicItem(1) }
            "clay_skull"-> { Glyphsherds.GLAZED_SKULL.createGlyphicItem(1) }
            "clay_dowel" -> { Glyphsherds.GLAZED_DOWEL.createGlyphicItem(1) }
            "clay_rods"-> { Glyphsherds.GLAZED_RODS.createGlyphicItem(1) }
            "clay_key" -> { Glyphsherds.GLAZED_KEY.createGlyphicItem(1) }
            else -> { Glyphsherds.GLAZED_ORB.createGlyphicItem(1) }
        }
        // Transfer attribute modifiers to runeware
        val attributeModifiers = input.itemMeta.attributeModifiers ?: return
        val newList = mutableListOf<Pair<Attribute, AttributeModifier>>()
        attributeModifiers.forEach { attributeKey, modifier ->
            val newModifier = AttributeModifier(modifier.key, modifier.amount * 1.25, modifier.operation, EquipmentSlotGroup.HAND)
            newList.add(Pair(attributeKey, newModifier))
        }
        glyphicItem.itemMeta = glyphicItem.itemMeta.also {
            for (pair in newList) { it.addAttributeModifier(pair.first, pair.second) }
        }
        glyphicItem.addGlyphAugmentTag()
        glyphicItem.setGlyphAugmentCount(3)
        // Set Result
        event.result = glyphicItem
    }


    // For Changing unknown runesherd to Runesherd drop
    //@EventHandler(priority = EventPriority.LOW)
    fun changeRunicRuinLootTable(event: BlockDropItemEvent) {
        if (event.block.type != Material.SUSPICIOUS_GRAVEL
            && event.block.type != Material.SUSPICIOUS_SAND) return
        if (event.items.size != 1) return
        val item = event.items[0]
        if (item.itemStack.type != Material.BRICK) return
        if (!item.itemStack.itemMeta.hasCustomModelData()) return
        //if (item.itemStack.itemMeta.customModelData != ItemModels.UNKNOWN_RUNESHERD) return
        // Roll 70 for a preset slot, 30 for random slot
        if ((0..100).random() > 70) {
            item.itemStack = runesherdRuinsList.random().createPresetSherdStack(1)
        }
        else {
            item.itemStack = runesherdRuinsList.random().createLootSherdStack(1) // Do more random slots
        }
        // Advancement
        val advancement = event.player.server.getAdvancement(NamespacedKey.fromString("odyssey:odyssey/dig_runesherd")!!)
        if (advancement != null) {
            event.player.getAdvancementProgress(advancement).awardCriteria("requirement")
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private fun craftRunesherd(event: CraftItemEvent) {
        if (event.recipe.result.type != Material.CLAY_BALL) return
        val runesherd = event.inventory.matrix[4] ?: return
        if (!runesherd.hasItemMeta()) return
        if (!runesherd.hasRunesherdTag()) return
        if (runesherd.hasGlyphwareTag()) return // No cloning runeware
        if (!runesherd.itemMeta.hasCustomModelData()) return
        // Result Checks
        val result = event.inventory.result ?: return
        if (!result.hasRunesherdTag()) return
        // Add to inventory
        val player = event.whoClicked
        val clone = runesherd.clone()
        clone.amount = 1
        val overflow = player.inventory.addItem(clone)
        if (overflow.isNotEmpty()) {
            player.world.dropItem(player.location, overflow[0]!!)
        }

    }

    @EventHandler(priority = EventPriority.LOW)
    private fun prepareCraftRunesherd(event: PrepareItemCraftEvent) {
        if (event.recipe == null) return
        if (event.recipe?.result?.type != Material.CLAY_BALL) return
        val runesherd = event.inventory.matrix[4] ?: return
        if (runesherd.type == Material.CLAY_BALL) return // No Cloning clones
        if (!runesherd.hasItemMeta()) return
        if (!runesherd.hasRunesherdTag()) return
        if (runesherd.hasGlyphwareTag()) return // No cloning runeware
        if (!runesherd.itemMeta.hasCustomModelData()) return
        // Passed basic checks
        val modifiers = runesherd.itemMeta.attributeModifiers ?: return
        val runeName = runesherd.getRuneIdentifier() ?: "rune.generic"
        val runeAttribute = findRunesherdAttribute(runeName) ?: return
        val runeAttributeModifiers = modifiers.get(runeAttribute) ?: return
        val runeKey = AttributeTags.GLYPH_SLOT_KEY
        val runeModifier = runeAttributeModifiers.find { it.name == runeKey } ?: return
        // Add to result
        val result = event.inventory.result ?: return
        val resultMeta = result.itemMeta
        resultMeta.addAttributeModifier(runeAttribute, runeModifier)
        resultMeta.displayName(Component.text("Runepeice"))
        resultMeta.setCustomModelData(runesherd.itemMeta.customModelData)
        result.itemMeta = resultMeta
        result.addRunesherdTag()
        result.addRuneIdentifier(runeName)
        event.inventory.result = result

    }



}
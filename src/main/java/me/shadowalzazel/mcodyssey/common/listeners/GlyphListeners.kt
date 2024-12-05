@file:Suppress("UnstableApiUsage")

package me.shadowalzazel.mcodyssey.common.listeners

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers
import me.shadowalzazel.mcodyssey.common.glyphs.GlyphManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*
import org.bukkit.inventory.EquipmentSlotGroup

object GlyphListeners : Listener, GlyphManager {

    @EventHandler
    fun glyphSmithingHandler(event: PrepareSmithingEvent) {
        // Variables, return if any null
        val recipe = event.inventory.recipe ?: return
        val mineral = event.inventory.inputMineral ?: return
        val equipment = event.inventory.inputEquipment?.clone() ?: return
        val glyph = event.inventory.inputTemplate ?: return
        if (glyph.hasTag(ItemDataTags.IS_GLYPHIC_ITEM)) return // No cloning glyphic items
        // Check types
        if (mineral.type != Material.DIAMOND) return
        if (recipe.result.type != Material.BRICK) return

        // Check if glyph
        if (glyph.getItemIdentifier()?.contains("glyph") != true) return
        // Apply
        val item = equipment.clone()
        item.addGlyph(glyph)
        event.result = item
    }

    @EventHandler
    fun glyphKilnFiringHandler(event: FurnaceStartSmeltEvent) {
        // Sentries
        if (event.block.type != Material.BLAST_FURNACE) return
        if (event.source.type != Material.CLAY_BALL) return
        if (event.recipe.result.type != Material.BRICK) return
        // Get Build, slow down if not configured right
        val location = event.block.location.clone().toCenterLocation()
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
        val source = event.source
        if (source.type != Material.CLAY_BALL) return
        if (!source.hasItemMeta()) return
        val recipe = event.recipe ?: return
        if (recipe.result.type != Material.BRICK) return
        if (event.result.type != Material.BRICK) return
        // Tag Check
        if (!source.hasTag(ItemDataTags.IS_GLYPHIC_ITEM)) return
        val itemId = source.getItemIdentifier() ?: return
        // Cancel if more than 1
        if (event.result.amount > 1) {
            event.isCancelled = true
            return
        }
        // Get new itemName
        val result = source.clone()
        val newItemName = itemId.replace("clay", "glyphic")
        // Transfer attribute modifiers to runeware
        val attributeModifiers = source.itemMeta.attributeModifiers ?: return
        val attributeBuilder = ItemAttributeModifiers.itemAttributes()
        attributeModifiers.forEach { attribute, modifier ->
            val newModifier = AttributeModifier(modifier.key, modifier.amount * 1.25, modifier.operation, EquipmentSlotGroup.HAND)
            attributeBuilder.addModifier(attribute, newModifier)
        }
        // Set Data
        result.setData(DataComponentTypes.ITEM_NAME, Component.text(newItemName))
        result.setData(DataComponentTypes.ITEM_MODEL, createOdysseyKey(newItemName))
        result.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributeBuilder)
        result.setIntTag(ItemDataTags.GLYPH_AUGMENT_COUNT, attributeModifiers.size())
        // Set Result
        event.result = result
    }


    @EventHandler(priority = EventPriority.HIGH)
    private fun createGlyphCopy(event: CraftItemEvent) {
        if (event.recipe.result.type != Material.CLAY_BALL) return
        val glyphsherd = event.inventory.matrix[4] ?: return // Center
        if (!glyphsherd.hasItemMeta()) return
        if (glyphsherd.hasTag(ItemDataTags.IS_GLYPHIC_ITEM)) return // No cloning glyphic items
        // Result Checks
        event.inventory.result ?: return
        // Add to inventory
        val player = event.whoClicked
        val clone = glyphsherd.clone()
        clone.amount = 1
        val overflow = player.inventory.addItem(clone)
        if (overflow.isNotEmpty()) {
            player.world.dropItem(player.location, overflow[0]!!)
        }
    }


    @EventHandler(priority = EventPriority.LOW)
    private fun prepareCraftGlyphsherd(event: PrepareItemCraftEvent) {
        if (event.recipe == null) return
        if (event.recipe?.result?.type != Material.CLAY_BALL) return
        val glyphsherd = event.inventory.matrix[4] ?: return
        if (glyphsherd.type == Material.CLAY_BALL) return // No Cloning clones
        if (!glyphsherd.hasItemMeta()) return
        if (glyphsherd.hasTag(ItemDataTags.IS_GLYPHIC_ITEM)) return // No cloning glyphic items
        // Passed basic checks
        val modifiers = glyphsherd.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS) ?: return
        val glyphAugment = modifiers.modifiers().find { it.modifier().name == "glyph.slot" } ?: return
        val newModifiers = ItemAttributeModifiers.itemAttributes()
        newModifiers.addModifier(glyphAugment.attribute(), glyphAugment.modifier())
        // Add to result
        val result = event.inventory.result ?: return
        val newName = "makeshift_glyph"
        result.setData(DataComponentTypes.ITEM_NAME, Component.text(newName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
        result.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, newModifiers)
        result.addTag(ItemDataTags.IS_COPIED_GLYPH)
        event.inventory.result = result

    }



}
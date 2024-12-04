package me.shadowalzazel.mcodyssey.common.glyphs

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers
import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.RegistryTagManager
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface GlyphManager :  AttributeManager, DataTagManager, RegistryTagManager {

    // Checks for "odyssey:glyph.slot.[SLOT_GROUP_NAME]"
    fun getGlyphEntry(item: ItemStack): ItemAttributeModifiers.Entry? {
        val modifiers = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS) ?: return null
        val name = AttributeTags.GLYPH_SLOT
        val found = modifiers.modifiers().first { it.modifier().name == ("$name.${it.modifier().slotGroup}") }
        return found
    }

    // Checks for raw "odyssey.glyph.slot"
    fun getRawGlyphEntry(item: ItemStack): ItemAttributeModifiers.Entry? {
        val modifiers = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS) ?: return null
        val found = modifiers.modifiers().first { AttributeTags.GLYPH_SLOT in it.modifier().name }
        return found
    }

    fun getGlyphAttributeModifier(item: ItemStack): ItemAttributeModifiers.Entry? {
        return getGlyphEntry(item) ?: getRawGlyphEntry(item)
    }

    fun getGlyphModifier(item: ItemStack): AttributeModifier? {
        return getGlyphAttributeModifier(item)?.modifier()
    }

    fun getGlyphAttribute(item: ItemStack): Attribute? {
        return getGlyphAttributeModifier(item)?.attribute()
    }

    fun ItemStack.addGlyph(glyphItem: ItemStack) {
        // Sentries
        // ADD Separate function to handle holders of glyphs
        val glyphAugment = getGlyphAttributeModifier(glyphItem) ?: return
        val glyphModifier = glyphAugment.modifier()
        // Add sentry to PREVENT Adding glyphs to glyphs?
        // Try and get old Augment -> if found then remove it
        val hasAugment = this.hasTag(ItemDataTags.HAS_GLYPH_AUGMENT)
        val oldGlyphAugment = if (hasAugment) getGlyphAttributeModifier(this) else null
        val itemModifiers = this.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)
        itemModifiers?.modifiers()?.remove(oldGlyphAugment)
        // Create New augment
        val slotGroup = glyphModifier.slotGroup
        val newGlyphKey = createOdysseyKey("${AttributeTags.GLYPH_SLOT}.$slotGroup")
        val newValue = glyphModifier.amount
        val operation = glyphModifier.operation
        val newGlyphAugment = AttributeModifier(newGlyphKey, newValue, operation, slotGroup)
        // Add
        val newBuilder = ItemAttributeModifiers.itemAttributes().addModifier(glyphAugment.attribute(), newGlyphAugment)
        if (itemModifiers != null && itemModifiers.modifiers().isNotEmpty()) {
            for (x in itemModifiers.modifiers()) {
                newBuilder.addModifier(x.attribute(), x.modifier())
            }
        }
        // Set Data
        this.setTag(ItemDataTags.HAS_GLYPH_AUGMENT)
        this.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, newBuilder)
    }


}
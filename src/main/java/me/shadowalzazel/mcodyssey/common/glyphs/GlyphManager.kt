package me.shadowalzazel.mcodyssey.common.glyphs

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers
import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.api.RegistryTagManager
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface GlyphManager :  AttributeManager, DataTagManager, RegistryTagManager {

    // Checks for "odyssey:glyph.slot.[SLOT_GROUP_NAME]"
    fun getGlyphEntry(item: ItemStack): ItemAttributeModifiers.Entry? {
        val modifiers = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS) ?: return null
        val name = AttributeTags.GLYPH_SLOT
        val found = modifiers.modifiers().find { it.modifier().name == ("$name.${it.modifier().slotGroup}") }
        return found
    }

    // Checks for raw "odyssey:glyph.slot"
    fun getRawGlyphEntry(item: ItemStack): ItemAttributeModifiers.Entry? {
        val modifiers = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS) ?: return null
        val found = modifiers.modifiers().find { AttributeTags.GLYPH_SLOT in it.modifier().name }
        return found
    }

    fun getGlyphAttributeModifier(item: ItemStack): ItemAttributeModifiers.Entry? {
        return getGlyphEntry(item) ?: getRawGlyphEntry(item)
    }

    fun ItemStack.addGlyph(glyphItem: ItemStack) {
        if (this.hasTag(ItemDataTags.IS_GLYPHIC_ITEM)) {
            return // TODO: Finish
        } else {
            this.addGlyphToSlot(glyphItem)
        }
    }

    // Used for most items that have a singular glyph slot
    private fun ItemStack.addGlyphToSlot(glyphItem: ItemStack) {
        // Sentries
        // ADD Separate function to handle holders of glyphs
        val glyphAugment = getGlyphAttributeModifier(glyphItem) ?: return
        val glyphModifier = glyphAugment.modifier()
        // Add sentry to PREVENT Adding glyphs to glyphs?
        // Try and get old Augment -> if found then remove it
        val hasAugment = this.hasTag(ItemDataTags.HAS_GLYPH_AUGMENT)
        val oldGlyphAugment = if (hasAugment) getGlyphAttributeModifier(this) else null
        val itemModifiers = this.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)?.modifiers()?.toMutableList() ?: mutableListOf()
        if (oldGlyphAugment != null) itemModifiers.remove(oldGlyphAugment)
        // Create New augment
        val newSlotGroup = EquipmentSlotGroup.getByName(getSlotGroupFromMaterial(this.type)) ?: glyphModifier.slotGroup
        val newGlyphKey = createOdysseyKey("${AttributeTags.GLYPH_SLOT}.$newSlotGroup")
        val newValue = glyphModifier.amount
        val operation = glyphModifier.operation
        val newGlyphAugment = AttributeModifier(newGlyphKey, newValue, operation, newSlotGroup)
        // Add
        val attributeBuilder = ItemAttributeModifiers.itemAttributes().addModifier(glyphAugment.attribute(), newGlyphAugment)
        if (itemModifiers.isNotEmpty()) {
            for (x in itemModifiers) {
                attributeBuilder.addModifier(x.attribute(), x.modifier())
            }
        }
        // Set Data
        this.addTag(ItemDataTags.HAS_GLYPH_AUGMENT)
        this.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributeBuilder)
    }


    private fun getSlotGroupFromMaterial(material: Material): String {
        return when (material) {
            Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
            Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
            Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
            Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL,
            Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE,
            Material.MACE, Material.TRIDENT, Material.FISHING_ROD, Material.STICK -> {
                "mainhand"
            }
            Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.LEATHER_LEGGINGS -> {
                "legs"
            }
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.LEATHER_CHESTPLATE,
            Material.ELYTRA -> {
                "chest"
            }
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.CHAINMAIL_BOOTS, Material.GOLDEN_BOOTS, Material.LEATHER_BOOTS -> {
                "feet"
            }
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.CHAINMAIL_HELMET, Material.GOLDEN_HELMET, Material.LEATHER_HELMET,
            Material.TURTLE_HELMET, Material.CARVED_PUMPKIN -> {
                "head"
            }
            Material.BOW, Material.CROSSBOW -> {
                "mainhand"
            }
            Material.BRICK, Material.CLAY_BALL -> {
                "any"
            }
            else -> {
                "offhand"
            }
        }
    }

}


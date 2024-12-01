package me.shadowalzazel.mcodyssey.common.items.custom

import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.common.items.OdysseyItem
import me.shadowalzazel.mcodyssey.util.GlyphManager
import me.shadowalzazel.mcodyssey.util.SpaceRuneManager
import me.shadowalzazel.mcodyssey.common.items.Glyphsherd
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
object Glyphsherds : GlyphManager, SpaceRuneManager {

    private val GRAY = TextColor.color(170, 170, 170)
    private val RUNEVOID = TextColor.color(85, 67 ,129)

    fun Glyphsherd.createPresetSherdStack(amount: Int = 1): ItemStack {
        val item = this.newItemStack(amount).also {
            it.addRunesherdTag()
        }
        // Add a key that all runesherds share
        if (attribute != null && slotGroup != null) {
            val runeKey = AttributeTags.GLYPH_SLOT_KEY
            item.setGenericAttribute(value, runeKey, attribute, null, slotGroup)
        }
        return item
    }

    fun Glyphsherd.createLootSherdStack(amount: Int = 1, bonus: Double = 0.0): ItemStack {
        val item = this.newItemStack(amount).also {
            it.addRunesherdTag()
        }
        // Add a key that all runesherds share
        if (attribute != null && slotGroup != null) {
            val runeKey = AttributeTags.GLYPH_SLOT_KEY
            item.setGenericAttribute(value, runeKey, attribute, null, slotGroup)
        }
        /*
        if (attribute != null && !slotGroup.isNullOrEmpty()) {
            val runeName = "odyssey." + itemName + "_modifier"
            val runeID = getRuneAttributeName(attribute)
            val newValue = value + (((0..100).random() / 100) * bonus) // Get an offset
            item.addRuneModifier(attribute, newValue, runeName, runeID, ALL_LIST.random())
        }
         */
        return item
    }

    fun OdysseyItem.createGlyphicItem(amount: Int = 1): ItemStack {
        val item = this.newItemStack(amount).also {
            it.addRunewareTag()
            //val newMeta = it.itemMeta // One stack size?
            //newMeta.setMaxStackSize(8)
            //it.itemMeta = newMeta
        }
        return item
    }

    fun OdysseyItem.createSpaceRuneTablet(amount: Int = 1): ItemStack {
        val item = this.newItemStack(amount).also {
            it.addTag(ItemDataTags.IS_SPACERUNE)
            it.createSpaceRuneComponents()
            it.createSpaceRuneMatrixLore()
        }
        return item
    }

    /*-----------------------------------------------------------------------------------------------*/

    // ORB
    val CLAY_ORB = OdysseyItem("clay_orb", Material.CLAY_BALL, "Clay Orb",
        lore = listOf(Component.text("An item capable of holding 3 glyphs.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val GLAZED_ORB = OdysseyItem("glazed_orb", Material.BRICK, "Glazed Orb",
        lore = listOf(Component.text("An item capable of holding 3 glyphs.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // TOTEM
    val CLAY_TOTEM = OdysseyItem("clay_totem", Material.CLAY_BALL, "Clay Totem",
        lore = listOf(Component.text("An item capable of holding 3 glyphs.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val GLAZED_TOTEM = OdysseyItem("glazed_totem", Material.BRICK, "Glazed Totem",
        lore = listOf(Component.text("An item capable of holding 3 glyphs.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // SKULL
    val CLAY_SKULL = OdysseyItem("clay_skull", Material.CLAY_BALL, "Clay Skull",
        lore = listOf(Component.text("An item capable of holding 3 glyphs.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val GLAZED_SKULL = OdysseyItem("glazed_skull", Material.BRICK, "Glazed Skull",
        lore = listOf(Component.text("An item capable of holding 3 glyphs.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // DOWEL
    val CLAY_DOWEL = OdysseyItem("clay_dowel", Material.CLAY_BALL, "Clay Dowel",
        lore = listOf(Component.text("An item capable of holding 3 glyphs.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val GLAZED_DOWEL = OdysseyItem("glazed_dowel", Material.BRICK, "Glazed Dowel",
        lore = listOf(Component.text("An item capable of holding 3 glyphs.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // RODS
    val CLAY_RODS = OdysseyItem("clay_rods", Material.CLAY_BALL, "Clay Rods",
        lore = listOf(Component.text("An item capable of holding 3 glyphs.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val GLAZED_RODS = OdysseyItem("glazed_rods", Material.BRICK, "Glazed Rods",
        lore = listOf(Component.text("An item capable of holding 3 glyphs.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // KEY
    val CLAY_KEY = OdysseyItem("clay_key", Material.CLAY_BALL, "Clay Key",
        lore = listOf(Component.text("An item capable of holding 3 glyphs.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val GLAZED_KEY = OdysseyItem("glazed_key", Material.BRICK, "Glazed Key",
        lore = listOf(Component.text("An item capable of holding 3 glyphs.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))



    // FLOWER [WIP]
    /*
    val CLAY_FLOWER = OdysseyItem("clay_flower", Material.CLAY_BALL, "Clay FLower", 221,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val GLAZED_RUNE_FLOWER = OdysseyItem("glazed_rune_flower", Material.BRICK, "Glazed Rune Flower", 11,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
     */

    /*-----------------------------------------------------------------------------------------------*/

    val ASSAULT_RUNESHERD = Glyphsherd(
        itemName = "assault_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Assault Runesherd",
        attribute = Attribute.ATTACK_DAMAGE,
        value = 1.0,
        slotGroup = EquipmentSlotGroup.MAINHAND
    )

    val GUARD_RUNESHERD = Glyphsherd(
        itemName = "guard_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Guard Runesherd",
        attribute = Attribute.ARMOR,
        value = 1.0,
        slotGroup = EquipmentSlotGroup.ARMOR
    )

    val FINESSE_RUNESHERD = Glyphsherd(
        itemName = "finesse_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Finesse Runesherd",
        attribute = Attribute.ATTACK_SPEED,
        value = 0.2,
        slotGroup = EquipmentSlotGroup.MAINHAND
    )

    val SWIFT_RUNESHERD = Glyphsherd(
        itemName = "swift_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Swift Runesherd",
        attribute = Attribute.MOVEMENT_SPEED,
        value = 0.03,
        slotGroup = EquipmentSlotGroup.FEET
    )

    val VITALITY_RUNESHERD = Glyphsherd(
        itemName = "vitality_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Vitality Runesherd",
        attribute = Attribute.MAX_HEALTH,
        value = 2.0,
        slotGroup = EquipmentSlotGroup.ARMOR
    )

    val STEADFAST_RUNESHERD = Glyphsherd(
        itemName = "steadfast_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Steadfast Runesherd",
        attribute = Attribute.KNOCKBACK_RESISTANCE,
        value = 0.2,
        slotGroup = EquipmentSlotGroup.ARMOR
    )

    val FORCE_RUNESHERD = Glyphsherd(
        itemName = "force_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Force Runesherd",
        attribute = Attribute.ATTACK_KNOCKBACK,
        value = 0.5,
        slotGroup = EquipmentSlotGroup.MAINHAND
    )

    val BREAK_RUNESHERD = Glyphsherd(
        itemName = "break_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Break Runesherd",
        attribute = Attribute.BLOCK_BREAK_SPEED,
        value = 0.5,
        slotGroup = EquipmentSlotGroup.MAINHAND
    )

    val GRASP_RUNESHERD = Glyphsherd(
        itemName = "grasp_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Grasp Runesherd",
        attribute = Attribute.BLOCK_INTERACTION_RANGE,
        value = 1.0,
        slotGroup = EquipmentSlotGroup.MAINHAND
    )

    val JUMP_RUNESHERD = Glyphsherd(
        itemName = "jump_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Jump Runesherd",
        attribute = Attribute.JUMP_STRENGTH,
        value = 0.3,
        slotGroup = EquipmentSlotGroup.LEGS
    )

    val GRAVITY_RUNESHERD = Glyphsherd(
        itemName = "gravity_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Gravity Runesherd",
        attribute = Attribute.GRAVITY,
        value = -0.01,
        slotGroup = EquipmentSlotGroup.ARMOR
    )

    val RANGE_RUNESHERD = Glyphsherd(
        itemName = "range_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Range Runesherd",
        attribute = Attribute.ENTITY_INTERACTION_RANGE,
        value = 0.5,
        slotGroup = EquipmentSlotGroup.MAINHAND
    )

    val SIZE_RUNESHERD = Glyphsherd(
        itemName = "size_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Size Runesherd",
        attribute = Attribute.SCALE,
        value = 0.25,
        slotGroup = EquipmentSlotGroup.ARMOR
    )

    val runesherdRuinsList = listOf(
        ASSAULT_RUNESHERD, GUARD_RUNESHERD, FINESSE_RUNESHERD, SWIFT_RUNESHERD,
        VITALITY_RUNESHERD, STEADFAST_RUNESHERD, FORCE_RUNESHERD, BREAK_RUNESHERD,
        GRASP_RUNESHERD,  JUMP_RUNESHERD,  GRAVITY_RUNESHERD,  RANGE_RUNESHERD,
        SIZE_RUNESHERD
    )

    /*-----------------------------------------------------------------------------------------------*/

    val SPACERUNE_TABLET = OdysseyItem("spacerune_tablet", Material.BRICK, "Spacerune Tablet",
        lore = listOf(Component.text("A tablet inscribed with spatial movement transformations.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))


    val SPACERUNE_SCROLL = OdysseyItem("spacerune_tablet", Material.BRICK, "Spacerune Tablet",
        lore = listOf(Component.text("A tablet inscribed with spatial movement transformations.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

}
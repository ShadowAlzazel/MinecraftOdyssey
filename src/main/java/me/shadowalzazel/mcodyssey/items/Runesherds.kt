package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.AttributeTags
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.common.items.OdysseyItem
import me.shadowalzazel.mcodyssey.util.RunesherdManager
import me.shadowalzazel.mcodyssey.util.SpaceRuneManager
import me.shadowalzazel.mcodyssey.common.items.OdysseyRunesherd
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
object Runesherds : RunesherdManager, SpaceRuneManager {

    private val GRAY = TextColor.color(170, 170, 170)
    private val RUNEVOID = TextColor.color(85, 67 ,129)

    fun OdysseyRunesherd.createPresetSherdStack(amount: Int = 1): ItemStack {
        val item = this.newItemStack(amount).also {
            it.addRunesherdTag()
        }
        // Add a key that all runesherds share
        if (attribute != null && slotGroup != null) {
            val runeKey = AttributeTags.RUNESHERD_KEY
            item.setGenericAttribute(value, runeKey, attribute, null, slotGroup)
        }
        return item
    }

    fun OdysseyRunesherd.createLootSherdStack(amount: Int = 1, bonus: Double = 0.0): ItemStack {
        val item = this.newItemStack(amount).also {
            it.addRunesherdTag()
        }
        // Add a key that all runesherds share
        if (attribute != null && slotGroup != null) {
            val runeKey = AttributeTags.RUNESHERD_KEY
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

    fun OdysseyItem.createRuneware(amount: Int = 1): ItemStack {
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
    val FRAGMENTED_ORB = OdysseyItem("fragmented_orb", Material.CLAY_BALL, "Fragmented Orb", ItemModels.FRAGMENTED_ORB,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val GLAZED_RUNE_ORB = OdysseyItem("glazed_rune_orb", Material.BRICK, "Glazed Rune Orb", ItemModels.GLAZED_RUNE_ORB,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // TOTEM
    val CLAY_TOTEM = OdysseyItem("clay_totem", Material.CLAY_BALL, "Clay Totem", ItemModels.CLAY_TOTEM,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val GLAZED_RUNE_TOTEM = OdysseyItem("glazed_rune_totem", Material.BRICK, "Glazed Rune Totem", ItemModels.GLAZED_RUNE_TOTEM,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // SKULL
    val CLAY_SKULL = OdysseyItem("clay_skull", Material.CLAY_BALL, "Clay Skull", ItemModels.CLAY_SKULL,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val GLAZED_RUNE_SKULL = OdysseyItem("glazed_skull", Material.BRICK, "Glazed Rune Skull", ItemModels.GLAZED_RUNE_SKULL,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // DOWEL
    val CLAY_DOWEL = OdysseyItem("clay_dowel", Material.CLAY_BALL, "Clay Dowel", ItemModels.CLAY_DOWEL,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val GLAZED_RUNE_DOWEL = OdysseyItem("glazed_rune_dowel", Material.BRICK, "Glazed Rune Dowel", ItemModels.GLAZED_RUNE_DOWEL,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // RODS
    val FRAGMENTED_RODS = OdysseyItem("fragmented_rods", Material.CLAY_BALL, "Fragmented Rods", ItemModels.FRAGMENTED_RODS,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val GLAZED_RUNE_RODS = OdysseyItem("glazed_rune_rods", Material.BRICK, "Glazed Rune Rods", ItemModels.GLAZED_RUNE_RODS,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // KEY
    val CLAY_KEY = OdysseyItem("clay_key", Material.CLAY_BALL, "Clay Key", ItemModels.CLAY_KEY,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val GLAZED_RUNE_KEY = OdysseyItem("glazed_rune_key", Material.BRICK, "Glazed Rune Key", ItemModels.GLAZED_RUNE_KEY,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))



    // FLOWER [WIP]
    /*
    val CLAY_FLOWER = OdysseyItem("clay_flower", Material.CLAY_BALL, "Clay FLower", 221,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val GLAZED_RUNE_FLOWER = OdysseyItem("glazed_rune_flower", Material.BRICK, "Glazed Rune Flower", 11,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
     */

    /*-----------------------------------------------------------------------------------------------*/

    val ASSAULT_RUNESHERD = OdysseyRunesherd(
        itemName = "assault_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Assault Runesherd",
        customModel = ItemModels.ASSAULT_RUNESHERD,
        attribute = Attribute.ATTACK_DAMAGE,
        value = 1.0,
        slotGroup = EquipmentSlotGroup.MAINHAND
    )

    val GUARD_RUNESHERD = OdysseyRunesherd(
        itemName = "guard_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Guard Runesherd",
        customModel = ItemModels.GUARD_RUNESHERD,
        attribute = Attribute.ARMOR,
        value = 1.0,
        slotGroup = EquipmentSlotGroup.ARMOR
    )

    val FINESSE_RUNESHERD = OdysseyRunesherd(
        itemName = "finesse_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Finesse Runesherd",
        customModel = ItemModels.FINESSE_RUNESHERD,
        attribute = Attribute.ATTACK_SPEED,
        value = 0.2,
        slotGroup = EquipmentSlotGroup.MAINHAND
    )

    val SWIFT_RUNESHERD = OdysseyRunesherd(
        itemName = "swift_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Swift Runesherd",
        customModel = ItemModels.SWIFT_RUNESHERD,
        attribute = Attribute.MOVEMENT_SPEED,
        value = 0.03,
        slotGroup = EquipmentSlotGroup.FEET
    )

    val VITALITY_RUNESHERD = OdysseyRunesherd(
        itemName = "vitality_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Vitality Runesherd",
        customModel = ItemModels.VITALITY_RUNESHERD,
        attribute = Attribute.MAX_HEALTH,
        value = 2.0,
        slotGroup = EquipmentSlotGroup.ARMOR
    )

    val STEADFAST_RUNESHERD = OdysseyRunesherd(
        itemName = "steadfast_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Steadfast Runesherd",
        customModel = ItemModels.STEADFAST_RUNESHERD,
        attribute = Attribute.KNOCKBACK_RESISTANCE,
        value = 0.2,
        slotGroup = EquipmentSlotGroup.ARMOR
    )

    val FORCE_RUNESHERD = OdysseyRunesherd(
        itemName = "force_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Force Runesherd",
        customModel = ItemModels.FORCE_RUNESHERD,
        attribute = Attribute.ATTACK_KNOCKBACK,
        value = 0.5,
        slotGroup = EquipmentSlotGroup.MAINHAND
    )

    val BREAK_RUNESHERD = OdysseyRunesherd(
        itemName = "break_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Break Runesherd",
        customModel = ItemModels.BREAK_RUNESHERD,
        attribute = Attribute.BLOCK_BREAK_SPEED,
        value = 0.5,
        slotGroup = EquipmentSlotGroup.MAINHAND
    )

    val GRASP_RUNESHERD = OdysseyRunesherd(
        itemName = "grasp_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Grasp Runesherd",
        customModel = ItemModels.GRASP_RUNESHERD,
        attribute = Attribute.BLOCK_INTERACTION_RANGE,
        value = 1.0,
        slotGroup = EquipmentSlotGroup.MAINHAND
    )

    val JUMP_RUNESHERD = OdysseyRunesherd(
        itemName = "jump_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Jump Runesherd",
        customModel = ItemModels.JUMP_RUNESHERD,
        attribute = Attribute.JUMP_STRENGTH,
        value = 0.3,
        slotGroup = EquipmentSlotGroup.LEGS
    )

    val GRAVITY_RUNESHERD = OdysseyRunesherd(
        itemName = "gravity_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Gravity Runesherd",
        customModel = ItemModels.GRAVITY_RUNESHERD,
        attribute = Attribute.GRAVITY,
        value = -0.01,
        slotGroup = EquipmentSlotGroup.ARMOR
    )

    val RANGE_RUNESHERD = OdysseyRunesherd(
        itemName = "range_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Range Runesherd",
        customModel = ItemModels.RANGE_RUNESHERD,
        attribute = Attribute.ENTITY_INTERACTION_RANGE,
        value = 0.5,
        slotGroup = EquipmentSlotGroup.MAINHAND
    )

    val SIZE_RUNESHERD = OdysseyRunesherd(
        itemName = "size_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Size Runesherd",
        customModel = ItemModels.SIZE_RUNESHERD,
        attribute = Attribute.SCALE,
        value = 0.25,
        slotGroup = EquipmentSlotGroup.ARMOR
    )

    val runesherdRuinsList = listOf(
        ASSAULT_RUNESHERD, GUARD_RUNESHERD, FINESSE_RUNESHERD, SWIFT_RUNESHERD,
        VITALITY_RUNESHERD, STEADFAST_RUNESHERD, FORCE_RUNESHERD, BREAK_RUNESHERD,
        GRASP_RUNESHERD,  JUMP_RUNESHERD,  GRAVITY_RUNESHERD,  RANGE_RUNESHERD,
        SIZE_RUNESHERD)

    /*-----------------------------------------------------------------------------------------------*/

    val SPACERUNE_TABLET = OdysseyItem("spacerune_tablet", Material.BRICK, "Spacerune Tablet", ItemModels.SPACERUNE_TABLET,
        lore = listOf(Component.text("A tablet inscribed with spatial movement transformations.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))


    val SPACERUNE_SCROLL = OdysseyItem("spacerune_tablet", Material.BRICK, "Spacerune Tablet", ItemModels.SPACERUNE_TABLET,
        lore = listOf(Component.text("A tablet inscribed with spatial movement transformations.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

}
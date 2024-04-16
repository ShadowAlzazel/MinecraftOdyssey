package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.addTag
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.rune_writing.RunesherdManager
import me.shadowalzazel.mcodyssey.rune_writing.SpaceRuneManager
import me.shadowalzazel.mcodyssey.rune_writing.base.OdysseyRunesherd
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object Runesherds : RunesherdManager, SpaceRuneManager {

    private val GRAY = TextColor.color(170, 170, 170)
    private val RUNEVOID = TextColor.color(85, 67 ,129)

    private val ARMOR_LIST = listOf(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD)
    private val ALL_LIST = listOf(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST,
        EquipmentSlot.HEAD, EquipmentSlot.OFF_HAND, EquipmentSlot.HAND)
    private val WEAPON_LIST = listOf(EquipmentSlot.OFF_HAND, EquipmentSlot.HAND)

    fun OdysseyRunesherd.createSherdStack(amount: Int = 1): ItemStack {
        val item = createItemStack(amount).also {
            it.addRunesherdTag()
        }
        if (attribute != null && !affectedEquipment.isNullOrEmpty()) {
            val runeName = "odyssey." + itemName + "_modifier"
            val runeID = getIDForRunesherd(attribute)
            item.addRuneModifier(attribute, value, runeName, runeID, affectedEquipment[0])
        }
        return item
    }

    fun OdysseyRunesherd.createLootSherdStack(amount: Int = 1, bonus: Double = 0.0): ItemStack {
        val item = createItemStack(amount).also {
            it.addRunesherdTag()
        }
        if (attribute != null && !affectedEquipment.isNullOrEmpty()) {
            val runeName = "odyssey." + itemName + "_modifier"
            val runeID = getIDForRunesherd(attribute)
            val newValue = value + (((0..100).random() / 100) * bonus) // Get an offset
            item.addRuneModifier(attribute, newValue, runeName, runeID, ALL_LIST.random())
        }
        return item
    }

    fun OdysseyItem.createRuneware(amount: Int = 1): ItemStack {
        val item = createItemStack(amount).also {
            it.addRunewareTag()
        }
        return item
    }

    fun OdysseyItem.createSpaceRuneTablet(amount: Int = 1): ItemStack {
        val item = createItemStack(amount).also {
            it.addTag(ItemTags.IS_SPACERUNE)
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

    // FLOWER [WIP]
    /*
    val CLAY_FLOWER = OdysseyItem("clay_flower", Material.CLAY_BALL, "Clay FLower", 221,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val GLAZED_RUNE_FLOWER = OdysseyItem("glazed_rune_flower", Material.BRICK, "Glazed Rune Flower", 11,
        lore = listOf(Component.text("An runeware capable of holding 3 runesherds.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
     */
    // ROD
    // SKULL

    /*-----------------------------------------------------------------------------------------------*/

    val ASSAULT_RUNESHERD = OdysseyRunesherd(
        itemName = "assault_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Assault Runesherd",
        customModel = ItemModels.ASSAULT_RUNESHERD,
        attribute = Attribute.GENERIC_ATTACK_DAMAGE,
        value = 1.0,
        affectedEquipment = listOf(EquipmentSlot.HAND)
    )

    val GUARD_RUNESHERD = OdysseyRunesherd(
        itemName = "guard_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Guard Runesherd",
        customModel = ItemModels.GUARD_RUNESHERD,
        attribute = Attribute.GENERIC_ARMOR,
        value = 1.0,
        affectedEquipment = listOf(EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.HEAD)
    )

    val FINESSE_RUNESHERD = OdysseyRunesherd(
        itemName = "finesse_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Finesse Runesherd",
        customModel = ItemModels.FINESSE_RUNESHERD,
        attribute = Attribute.GENERIC_ATTACK_SPEED,
        value = 0.2,
        affectedEquipment = listOf(EquipmentSlot.HAND)
    )

    val SWIFT_RUNESHERD = OdysseyRunesherd(
        itemName = "swift_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Swift Runesherd",
        customModel = ItemModels.SWIFT_RUNESHERD,
        attribute = Attribute.GENERIC_MOVEMENT_SPEED,
        value = 0.03,
        affectedEquipment = listOf(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD)
    )

    val VITALITY_RUNESHERD = OdysseyRunesherd(
        itemName = "vitality_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Vitality Runesherd",
        customModel = ItemModels.VITALITY_RUNESHERD,
        attribute = Attribute.GENERIC_MAX_HEALTH,
        value = 2.0,
        affectedEquipment = listOf(EquipmentSlot.HEAD, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.FEET)
    )

    val STEADFAST_RUNESHERD = OdysseyRunesherd(
        itemName = "steadfast_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Steadfast Runesherd",
        customModel = ItemModels.STEADFAST_RUNESHERD,
        attribute = Attribute.GENERIC_KNOCKBACK_RESISTANCE,
        value = 0.2,
        affectedEquipment = listOf(EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.CHEST, EquipmentSlot.HEAD)
    )

    val FORCE_RUNESHERD = OdysseyRunesherd(
        itemName = "force_runesherd",
        overrideMaterial = Material.BRICK,
        customName = "Force Runesherd",
        customModel = ItemModels.FORCE_RUNESHERD,
        attribute = Attribute.GENERIC_ATTACK_KNOCKBACK,
        value = 0.5,
        affectedEquipment = listOf(EquipmentSlot.HAND)
    )

    /*-----------------------------------------------------------------------------------------------*/

    val SPACERUNE_TABLET = OdysseyItem("spacerune_tablet", Material.BRICK, "Spacerune Tablet", ItemModels.SPACERUNE_TABLET,
        lore = listOf(Component.text("A tablet inscribed with spatial movement transformations.", RUNEVOID).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

}
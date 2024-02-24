package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.rune_writing.RunesherdManager
import me.shadowalzazel.mcodyssey.rune_writing.base.OdysseyRunesherd
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object Runesherds : RunesherdManager {

    private val ARMOR_LIST = listOf(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD)
    private val ALL_LIST = listOf(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST,
        EquipmentSlot.HEAD, EquipmentSlot.OFF_HAND, EquipmentSlot.HAND)
    private val WEAPON_LIST = listOf(EquipmentSlot.OFF_HAND, EquipmentSlot.HAND)

    fun OdysseyRunesherd.createSherdStack(amount: Int = 1): ItemStack {
        val item = createItemStack(amount).also {
            it.addRunesherdTag()
        }
        if (attribute != null && affectedEquipment != null && affectedEquipment.isNotEmpty()) {
            val runeName = "odyssey." + name + "_modifier"
            val runeID = getIDForRunesherd(attribute)
            item.addRuneModifier(attribute, value, runeName, runeID, affectedEquipment[0])
        }
        return item
    }

    fun OdysseyRunesherd.createLootSherdStack(amount: Int = 1, bonus: Double = 0.0): ItemStack {
        val item = createItemStack(amount).also {
            it.addRunesherdTag()
        }
        if (attribute != null && affectedEquipment != null && affectedEquipment.isNotEmpty()) {
            val runeName = "odyssey." + name + "_modifier"
            val runeID = getIDForRunesherd(attribute)
            val newValue = value + (((0..100).random() / 100) * bonus) // Get an offset
            item.addRuneModifier(attribute, newValue, runeName, runeID, ALL_LIST.random())
        }
        return item
    }

    fun OdysseyItem.createRuneware(amount: Int = 1): ItemStack {
        val item = createItemStack(1).also {
            it.addRunewareTag()
        }
        return item
    }

    /*-----------------------------------------------------------------------------------------------*/

    // ORB
    val FRAGMENTED_ORB = OdysseyItem(
        name = "fragmented_orb",
        material = Material.CLAY_BALL,
        displayName = Component.text("Fragmented Orb", TextColor.color(85, 67 ,129), TextDecoration.ITALIC),
        lore = listOf(Component.text("An un matured runeware runesherds", TextColor.color(155, 155, 155)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.FRAGMENTED_ORB
    ) // TURNS INTO RUNE_WARE IN KILN

    val GLAZED_RUNE_ORB = OdysseyItem(
        name = "glazed_rune_orb",
        material = Material.BRICK,
        displayName = Component.text("Glazed Rune Orb", TextColor.color(85, 67 ,129), TextDecoration.ITALIC),
        lore = listOf(Component.text("A matured runeware", TextColor.color(155, 155, 155)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.GLAZED_RUNE_ORB
    )

    // TOTEM
    val CLAY_TOTEM = OdysseyItem(
        name = "clay_totem",
        material = Material.CLAY_BALL,
        displayName = Component.text("Clay Totem", TextColor.color(85, 67 ,129), TextDecoration.ITALIC),
        lore = listOf(Component.text("An un matured runeware runesherds", TextColor.color(155, 155, 155)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CLAY_TOTEM
    ) // TURNS INTO RUNE_WARE IN KILN

    val GLAZED_RUNE_TOTEM = OdysseyItem(
        name = "glazed_rune_totem",
        material = Material.BRICK,
        displayName = Component.text("Glazed Rune Totem", TextColor.color(85, 67 ,129), TextDecoration.ITALIC),
        lore = listOf(Component.text("A matured runeware", TextColor.color(155, 155, 155)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.GLAZED_RUNE_TOTEM
    )

    /*-----------------------------------------------------------------------------------------------*/

    val ASSAULT_RUNESHERD = OdysseyRunesherd(
        name = "assault_runesherd",
        material = Material.BRICK,
        displayName = Component.text("Assault Runesherd", TextColor.color(85, 67 ,129)),
        customModel = ItemModels.ASSAULT_RUNESHERD,
        attribute = Attribute.GENERIC_ATTACK_DAMAGE,
        value = 1.0,
        affectedEquipment = listOf(EquipmentSlot.HAND)
    )

    val GUARD_RUNESHERD = OdysseyRunesherd(
        name = "guard_runesherd",
        material = Material.BRICK,
        displayName = Component.text("Guard Runesherd", TextColor.color(85, 67 ,129)),
        customModel = ItemModels.GUARD_RUNESHERD,
        attribute = Attribute.GENERIC_ARMOR,
        value = 1.0,
        affectedEquipment = listOf(EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.HEAD)
    )

    val FINESSE_RUNESHERD = OdysseyRunesherd(
        name = "finesse_runesherd",
        material = Material.BRICK,
        displayName = Component.text("Finesse Runesherd", TextColor.color(85, 67 ,129)),
        customModel = ItemModels.FINESSE_RUNESHERD,
        attribute = Attribute.GENERIC_ATTACK_SPEED,
        value = 0.2,
        affectedEquipment = listOf(EquipmentSlot.HAND)
    )

    val SWIFT_RUNESHERD = OdysseyRunesherd(
        name = "swift_runesherd",
        material = Material.BRICK,
        displayName = Component.text("Swift Runesherd", TextColor.color(85, 67 ,129)),
        customModel = ItemModels.SWIFT_RUNESHERD,
        attribute = Attribute.GENERIC_MOVEMENT_SPEED,
        value = 0.03,
        affectedEquipment = listOf(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD)
    )

    val VITALITY_RUNESHERD = OdysseyRunesherd(
        name = "vitality_runesherd",
        material = Material.BRICK,
        displayName = Component.text("Vitality Runesherd", TextColor.color(85, 67 ,129)),
        customModel = ItemModels.VITALITY_RUNESHERD,
        attribute = Attribute.GENERIC_MAX_HEALTH,
        value = 2.0,
        affectedEquipment = listOf(EquipmentSlot.HEAD, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.FEET)
    )

    val STEADFAST_RUNESHERD = OdysseyRunesherd(
        name = "steadfast_runesherd",
        material = Material.BRICK,
        displayName = Component.text("Steadfast Runesherd", TextColor.color(85, 67 ,129)),
        customModel = ItemModels.STEADFAST_RUNESHERD,
        attribute = Attribute.GENERIC_KNOCKBACK_RESISTANCE,
        value = 0.2,
        affectedEquipment = listOf(EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.CHEST, EquipmentSlot.HEAD)
    )

    val FORCE_RUNESHERD = OdysseyRunesherd(
        name = "force_runesherd",
        material = Material.BRICK,
        displayName = Component.text("Force Runesherd", TextColor.color(85, 67 ,129)),
        customModel = ItemModels.FORCE_RUNESHERD,
        attribute = Attribute.GENERIC_ATTACK_KNOCKBACK,
        value = 0.5,
        affectedEquipment = listOf(EquipmentSlot.HAND)
    )

}
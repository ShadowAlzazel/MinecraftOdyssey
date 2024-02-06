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

    fun OdysseyRunesherd.createSherdStack(amount: Int = 1): ItemStack {
        val item = createItemStack(1).also {
            // Add tag
            it.addRunesherdTag()
            // Add Attribute Modifiers
            it.addModifierToRunesherd(this@createSherdStack)
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

    /*-----------------------------------------------------------------------------------------------*/

    val ASSAULT_RUNESHERD = OdysseyRunesherd(
        name = "assault_runesherd",
        material = Material.BRICK,
        displayName = Component.text("Assault Runesherd", TextColor.color(85, 67 ,129)),
        customModel = ItemModels.ATTACK_RUNESHERD,
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
        affectedEquipment = listOf(EquipmentSlot.CHEST) // TEMP!!!!!!!!
    )

    // SO ATTRIBUTES WITH IN HAND ARE NOT AS STRONG
    // AS WHEN IN HELMET
    // ATTACK +1 when on armor is a different runesherd than attack_runesherd


}
package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Equipment {

    val SAYA = OdysseyItem(
        name = "saya",
        material = Material.RABBIT_HIDE,
        displayName = Component.text("Saya", TextColor.color(191, 186, 139), TextDecoration.ITALIC),
        lore = listOf(Component.text("A sublime sheath for a katana.", TextColor.color(191, 186, 139)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SAYA)

    // Basic ranged wand to trigger on book
    val ARCANE_WAND = OdysseyItem(
        name = "arcane_wand",
        material = Material.WOODEN_SHOVEL,
        displayName = Component.text("Arcane Wand", TextColor.color(141, 109, 209), TextDecoration.ITALIC),
        lore = listOf(Component.text("An arcane tool for using gilded books.", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.ARCANE_WAND) // TODO: Work with tome of polymerization and Volumes

    val WARPING_WAND = OdysseyItem(
        name = "warping_wand",
        material = Material.WOODEN_HOE,
        displayName = Component.text("Warping Wand", TextColor.color(191, 186, 139), TextDecoration.ITALIC),
        lore = listOf(Component.text("A warped arcane tool", TextColor.color(191, 186, 139)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.WARPING_WAND)

}
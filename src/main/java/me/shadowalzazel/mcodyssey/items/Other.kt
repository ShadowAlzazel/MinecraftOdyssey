package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Other {

    // UN-USED !!!

    val SILMARIL = OdysseyItem(
        name = "elencuile_sapling",
        material = Material.AMETHYST_SHARD,
        displayName = Component.text("Elencuile Sapling", TextColor.color(255, 84, 255), TextDecoration.ITALIC),
        lore = listOf(
            Component.text("A jewel fruit grown by the world tree on Lupercal", TextColor.color(192, 152, 255 )).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("Shining with stellar light...", TextColor.color(255, 227, 125)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SILMARIL_OF_YGGLADIEL)

    val FRUIT_OF_ERISHKIGAL = OdysseyItem(
        name = "elencuile_sapling",
        material = Material.ENCHANTED_GOLDEN_APPLE,
        displayName = Component.text("Elencuile Sapling", TextColor.color(255, 84, 255), TextDecoration.ITALIC),
        lore = listOf(Component.text("A fruit engineered at the atomic level", TextColor.color(80, 60, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("With the power to alter one's life...", TextColor.color(255, 41, 119)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.FRUIT_OF_ERISHKIGAL)


}
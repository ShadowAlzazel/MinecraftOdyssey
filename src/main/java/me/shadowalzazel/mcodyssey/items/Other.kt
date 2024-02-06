package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Other {

    val ASPEN_SEED = OdysseyItem(
        name = "aspen_seed",
        material = Material.WHEAT_SEEDS,
        displayName = Component.text("Aspen Seed", TextColor.color(155, 255, 155), TextDecoration.ITALIC),
        lore = listOf(Component.text("A magical seed that grows into an old tree", TextColor.color(155, 155, 155)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.ASPEN_SEED)

    val MAPLE_SEED = OdysseyItem(
        name = "maple_seed",
        material = Material.WHEAT_SEEDS,
        displayName = Component.text("Maple Seed", TextColor.color(155, 255, 155), TextDecoration.ITALIC),
        lore = listOf(Component.text("A magical seed that grows into an old tree", TextColor.color(155, 155, 155)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.MAPLE_SEED)

    val SAKURA_SEED = OdysseyItem(
        name = "sakura_seed",
        material = Material.WHEAT_SEEDS,
        displayName = Component.text("Sakura Seed", TextColor.color(155, 255, 155), TextDecoration.ITALIC),
        lore = listOf(Component.text("A magical seed that grows into an ancient tree", TextColor.color(155, 155, 155)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SAKURA_SEED)

    val REDWOOD_SEED = OdysseyItem(
        name = "redwood_seed",
        material = Material.WHEAT_SEEDS,
        displayName = Component.text("Redwood Seed", TextColor.color(155, 255, 155), TextDecoration.ITALIC),
        lore = listOf(Component.text("A magical seed that grows into an ancient tree", TextColor.color(155, 155, 155)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.REDWOOD_SEED)
}
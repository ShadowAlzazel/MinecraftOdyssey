package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Miscellaneous {

    val PRIMO_GEM = OdysseyItem(
        name = "Primogem",
        material = Material.EMERALD,
        displayName = Component.text("Primogem", TextColor.color(226, 137, 69), TextDecoration.ITALIC),
        lore = listOf(
            Component.text("A primordial crystalline gem that's beyond", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("the test world. Shines with the condensed hopes", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("and dreams of universes that once were.", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.PRIMOGEM)

    val TOTEM_OF_VEXING = OdysseyItem(
        name = "totem_of_vexing",
        material = Material.MILK_BUCKET,
        displayName = Component.text("Totem of Vexing", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A totem with vexing properties", TextColor.color(112, 123, 153)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.TOTEM_OF_VEXING)

    val SOUL_CATALYST  = OdysseyItem(
        name = "soul_catalyst",
        material = Material.AMETHYST_SHARD,
        displayName = Component.text("Soul Catalyst", TextColor.color(36, 29, 50), TextDecoration.ITALIC),
        lore = listOf(Component.text("A device made to temporarily contain souls...", TextColor.color(23, 170, 177)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SOUL_CATALYST)

    val IRRADIATED_FRUIT = OdysseyItem(
        name = "irradiated_fruit",
        material = Material.APPLE,
        displayName = Component.text("Irradiated Fruit", TextColor.color(255, 84, 255), TextDecoration.ITALIC),
        lore = listOf(Component.text("They say its good for you...", TextColor.color(87, 67, 96)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.IRRADIATED_FRUIT)

    val SCULK_HEART = OdysseyItem(
        name = "sculk_heart",
        material = Material.ROTTEN_FLESH,
        displayName = Component.text("Sculk Heart", TextColor.color(24, 90, 94), TextDecoration.ITALIC),
        lore = listOf(Component.text("A beating heart of the sculk", TextColor.color(24, 130, 154)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SCULK_HEART)

    val ENIGMATIC_OMAMORI = OdysseyItem(
        name = "enigmatic_omamori",
        material = Material.PAPER,
        displayName = Component.text("Enigmatic Omamori", TextColor.color(57, 63, 84), TextDecoration.ITALIC),
        lore = listOf(Component.text("A charm that attracts wandering souls...", TextColor.color(3, 170, 177)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.ENIGMATIC_OMAMORI)

    val BREEZE_IN_A_BOTTLE = OdysseyItem(
        name = "enigmatic_omamori",
        material = Material.GLASS_BOTTLE,
        displayName = Component.text("Breeze in a Bottle", TextColor.color(74, 140, 234), TextDecoration.ITALIC),
        lore = listOf(Component.text("Somehow the winds are contained in this?", TextColor.color(74, 140, 234)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.BREEZE_IN_A_BOTTLE)

    val CRYING_GOLD = OdysseyItem(
        name = "crying_gold",
        material = Material.GOLD_INGOT,
        displayName = Component.text("Crying Gold", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("WIP", TextColor.color(112, 123, 153)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CRYING_GOLD)


}


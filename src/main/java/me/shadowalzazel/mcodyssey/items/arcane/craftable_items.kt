package me.shadowalzazel.mcodyssey.items.arcane

import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

// BREEZE_IN_A_BOTTLE
object BreezeInABottle : OdysseyItem("Breeze in a Bottle",
    Material.GLASS_BOTTLE,
    Component.text("Breeze in a Bottle", TextColor.color(74, 140, 234), TextDecoration.ITALIC),
    listOf(Component.text("The wind contained?", TextColor.color(74, 140, 234)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.BREEZE_IN_A_BOTTLE)

// ENIGMATIC_OMAMORI
object EnigmaticOmamori : OdysseyItem("Enigmatic Omamori",
    Material.PAPER,
    Component.text("Enigmatic Omamori", TextColor.color(57, 63, 84), TextDecoration.ITALIC),
    listOf(Component.text("A charm to help guide souls...", TextColor.color(23, 170, 177)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.ENIGMATIC_OMAMORI)

// IRRADIATED_FRUIT
object IrradiatedFruit : OdysseyItem("Irradiated Fruit",
    Material.APPLE,
    Component.text("Irradiated Fruit", TextColor.color(255, 84, 255), TextDecoration.ITALIC),
    listOf(Component.text("A fruit with unusual properties...", TextColor.color(87, 67, 96)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.IRRADIATED_FRUIT,
    mapOf(Enchantment.MENDING to 1))

// SCULK_HEART
object SculkHeart : OdysseyItem("Sculk Heart",
    Material.ROTTEN_FLESH,
    Component.text("Sculk Heart", TextColor.color(24, 90, 94), TextDecoration.ITALIC),
    listOf(Component.text("The heart of the sculk...", TextColor.color(24, 130, 154)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.SCULK_HEART)

// HOURGLASS_FROM_BABEL
object HourglassFromBabel : OdysseyItem("Hourglass from Babel",
    Material.RAW_GOLD,
    Component.text("Hourglass from Babel", TextColor.color(191, 166, 95), TextDecoration.ITALIC),
    listOf(Component.text("A strange hourglass with temporal properties...", TextColor.color(231, 166, 95)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.HOURGLASS_FROM_BABEL)

// ENIGMATIC_ANCHOR
object EnigmaticAnchor : OdysseyItem("Enigmatic Anchor",
    Material.RAW_IRON,
    Component.text("Enigmatic Anchor", TextColor.color(57, 63, 84), TextDecoration.ITALIC),
    listOf(Component.text("An unusual device to anchor a soul...", TextColor.color(23, 170, 177)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.ENIGMATIC_ANCHOR)

// RESTLESS_GALE
object RestlessGale : OdysseyItem("Hourglass from Babel",
    Material.RAW_GOLD,
    Component.text("Hourglass from Babel", TextColor.color(191, 166, 95), TextDecoration.ITALIC),
    listOf(Component.text("A strange hourglass with temporal properties...", TextColor.color(231, 166, 95)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.HOURGLASS_FROM_BABEL)

// Anchor - Yone E
// TODO: Flashbang, Knock-Up, Freeze,

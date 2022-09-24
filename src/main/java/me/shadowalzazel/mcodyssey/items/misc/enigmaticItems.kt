package me.shadowalzazel.mcodyssey.items.misc

import me.shadowalzazel.mcodyssey.items.odyssey.OdysseyItem
import me.shadowalzazel.mcodyssey.constants.ItemModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

// ECTOPLASM
object Ectoplasm : OdysseyItem("Ectoplasm",
    Material.BONE,
    Component.text("Ectoplasm", TextColor.color(37, 198, 205), TextDecoration.ITALIC),
    listOf(Component.text("Escapist Souls trapped in a decaying substance...", TextColor.color(97, 75, 61)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.ECTOPLASM)

// SOUL_CRYSTAL
object SoulCrystal : OdysseyItem("Soul Crystal",
    Material.QUARTZ,
    Component.text("Soul Crystal", TextColor.color(37, 198, 205), TextDecoration.ITALIC),
    listOf(Component.text("A crystal forged by lost souls...", TextColor.color(27, 104, 115)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.SOUL_CRYSTAL)

// SORROWING_SOUL
object SorrowingSoul : OdysseyItem("Sorrowing Soul",
    Material.PAPER,
    Component.text("Sorrowing Soul", TextColor.color(142, 42, 17), TextDecoration.ITALIC),
    listOf(Component.text("A catalyst written with souls...", TextColor.color(180, 80, 32)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.SORROWING_SOUL)

// COAGULATED_BLOOD
object CoagulatedBlood : OdysseyItem("Coagulated Blood",
    Material.SLIME_BALL,
    Component.text("Coagulated Blood", TextColor.color(182, 42, 17), TextDecoration.ITALIC),
    listOf(Component.text("A sticky bloody ball...", TextColor.color(210, 60, 62)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.COAGULATED_BLOOD)


// Soul Catalyst
object SoulCatalyst : OdysseyItem("Soul Catalyst",
    Material.AMETHYST_SHARD,
    Component.text("Soul Catalyst", TextColor.color(36, 29, 50), TextDecoration.ITALIC),
    listOf(Component.text("A device made to contain souls...", TextColor.color(23, 170, 177)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.SOUL_CATALYST)

// Soul Steel Ingot
object SoulSteelIngot : OdysseyItem("Soul Steel Ingot",
    Material.IRON_INGOT,
    Component.text("Soul Steel Ingot", TextColor.color(88, 95, 123), TextDecoration.ITALIC),
    listOf(Component.text("An ingot forged with souls...", TextColor.color(57, 63, 84)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.SOUL_STEEL_INGOT)


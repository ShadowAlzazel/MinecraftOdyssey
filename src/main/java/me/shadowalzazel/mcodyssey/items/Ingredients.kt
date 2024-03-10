package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Ingredients {

    val NEPTUNIAN_DIAMOND = OdysseyItem(
        name = "neptunian_diamond",
        material = Material.DIAMOND,
        displayName = Component.text("Neptunian Diamond", TextColor.color(47, 122, 228), TextDecoration.ITALIC),
        lore = listOf(Component.text("An unmatched diamond forged under immense pressure", TextColor.color(47, 122, 228)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.NEPTUNIAN_DIAMOND)

    val JOVIAN_EMERALD = OdysseyItem(
        name = "jovian_emerald",
        material = Material.EMERALD,
        displayName = Component.text("Neptunian Diamond", TextColor.color(210, 234, 64), TextDecoration.ITALIC),
        lore = listOf(Component.text("An pristine emerald harvested from the clouds", TextColor.color(210, 234, 64)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.JOVIAN_EMERALD)

    val RUBY = OdysseyItem(
        name = "ruby",
        material = Material.EMERALD,
        displayName = Component.text("Ruby", TextColor.color(210, 64, 64), TextDecoration.ITALIC),
        lore = listOf(Component.text("Ruby", TextColor.color(210, 64, 64)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.RUBY)

    val JADE = OdysseyItem(
        name = "jade",
        material = Material.EMERALD,
        displayName = Component.text("Jade", TextColor.color(0, 122, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Jade", TextColor.color(0, 122, 85)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.JADE)

    val KUNZITE = OdysseyItem(
        name = "kunzite",
        material = Material.EMERALD,
        displayName = Component.text("Kunzite", TextColor.color(255, 150, 210), TextDecoration.ITALIC),
        lore = listOf(Component.text("Kunzite", TextColor.color(255, 150, 210)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.KUNZITE)

    val ALEXANDRITE = OdysseyItem(
        name = "alexandrite",
        material = Material.EMERALD,
        displayName = Component.text("Alexandrite", TextColor.color(125, 110, 217), TextDecoration.ITALIC),
        lore = listOf(Component.text("Alexandrite", TextColor.color(39, 89, 111)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.ALEXANDRITE)

    val IRRADIATED_SHARD = OdysseyItem(
        name = "irradiated_shard",
        material = Material.PRISMARINE_SHARD,
        displayName = Component.text("Irradiated Shard", TextColor.color(198, 196, 178), TextDecoration.ITALIC),
        lore = listOf(Component.text("A shard dangerous to handle...", TextColor.color(198, 196, 178)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.IRRADIATED_SHARD)

    val IRRADIATED_ROD = OdysseyItem(
        name = "irradiated_rod",
        material = Material.PRISMARINE_SHARD,
        displayName = Component.text("Irradiated Rod", TextColor.color(58, 50, 95), TextDecoration.ITALIC),
        lore = listOf(Component.text("A mechanical piece, dangerous to handle...", TextColor.color(58, 50, 95)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.IRRADIATED_ROD)

    val BONE_OF_FROST = OdysseyItem( // TODO
        name = "bone_of_frost",
        material = Material.BONE,
        displayName = Component.text("Bone of Frost", TextColor.color(163, 211, 255), TextDecoration.ITALIC),
        lore = listOf(Component.text("A frosty Bone", TextColor.color(163, 211, 255)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SOUL_CATALYST)

    val ECTOPLASM = OdysseyItem(
        name = "ectoplasm",
        material = Material.BONE,
        displayName = Component.text("Ectoplasm", TextColor.color(37, 198, 205), TextDecoration.ITALIC),
        lore = listOf(Component.text("Souls trapped in a decaying substance...", TextColor.color(97, 75, 61)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.ECTOPLASM)

    val COAGULATED_BLOOD = OdysseyItem(
        name = "coagulated_blood",
        material = Material.SLIME_BALL,
        displayName = Component.text("Coagulated Blood", TextColor.color(182, 42, 17), TextDecoration.ITALIC),
        lore = listOf(Component.text("A sticky bloody ball...", TextColor.color(210, 60, 62)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.COAGULATED_BLOOD)

    val SOUL_QUARTZ = OdysseyItem(
        name = "soul_quartz",
        material = Material.QUARTZ,
        displayName = Component.text("Soul Quartz", TextColor.color(24, 90, 94), TextDecoration.ITALIC),
        lore = listOf(Component.text("A crystal forged to contain souls...", TextColor.color(3, 170, 177)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SOUL_CRYSTAL)

    val WARDEN_ENTRAILS = OdysseyItem(
        name = "sculk_heart",
        material = Material.ROTTEN_FLESH,
        displayName = Component.text("Warden Entrails", TextColor.color(24, 70, 74), TextDecoration.ITALIC),
        lore = listOf(Component.text("The organs of the silenced", TextColor.color(24, 70, 74)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.WARDEN_ENTRAILS)

    val SOUL_STEEL_INGOT = OdysseyItem(
        name = "soul_steel_ingot",
        material = Material.IRON_INGOT,
        displayName = Component.text("Soul Steel Ingot", TextColor.color(88, 95, 123), TextDecoration.ITALIC),
        lore = listOf(Component.text("A crucible of metallic souls", TextColor.color(37, 198, 205)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SOUL_STEEL_INGOT)


}
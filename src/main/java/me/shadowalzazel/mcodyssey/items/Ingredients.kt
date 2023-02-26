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
        material = Material.DIAMOND,
        displayName = Component.text("Neptunian Diamond", TextColor.color(210, 234, 64), TextDecoration.ITALIC),
        lore = listOf(Component.text("An pristine emerald harvested from the clouds", TextColor.color(210, 234, 64)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.JOVIAN_EMERALD)

    val KUNZITE = OdysseyItem(
        name = "kunzite",
        material = Material.EMERALD,
        displayName = Component.text("Neptunian Diamond", TextColor.color(255, 150, 210), TextDecoration.ITALIC),
        lore = listOf(Component.text("An pristine emerald harvested from the clouds", TextColor.color(255, 150, 210)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.JOVIAN_EMERALD)

    // RUBY? -> 210, 64, 64

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
        customModel = ItemModels.IRRADIATED_SHARD)

    val BONE_OF_FROST = OdysseyItem( // TODO
        name = "bone_of_frost",
        material = Material.BONE,
        displayName = Component.text("Soul Catalyst", TextColor.color(163, 211, 255), TextDecoration.ITALIC),
        lore = listOf(Component.text("A device made to temporarily contain souls...", TextColor.color(163, 211, 255)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SOUL_CATALYST)

    val ECTOPLASM = OdysseyItem(
        name = "ectoplasm",
        material = Material.BONE,
        displayName = Component.text("Ectoplasm", TextColor.color(37, 198, 205), TextDecoration.ITALIC),
        lore = listOf(Component.text("Escapist Souls trapped in a decaying substance...", TextColor.color(97, 75, 61)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.ECTOPLASM)

    val COAGULATED_BLOOD = OdysseyItem(
        name = "coagulated_blood",
        material = Material.SLIME_BALL,
        displayName = Component.text("Coagulated Blood", TextColor.color(182, 42, 17), TextDecoration.ITALIC),
        lore = listOf(Component.text("A sticky bloody ball...", TextColor.color(210, 60, 62)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.COAGULATED_BLOOD)

    val SOUL_CRYSTAL = OdysseyItem(
        name = "soul_crystal",
        material = Material.QUARTZ,
        displayName = Component.text("Soul Crystal", TextColor.color(24, 90, 94), TextDecoration.ITALIC),
        lore = listOf(Component.text("A crystal forged to contain souls...", TextColor.color(3, 170, 177)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SCULK_HEART)

    val WARDEN_ENTRAILS = OdysseyItem(
        name = "sculk_heart",
        material = Material.ROTTEN_FLESH,
        displayName = Component.text("Sculk Heart", TextColor.color(24, 70, 74), TextDecoration.ITALIC),
        lore = listOf(Component.text("A beating heart of the sculk", TextColor.color(24, 70, 74)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SCULK_HEART)

    val SOUL_STEEL_INGOT = OdysseyItem(
        name = "soul_steel_ingot",
        material = Material.IRON_INGOT,
        displayName = Component.text("Soul Steel Ingot", TextColor.color(88, 95, 123), TextDecoration.ITALIC),
        lore = listOf(Component.text("A beating heart of the sculk", TextColor.color(57, 63, 84)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SCULK_HEART)

    val NEUTRONIUM_BARK = OdysseyItem(
        name = "neutronium_plank",
        material = Material.NETHERITE_SCRAP,
        displayName = Component.text("Soul Steel Ingot", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Extremely dense piece of stuff", TextColor.color(80, 60, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SCULK_HEART)

    val NEUTRONIUM_PLANK = OdysseyItem(
        name = "neutronium_plank",
        material = Material.NETHERITE_INGOT,
        displayName = Component.text("Neutronium Plank", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Extremely dense piece of stuff", TextColor.color(80, 60, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SCULK_HEART)

    val ANTI_MATTER_CRYSTAL = OdysseyItem(
        name = "anti_matter_crystal",
        material = Material.PRISMARINE_SHARD,
        displayName = Component.text("Unstable Antimatter Crystal", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("DANGER! DANGER! DANGER!", TextColor.color(80, 60, 170))
            .append(Component.text("DANGER! DANGER!").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(80, 60, 170)))
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("Purely Unstable!!! Quantum Disintegration Imminent!", TextColor.color(80, 60, 170))),
        customModel = ItemModels.SCULK_HEART)

    val ELENCUILE_ESSENCE = OdysseyItem(
        name = "elencuile_essence",
        material = Material.HONEY_BOTTLE,
        displayName = Component.text("Soul Steel Ingot", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A sappy substance with the life of the stars", TextColor.color(160, 45, 160)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.ELENCUILE_ESSENCE)

}
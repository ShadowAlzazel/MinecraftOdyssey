package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Foods {

    val BEETROOT_COOKIE = OdysseyItem(
        name = "beetroot_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Beetroot Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A Beetroot Cookie!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.GOLDEN_COOKIE)

    val PUMPKIN_COOKIE = OdysseyItem(
        name = "pumpkin_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Pumpkin Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A Pumpkin Cookie!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.PUMPKIN_COOKIE)

    val HONEY_COOKIE = OdysseyItem(
        name = "honey_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Honey Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A Honey Cookie!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.GOLDEN_COOKIE)

    val APPLE_COOKIE = OdysseyItem(
        name = "apple_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Apple Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("An Apple Cookie!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.APPLE_COOKIE)

    val BERRY_COOKIE = OdysseyItem(
        name = "berry_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Berry Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A Berry Cookie!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.BERRY_COOKIE)

    val GLOW_BERRY_COOKIE = OdysseyItem(
        name = "glow_berry_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Glow Berry Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A Glow Berry Cookie!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.GLOW_BERRY_COOKIE)

    val MELON_COOKIE = OdysseyItem(
        name = "melon_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Melon Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A Melon Cookie!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.MELON_COOKIE)

    val SUGAR_COOKIE = OdysseyItem(
        name = "sugar_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Sugar Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A Sugar Cookie!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SUGAR_COOKIE)

    val GOLDEN_COOKIE = OdysseyItem(
        name = "golden_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Golden Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A Golden Cookie!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.GOLDEN_COOKIE)

    val FRENCH_TOAST = OdysseyItem(
        name = "french_toast",
        material = Material.BREAD,
        displayName = Component.text("French Toast", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("French Toast!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.FRENCH_TOAST)

    val SUGARY_BREAD = OdysseyItem(
        name = "sugary_bread",
        material = Material.BREAD,
        displayName = Component.text("Sugary Bread", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Sugary Bread!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SUGARY_BREAD)

    val CRYSTAL_CANDY = OdysseyItem(
        name = "crystal_candy",
        material = Material.SWEET_BERRIES,
        displayName = Component.text("Crystal Candy", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Crystalline candy?", TextColor.color(179, 142, 243)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CRYSTAL_CANDY)

    val BACON = OdysseyItem(
        name = "bacon",
        material = Material.COOKED_PORKCHOP,
        displayName = Component.text("Bacon", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Bacon!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.BACON)

    val SALMON_ROLL = OdysseyItem(
        name = "salmon_roll",
        material = Material.COOKED_SALMON,
        displayName = Component.text("Salmon Roll", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Salmon Roll Sushi!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SALMON_ROLL)

}
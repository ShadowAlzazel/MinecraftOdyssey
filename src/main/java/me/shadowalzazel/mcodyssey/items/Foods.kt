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
        lore = listOf(Component.text("Gives you a taste of strength", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.GOLDEN_COOKIE)

    val PUMPKIN_COOKIE = OdysseyItem(
        name = "pumpkin_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Pumpkin Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Gives you a taste of haste", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.PUMPKIN_COOKIE)

    val HONEY_COOKIE = OdysseyItem(
        name = "honey_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Honey Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Gives you a taste of regeneration", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.HONEY_COOKIE)

    val APPLE_COOKIE = OdysseyItem(
        name = "apple_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Apple Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Gives you a taste of resistance", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.APPLE_COOKIE)

    val BERRY_COOKIE = OdysseyItem(
        name = "berry_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Berry Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Gives you a taste of strength", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.BERRY_COOKIE)

    val GLOW_BERRY_COOKIE = OdysseyItem(
        name = "glow_berry_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Glow Berry Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Makes you glow for a bit", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.GLOW_BERRY_COOKIE)

    val MELON_COOKIE = OdysseyItem(
        name = "melon_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Melon Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Gives you a taste of regeneration", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.MELON_COOKIE)

    val SUGAR_COOKIE = OdysseyItem(
        name = "sugar_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Sugar Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Gives you a taste of speed", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SUGAR_COOKIE)

    val GOLDEN_COOKIE = OdysseyItem(
        name = "golden_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Golden Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A magical cookie", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.GOLDEN_COOKIE)

    val CHORUS_FRUIT_COOKIE = OdysseyItem(
        name = "chorus_fruit_cookie",
        material = Material.COOKIE,
        displayName = Component.text("Chorus Fruit Cookie", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Anywhere but here", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CHORUS_FRUIT_COOKIE)

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/


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

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

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
        lore = listOf(Component.text("Salmon Sushi!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SALMON_ROLL)

    val FISH_N_CHIPS = OdysseyItem(
        name = "fish_n_chips",
        material = Material.COOKED_BEEF,
        displayName = Component.text("Fish n' Chips", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Fish with Fries!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.FISH_N_CHIPS)

    val FRUIT_BOWL = OdysseyItem(
        name = "fruit_bowl",
        material = Material.RABBIT_STEW,
        displayName = Component.text("Fruit Bowl", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A Pick of Fruits", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.FRUIT_BOWL)

    val COFFEE = OdysseyItem(
        name = "coffee",
        material = Material.POTION,
        displayName = Component.text("Coffee", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Extra Caffeinated", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.COFFEE)

    val SPIDER_EYE_BOBA = OdysseyItem(
        name = "spider_eye_boba",
        material = Material.SPIDER_EYE,
        displayName = Component.text("Spider Eye Boba", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("With Monster Milk", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SPIDER_EYE_BOBA)

    val STRAWBERRY_TART = OdysseyItem(
        name = "strawberry_tart",
        material = Material.COOKIE,
        displayName = Component.text("Strawberry Tart", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A Delicious Sweet", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.STRAWBERRY_TART)

    val CHOCOLATE_MOCHI = OdysseyItem(
        name = "chocolate_mochi",
        material = Material.COOKIE,
        displayName = Component.text("Chocolate Mochi", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A Chocolate Sweet", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CHOCOLATE_MOCHI)


    // FROGSPAWN POTION
    // -> for more potions
    // frogspawn boba?

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    val RAINBOW_TROUT = OdysseyItem(
        name = "rainbow_trout",
        material = Material.SALMON,
        displayName = Component.text("Rainbow Trout", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A freshwater fish with colorful markings", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.RAINBOW_TROUT)

    val BLUEGILL = OdysseyItem(
        name = "bluegill",
        material = Material.SALMON,
        displayName = Component.text("Bluegill", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A common river fish", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.RAINBOW_TROUT)

    val DOG_SPINACH = OdysseyItem(
        name = "dog_spinach",
        material = Material.COOKED_BEEF,
        displayName = Component.text("Dog Spinach", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Clifford is going to pack a punch", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.DOG_SPINACH)

    val DOG_SIZZLE_CRISP = OdysseyItem(
        name = "dog_sizzle_crisp",
        material = Material.COOKED_BEEF,
        displayName = Component.text("Dog Sizzle Crisp", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Keeps your furry companions safe from fire", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.DOG_SIZZLE_CRISP)

    val DOG_MILK_BONE = OdysseyItem(
        name = "dog_milk_bone",
        material = Material.COOKED_BEEF,
        displayName = Component.text("Dog Milk Bone", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("Makes your furry companion an absolute unit", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.DOG_MILK_BONE)
}
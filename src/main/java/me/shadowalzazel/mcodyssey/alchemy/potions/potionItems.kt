package me.shadowalzazel.mcodyssey.alchemy.potions

import me.shadowalzazel.mcodyssey.alchemy.utility.OdysseyPotion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object CrystallinePotion : OdysseyPotion("Crystalline Potion",
    Component.text("Crystalline Potion", TextColor.color(170, 0, 255)),
    listOf(Component.text("A crystalline liquid...", TextColor.color(170, 0, 255)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    listOf(PotionEffect(PotionEffectType.LUCK, 0 , 0)), Color.fromRGB(147, 84, 255))


object PotionOfLevitation : OdysseyPotion("Potion of Levitation",
    Component.text("Potion of Levitation", TextColor.color(85, 255, 255)),
    potionEffects = listOf(PotionEffect(PotionEffectType.LEVITATION, 10 * 20 , 0)),
    potionColor = Color.fromRGB(160, 183, 242))


object PotionOfWithering : OdysseyPotion("Potion of Withering",
    Component.text("Potion of Withering", TextColor.color(85, 255, 255)),
    potionEffects = listOf(PotionEffect(PotionEffectType.WITHER, 30 * 20 , 0)),
    potionColor = Color.fromRGB(23, 20, 19))


object PotionOfBioluminescence : OdysseyPotion("Potion of Bioluminescence",
    Component.text("Potion of Bioluminescence", TextColor.color(85, 255, 255)),
    potionEffects = listOf(PotionEffect(PotionEffectType.GLOWING, 300 * 20 , 0)),
    potionColor = Color.fromRGB(0, 255, 179))


object PotionOfLuck : OdysseyPotion("Potion of Luck",
    Component.text("Potion of Luck", TextColor.color(85, 255, 255)),
    potionEffects = listOf(PotionEffect(PotionEffectType.LUCK, 300 * 20 , 0)),
    potionColor = Color.fromRGB(255, 157, 0))


object PotionOfResistance : OdysseyPotion("Potion of Resistance",
    Component.text("Potion of Resistance", TextColor.color(85, 255, 255)),
    potionEffects = listOf(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60 * 20 , 0)),
    potionColor = Color.fromRGB(65, 97, 122))


object PotionOfHaste : OdysseyPotion("Potion of Haste",
    Component.text("Potion of Haste", TextColor.color(85, 255, 255)),
    potionEffects = listOf(PotionEffect(PotionEffectType.FAST_DIGGING, 60 * 20 , 0)),
    potionColor = Color.fromRGB(255, 233, 133))


object PotionOfConstitution : OdysseyPotion("Potion of Constitution",
    Component.text("Potion of Constitution", TextColor.color(85, 255, 255)),
    potionEffects = listOf(PotionEffect(PotionEffectType.HEALTH_BOOST, 60 * 20 , 0)),
    potionColor = Color.fromRGB(209, 0, 49))


object PotionOfStoneSkin : OdysseyPotion("Potion of Stone Skin",
    Component.text("Potion of Stone Skin", TextColor.color(85, 255, 255)),
    potionEffects = listOf(
        PotionEffect(PotionEffectType.ABSORPTION, 60 * 20 , 2),
        PotionEffect(PotionEffectType.SLOW, 60 * 20 , 0)
    ),
    potionColor = Color.fromRGB(42, 51, 38))


object PotionOfWrath : OdysseyPotion("Potion of Wrath",
    Component.text("Potion of Wrath", TextColor.color(85, 255, 255)),
    potionEffects = listOf(
        PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40 * 20 , 2),
        PotionEffect(PotionEffectType.HUNGER, 40 * 20 , 1)
    ),
    potionColor = Color.fromRGB(250, 60, 17))


object PotionOfDecay : OdysseyPotion("Bottle o' Decay",
    Component.text("Bottle o' Decay", TextColor.color(114, 227, 154)),
    listOf(Component.text("Decaying (0:30)", TextColor.color(114, 227, 154)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    potionEffects = emptyList(),
    potionColor = Color.fromRGB(114, 227, 154))


object PotionOfFrost : OdysseyPotion("Bottle o' Frost",
    Component.text("Bottle o' Frost", TextColor.color(163, 211, 255)),
    listOf(Component.text("Freezing (0:30)", TextColor.color(163, 211, 255)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    potionEffects = emptyList(),
    potionColor = Color.fromRGB(163, 211, 255))


object PotionOfDouse : OdysseyPotion("Bottle o' Douse",
    Component.text("Bottle o' Douse", TextColor.color(66, 66, 38)),
    listOf(Component.text("Douse (0:40)", TextColor.color(66, 66, 38)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    potionEffects = emptyList(),
    potionColor = Color.fromRGB(36, 36, 38))


object PotionOfAblaze : OdysseyPotion("Bottle o' Ablaze",
    Component.text("Bottle o' Ablaze", TextColor.color(247, 74, 0)),
    listOf(Component.text("Blazing (0:30)", TextColor.color(247, 74, 0)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    potionEffects = emptyList(),
    potionColor = Color.fromRGB(247, 74, 0))


object PotionOfThorns : OdysseyPotion("Potion of Thorns",
    Component.text("Potion of Thorns", TextColor.color(0, 207, 112)),
    listOf(Component.text("Thorns (0:50)", TextColor.color(0, 207, 112)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    potionEffects = emptyList(),
    potionColor = Color.fromRGB(0, 207, 112))

object PuffyPricklyPotion : OdysseyPotion("Puffy n' Prickly Potion",
    Component.text("Puffy n' Prickly Potion", TextColor.color(208, 247, 166)),
    listOf(Component.text("Puffy n' Prickly (0:40)", TextColor.color(208, 247, 166)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    potionEffects = emptyList(),
    potionColor = Color.fromRGB(208, 247, 166))


object BottledSouls : OdysseyPotion("Bottled Souls",
    Component.text("Bottled Souls", TextColor.color(94, 210, 215)),
    listOf(Component.text("Soul Damage I", TextColor.color(94, 210, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    potionEffects = emptyList(),
    potionColor = Color.fromRGB(94, 210, 215))

object PoltergeistBrew : OdysseyPotion("Poltergeist Brew",
    Component.text("Poltergeist Brew", TextColor.color(137, 24, 40)),
    listOf(Component.text("Accursed (0:30)", TextColor.color(137, 24, 40)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    potionEffects = emptyList(),
    potionColor = Color.fromRGB(137, 24, 40))

object PotionOfShimmer : OdysseyPotion("Bottle o' Shimmer",
    Component.text("Bottle o' Shimmer", TextColor.color(78, 0, 161)),
    listOf(Component.text("Shimmer (0:20)", TextColor.color(78, 0, 161)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    potionEffects = listOf(
        PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 20, 1),
        PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 20, 1),
        PotionEffect(PotionEffectType.GLOWING, 20 * 20, 1),
        PotionEffect(PotionEffectType.HUNGER, 20 * 20, 0),
        PotionEffect(PotionEffectType.CONFUSION, 15 * 20, 0),
    ),
    potionColor = Color.fromRGB(78, 0, 161))
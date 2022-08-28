package me.shadowalzazel.mcodyssey.alchemy.potions

import me.shadowalzazel.mcodyssey.alchemy.utility.OdysseyPotion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object CrystallinePotion : OdysseyPotion("Crystalline Potion",
    Component.text("Crystalline Potion", TextColor.color(255, 255, 255)),
    listOf("${ChatColor.GRAY}${ChatColor.ITALIC}A crystalline liquid..."),
    listOf(PotionEffect(PotionEffectType.LUCK, 0 , 0)), Color.fromRGB(147, 84, 255))


object PotionOfLevitation : OdysseyPotion("Potion of Levitation",
    Component.text("Potion of Levitation", TextColor.color(255, 255, 255)),
    potionEffects = listOf(PotionEffect(PotionEffectType.LEVITATION, 10 * 20 , 0)),
    potionColor = Color.fromRGB(160, 183, 242))


object PotionOfWithering : OdysseyPotion("Potion of Withering",
    Component.text("Potion of Withering", TextColor.color(255, 255, 255)),
    potionEffects = listOf(PotionEffect(PotionEffectType.WITHER, 30 * 20 , 0)),
    potionColor = Color.fromRGB(23, 20, 19))


object PotionOfBioluminescence : OdysseyPotion("Potion of Bioluminescence",
    Component.text("Potion of Bioluminescence", TextColor.color(255, 255, 255)),
    potionEffects = listOf(PotionEffect(PotionEffectType.GLOWING, 60 * 20 , 0)),
    potionColor = Color.fromRGB(0, 255, 179))


object PotionOfLuck : OdysseyPotion("Potion of Luck",
    Component.text("Potion of Luck", TextColor.color(0, 170, 0)),
    potionEffects = listOf(PotionEffect(PotionEffectType.LUCK, 300 * 20 , 0)),
    potionColor = Color.fromRGB(255, 157, 0))


object PotionOfResistance : OdysseyPotion("Potion of Resistance",
    Component.text("Potion of Resistance", TextColor.color(255, 255, 255)),
    potionEffects = listOf(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30 * 20 , 0)),
    potionColor = Color.fromRGB(65, 97, 122))


object PotionOfHaste : OdysseyPotion("Potion of Haste",
    Component.text("Potion of Haste", TextColor.color(255, 255, 255)),
    potionEffects = listOf(PotionEffect(PotionEffectType.FAST_DIGGING, 30 * 20 , 0)),
    potionColor = Color.fromRGB(255, 233, 133))


object PotionOfConstitution : OdysseyPotion("Potion of Constitution",
    Component.text("Potion of Constitution", TextColor.color(255, 255, 255)),
    potionEffects = listOf(PotionEffect(PotionEffectType.HEALTH_BOOST, 30 * 20 , 0)),
    potionColor = Color.fromRGB(209, 0, 49))


object PotionOfStoneSkin : OdysseyPotion("Potion of Stone Skin",
    Component.text("Potion of Stone Skin", TextColor.color(85, 255, 255)),
    potionEffects = listOf(
        PotionEffect(PotionEffectType.ABSORPTION, 40 * 20 , 2),
        PotionEffect(PotionEffectType.SLOW, 40 * 20 , 0)
    ),
    potionColor = Color.fromRGB(42, 51, 38))


object PotionOfWrath : OdysseyPotion("Potion of Wrath",
    Component.text("Potion of Wrath", TextColor.color(85, 255, 255)),
    potionEffects = listOf(
        PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40 * 20 , 2),
        PotionEffect(PotionEffectType.BLINDNESS, 40 * 20 , 0)
    ),
    potionColor = Color.fromRGB(250, 60, 17))


object PotionOfDecay : OdysseyPotion("Bottle o' Decay",
    Component.text("Bottle o' Decay", TextColor.color(85, 255, 255)),
    listOf("${ChatColor.GOLD}Decaying (0:20)"),
    potionEffects = emptyList(),
    potionColor = Color.fromRGB(134, 227, 116))


object PotionOfFrost : OdysseyPotion("Bottle o' Frost",
    Component.text("Bottle o' Frost", TextColor.color(255, 255, 85)),
    listOf("${ChatColor.GOLD}Freezing (0:20)"),
    potionEffects = emptyList(),
    potionColor = Color.fromRGB(163, 211, 255))


object PotionOfDouse : OdysseyPotion("Bottle o' Douse",
    Component.text("Bottle o' Douse", TextColor.color(85, 255, 255)),
    listOf("${ChatColor.GOLD}Douse (0:30)"),
    potionEffects = emptyList(),
    potionColor = Color.fromRGB(66, 46, 38))


object PotionOfAblaze : OdysseyPotion("Bottle o' Ablaze",
    Component.text("Bottle o' Ablaze", TextColor.color(85, 255, 255)),
    listOf("${ChatColor.GOLD}Blazing (0:20)"),
    potionEffects = emptyList(),
    potionColor = Color.fromRGB(247, 74, 0))

object BottledSouls : OdysseyPotion("Bottled Souls",
    Component.text("Bottled Souls", TextColor.color(0, 170, 170)),
    listOf("${ChatColor.DARK_AQUA}Soul Damage I"),
    potionEffects = emptyList(),
    potionColor = Color.fromRGB(94, 250, 255))

object PotionOfShimmer : OdysseyPotion("Bottle o' Shimmer",
    Component.text("Bottle o' Shimmer", TextColor.color(170, 0, 170)),
    listOf("${ChatColor.DARK_PURPLE}Shimmer (0:20)"),
    potionEffects = listOf(
        PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 20, 1),
        PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 20, 1),
        PotionEffect(PotionEffectType.GLOWING, 20 * 20, 1),
        PotionEffect(PotionEffectType.HUNGER, 20 * 20, 0),
        PotionEffect(PotionEffectType.CONFUSION, 15 * 20, 0),
    ),
    potionColor = Color.fromRGB(78, 0, 181))
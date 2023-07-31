package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

object Potions {

    // Extension function to create potions when this is called in the scope
    fun OdysseyItem.createPotion(): ItemStack {
        val newPotion = this.createItemStack(1)
        if (potionEffects != null) {
            newPotion.itemMeta = (newPotion.itemMeta as PotionMeta).also {
                // Check if list not empty for non-custom effects; if empty then un-craft-able type
                if (potionEffects.isNotEmpty()) {
                    for (someEffect in potionEffects) { it.addCustomEffect(someEffect, true) }
                }
                else {
                    it.basePotionData = PotionData(PotionType.UNCRAFTABLE)
                }
                if (potionColor != null) {
                    it.color = potionColor
                }
            }
        }
        return newPotion
    }

    val CRYSTALLINE_POTION = OdysseyItem(
        name = "crystalline_potion",
        material = Material.POTION,
        displayName =  Component.text("Crystalline Potion", TextColor.color(170, 0, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        lore = listOf(Component.text("A crystalline liquid...", TextColor.color(170, 0, 255)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        potionEffects = listOf(PotionEffect(PotionEffectType.LUCK, 0 , 0)),
        potionColor = Color.fromRGB(147, 84, 255))

    val POTION_OF_LEVITATION = OdysseyItem(
        name = "potion_of_levitation",
        material = Material.POTION,
        displayName = Component.text("Potion of Levitation", TextColor.color(85, 255, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.LEVITATION, 10 * 20 , 0)),
        potionColor = Color.fromRGB(85, 255, 255))

    val POTION_OF_WITHERING = OdysseyItem(
        name = "potion_of_withering",
        material = Material.POTION,
        displayName = Component.text("Potion of Withering", TextColor.color(85, 255, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.WITHER, 10 * 20 , 0)),
        potionColor = Color.fromRGB(85, 255, 255))

    val POTION_OF_LUCK = OdysseyItem(
        name = "potion_of_luck",
        material = Material.POTION,
        displayName = Component.text("Potion of Luck", TextColor.color(85, 255, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.LUCK, 300 * 20 , 0)),
        potionColor = Color.fromRGB(255, 157, 0))

    val POTION_OF_RESISTANCE = OdysseyItem(
        name = "potion_of_resistance",
        material = Material.POTION,
        displayName = Component.text("Potion of Resistance", TextColor.color(85, 255, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 150 * 20 , 0)),
        potionColor = Color.fromRGB(65, 97, 122))

    val POTION_OF_HASTE = OdysseyItem(
        name = "potion_of_resistance",
        material = Material.POTION,
        displayName = Component.text("Potion of Haste", TextColor.color(85, 255, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.FAST_DIGGING, 150 * 20 , 0)),
        potionColor = Color.fromRGB(255, 233, 133))

    val POTION_OF_BIOLUMINESCENCE = OdysseyItem(
        name = "potion_of_bioluminescence",
        material = Material.POTION,
        displayName = Component.text("Potion of Bioluminescence", TextColor.color(55, 55, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.GLOWING, 300 * 20 , 0)),
        potionColor = Color.fromRGB(0, 255, 179))

    val POTION_OF_CONSTITUTION = OdysseyItem(
        name = "potion_of_constitution",
        material = Material.POTION,
        displayName = Component.text("Potion of Constitution", TextColor.color(55, 55, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.HEALTH_BOOST, 180 * 20 , 0)),
        potionColor = Color.fromRGB(209, 0, 49))

    val POTION_OF_STONE_SKIN = OdysseyItem(
        name = "potion_of_stone_skin",
        material = Material.POTION,
        displayName = Component.text("Potion of Stone Skin", TextColor.color(55, 55, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.ABSORPTION, 180 * 20 , 2),
            PotionEffect(PotionEffectType.SLOW, 180 * 20 , 0)),
        potionColor = Color.fromRGB(42, 51, 38))

    val POTION_OF_WRATH = OdysseyItem(
        name = "potion_of_wrath",
        material = Material.POTION,
        displayName = Component.text("Potion of Wrath", TextColor.color(55, 55, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120 * 20 , 2),
            PotionEffect(PotionEffectType.HUNGER, 120 * 20 , 1)),
        potionColor = Color.fromRGB(250, 60, 17))

    val POTION_OF_WHIZ = OdysseyItem(
        name = "potion_of_whiz",
        material = Material.POTION,
        displayName = Component.text("Whiz Potion", TextColor.color(55, 55, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.SPEED, 120 * 20 , 2),
            PotionEffect(PotionEffectType.CONFUSION, 10 * 20 , 0),
            PotionEffect(PotionEffectType.HUNGER, 120 * 20 , 1)),
        potionColor = Color.fromRGB(220, 210, 217))

    val LARGE_POTION_OF_HASTE = OdysseyItem(
        name = "potion_of_levitation",
        material = Material.POTION,
        displayName = Component.text("Large Potion of Haste", TextColor.color(255, 255, 55)),
        customModel = ItemModels.LARGE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 60 , 0)),
        potionColor = Color.fromRGB(185, 255, 155))

    val FLASK_OF_DECAY = OdysseyItem(
        name = "flask_of_decay",
        material = Material.POTION,
        displayName = Component.text("Flask o' Decay", TextColor.color(114, 227, 154)),
        lore = listOf(Component.text("Decaying (0:30)", TextColor.color(114, 227, 154)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CONICAL_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(114, 227, 154))

    val FLASK_OF_FROST = OdysseyItem(
        name = "flask_of_frost",
        material = Material.POTION,
        displayName = Component.text("Flask o' Frost", TextColor.color(163, 211, 255)),
        lore = listOf(Component.text("Freezing (0:30)", TextColor.color(163, 211, 255)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CONICAL_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(163, 211, 255))

    val FLASK_OF_DOUSE = OdysseyItem(
        name = "flask_of_douse",
        material = Material.POTION,
        displayName = Component.text("Flask o' Douse", TextColor.color(66, 66, 38)),
        lore = listOf(Component.text("Douse (0:40)", TextColor.color(66, 66, 38)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CONICAL_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(66, 66, 38))

    val FLASK_OF_ABLAZE = OdysseyItem(
        name = "flask_of_ablaze",
        material = Material.POTION,
        displayName = Component.text("Flask o' Ablaze", TextColor.color(247, 74, 0)),
        lore = listOf(Component.text("Blazing (0:30)", TextColor.color(247, 74, 0)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CONICAL_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(247, 74, 0))

    val FLASK_OF_ROSE = OdysseyItem( // TODO: Change !!!
        name = "flask_of_rose",
        material = Material.POTION,
        displayName = Component.text("Flask o' Rose", TextColor.color(250, 14, 60)),
        lore = listOf(Component.text("Rose (0:30)", TextColor.color(250, 14, 60)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CONICAL_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(247, 74, 0))

    val FLASK_OF_PUFFJUICE = OdysseyItem( // TODO: Fix
        name = "flask_of_puffjuice",
        material = Material.POTION,
        displayName = Component.text("Flask o' Puffjuice", TextColor.color(222, 255, 166)),
        lore = listOf(Component.text("Puffy (0:30)", TextColor.color(222, 255, 166)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CONICAL_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(247, 74, 0))

    val FLASK_OF_MIASMA = OdysseyItem( // TODO: Applies Damage and Slowness. MAYBE DISABLES HUNGER REGEN
        name = "flask_of_miasma",
        material = Material.POTION,
        displayName = Component.text("Flask o' Miasma", TextColor.color(80, 20, 90)),
        lore = listOf(Component.text("Miasma (0:30)", TextColor.color(80, 20, 90)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CONICAL_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(247, 74, 0))

    val BOTTLE_OF_SOULS = OdysseyItem(
        name = "bottle_of_souls",
        material = Material.POTION,
        displayName = Component.text("Bottled Souls", TextColor.color(94, 210, 215)),
        lore = listOf(Component.text("Soul Damage I", TextColor.color(94, 210, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.ROUND_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(0, 255, 179))

    val POLTERGEIST_BREW = OdysseyItem(
        name = "poltergeist_brew",
        material = Material.POTION,
        displayName = Component.text("Poltergeist Brew", TextColor.color(137, 24, 40)),
        lore = listOf(Component.text("Accursed", TextColor.color(137, 24, 40)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.ROUND_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(137, 24, 40))

    val BOTTLE_OF_SHIMMER = OdysseyItem(
        name = "bottle_of_shimmer",
        material = Material.POTION,
        displayName = Component.text("Bottle o' Shimmer", TextColor.color(78, 0, 161)),
        lore = listOf(Component.text("Shimmer (2:00)", TextColor.color(78, 0, 161)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.DAIRYSHAPE_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120 * 20, 1),
            PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120 * 20, 1),
            PotionEffect(PotionEffectType.GLOWING, 120 * 20, 1),
            PotionEffect(PotionEffectType.HUNGER, 120 * 20, 0),
            PotionEffect(PotionEffectType.CONFUSION, 120 * 20, 0)),
        potionColor = Color.fromRGB(247, 74, 0))






    // ---------------------------------------
    val potionSet = setOf(
        CRYSTALLINE_POTION, POTION_OF_LEVITATION, POTION_OF_WITHERING, POTION_OF_BIOLUMINESCENCE, POTION_OF_LUCK, POTION_OF_RESISTANCE, POTION_OF_HASTE, POTION_OF_CONSTITUTION, POTION_OF_STONE_SKIN, POTION_OF_WRATH,
        FLASK_OF_DECAY, FLASK_OF_FROST, FLASK_OF_DOUSE, FLASK_OF_ABLAZE, FLASK_OF_ROSE, FLASK_OF_MIASMA, BOTTLE_OF_SOULS, POLTERGEIST_BREW, BOTTLE_OF_SHIMMER
    )
}
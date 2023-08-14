package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.alchemy.base.OdysseyPotion
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

    /*
    fun OdysseyItem.createPotion(): ItemStack {
        val newPotion = this.createItemStack(1)
        if (potionEffects != null) {
            newPotion.itemMeta = (newPotion.itemMeta as PotionMeta).also {
                // Check if list not empty for non-custom effects; if empty then un-craft-able type
                if (potionEffects.isNotEmpty()) {
                    for (effect in potionEffects) { it.addCustomEffect(effect, true) }
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


     */
    /*-----------------------------------------------------------------------------------------------*/
    // DOES NOT CONTAIN:
    // Effects
    // Tags


    val CRYSTALLINE_POTION = OdysseyPotion(
        name = "crystalline_potion",
        material = Material.POTION,
        displayName =  Component.text("Crystalline Potion", TextColor.color(170, 0, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        lore = listOf(Component.text("A crystalline liquid...", TextColor.color(170, 0, 255)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        potionEffects = listOf(PotionEffect(PotionEffectType.LUCK, 0 , 0)),
        potionColor = Color.fromRGB(147, 84, 255))

    val POTION_OF_LEVITATION = OdysseyPotion(
        name = "potion_of_levitation",
        material = Material.POTION,
        displayName = Component.text("Potion of Levitation", TextColor.color(85, 255, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.LEVITATION, 30 * 20 , 0)),
        potionColor = Color.fromRGB(85, 255, 255))

    val POTION_OF_WITHERING = OdysseyPotion(
        name = "potion_of_withering",
        material = Material.POTION,
        displayName = Component.text("Potion of Withering", TextColor.color(85, 255, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.WITHER, 30 * 20 , 0)),
        potionColor = Color.fromRGB(55, 55, 55))

    val POTION_OF_LUCK = OdysseyPotion(
        name = "potion_of_luck",
        material = Material.POTION,
        displayName = Component.text("Potion of Luck", TextColor.color(85, 255, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.LUCK, 300 * 20 , 0)),
        potionColor = Color.fromRGB(255, 157, 0))

    val POTION_OF_RESISTANCE = OdysseyPotion(
        name = "potion_of_resistance",
        material = Material.POTION,
        displayName = Component.text("Potion of Resistance", TextColor.color(85, 255, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 180 * 20 , 0)),
        potionColor = Color.fromRGB(65, 97, 122))

    val POTION_OF_HASTE = OdysseyPotion(
        name = "potion_of_resistance",
        material = Material.POTION,
        displayName = Component.text("Potion of Haste", TextColor.color(85, 255, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.FAST_DIGGING, 180 * 20 , 0)),
        potionColor = Color.fromRGB(255, 233, 133))

    /*-----------------------------------------------------------------------------------------------*/

    val POTION_OF_BIOLUMINESCENCE = OdysseyPotion(
        name = "potion_of_bioluminescence",
        material = Material.POTION,
        displayName = Component.text("Potion of Bioluminescence", TextColor.color(85, 255, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.GLOWING, 180 * 20 , 0)),
        potionColor = Color.fromRGB(0, 255, 179))

    val POTION_OF_CONSTITUTION = OdysseyPotion(
        name = "potion_of_constitution",
        material = Material.POTION,
        displayName = Component.text("Potion of Constitution", TextColor.color(85, 255, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.HEALTH_BOOST, 180 * 20 , 0)),
        potionColor = Color.fromRGB(209, 0, 49))

    /*-----------------------------------------------------------------------------------------------*/

    val POTION_OF_STONE_SKIN = OdysseyPotion(
        name = "potion_of_stone_skin",
        material = Material.POTION,
        displayName = Component.text("Potion of Stone Skin", TextColor.color(55, 55, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.ABSORPTION, 120 * 20 , 2),
            PotionEffect(PotionEffectType.SLOW, 120 * 20 , 0)),
        potionColor = Color.fromRGB(42, 51, 38))

    val POTION_OF_WRATH = OdysseyPotion(
        name = "potion_of_wrath",
        material = Material.POTION,
        displayName = Component.text("Potion of Wrath", TextColor.color(55, 55, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120 * 20 , 2),
            PotionEffect(PotionEffectType.HUNGER, 120 * 20 , 1)),
        potionColor = Color.fromRGB(250, 60, 17))

    val POTION_OF_WHIZ = OdysseyPotion(
        name = "potion_of_whiz",
        material = Material.POTION,
        displayName = Component.text("Whiz Potion", TextColor.color(55, 55, 255)),
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.SPEED, 120 * 20 , 2),
            PotionEffect(PotionEffectType.CONFUSION, 10 * 20 , 0),
            PotionEffect(PotionEffectType.HUNGER, 120 * 20 , 1)),
        potionColor = Color.fromRGB(220, 210, 217))

    /*-----------------------------------------------------------------------------------------------*/

    // Anglers' Potion
    // Night vision + water breathing + 2 Kelp
    // (10 minutes)
    val ANGLERS_DECOCTION = OdysseyPotion(
        name = "anglers_decoction",
        material = Material.POTION,
        displayName = Component.text("Anglers' Decoction", TextColor.color(255, 255, 55)),
        customModel = ItemModels.DECOCTION_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.NIGHT_VISION, 3 * 60 * 20 , 0),
            PotionEffect(PotionEffectType.WATER_BREATHING, 3 * 60 * 20, 0)),
        potionColor = Color.fromRGB(115, 215, 233))

    // Spelunkers' Potion
    // Haste + Glowing + 1 Honeycomb
    // (7 minutes)

    // Nether Owl Potion
    // Night Vision + Fire Resistance + 1 Weeping Vine
    // (10 minutes)

    /*-----------------------------------------------------------------------------------------------*/

    // CUSTOM DECOCTION
    // Potion + Potion + Thick Potion + Popped Chorus Fruit
    // Adds both potion effects at 40%

    val CUSTOM_DECOCTION = OdysseyPotion(
        name = "custom_decoction",
        material = Material.POTION,
        displayName = Component.text("Decoction", TextColor.color(255, 255, 55)),
        customModel = ItemModels.DECOCTION_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(185, 255, 155))

    val LARGE_POTION = OdysseyPotion(
        name = "large_potion",
        material = Material.POTION,
        displayName = Component.text("Large Potion of TODO", TextColor.color(255, 255, 55)),
        customModel = ItemModels.LARGE_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(185, 255, 155))

    /*-----------------------------------------------------------------------------------------------*/

    val FLASK_OF_ROT = OdysseyPotion(
        name = "flask_of_rot",
        material = Material.POTION,
        displayName = Component.text("Flask o' Rot", TextColor.color(114, 227, 154)),
        lore = listOf(Component.text("Rotting (1:00)", TextColor.color(114, 227, 154)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CONICAL_BOTTLE,
        potionColor = Color.fromRGB(114, 227, 154))

    val FLASK_OF_FROST = OdysseyPotion(
        name = "flask_of_frost",
        material = Material.POTION,
        displayName = Component.text("Flask o' Frost", TextColor.color(163, 211, 255)),
        lore = listOf(Component.text("Freezing (00:30)", TextColor.color(163, 211, 255)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CONICAL_BOTTLE,
        potionColor = Color.fromRGB(163, 211, 255))

    val FLASK_OF_TAR = OdysseyPotion(
        name = "flask_of_tar",
        material = Material.POTION,
        displayName = Component.text("Flask o' Tar", TextColor.color(66, 66, 38)),
        lore = listOf(Component.text("Tarred I (01:00)", TextColor.color(66, 66, 38)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CONICAL_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(66, 66, 38))

    val FLASK_OF_ABLAZE = OdysseyPotion(
        name = "flask_of_ablaze",
        material = Material.POTION,
        displayName = Component.text("Flask o' Ablaze", TextColor.color(247, 74, 0)),
        lore = listOf(Component.text("Blazing I (00:30)", TextColor.color(247, 74, 0)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CONICAL_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(247, 74, 0))

    val FLASK_OF_IRRADIATION = OdysseyPotion( // TODO: Change !!!
        name = "flask_of_irradiation",
        material = Material.POTION,
        displayName = Component.text("Flask o' Irradiation", TextColor.color(58, 50, 95)),
        lore = listOf(Component.text("Irradiated I (02:30)", TextColor.color(58, 50, 95)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CONICAL_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(247, 74, 0))

    val FLASK_OF_CORROSION = OdysseyPotion(
        name = "flask_of_corrosion",
        material = Material.POTION,
        displayName = Component.text("Flask o' Corrosion", TextColor.color(222, 255, 166)),
        lore = listOf(Component.text("Corrosion I (00:30)", TextColor.color(222, 255, 166)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CONICAL_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(247, 74, 0))

    val FLASK_OF_MIASMA = OdysseyPotion(
        name = "flask_of_miasma",
        material = Material.POTION,
        displayName = Component.text("Flask o' Miasma", TextColor.color(80, 20, 90)),
        lore = listOf(Component.text("Miasma I (02:30)", TextColor.color(80, 20, 90)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.CONICAL_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(247, 74, 0))

    // TONIC OF INVINCIBILITY

    val BOTTLE_OF_SOULS = OdysseyPotion(
        name = "bottle_of_souls",
        material = Material.POTION,
        displayName = Component.text("Bottled Souls", TextColor.color(94, 210, 215)),
        lore = listOf(Component.text("Soul Damage I", TextColor.color(94, 210, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.ROUND_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(0, 255, 179))

    val ACCURSED_BREW = OdysseyPotion(
        name = "accursed_brew",
        material = Material.POTION,
        displayName = Component.text("Accursed Brew", TextColor.color(137, 24, 40)),
        lore = listOf(Component.text("Accursed I", TextColor.color(137, 24, 40)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.ROUND_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(137, 24, 40))

    // MAYBE POTION OF KNOCK UP?

    val BOTTLE_OF_SHIMMER = OdysseyPotion(
        name = "bottle_of_shimmer",
        material = Material.POTION,
        displayName = Component.text("Bottle o' Shimmer", TextColor.color(78, 0, 161)),
        lore = listOf(Component.text("Shimmer (02:00)", TextColor.color(78, 0, 161)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
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
        FLASK_OF_ROT, FLASK_OF_FROST, FLASK_OF_TAR, FLASK_OF_ABLAZE, FLASK_OF_IRRADIATION, FLASK_OF_MIASMA, BOTTLE_OF_SOULS, ACCURSED_BREW, BOTTLE_OF_SHIMMER
    )
}
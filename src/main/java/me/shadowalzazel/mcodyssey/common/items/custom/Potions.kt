package me.shadowalzazel.mcodyssey.common.items.custom

import me.shadowalzazel.mcodyssey.common.effects.EffectColors
import me.shadowalzazel.mcodyssey.common.items.ItemConstructor.PotionConstructor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


internal object Potions {

    val CRYSTALLINE_POTION = PotionConstructor(
        material = Material.POTION,
        itemModel = "crystalline_potion",
        color = Color.fromRGB(147, 84, 255))

    val POTION_OF_LEVITATION = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "pearshape_bottle",
        capModel = "potion_cap",
        effects = listOf(PotionEffect(PotionEffectType.LEVITATION, 30 * 20, 0)),
        color = Color.fromRGB(55, 55, 55))

    val POTION_OF_WITHERING = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "pearshape_bottle",
        capModel = "potion_cap",
        effects = listOf(PotionEffect(PotionEffectType.WITHER, 30 * 20, 0)),
        color = Color.fromRGB(55, 55, 55))

    val POTION_OF_DARKNESS = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "pearshape_bottle",
        capModel = "potion_cap",
        effects = listOf(PotionEffect(PotionEffectType.DARKNESS, 30 * 20, 0)),
        color = Color.fromRGB(0, 50, 48))

    val POTION_OF_LUCK = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "pearshape_bottle",
        capModel = "potion_cap",
        effects = listOf(PotionEffect(PotionEffectType.LUCK, 3 * 60 * 20, 0)),
        color = Color.fromRGB(255, 157, 0))

    val POTION_OF_RESISTANCE = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "pearshape_bottle",
        capModel = "potion_cap",
        effects = listOf(PotionEffect(PotionEffectType.RESISTANCE, 3 * 60 * 20, 0)),
        color = Color.fromRGB(65, 97, 122))

    val POTION_OF_HASTE = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "pearshape_bottle",
        capModel = "potion_cap",
        effects = listOf(PotionEffect(PotionEffectType.HASTE, 3 * 60 * 20, 0)),
        color = Color.fromRGB(255, 233, 133))

    val POTION_OF_BIOLUMINESCENCE = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "pearshape_bottle",
        capModel = "potion_cap",
        effects = listOf(PotionEffect(PotionEffectType.GLOWING, 3 * 60 * 20, 0)),
        color = Color.fromRGB(0, 255, 179))

    val POTION_OF_CONSTITUTION = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "pearshape_bottle",
        capModel = "potion_cap",
        effects = listOf(PotionEffect(PotionEffectType.HEALTH_BOOST, 3 * 60 * 20, 0)),
        color = Color.fromRGB(209, 0, 49))

    val POTION_OF_STONE_SKIN = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "standard_bottle",
        capModel = "tall_cap",
        effects = listOf(
            PotionEffect(PotionEffectType.ABSORPTION, 2 * 60 * 20 , 2),
            PotionEffect(PotionEffectType.SLOWNESS, 2 * 60 * 20 , 0)),
        color = Color.fromRGB(42, 51, 38))

    val POTION_OF_WRATH = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "standard_bottle",
        capModel = "tall_cap",
        effects = listOf(
            PotionEffect(PotionEffectType.STRENGTH, 2 * 60 * 20 , 2),
            PotionEffect(PotionEffectType.HUNGER, 2 * 60 * 20 , 1)),
        color = Color.fromRGB(250, 60, 17))

    val POTION_OF_RECOUP = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "standard_bottle",
        capModel = "tall_cap",
        effects = listOf(
            PotionEffect(PotionEffectType.REGENERATION, 2 * 60 * 20 , 2),
            PotionEffect(PotionEffectType.WEAKNESS, 2 * 60 * 20 , 0)),
        color = Color.fromRGB(220, 210, 217))

    val POTION_OF_ZOOM = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "standard_bottle",
        capModel = "tall_cap",
        effects = listOf(
            PotionEffect(PotionEffectType.SPEED, 2 * 60 * 20 , 2),
            PotionEffect(PotionEffectType.HUNGER, 2 * 60 * 20 , 1),
            PotionEffect(PotionEffectType.NAUSEA, 10 * 20 , 0)),
        color = Color.fromRGB(220, 210, 217))

    val POTION_OF_SHIMMER = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "standard_bottle",
        capModel = "tall_cap",
        effects = listOf(
            PotionEffect(PotionEffectType.RESISTANCE, 60 * 20, 1),
            PotionEffect(PotionEffectType.STRENGTH, 60 * 20, 1),
            PotionEffect(PotionEffectType.GLOWING, 60 * 20, 1),
            PotionEffect(PotionEffectType.HUNGER, 60 * 20, 0)),
        color = EffectColors.SHIMMER.toItemColor())

    val ANGLERS_CONCOCTION = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "concoction_bottle",
        capModel = "screw_cap",
        effects = listOf(
            PotionEffect(PotionEffectType.NIGHT_VISION, 4 * 60 * 20 , 0),
            PotionEffect(PotionEffectType.WATER_BREATHING, 4 * 60 * 20, 0)),
        color = Color.fromRGB(115, 215, 233))

    val NETHER_OWL_CONCOCTION = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "concoction_bottle",
        capModel = "screw_cap",
        effects = listOf(
            PotionEffect(PotionEffectType.NIGHT_VISION, 3 * 60 * 20 , 0),
            PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3 * 60 * 20, 0)),
        color = Color.fromRGB(255, 105, 78))

    val SPELUNKERS_CONCOCTION = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "concoction_bottle",
        capModel = "screw_cap",
        effects = listOf(
            PotionEffect(PotionEffectType.HASTE, 3 * 60 * 20 , 0),
            PotionEffect(PotionEffectType.GLOWING, 3 * 60 * 20, 0)),
        color = Color.fromRGB(233, 213, 115))

    val CUSTOM_CONCOCTION = PotionConstructor(
        material = Material.POTION,
        itemModel = "alchemy_potion",
        bottleModel = "concoction_bottle",
        capModel = "screw_cap",
        color = Color.fromRGB(210, 222, 222))

    val POTION_VIAL = PotionConstructor(
        material = Material.POTION,
        itemModel = "potion_vial",
        color = Color.fromRGB(210, 222, 222))

}
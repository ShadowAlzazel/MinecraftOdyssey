package me.shadowalzazel.mcodyssey.items.arcane

import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

// SILMARIL_OF_YGGLADIEL
object SilmarilOfYggladiel : OdysseyItem("Silmaril Of Yggladiel",
    Material.AMETHYST_CLUSTER,
    Component.text("Silmaril Of Yggladiel", TextColor.color(255, 84, 255), TextDecoration.ITALIC),
    listOf(
        Component.text("A jewel fruit grown by the world tree on Lupercal", TextColor.color(192, 152, 255 )).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
        Component.text("Shining with stellar light...", TextColor.color(255, 227, 125)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.SILMARIL_OF_YGGLADIEL,
    mapOf(Enchantment.MENDING to 1))

// Rho-Annulus Schematics
object BabelAnnulusSchematics : OdysseyItem("Babel Annulus Schematics",
    Material.PAPER,
    Component.text("Babel Annulus", TextColor.color(255, 255, 85), TextDecoration.ITALIC).append(Component.text("-Schematics").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(255, 255, 85))),
    listOf(Component.text("Keple", TextColor.color(255, 255, 85))
        .append(Component.text("r-186f").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(255, 255, 85)))
        .append(Component.text("Babel Sys").color(TextColor.color(255, 255, 85)))
        .append(Component.text("tem. Vail's Test Site... Section A2").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(255, 255, 85)))
        .append(Component.text("002").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(255, 255, 85)))
        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.BABEL_ANNULUS_SCHEMATICS)

// FIX!!
// DormantCyberStar
object DormantCyberStar : OdysseyItem("Dormant Cyber Star",
    Material.NETHER_STAR,
    Component.text("Dormant Cyber System-", TextColor.color(255, 255, 85), TextDecoration.ITALIC).append(Component.text("Mini-Matrioshka").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(255, 255, 85))),
    listOf(Component.text("Something is speaking to you...", TextColor.color(80, 60, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.DORMANT_CYBER_STAR,
    mapOf(Enchantment.LOYALTY to 5))

// FRUIT_OF_ERISHKIGAL
object FruitOfErishkigal : OdysseyItem("Fruit of Erishkigal",
    Material.ENCHANTED_GOLDEN_APPLE,
    Component.text("Fruit of Erishkigal", TextColor.color(255, 84, 255), TextDecoration.ITALIC),
    listOf(Component.text("A fruit engineered at the atomic level", TextColor.color(80, 60, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
        Component.text("With the power to alter one's life...", TextColor.color(255, 41, 119)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.FRUIT_OF_ERISHKIGAL,
    mapOf(Enchantment.MENDING to 1))


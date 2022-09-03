package me.shadowalzazel.mcodyssey.items.odyssey

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.resources.CustomModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

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
    CustomModels.BABEL_ANNULUS_SCHEMATICS)

// FIX!!
// DormantSentientStar
object DormantSentientStar : OdysseyItem("Dormant Star System",
    Material.NETHER_STAR,
    Component.text("Dormant Star System-", TextColor.color(255, 255, 85), TextDecoration.ITALIC).append(Component.text("Mini-Matrioshka").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(255, 255, 85))),
    listOf(Component.text("Something is speaking to you...", TextColor.color(80, 60, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.DORMANT_SENTIENT_STAR,
    mapOf(Enchantment.LOYALTY to 5))


// IMPURE_ANTI_MATTER_SHARD
object ImpureAntiMatterShard : OdysseyItem("Impure Anti-matter Shard",
    Material.PRISMARINE_SHARD,
    Component.text("Impure Anti-matter Shard", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("An impure yet stabilized shard of anti-matter...", TextColor.color(80, 60, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.IMPURE_ANTI_MATTER_SHARD,
    mapOf(Enchantment.ARROW_INFINITE to 1))


// PURE_ANTIMATTER_CRYSTAL
object PureAntimatterCrystal : OdysseyItem("Pure Anti-matter Crystal",
    Material.PRISMARINE_SHARD,
    Component.text("Pure Anti-matter Shard", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("DANGER! DANGER! DANGER!", TextColor.color(80, 60, 170))
        .append(Component.text("DANGER! DANGER!").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(80, 60, 170)))
        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
        Component.text("Purely Unstable!!! Quantum Disintegration Imminent!", TextColor.color(80, 60, 170))),
    CustomModels.PURE_ANTIMATTER_CRYSTAL,
    mapOf(Enchantment.ARROW_INFINITE to 1))


// NEUTRONIUM_BARK_INGOT
object NeutroniumBarkIngot: OdysseyItem("Neutronium-Bark Ingot",
    Material.NETHERITE_INGOT,
    Component.text("Neutronium-Bark Ingot", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("A refined plank of very dense matter...", TextColor.color(80, 60, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.NEUTRONIUM_BARK_INGOT,
    mapOf(Enchantment.DURABILITY to 5))


// FRUIT_OF_ERISHKIGAL
object FruitOfErishkigal : OdysseyItem("Fruit of Erishkigal",
    Material.ENCHANTED_GOLDEN_APPLE,
    Component.text("Fruit of Erishkigal", TextColor.color(255, 84, 255), TextDecoration.ITALIC),
    listOf(Component.text("A fruit engineered at the atomic level", TextColor.color(80, 60, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("With the power to alter one's life...", TextColor.color(255, 41, 119)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.FRUIT_OF_ERISHKIGAL,
    mapOf(Enchantment.MENDING to 1))


// SILMARIL_OF_YGGLADIEL
object SilmarilOfYggladiel : OdysseyItem("Silmaril Of Yggladiel",
    Material.AMETHYST_CLUSTER,
    Component.text("Silmaril Of Yggladiel", TextColor.color(255, 84, 255), TextDecoration.ITALIC),
    listOf(Component.text("A jewel fruit grown by the world tree on Lupercal", TextColor.color(192, 152, 255 )).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
        Component.text("Shining with stellar light...", TextColor.color(255, 227, 125)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.SILMARIL_OF_YGGLADIEL,
    mapOf(Enchantment.MENDING to 1))
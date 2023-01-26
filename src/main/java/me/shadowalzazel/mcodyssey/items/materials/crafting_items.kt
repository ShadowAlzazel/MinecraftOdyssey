package me.shadowalzazel.mcodyssey.items.materials

import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

// SOUL_CRYSTAL
object SoulCrystal : OdysseyItem("Soul Crystal",
    Material.QUARTZ,
    Component.text("Soul Crystal", TextColor.color(37, 198, 205), TextDecoration.ITALIC),
    listOf(Component.text("A crystal forged by lost souls...", TextColor.color(27, 104, 115)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.SOUL_CRYSTAL)

// SORROWING_SOUL
object SorrowingSoul : OdysseyItem("Sorrowing Soul",
    Material.PAPER,
    Component.text("Sorrowing Soul", TextColor.color(142, 42, 17), TextDecoration.ITALIC),
    listOf(Component.text("A catalyst written with souls...", TextColor.color(180, 80, 32)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.SORROWING_SOUL)

// SOUL_CATALYST
object SoulCatalyst : OdysseyItem("Soul Catalyst",
    Material.AMETHYST_SHARD,
    Component.text("Soul Catalyst", TextColor.color(36, 29, 50), TextDecoration.ITALIC),
    listOf(Component.text("A device made to contain souls...", TextColor.color(23, 170, 177)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.SOUL_CATALYST)

// SOUL_STEEL_INGOT
object SoulSteelIngot : OdysseyItem("Soul Steel Ingot",
    Material.IRON_INGOT,
    Component.text("Soul Steel Ingot", TextColor.color(88, 95, 123), TextDecoration.ITALIC),
    listOf(Component.text("An ingot forged with souls...", TextColor.color(57, 63, 84)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.SOUL_STEEL_INGOT)

// ELENCUILE_ESSENCE
object IdescineEssence : OdysseyItem("Elencuile Essence",
    Material.HONEY_BOTTLE,
    Component.text("Elencuile Essence", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("A sappy substance with the life of the stars", TextColor.color(160, 45, 160)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.ELENCUILE_ESSENCE)

// PURE_ANTIMATTER_CRYSTAL
object PureAntimatterCrystal : OdysseyItem("Pure Anti-matter Crystal",
    Material.PRISMARINE_SHARD,
    Component.text("Pure Anti-matter Shard", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("DANGER! DANGER! DANGER!", TextColor.color(80, 60, 170))
        .append(Component.text("DANGER! DANGER!").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(80, 60, 170)))
        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
        Component.text("Purely Unstable!!! Quantum Disintegration Imminent!", TextColor.color(80, 60, 170))),
    OdysseyItemModels.PURE_ANTIMATTER_CRYSTAL,
    mapOf(Enchantment.ARROW_INFINITE to 1))

// NEUTRONIUM_BARK_INGOT
object NeutroniumBarkIngot: OdysseyItem("Neutronium-Bark Ingot",
    Material.NETHERITE_INGOT,
    Component.text("Neutronium-Bark Ingot", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("A refined plank of very dense matter...", TextColor.color(80, 60, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.NEUTRONIUM_BARK_INGOT,
    mapOf(Enchantment.DURABILITY to 5))

package me.shadowalzazel.mcodyssey.items.miscellaneous

import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

// Paper's of Arcus
object PapersOfArcus : OdysseyItem("Paper's Of Arcus",
    Material.PAPER,
    Component.text("Paper's Of Arcus-", TextColor.color(85, 85, 85), TextDecoration.ITALIC).append(Component.text("The Imperium Vol.${(0..13).random()}").decorate(TextDecoration.OBFUSCATED)).color(TextColor.color(85, 85, 85)),
    customModel = OdysseyItemModels.PAPERS_OF_ARCUS)

// Galvanized Steel
object GalvanizedSteel : OdysseyItem("Galvanized Steel",
    Material.IRON_BLOCK,
    Component.text("Galvanized Steel", TextColor.color(85, 85, 85), TextDecoration.ITALIC),
    customModel = OdysseyItemModels.GALVANIZED_STEEL)

// Pure-Alloy Copper
object PureAlloyCopper : OdysseyItem("Pure-Alloy Copper",
    Material.COPPER_BLOCK,
    Component.text("Pure-Alloy Copper", TextColor.color(85, 85, 85), TextDecoration.ITALIC),
    customModel = OdysseyItemModels.PURE_ALLOY_COPPER)

// Pure-Alloy Gold
object PureAlloyGold : OdysseyItem("Pure-Alloy Gold",
    Material.GOLD_BLOCK,
    Component.text("Pure-Alloy Gold", TextColor.color(85, 85, 85), TextDecoration.ITALIC),
    customModel = OdysseyItemModels.PURE_ALLOY_GOLD)

// Polymorphic Glue
object PolymorphicGlue : OdysseyItem("Polymorphic Glue",
    Material.SLIME_BLOCK,
    Component.text("Polymorphic Glue", TextColor.color(85, 85, 85), TextDecoration.ITALIC),
    customModel = OdysseyItemModels.POLYMORPHIC_GLUE)

// Hawking Containment Unit
object HawkingEntangledUnit : OdysseyItem("Hawking Entangled Unit",
    Material.ENDER_CHEST,
    Component.text("Hawking Entangled Unit-", TextColor.color(85, 85, 255), TextDecoration.ITALIC).append(Component.text("0x1000068").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(85, 85, 255))),
    listOf(Component.text("A linked vacuum of matter and energy...", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.HAWKING_ENTANGLED_UNIT)

// Kugelblitz Containment Silo
object KugelblitzContainmentUnit : OdysseyItem("Kugelblitz Containment Unit",
    Material.BLACK_SHULKER_BOX,
    Component.text("Kugelblitz Containment Unit-", TextColor.color(85, 85, 255), TextDecoration.ITALIC).append(Component.text("0x5000049").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(85, 85, 255))),
    listOf(Component.text("A portable device capable of holding large amounts of matter", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.KUGELBLITZ_CONTAINMENT_UNIT)


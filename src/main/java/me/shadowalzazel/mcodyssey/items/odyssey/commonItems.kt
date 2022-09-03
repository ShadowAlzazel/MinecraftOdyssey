package me.shadowalzazel.mcodyssey.items.odyssey

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.resources.CustomModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

// Paper's of Arcus
object PapersOfArcus : OdysseyItem("Paper's Of Arcus",
    Material.PAPER,
    Component.text("Paper's Of Arcus-", TextColor.color(85, 85, 85), TextDecoration.ITALIC).append(Component.text("The Imperium Vol.${(0..13).random()}").decorate(TextDecoration.OBFUSCATED)).color(TextColor.color(85, 85, 85)),
    customModel = CustomModels.PAPERS_OF_ARCUS)

// Galvanized Steel
object GalvanizedSteel : OdysseyItem("Galvanized Steel",
    Material.IRON_BLOCK,
    Component.text("Galvanized Steel", TextColor.color(85, 85, 85), TextDecoration.ITALIC),
    customModel = CustomModels.GALVANIZED_STEEL)

// Pure-Alloy Copper
object PureAlloyCopper : OdysseyItem("Pure-Alloy Copper",
    Material.COPPER_BLOCK,
    Component.text("Pure-Alloy Copper", TextColor.color(85, 85, 85), TextDecoration.ITALIC),
    customModel = CustomModels.PURE_ALLOY_COPPER)
// Pure-Alloy Gold
object PureAlloyGold : OdysseyItem("Pure-Alloy Gold",
    Material.GOLD_BLOCK,
    Component.text("Pure-Alloy Gold", TextColor.color(85, 85, 85), TextDecoration.ITALIC),
    customModel = CustomModels.PURE_ALLOY_GOLD)

// Hawking Containment Unit
object HawkingEntangledUnit : OdysseyItem("Hawking Entangled Unit",
    Material.ENDER_CHEST,
    Component.text("Hawking Entangled Unit-", TextColor.color(85, 85, 255), TextDecoration.ITALIC).append(Component.text("0x1000068").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(85, 85, 255))),
    listOf(Component.text("A linked vacuum of matter and energy...", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.HAWKING_ENTANGLED_UNIT)

// Kugelblitz Containment Silo
object KugelblitzContainmentUnit : OdysseyItem("Kugelblitz Containment Unit",
    Material.PURPLE_SHULKER_BOX,
    Component.text("Hawking Containment Unit-", TextColor.color(85, 85, 255), TextDecoration.ITALIC).append(Component.text("0x5000049").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(85, 85, 255))),
    listOf(Component.text("A portable device capable of holding large amounts of matter", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.KUGELBLITZ_CONTAINMENT_UNIT)

// Polymorphic Glue
object PolymorphicGlue : OdysseyItem("Polymorphic Glue",
    Material.SLIME_BLOCK,
    Component.text("Polymorphic Glue", TextColor.color(85, 85, 85), TextDecoration.ITALIC),
    listOf(Component.text("Industrial Glue...", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    customModel = CustomModels.POLYMORPHIC_GLUE)


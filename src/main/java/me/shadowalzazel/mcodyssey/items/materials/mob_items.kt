package me.shadowalzazel.mcodyssey.items.materials

import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

// COAGULATED_BLOOD
object CoagulatedBlood : OdysseyItem("Coagulated Blood",
    Material.SLIME_BALL,
    Component.text("Coagulated Blood", TextColor.color(182, 42, 17), TextDecoration.ITALIC),
    listOf(Component.text("A sticky bloody ball...", TextColor.color(210, 60, 62)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.COAGULATED_BLOOD)

// ECTOPLASM
object Ectoplasm : OdysseyItem("Ectoplasm",
    Material.BONE,
    Component.text("Ectoplasm", TextColor.color(37, 198, 205), TextDecoration.ITALIC),
    listOf(Component.text("Escapist Souls trapped in a decaying substance...", TextColor.color(97, 75, 61)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.ECTOPLASM)

// IRRADIATED_SHARD
object IrradiatedShard : OdysseyItem("Irradiated Shard",
    Material.PRISMARINE_SHARD,
    Component.text("Irradiated Shard", TextColor.color(198, 196, 178), TextDecoration.ITALIC),
    listOf(Component.text("A shard dangerous to handle...", TextColor.color(191, 126, 85)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.IRRADIATED_SHARD)

// IRRADIATED_ROD
object IrradiatedRod : OdysseyItem("Irradiated Rod",
    Material.PRISMARINE_SHARD,
    Component.text("Irradiated Rod", TextColor.color(58, 50, 95), TextDecoration.ITALIC),
    listOf(Component.text("A mechanical piece, dangerous to handle...", TextColor.color(191, 126, 85)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.IRRADIATED_ROD)

// WARDEN_ENTRAILS
object WardenEntrails : OdysseyItem("Warden Entrails",
    Material.ROTTEN_FLESH,
    Component.text("Warden Entrails", TextColor.color(24, 70, 74), TextDecoration.ITALIC),
    listOf(Component.text("The remains of the ancient warden...", TextColor.color(24, 70, 74)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.WARDEN_ENTRAILS)

// BONE_OF_FROST
object BoneOfFrost : OdysseyItem("Bone Of Frost",
    Material.BONE,
    Component.text("Bone Of Frost", TextColor.color(163, 211, 255), TextDecoration.ITALIC),
    listOf(Component.text("The remains of the ancient warden...", TextColor.color(163, 211, 255)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.WARDEN_ENTRAILS)

// TOTEM_OF_VEXING
object TotemOfVexing : OdysseyItem("Totem of Vexing",
    Material.MILK_BUCKET,
    Component.text("Totem of Vexing", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("A totem with vexing properties", TextColor.color(112, 123, 153)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.TOTEM_OF_VEXING)
package me.shadowalzazel.mcodyssey.items.misc

import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

// HOURGLASS_FROM_BABEL
object HourglassFromBabel : OdysseyItem("Hourglass from Babel",
    Material.RAW_GOLD,
    Component.text("Hourglass from Babel", TextColor.color(191, 166, 95), TextDecoration.ITALIC),
    listOf(Component.text("A strange hourglass with temporal properties...", TextColor.color(231, 166, 95)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.HOURGLASS_FROM_BABEL)

// SOUL_ANCHOR
object EnigmaticAnchor : OdysseyItem("Enigmatic Anchor",
    Material.RAW_IRON,
    Component.text("Enigmatic Anchor", TextColor.color(57, 63, 84), TextDecoration.ITALIC),
    listOf(Component.text("An unusual device to anchor a soul...", TextColor.color(23, 170, 177)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.ENIGMATIC_ANCHOR)
// Yone E

object GemmaPrimus : OdysseyItem("Primogem",
    Material.EMERALD,
    Component.text("Primogem", TextColor.color(226, 137, 69), TextDecoration.ITALIC),
    listOf(
        Component.text("A primordial crystalline gem that's beyond", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
        Component.text("the test world. Shines with the condensed hopes", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
        Component.text("and dreams of universes that once were.", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.GEMMA_PRIMUS)

// TODO: Flashbang, Knock-Up, Freeze,

object RestlessGale : OdysseyItem("Hourglass from Babel",
    Material.RAW_GOLD,
    Component.text("Hourglass from Babel", TextColor.color(191, 166, 95), TextDecoration.ITALIC),
    listOf(Component.text("A strange hourglass with temporal properties...", TextColor.color(231, 166, 95)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.HOURGLASS_FROM_BABEL)

// TOTEM_OF_VEXING
object TotemOfVexing : OdysseyItem("Totem of Vexing",
    Material.MILK_BUCKET,
    Component.text("Totem of Vexing", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("A totem with vexing properties", TextColor.color(112, 123, 153)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.TOTEM_OF_VEXING)

// NEUTRONIUM_BARK_SCRAPS
object NeutroniumBarkScraps : OdysseyItem("Neutronium-Bark Scraps",
    Material.NETHERITE_SCRAP,
    Component.text("Neutronium-Bark Scraps", TextColor.color(85, 255, 255), TextDecoration.ITALIC),
    listOf(Component.text("Pieces of bark with extreme weight and density", TextColor.color(45, 45, 45)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.NEUTRONIUM_BARK_SCRAPS)
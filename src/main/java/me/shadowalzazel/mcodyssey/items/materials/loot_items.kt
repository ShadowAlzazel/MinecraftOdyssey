package me.shadowalzazel.mcodyssey.items.materials

import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

// NEPTUNIAN_DIAMOND
object NeptunianDiamond : OdysseyItem("Neptunian-Diamond",
    Material.DIAMOND,
    Component.text("Neptunian-Diamond", TextColor.color(47, 122, 228), TextDecoration.ITALIC),
    listOf(Component.text("A diamond forged inside a colossal planet refined to an impressive caliber", TextColor.color(47, 122, 228)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.NEPTUNIAN_DIAMOND,
    mapOf(Enchantment.LOOT_BONUS_BLOCKS to 1))

// JOVIAN_EMERALD
object JovianEmerald : OdysseyItem("Jovian-Emerald",
    Material.EMERALD,
    Component.text("Jovian-Emerald", TextColor.color(210, 234, 64), TextDecoration.ITALIC),
    listOf(Component.text("An emerald grown near a Jovian super-planet to unmatched pristine", TextColor.color(210, 234, 64)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.JOVIAN_EMERALD,
    mapOf(Enchantment.LOOT_BONUS_BLOCKS to 1))

// KUNZITE
object Kunzite : OdysseyItem("Kunzite",
    Material.EMERALD,
    Component.text("Kunzite", TextColor.color(255, 150, 210), TextDecoration.ITALIC),
    listOf(Component.text("Kunzite", TextColor.color(255, 150, 210)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.JOVIAN_EMERALD)

// RUBY
object Ruby : OdysseyItem("Ruby",
    Material.EMERALD,
    Component.text("Ruby", TextColor.color(210, 64, 64), TextDecoration.ITALIC),
    listOf(Component.text("Ruby", TextColor.color(210, 64, 64)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.JOVIAN_EMERALD)

// IMPURE_ANTI_MATTER_SHARD
object ImpureAntiMatterShard : OdysseyItem("Impure Anti-matter Shard",
    Material.PRISMARINE_SHARD,
    Component.text("Impure Anti-matter Shard", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("An impure yet stabilized shard of anti-matter...", TextColor.color(80, 60, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.IMPURE_ANTI_MATTER_SHARD,
    mapOf(Enchantment.ARROW_INFINITE to 1))

// ELENCUILE_SAPLING
object ElencuileSaplings : OdysseyItem("Elencuile Saplings",
    Material.OAK_SAPLING,
    Component.text("Elencuile Saplings", TextColor.color(85, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("A seed from the stars not ready to fully mature", TextColor.color(45, 85, 45)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.ELENCUILE_SAPLING) // Star - life : Hydrogen color

// NEUTRONIUM_BARK_SCRAPS
object NeutroniumBarkScraps : OdysseyItem("Neutronium-Bark Scraps",
    Material.NETHERITE_SCRAP,
    Component.text("Neutronium-Bark Scraps", TextColor.color(85, 255, 255), TextDecoration.ITALIC),
    listOf(Component.text("Pieces of bark with extreme weight and density", TextColor.color(45, 45, 45)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.NEUTRONIUM_BARK_SCRAPS)

// PRIMOGEM
object Primogem : OdysseyItem("Primogem",
    Material.EMERALD,
    Component.text("Primogem", TextColor.color(226, 137, 69), TextDecoration.ITALIC),
    listOf(
        Component.text("A primordial crystalline gem that's beyond", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
        Component.text("the test world. Shines with the condensed hopes", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
        Component.text("and dreams of universes that once were.", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.PRIMOGEM)
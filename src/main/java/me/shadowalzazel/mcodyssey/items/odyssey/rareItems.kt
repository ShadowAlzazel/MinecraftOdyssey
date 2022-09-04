package me.shadowalzazel.mcodyssey.items.odyssey

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.assets.CustomModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

// Refined Neptunian Diamond
object RefinedNeptunianDiamonds : OdysseyItem("Refined Neptunian-Diamond",
    Material.DIAMOND,
    Component.text("Refined Neptunian-Diamond", TextColor.color(85, 255, 255), TextDecoration.ITALIC),
    listOf(Component.text("A diamond forged inside a colossal planet refined to an impressive caliber", TextColor.color(47, 122, 228)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.NEPTUNIAN_DIAMOND,
    mapOf(Enchantment.LOOT_BONUS_BLOCKS to 1))

// Refined Iojovian Emerald
object RefinedIojovianEmeralds : OdysseyItem("Refined Iojovian-Emerald",
    Material.EMERALD,
    Component.text("Refined Iojovian-Emerald", TextColor.color(85, 255, 255), TextDecoration.ITALIC),
    listOf(Component.text("An emerald grown near a Jovian super-planet to unmatched pristine", TextColor.color(210, 234, 64)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.IOJOVIAN_EMERALD,
    mapOf(Enchantment.LOOT_BONUS_BLOCKS to 1))

// Neutronium Scraps
object NeutroniumBarkScraps : OdysseyItem("Neutronium-Bark Scraps",
    Material.NETHERITE_SCRAP,
    Component.text("Neutronium-Bark Scraps", TextColor.color(85, 255, 255), TextDecoration.ITALIC),
    listOf(Component.text("Pieces of bark with extreme weight and density", TextColor.color(45, 45, 45)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.NEUTRONIUM_BARK_SCRAPS)

// Idescine Sapling
object IdescineSaplings : OdysseyItem("Idescine Saplings",
    Material.OAK_SAPLING,
    Component.text("Idescine Saplings", TextColor.color(85, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("A seed not ready to fully mature", TextColor.color(45, 85, 45)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("due to the conditions of the test-world...", TextColor.color(45, 85, 45)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.IDESCINE_SAPLINGS)

// Idescine Essence
object IdescineEssence : OdysseyItem("Idescine Essence",
    Material.HONEY_BOTTLE,
    Component.text("Idescine Essence", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("A sappy substance brimming with life", TextColor.color(160, 45, 160)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.IDESCINE_ESSENCE)
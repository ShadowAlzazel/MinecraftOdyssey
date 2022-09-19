package me.shadowalzazel.mcodyssey.items.odyssey

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.constants.ItemModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

// Refined Neptunian Diamond
object RefinedNeptunianDiamonds : OdysseyItem("Refined Neptunian-Diamond",
    Material.DIAMOND,
    Component.text("Refined Neptunian-Diamond", TextColor.color(47, 122, 228), TextDecoration.ITALIC),
    listOf(Component.text("A diamond forged inside a colossal planet refined to an impressive caliber", TextColor.color(47, 122, 228)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.NEPTUNIAN_DIAMOND,
    mapOf(Enchantment.LOOT_BONUS_BLOCKS to 1))

// Refined Iojovian Emerald
object RefinedIojovianEmeralds : OdysseyItem("Refined Iojovian-Emerald",
    Material.EMERALD,
    Component.text("Refined Iojovian-Emerald", TextColor.color(210, 234, 64), TextDecoration.ITALIC),
    listOf(Component.text("An emerald grown near a Jovian super-planet to unmatched pristine", TextColor.color(210, 234, 64)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.IOJOVIAN_EMERALD,
    mapOf(Enchantment.LOOT_BONUS_BLOCKS to 1))

// Neutronium Scraps
object NeutroniumBarkScraps : OdysseyItem("Neutronium-Bark Scraps",
    Material.NETHERITE_SCRAP,
    Component.text("Neutronium-Bark Scraps", TextColor.color(85, 255, 255), TextDecoration.ITALIC),
    listOf(Component.text("Pieces of bark with extreme weight and density", TextColor.color(45, 45, 45)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.NEUTRONIUM_BARK_SCRAPS)

// Idescine Sapling
object IdescineSaplings : OdysseyItem("Idescine Saplings",
    Material.OAK_SAPLING,
    Component.text("Idescine Saplings", TextColor.color(85, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("A seed not ready to fully mature", TextColor.color(45, 85, 45)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("due to the conditions of the test-world...", TextColor.color(45, 85, 45)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.IDESCINE_SAPLINGS)

// Idescine Essence
object IdescineEssence : OdysseyItem("Idescine Essence",
    Material.HONEY_BOTTLE,
    Component.text("Idescine Essence", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("A sappy substance brimming with life", TextColor.color(160, 45, 160)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.IDESCINE_ESSENCE)

// Irradiated Shard
object IrradiatedShard : OdysseyItem("Irradiated Shard",
    Material.PRISMARINE_SHARD,
    Component.text("Irradiated Shard", TextColor.color(198, 196, 178), TextDecoration.ITALIC),
    listOf(Component.text("A shard dangerous to handle...", TextColor.color(191, 126, 85)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.IRRADIATED_SHARD)

// Irradiated Rod
object IrradiatedRod : OdysseyItem("Irradiated Rod",
    Material.PRISMARINE_SHARD,
    Component.text("Irradiated Rod", TextColor.color(58, 50, 95), TextDecoration.ITALIC),
    listOf(Component.text("A mechanical piece, dangerous to handle...", TextColor.color(191, 126, 85)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.IRRADIATED_ROD)

// Breeze in a Bottle
object BreezeInABottle : OdysseyItem("Breeze in a Bottle",
    Material.GLASS_BOTTLE,
    Component.text("Breeze in a Bottle", TextColor.color(74, 140, 234), TextDecoration.ITALIC),
    listOf(Component.text("The wind contained?", TextColor.color(74, 140, 234)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.BREEZE_IN_A_BOTTLE)

// Warden Entrails
object WardenEntrails : OdysseyItem("Warden Entrails",
    Material.ROTTEN_FLESH,
    Component.text("Warden Entrails", TextColor.color(24, 70, 74), TextDecoration.ITALIC),
    listOf(Component.text("The remains of the ancient warden...", TextColor.color(24, 70, 74)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.WARDEN_ENTRAILS)

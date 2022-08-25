package me.shadowalzazel.mcodyssey.items.odyssey

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.models.CustomModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

// Refined Neptunian Diamond
object RefinedNeptunianDiamonds : OdysseyItem("Refined Neptunian-Diamond",
    Material.DIAMOND,
    Component.text("Refined Neptunian-Diamond", TextColor.color(85, 255, 255), TextDecoration.ITALIC),
    listOf("A diamond forged inside a colossal planet refined to an impressive caliber"),
    CustomModels.NEPTUNIAN_DIAMOND,
    mapOf(Enchantment.LOOT_BONUS_BLOCKS to 1))

// Refined Iojovian Emerald
object RefinedIojovianEmeralds : OdysseyItem("Refined Iojovian-Emerald",
    Material.EMERALD,
    Component.text("Refined Iojovian-Emerald", TextColor.color(85, 255, 255), TextDecoration.ITALIC),
    listOf("An emerald grown near a Jovian super-planet to unmatched pristine"),
    CustomModels.IOJOVIAN_EMERALD,
    mapOf(Enchantment.LOOT_BONUS_BLOCKS to 1))

// Neutronium Scraps
object NeutroniumBarkScraps : OdysseyItem("Neutronium-Bark Scraps",
    Material.NETHERITE_SCRAP,
    Component.text("Neutronium-Bark Scraps", TextColor.color(85, 255, 255), TextDecoration.ITALIC),
    customModel = CustomModels.NEUTRONIUM_BARK_SCRAPS)

// Idescine Sapling
object IdescineSaplings : OdysseyItem("Idescine Saplings",
    Material.OAK_SAPLING,
    Component.text("Idescine Saplings", TextColor.color(85, 255, 85), TextDecoration.ITALIC),
    listOf("A seed not ready to fully mature", "due to the conditions of the test-world..."),
    CustomModels.IDESCINE_SAPLINGS)

// Idescine Essence
object IdescineEssence : OdysseyItem("Idescine Essence",
    Material.HONEY_BOTTLE,
    Component.text("Idescine Essence", TextColor.color(85, 255, 85), TextDecoration.ITALIC),
    listOf("${ChatColor.DARK_PURPLE}${ChatColor.ITALIC}A sappy substance brimming with life"),
    CustomModels.IDESCINE_ESSENCE)
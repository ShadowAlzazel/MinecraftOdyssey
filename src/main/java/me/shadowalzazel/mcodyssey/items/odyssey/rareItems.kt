package me.shadowalzazel.mcodyssey.items.odyssey

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.resources.CustomModels
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

// Refined Neptunian Diamond
object RefinedNeptunianDiamonds : OdysseyItem("Refined Neptunian-Diamond", Material.DIAMOND, customModel = CustomModels.NEPTUNIAN_DIAMOND) {
    override val odysseyDisplayName: String =
        "${ChatColor.AQUA}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("A diamond forged inside a colossal planet refined to an impressive caliber")
    override val isEnchanted: Boolean = true
    override val someEnchantType: Enchantment = Enchantment.LOOT_BONUS_BLOCKS
    override val someEnchantLevel: Int = 1
}

// Refined Iojovian Emerald
object RefinedIojovianEmeralds : OdysseyItem("Refined Iojovian-Emerald", Material.EMERALD, customModel = CustomModels.IOJOVIAN_EMERALD) {
    override val odysseyDisplayName: String =
        "${ChatColor.AQUA}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("An emerald grown near a Jovian super-planet to unmatched pristine")
    override val isEnchanted: Boolean = true
    override val someEnchantType: Enchantment = Enchantment.LOOT_BONUS_BLOCKS
    override val someEnchantLevel: Int = 1
}

// Neutronium Scraps
object NeutroniumBarkScraps : OdysseyItem("Neutronium-Bark Scraps", Material.NETHERITE_SCRAP) {
    override val odysseyDisplayName: String = "${ChatColor.DARK_GRAY}${ChatColor.ITALIC}$name"
}

// Idescine Sapling
object IdescineSaplings : OdysseyItem("Idescine Saplings", Material.OAK_SAPLING) {
    override val odysseyDisplayName: String =
        "${ChatColor.GREEN}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("A seed not ready to fully mature", "due to the conditions of the test-world...")
}

// Idescine Essence
object IdescineEssence : OdysseyItem("Idescine Essence", Material.HONEY_BOTTLE) {
    override val odysseyDisplayName: String =
        "${ChatColor.GREEN}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.DARK_PURPLE}${ChatColor.ITALIC}A sappy substance brimming with life")
}
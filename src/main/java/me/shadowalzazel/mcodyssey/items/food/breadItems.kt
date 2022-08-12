package me.shadowalzazel.mcodyssey.items.food

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import org.bukkit.ChatColor
import org.bukkit.Material

// FRENCH_TOAST
object FrenchToast : OdysseyItem("French Toast", Material.BREAD) {
    override val odysseyDisplayName: String = "${ChatColor.RED}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GRAY}French Toast!")
}

// SUGARY_BREAD
object SugaryBread : OdysseyItem("Sugary Bread", Material.BREAD) {
    override val odysseyDisplayName: String = "${ChatColor.RED}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GRAY}Sugary Bread!")
}
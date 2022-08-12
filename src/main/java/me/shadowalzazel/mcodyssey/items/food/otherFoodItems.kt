package me.shadowalzazel.mcodyssey.items.food

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import org.bukkit.ChatColor
import org.bukkit.Material

// BACON
object Bacon : OdysseyItem("Bacon", Material.COOKED_PORKCHOP) {
    override val odysseyDisplayName: String = "${ChatColor.RED}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GRAY}Bacon!")
}

// BEETROOT_COOKIE
object SalmonRollSushi : OdysseyItem("Salmon Roll", Material.COOKED_SALMON) {
    override val odysseyDisplayName: String = "${ChatColor.RED}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GRAY}Salmon Roll Sushi!")
}
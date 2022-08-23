package me.shadowalzazel.mcodyssey.items.food

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.resources.CustomModels
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material

// BACON
object Bacon : OdysseyItem("Bacon", Material.COOKED_PORKCHOP, Component.text("${ChatColor.RED}${ChatColor.ITALIC}${"Bacon"}"),
    listOf("${ChatColor.GRAY}Bacon!"), CustomModels.BACON)

// SALMON_ROLL
object SalmonRollSushi : OdysseyItem("Salmon Roll", Material.COOKED_SALMON, Component.text("${ChatColor.RED}${ChatColor.ITALIC}${"Salmon Roll"}"),
    listOf("${ChatColor.GRAY}Salmon Roll Sushi!"), CustomModels.SALMON_ROLL)
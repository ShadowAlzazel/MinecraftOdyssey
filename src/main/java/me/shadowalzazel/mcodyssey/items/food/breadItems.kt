package me.shadowalzazel.mcodyssey.items.food

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.resources.CustomModels
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material

// FRENCH_TOAST
object FrenchToast : OdysseyItem("French Toast", Material.BREAD, Component.text("${ChatColor.RED}${ChatColor.ITALIC}${"French Toast"}"),
    listOf("${ChatColor.GRAY}French Toast!"), CustomModels.FRENCH_TOAST)

// SUGARY_BREAD
object SugaryBread : OdysseyItem("Sugary Bread", Material.BREAD, Component.text("${ChatColor.RED}${ChatColor.ITALIC}${"Sugary Bread"}"),
    listOf("${ChatColor.GRAY}Sugary Bread!"), CustomModels.SUGARY_BREAD)
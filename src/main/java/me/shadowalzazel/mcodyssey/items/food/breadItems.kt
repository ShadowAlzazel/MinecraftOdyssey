package me.shadowalzazel.mcodyssey.items.food

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.models.CustomModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.ChatColor
import org.bukkit.Material

// FRENCH_TOAST
object FrenchToast : OdysseyItem("French Toast",
    Material.BREAD,
    Component.text("French Toast", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf("${ChatColor.GRAY}French Toast!"),
    CustomModels.FRENCH_TOAST)

// SUGARY_BREAD
object SugaryBread : OdysseyItem("Sugary Bread",
    Material.BREAD,
    Component.text("Sugary Bread", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf("${ChatColor.GRAY}Sugary Bread!"),
    CustomModels.SUGARY_BREAD)
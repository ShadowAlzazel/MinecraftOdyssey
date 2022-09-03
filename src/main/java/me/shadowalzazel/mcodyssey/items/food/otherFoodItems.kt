package me.shadowalzazel.mcodyssey.items.food

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.resources.CustomModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.ChatColor
import org.bukkit.Material

// BACON
object Bacon : OdysseyItem("Bacon",
    Material.COOKED_PORKCHOP,
    Component.text("Bacon", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Bacon!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.BACON)

// SALMON_ROLL
object SalmonRollSushi : OdysseyItem("Salmon Roll",
    Material.COOKED_SALMON,
    Component.text("Salmon Roll", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Salmon Roll Sushi!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.SALMON_ROLL)
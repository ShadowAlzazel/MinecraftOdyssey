package me.shadowalzazel.mcodyssey.items.food

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.constants.ItemModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

// BACON
object Bacon : OdysseyItem("Bacon",
    Material.COOKED_PORKCHOP,
    Component.text("Bacon", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Bacon!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.BACON)

// SALMON_ROLL
object SalmonRollSushi : OdysseyItem("Salmon Roll",
    Material.COOKED_SALMON,
    Component.text("Salmon Roll", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Salmon Roll Sushi!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.SALMON_ROLL)

object CrystalCandy: OdysseyItem("Crystal Candy",
    Material.SWEET_BERRIES,
    Component.text("Crystal Candy", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("A crystalline candy...", TextColor.color(179, 142, 243)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.CRYSTAL_CANDY)
package me.shadowalzazel.mcodyssey.items.foods

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

// BACON
object Bacon : OdysseyItem("Bacon",
    Material.COOKED_PORKCHOP,
    Component.text("Bacon", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Bacon!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.BACON)

// SALMON_ROLL
object SalmonRoll : OdysseyItem("Salmon Roll",
    Material.COOKED_SALMON,
    Component.text("Salmon Roll", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Salmon Roll Sushi!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.SALMON_ROLL)

// CRYSTAL_CANDY
object CrystalCandy: OdysseyItem("Crystal Candy",
    Material.SWEET_BERRIES,
    Component.text("Crystal Candy", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("A crystalline candy...", TextColor.color(179, 142, 243)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.CRYSTAL_CANDY)
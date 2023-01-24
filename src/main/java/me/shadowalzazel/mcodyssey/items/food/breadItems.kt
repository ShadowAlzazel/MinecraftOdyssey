package me.shadowalzazel.mcodyssey.items.food

import me.shadowalzazel.mcodyssey.items.odyssey.OdysseyItem
import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

// FRENCH_TOAST
object FrenchToast : OdysseyItem("French Toast",
    Material.BREAD,
    Component.text("French Toast", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("French Toast!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.FRENCH_TOAST)

// SUGARY_BREAD
object SugaryBread : OdysseyItem("Sugary Bread",
    Material.BREAD,
    Component.text("Sugary Bread", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Sugary Bread!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.SUGARY_BREAD)
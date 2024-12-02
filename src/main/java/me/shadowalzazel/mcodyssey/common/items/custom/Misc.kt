package me.shadowalzazel.mcodyssey.common.items.custom

import me.shadowalzazel.mcodyssey.common.items.ItemConstructor.GenericItemConstructor
import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Misc {

    val BLAZING_ROCKET = GenericItemConstructor(Material.FIREWORK_ROCKET, "blazing_rocket",
        listOf(Component.text("This rocket has a chance to explode!", CustomColors.DARK_GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val PRIMO_GEM = GenericItemConstructor(Material.FIREWORK_ROCKET, "blazing_rocket",
        listOf(
            Component.text("A primordial crystalline gem that's beyond", CustomColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("the test world. Shines with the condensed hopes", CustomColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("and dreams of universes that once were.", CustomColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))


}
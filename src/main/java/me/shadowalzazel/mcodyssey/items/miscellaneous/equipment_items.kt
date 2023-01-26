package me.shadowalzazel.mcodyssey.items.miscellaneous

import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

// SAYA
object Saya : OdysseyItem("Saya",
    Material.RABBIT_HIDE,
    Component.text("Saya", TextColor.color(191, 186, 139), TextDecoration.ITALIC),
    listOf(Component.text("Saya", TextColor.color(191, 186, 139)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.SAYA)

// SNOW CRASH
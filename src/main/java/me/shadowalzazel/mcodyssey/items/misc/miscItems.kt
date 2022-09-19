package me.shadowalzazel.mcodyssey.items.misc

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

// ARCANE_BOOK
object Saya : OdysseyItem("Saya",
    Material.RABBIT_HIDE,
    Component.text("Saya", TextColor.color(191, 186, 139), TextDecoration.ITALIC),
    listOf(Component.text("Saya", TextColor.color(191, 186, 139)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    ItemModels.SAYA)
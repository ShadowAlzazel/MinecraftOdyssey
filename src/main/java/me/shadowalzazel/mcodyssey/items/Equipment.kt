package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Equipment {

    val SAYA = OdysseyItem(
        name = "ectoplasm",
        material = Material.RABBIT_HIDE,
        displayName = Component.text("Saya", TextColor.color(191, 186, 139), TextDecoration.ITALIC),
        lore = listOf(Component.text("A sublime sheath for a katana", TextColor.color(191, 186, 139)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SAYA)


}
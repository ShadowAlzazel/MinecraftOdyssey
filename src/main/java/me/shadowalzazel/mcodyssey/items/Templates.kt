package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Templates {

    val KATANA_TEMPLATE = OdysseyItem(
        name = "katana_template",
        material = Material.PAPER,
        displayName = Component.text("Katana Template", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("A Smithing Template used to upgrade a sword into a katana", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.KATANA_TEMPLATE)


}
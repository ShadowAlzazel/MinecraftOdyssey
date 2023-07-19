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
        material = Material.COPPER_INGOT,
        displayName = Component.text("Katana Template", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text(
            "A Smithing Template used to upgrade a sword into a katana",
            TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.KATANA_TEMPLATE)

    val KATANA_MOLD = OdysseyItem(
        name = "katana_mold",
        material = Material.BRICK,
        displayName = Component.text("Katana Mold", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text(
            "A Smithing Mold used to create a new katana",
            TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.KATANA_MOLD)

    val SOUL_STEEL_UPGRADE_TEMPLATE = OdysseyItem(
        name = "soul_steel_upgrade_template",
        material = Material.COPPER_INGOT,
        displayName = Component.text("Soul Steel Upgrade Template", TextColor.color(255, 255, 85))
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
        lore = listOf(Component.text(
            "A Smithing Template used to upgrade Iron tools into Soul Steel", TextColor.color(97, 75, 61))
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.SOUL_STEEL_UPGRADE_TEMPLATE)


}
package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Exotics {

    // Ambassador
    val KINETIC_BLASTER = OdysseyItem(
        itemName = "kinetic_blaster",
        customName = "Kinetic Blaster",
        lore = listOf(
            Component.text("A weapon commissioned to launch kinetic projectiles at stellar speed.",
                TextColor.color(39, 79, 152)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        overrideMaterial = Material.BOW,
        customModel = ItemModels.KINETIC_BLASTER
    )

    /*
        enchantments = mutableMapOf(
            Enchantment.ARROW_DAMAGE to 7,
            Enchantment.ARROW_INFINITE to 1,
            Enchantment.ARROW_KNOCKBACK to 3,
            Enchantment.DURABILITY to 3
        )
     */

}
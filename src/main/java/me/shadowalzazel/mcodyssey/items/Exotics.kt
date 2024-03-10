package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

object Exotics {

    // Ambassador
    val KINETIC_BLASTER = OdysseyItem(
        name = "kinetic_blaster",
        displayName = Component.text("Kinetic Blaster",
            TextColor.color(39, 79, 152)),
        lore = listOf(
            Component.text("A weapon commissioned to launch kinetic projectiles at stellar speed.",
                TextColor.color(39, 79, 152)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        material = Material.BOW,
        customModel = ItemModels.KINETIC_BLASTER,
        enchantments = mutableMapOf(
            Enchantment.ARROW_DAMAGE to 7,
            Enchantment.ARROW_INFINITE to 1,
            Enchantment.ARROW_KNOCKBACK to 3,
            Enchantment.DURABILITY to 3
        )
    )

}
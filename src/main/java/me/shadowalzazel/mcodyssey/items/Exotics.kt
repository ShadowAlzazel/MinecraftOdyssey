package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Exotics  {

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

    val KNIGHT_BREAKER  = OdysseyItem(
        itemName = "knight_breaker",
        customName = "Knight Breaker",
        lore = listOf(
            Component.text("A knight is always vulnerable from behind or when in bed.",
                TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        overrideMaterial = Material.IRON_SWORD,
        customModel = ItemModels.KNIGHT_BREAKER
    )

    val SHOGUN_LIGHTNING  = OdysseyItem(
        itemName = "shogun_lightning",
        customName = "Shogun Lightning",
        lore = listOf(
            Component.text("No blade ever retreats from battle, unless its lightning.",
                TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        overrideMaterial = Material.IRON_SWORD,
        customModel = ItemModels.SHOGUN_LIGHTNING
    )

    val ABZU_BLADE  = OdysseyItem(
        itemName = "abzu_blade",
        customName = "Abzu Blade",
        lore = listOf(
            Component.text("A dark yet living blade meant to destroy foes or friends...",
                TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        overrideMaterial = Material.IRON_SWORD,
        customModel = ItemModels.ABZU_BLADE
    )

    val EXCALIBUR  = OdysseyItem(
        itemName = "excalibur",
        customName = "Excalibur",
        lore = listOf(
            Component.text("This master crafted sword is waiting for a worthy owner...",
                TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        overrideMaterial = Material.IRON_SWORD,
        customModel = ItemModels.EXCALIBUR
    )

    val FROST_FANG  = OdysseyItem(
        itemName = "frost_fang",
        customName = "Frost Fang",
        lore = listOf(
            Component.text("tooth and teeth can also be turned into ornate weapons.",
                TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        overrideMaterial = Material.IRON_SWORD,
        customModel = ItemModels.FROST_FANG
    )

    val ELUCIDATOR  = OdysseyItem(
        itemName = "elucidator",
        customName = "Elucidator",
        lore = listOf(
            Component.text("The sky castle was bested by this one...",
                TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        overrideMaterial = Material.IRON_SWORD,
        customModel = ItemModels.ELUCIDATOR
    )


}
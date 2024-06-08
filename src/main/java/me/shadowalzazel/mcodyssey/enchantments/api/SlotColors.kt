package me.shadowalzazel.mcodyssey.enchantments.api

import net.kyori.adventure.text.format.TextColor

enum class SlotColors(val color: TextColor) {

    GRAY(TextColor.color(170, 170, 170)),
    DARK_GRAY(TextColor.color(85, 85, 85)),
    ENCHANT(TextColor.color(191, 255, 189)),
    GILDED(TextColor.color(255, 170, 0)),
    ARCANE(TextColor.color(207, 187, 255)),
    AMETHYST(TextColor.color(141, 109, 209)),
    CURSED(TextColor.color(255, 85, 85)),
    SOUL(TextColor.color(94, 210, 215))

}
package me.shadowalzazel.mcodyssey.enchantments.base

import net.kyori.adventure.text.format.TextColor

enum class Subtype(val displayColor: TextColor) {

    NORMAL(TextColor.color(170, 170, 170)),
    GILDED(TextColor.color(255, 170, 0)), // ?
    POWERFUL(TextColor.color(255, 170, 0)), // ?
    ARCANE(TextColor.color(255, 170, 0)), // -> Upgradable

}
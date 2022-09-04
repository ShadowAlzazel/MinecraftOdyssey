package me.shadowalzazel.mcodyssey.items.misc

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.assets.CustomModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Ectoplasm : OdysseyItem("Ectoplasm",
    Material.BONE,
    Component.text("Ectoplasm", TextColor.color(37, 198, 205), TextDecoration.ITALIC),
    listOf(Component.text("Escapist Souls trapped in a decaying substance...", TextColor.color(97, 75, 61)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.ECTOPLASM)

object SoulCrystal : OdysseyItem("Soul Crystal",
    Material.QUARTZ,
    Component.text("Ectoplasm", TextColor.color(37, 198, 205), TextDecoration.ITALIC),
    listOf(Component.text("A crystal forged by lost souls...", TextColor.color(37, 198, 205)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.SOUL_CRYSTAL)
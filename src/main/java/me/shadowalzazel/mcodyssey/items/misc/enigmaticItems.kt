package me.shadowalzazel.mcodyssey.items.misc

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.assets.CustomModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

// ECTOPLASM
object Ectoplasm : OdysseyItem("Ectoplasm",
    Material.BONE,
    Component.text("Ectoplasm", TextColor.color(37, 198, 205), TextDecoration.ITALIC),
    listOf(Component.text("Escapist Souls trapped in a decaying substance...", TextColor.color(97, 75, 61)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.ECTOPLASM)

// SOUL_CRYSTAL
object SoulCrystal : OdysseyItem("Soul Crystal",
    Material.QUARTZ,
    Component.text("Soul Crystal", TextColor.color(37, 198, 205), TextDecoration.ITALIC),
    listOf(Component.text("A crystal forged by lost souls...", TextColor.color(37, 198, 205)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.SOUL_CRYSTAL)

// SORROWING_SOUL
object SorrowingSoul : OdysseyItem("Sorrowing Soul",
    Material.PAPER,
    Component.text("Sorrowing Soul", TextColor.color(142, 42, 17), TextDecoration.ITALIC),
    listOf(Component.text("A catalyst written with souls...", TextColor.color(180, 80, 32)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.SORROWING_SOUL)
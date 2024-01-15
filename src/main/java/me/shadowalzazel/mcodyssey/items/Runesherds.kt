package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.rune_writing.RunewriterManager
import me.shadowalzazel.mcodyssey.rune_writing.base.OdysseyRunesherd
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object Runesherds : RunewriterManager {

    fun OdysseyRunesherd.createSherdStack(): ItemStack {
        return createItemStack(1).apply {
            // Add tag
            addRunesherdTag()
            // Add Attribute Modifiers
            addModifierToRunesherd(this@createSherdStack)
        }
    }

    // ITEMS

    val ATTACK_RUNESHERD = OdysseyRunesherd(
        name = "attack_runesherd",
        material = Material.BRICK,
        displayName = Component.text("Attack Runesherd", TextColor.color(85, 67 ,129)),
        customModel = ItemModels.ATTACK_RUNESHERD,
        attribute = Attribute.GENERIC_ATTACK_DAMAGE,
        value = 1.0,
        affectedEquipment = listOf(EquipmentSlot.HAND)
    )

}
package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.Identifiers
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object Helmets {

    fun OdysseyItem.createArmor(armorOverride: Double = 1.0): ItemStack {
        val newArmor = this.createItemStack(1)
        newArmor.itemMeta = newArmor.itemMeta.also {
            val armorModifier = AttributeModifier(Identifiers.ARMOR_HELMET_UUID, "odyssey.armor_helmet", armorOverride, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD)
            it.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier)
        }
        return newArmor
    }

    val HORNED_HELMET = OdysseyItem(
        name = "horned_helmet",
        material = Material.CARVED_PUMPKIN,
        displayName = Component.text("Horned Helmet", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(Component.text("The fashion wear of a viking!", TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.HORNED_HELMET)

}
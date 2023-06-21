package me.shadowalzazel.mcodyssey.items

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
import java.util.*

object Spirit {


    // TO DIFFERENT TYPES OF BUFFING ITEMS
    // TOTEMS ARE FOUND -> More Potent / Effects can be spread
    // CHARMS ARE MADE -> Less Potent / Personal

    // Used to imbue a totem (or object) with attributes
    fun OdysseyItem.createSpiritTotem(
        statAttribute: Attribute,
        statIdentifier: UUID,
        statAmount: Double = 0.0,
        statSlot: EquipmentSlot = EquipmentSlot.OFF_HAND,
        statOperation: AttributeModifier.Operation = AttributeModifier.Operation.ADD_NUMBER
    ): ItemStack {
        val attributeName = "odyssey.${name}_spirit"
        val newTotem = this.createItemStack(1)
        newTotem.itemMeta = newTotem.itemMeta.also {
            val spiritModifier = AttributeModifier(statIdentifier, attributeName, statAmount, statOperation, statSlot)
            it.addAttributeModifier(statAttribute, spiritModifier)
        }
        return newTotem
    }

    // IDK USE
    val TOTEM_OF_INSIGHT = OdysseyItem(
        name = "totem_of_insight",
        material = Material.BRICK,
        displayName = Component.text("Totem of Insight", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(
            Component.text("Increases the level of an enchantment by one, up to the max",
            TextColor.color(210, 255, 74)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.TOTEM_OF_INSIGHT)

    // GIVES 4 HEARTS
    val TOTEM_OF_VIGOR = OdysseyItem(
        name = "totem_of_insight",
        material = Material.BRICK,
        displayName = Component.text("Totem of Insight", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
        lore = listOf(
            Component.text("Increases the level of an enchantment by one, up to the max",
                TextColor.color(210, 255, 74)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        customModel = ItemModels.TOTEM_OF_INSIGHT)



}
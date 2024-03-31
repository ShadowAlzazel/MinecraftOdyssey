package me.shadowalzazel.mcodyssey.items.base

import me.shadowalzazel.mcodyssey.constants.DataKeys
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType


open class OdysseyItem(
    val itemName: String,
    val overrideMaterial: Material,
    val customName: String,
    val customModel: Int? = null,
    val lore: List<Component>? = null
) {

    fun createItemStack(amount: Int = 1): ItemStack {
        val itemStack = ItemStack(overrideMaterial, amount).also {
            // Set Variables
            val meta = it.itemMeta
            meta.persistentDataContainer.set(DataKeys.ITEM_KEY, PersistentDataType.STRING, itemName) // Change for 1.20.5 to itemName component
            meta.displayName(Component.text(customName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            // Optional Variables
            if (customModel != null) { meta.setCustomModelData(customModel) }
            if (lore != null) { meta.lore(lore) }
            // Assemble meta
            it.itemMeta = meta
        }
        return itemStack
    }
    /*
    fun createItemStack(amount: Int): ItemStack {
        val itemStack = ItemStack(material, amount)
        // On item meta; Add lore, display name, custom model, damage stats, effects, color if applicable
        var enchantSlots = 0
        var gildedSlots = 0
        itemStack.itemMeta = (itemStack.itemMeta as ItemMeta).also {
            if (displayName != null) { it.displayName(displayName) }
            if (lore != null) { it.lore(lore) }
            if (customModel != null) { it.setCustomModelData(customModel) }
            if (enchantments != null) {
                for (enchant in enchantments) {
                    if (enchant.key.isOdysseyEnchant()) { gildedSlots += 1 } else { enchantSlots += 1 }
                    it.addEnchant(enchant.key, enchant.value, true)
                }
            }
            it.persistentDataContainer.set(DataKeys.ITEM_KEY, PersistentDataType.STRING, name)
        }
        if (enchantments != null) {
            itemStack.addTag(ItemTags.IS_SLOTTED)
            itemStack.setIntTag(ItemTags.ENCHANT_SLOTS, enchantSlots)
            itemStack.setIntTag(ItemTags.GILDED_SLOTS, gildedSlots)
            itemStack.updateSlotLore()
            itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
        return itemStack
    }
     */

}

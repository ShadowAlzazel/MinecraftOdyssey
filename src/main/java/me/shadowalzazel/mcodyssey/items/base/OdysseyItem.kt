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

}

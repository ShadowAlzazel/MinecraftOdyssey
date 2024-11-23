package me.shadowalzazel.mcodyssey.common.items

import me.shadowalzazel.mcodyssey.util.DataKeys
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType


open class OdysseyItem(
    val itemName: String,
    val overrideMaterial: Material,
    val customName: String,
    val lore: List<Component>? = null,
    val maxStackSize: Int? = null,
    val maxDamage: Int? = null
) {

    fun newItemStack(amount: Int = 1): ItemStack { // Mostly used for internal functions that extend the base
        val itemStack = ItemStack(overrideMaterial, amount).also {
            // Set Variables
            val meta = it.itemMeta
            meta.persistentDataContainer.set(DataKeys.ITEM_KEY, PersistentDataType.STRING, itemName) // Change for 1.20.5 to itemName component
            meta.displayName(Component.text(customName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            meta.itemName(Component.text(this.itemName))
            // Optional Variables
            //if (customModel != null) { meta.setCustomModelData(customModel) }
            meta.itemModel = DataKeys.newKey(itemName)
            if (lore != null) { meta.lore(lore) }
            if (maxDamage != null && maxStackSize == 1) {
                (meta as Damageable).setMaxStackSize(1)
                meta.setMaxDamage(maxDamage)
            }
            // Assemble meta
            it.itemMeta = meta
        }
        return itemStack
    }

}

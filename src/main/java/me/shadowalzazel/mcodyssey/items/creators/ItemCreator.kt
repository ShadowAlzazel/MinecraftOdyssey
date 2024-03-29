package me.shadowalzazel.mcodyssey.items.creators

import me.shadowalzazel.mcodyssey.constants.DataKeys
import me.shadowalzazel.mcodyssey.items.Materials
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

interface ItemCreator {

    fun Materials.createItemStack(amount: Int = 1): ItemStack {
        val itemStack = ItemStack(overrideMaterial, amount).also {
            // Set Variables
            val meta  = it.itemMeta
            meta.setCustomModelData(customModel)
            meta.persistentDataContainer.set(DataKeys.ITEM_KEY, PersistentDataType.STRING, itemName) // Change for 1.20.5 to itemName component
            meta.displayName(Component.text(customName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            // Optional Variables
            if (lore != null) {
                meta.lore(lore)
            }
            // Assemble meta
            it.itemMeta = meta
        }
        return itemStack
    }

    fun getByItemKey(text: String): Materials? {
        try {
            val mat = Materials.valueOf(text.uppercase(Locale.getDefault()))
            return mat
        }
        catch (exception: IllegalArgumentException) {
            return null
        }
    }

}
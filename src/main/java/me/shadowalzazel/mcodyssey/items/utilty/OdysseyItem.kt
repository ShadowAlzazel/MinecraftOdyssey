package me.shadowalzazel.mcodyssey.items.utilty

import net.kyori.adventure.text.Component
//import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

open class OdysseyItem(val name: String,
                       internal val material: Material,
                       internal val odysseyDisplayName: Component? = null,
                       internal val odysseyLore: List<String>? = null,
                       internal val customModel: Int? = null,
                       private val itemEnchantments: Map<Enchantment, Int>? = null) {

    val romanNumeralList = mapOf(1 to "I", 2 to "II", 3 to "III", 4 to "IV", 5 to "V", 6 to "VI", 7 to "VII", 8 to "VIII", 9 to "IX", 10 to "X")

    open fun createItemStack(amount: Int): ItemStack {
        val newOdysseyItemStack = ItemStack(material, amount)

        // Assign item meta
        newOdysseyItemStack.itemMeta = (newOdysseyItemStack.itemMeta as ItemMeta).also {
            // Add enchantments, lore, display name, and custom model if applicable
            if (itemEnchantments != null) { for (enchant in itemEnchantments) { it.addEnchant(enchant.key, enchant.value, true) } }
            if (odysseyLore != null) { it.lore = odysseyLore } // FIX
            if (odysseyDisplayName != null) { it.displayName(odysseyDisplayName) }
            it.setCustomModelData(customModel)
        }

        return newOdysseyItemStack
    }

}
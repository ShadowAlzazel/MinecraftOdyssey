package me.shadowalzazel.mcodyssey.items.utilty

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

open class OdysseyItem(val name: String,
                       internal val material: Material,
                       internal val displayName: Component? = null,
                       internal val odysseyLore: List<String>? = null,
                       internal val customModel: Int? = null,
                       private val itemEnchantments: Map<Enchantment, Int>? = null) {

    val romanNumeralList = mapOf(1 to "I", 2 to "II", 3 to "III", 4 to "IV", 5 to "V", 6 to "VI", 7 to "VII", 8 to "VIII", 9 to "IX", 10 to "X")

    open fun createItemStack(amount: Int): ItemStack {
        val newOdysseyItemStack = ItemStack(material, amount)
        val newOdysseyMeta: ItemMeta = newOdysseyItemStack.itemMeta
        //
        if (itemEnchantments != null) { for (enchant in itemEnchantments) { newOdysseyMeta.addEnchant(enchant.key, enchant.value, true) } }
        if (odysseyLore != null) { newOdysseyMeta.lore = odysseyLore } //FIX
        if (displayName != null) { newOdysseyMeta.displayName(displayName) }
        newOdysseyMeta.setCustomModelData(customModel)
        //
        newOdysseyItemStack.itemMeta = newOdysseyMeta
        return newOdysseyItemStack
    }

}
package me.shadowalzazel.mcodyssey.odysseyUtility.utilty

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

@Suppress("DEPRECATION")
open class OdysseyItem(itemName: String, itemMaterial: Material) {
    val name = itemName
    val material = itemMaterial
    open val odysseyDisplayName: String? = null
    open val odysseyLore: List<String> = listOf()
    open val isEnchanted: Boolean = false

    fun createItemStack(amount: Int, enchantmentLevel: Int?, enchantmentType: Enchantment?): ItemStack {
        val newOdysseyItemStack = ItemStack(material, amount)
        val newOdysseyMeta: ItemMeta = newOdysseyItemStack.itemMeta
        if (isEnchanted) {
            if (enchantmentLevel != null) {
                if (enchantmentType != null) {
                    newOdysseyMeta.addEnchant(enchantmentType, enchantmentLevel, true)
                }
            }
        }
        newOdysseyMeta.lore = odysseyLore
        newOdysseyMeta.setDisplayName(odysseyDisplayName)
        newOdysseyItemStack.itemMeta = newOdysseyMeta
        return newOdysseyItemStack
    }

}
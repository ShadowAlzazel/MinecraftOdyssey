package me.shadowalzazel.mcodyssey.odysseyUtility.utilty

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

@Suppress("DEPRECATION")
open class OdysseyItem(itemName: String, itemMaterial: Material) {
    val name = itemName
    private val material = itemMaterial
    open val odysseyDisplayName: String? = null
    open val odysseyLore: List<String> = listOf()
    open val isEnchanted: Boolean = false
    open val someEnchantType: Enchantment? = null
    open val someEnchantLevel: Int? = 0

    fun createItemStack(amount: Int): ItemStack {
        val newOdysseyItemStack = ItemStack(material, amount)
        val newOdysseyMeta: ItemMeta = newOdysseyItemStack.itemMeta
        if (isEnchanted) {
            if (someEnchantType != null) {
                if (someEnchantLevel != null) {
                    newOdysseyMeta.addEnchant(someEnchantType!!, someEnchantLevel!!, true)
                }
            }
        }
        newOdysseyMeta.lore = odysseyLore
        newOdysseyMeta.setDisplayName(odysseyDisplayName)
        newOdysseyItemStack.itemMeta = newOdysseyMeta
        return newOdysseyItemStack
    }

}
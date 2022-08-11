package me.shadowalzazel.mcodyssey.enchantments.other

import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantmentWrapper
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object HookShot : OdysseyEnchantmentWrapper("hookshot", "Hook Shot", 2) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            ARROW_INFINITE -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.FISHING_ROD -> {
                true
            }
            else -> {
                false
            }
        }
    }

}
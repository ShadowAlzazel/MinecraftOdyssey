package me.shadowalzazel.mcodyssey.enchantments

import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantmentWrapper
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object GildedPower : OdysseyEnchantmentWrapper("gildedpower", "Gilded Power", 3) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    // BOOKS CAN NOT HAVE GILDED POWER
    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.DIAMOND_AXE, Material.NETHERITE_SWORD -> {
                true
            }
            else -> {
                false
            }
        }
    }

}
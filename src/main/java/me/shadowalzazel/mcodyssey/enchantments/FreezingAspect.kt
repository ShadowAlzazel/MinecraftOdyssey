package me.shadowalzazel.mcodyssey.enchantments

import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantmentWrapper
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object FreezingAspect : OdysseyEnchantmentWrapper("frezzingaspect", "Freezing Aspect", 3) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            FIRE_ASPECT -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.DIAMOND_AXE, Material.NETHERITE_SWORD -> {
                true
            }
            else -> {
                false
            }
        }
    }

}
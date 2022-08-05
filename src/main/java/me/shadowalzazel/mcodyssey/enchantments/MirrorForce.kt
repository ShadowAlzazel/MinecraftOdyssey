package me.shadowalzazel.mcodyssey.enchantments

import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantmentWrapper
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object MirrorForce : OdysseyEnchantmentWrapper("mirrorforce", "Mirror Force", 3) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            OdysseyEnchantments.GRAVITY_WELL -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.SHIELD -> {
                true
            }
            else -> {
                false
            }
        }
    }

}
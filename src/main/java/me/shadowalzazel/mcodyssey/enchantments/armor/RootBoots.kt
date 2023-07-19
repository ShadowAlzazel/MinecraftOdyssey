package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object RootBoots : OdysseyEnchantment("root_boots", "Root Boots", 3) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            DEPTH_STRIDER -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK,
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS -> {
                true
            }
            else -> {
                false
            }
        }
    }

}
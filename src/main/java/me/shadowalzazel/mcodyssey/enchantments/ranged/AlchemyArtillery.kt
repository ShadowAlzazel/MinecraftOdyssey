package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantmentWrapper
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object AlchemyArtillery : OdysseyEnchantmentWrapper("alchemyartillery", "Alchemy Artillery", 3) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            MULTISHOT -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.CROSSBOW -> {
                true
            }
            else -> {
                false
            }
        }
    }

}
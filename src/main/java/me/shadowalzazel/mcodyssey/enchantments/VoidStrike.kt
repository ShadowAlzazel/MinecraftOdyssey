package me.shadowalzazel.mcodyssey.enchantments

import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantmentWrapper
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object VoidStrike : OdysseyEnchantmentWrapper("voidstrike", "Void Strike", 1) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            SWEEPING_EDGE, OdysseyEnchantments.EXPLODING -> {
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
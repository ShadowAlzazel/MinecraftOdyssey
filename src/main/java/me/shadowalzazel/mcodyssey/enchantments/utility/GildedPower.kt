package me.shadowalzazel.mcodyssey.enchantments.utility

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
            Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
            Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.CHAINMAIL_HELMET, Material.LEATHER_CHESTPLATE -> {
                true
            }
            else -> {
                false
            }
        }
    }

}
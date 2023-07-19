package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object DreadfulShriek : OdysseyEnchantment("dreadful_shriek", "Dreadful Shriek", 3) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            OdysseyEnchantments.BREWFUL_BREATH -> {
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
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET -> {
                true
            }
            else -> {
                false
            }
        }
    }

    // Weaken and slow,
    // IF low enough fear
    // Maybe in a cone?
}
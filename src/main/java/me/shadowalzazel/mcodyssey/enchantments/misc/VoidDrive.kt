package me.shadowalzazel.mcodyssey.enchantments.misc

import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object VoidDrive : OdysseyEnchantment("void_drive", "Void Drive", 3) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            PROTECTION_ENVIRONMENTAL, PROTECTION_PROJECTILE, PROTECTION_EXPLOSIONS, PROTECTION_FIRE -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.ELYTRA -> {
                true
            }
            else -> {
                false
            }
        }
    }

}
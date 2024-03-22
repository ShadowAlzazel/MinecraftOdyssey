package me.shadowalzazel.mcodyssey.enchantments.misc

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.Enchantment.*
import org.bukkit.inventory.ItemStack

// Increase XP Gain from fishing
object WiseBait : OdysseyEnchantment("wise_bait", "Wise Bait", 4) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            PROTECTION_ENVIRONMENTAL, PROTECTION_PROJECTILE, PROTECTION_EXPLOSIONS, PROTECTION_FIRE,
            OdysseyEnchantments.O_SHINY.toBukkit() -> {
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

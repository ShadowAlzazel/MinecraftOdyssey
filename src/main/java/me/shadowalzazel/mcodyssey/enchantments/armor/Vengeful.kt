package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantmentWrapper
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Vengeful : OdysseyEnchantmentWrapper("vengeful", "Vengeful", 3) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            THORNS, OdysseyEnchantments.FRUITFUL_FARE, OdysseyEnchantments.POTION_BARRIER -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE -> {
                true
            }
            else -> {
                false
            }
        }
    }

    fun todo() {
        TODO("Add Vengeful tag and if attacked consumed + damage")
    }
}
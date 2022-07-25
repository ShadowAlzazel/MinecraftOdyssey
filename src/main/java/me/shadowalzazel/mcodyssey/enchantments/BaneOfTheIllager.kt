package me.shadowalzazel.mcodyssey.enchantments

import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantmentWrapper
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object BaneOfTheIllager : OdysseyEnchantmentWrapper("baneoftheillager", "Bane of the Illager", 5) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            DAMAGE_ARTHROPODS, IMPALING, DAMAGE_ALL, DAMAGE_UNDEAD -> {
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
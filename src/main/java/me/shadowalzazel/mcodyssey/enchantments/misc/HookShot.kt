package me.shadowalzazel.mcodyssey.enchantments.misc

import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.Enchantment.ARROW_INFINITE
import org.bukkit.inventory.ItemStack

object HookShot : OdysseyEnchantment("hook_shot", "Hook Shot", 2) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            ARROW_INFINITE -> {
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
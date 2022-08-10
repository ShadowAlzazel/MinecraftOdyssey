package me.shadowalzazel.mcodyssey.enchantments.melee

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantmentWrapper
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object BuzzyBees : OdysseyEnchantmentWrapper("buzzybees", "Buzzy Bees", 3) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            SWEEPING_EDGE, OdysseyEnchantments.BACKSTABBER, OdysseyEnchantments.VOID_STRIKE -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
            Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE -> {
                true
            }
            else -> {
                false
            }
        }
    }

}
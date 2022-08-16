package me.shadowalzazel.mcodyssey.enchantments.melee

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantmentWrapper
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object VoidStrike : OdysseyEnchantmentWrapper("voidstrike", "Void Strike", 3) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            SWEEPING_EDGE, OdysseyEnchantments.BACKSTABBER, DAMAGE_ALL, DAMAGE_UNDEAD, DAMAGE_ARTHROPODS, OdysseyEnchantments.BANE_OF_THE_SEA, OdysseyEnchantments.BANE_OF_THE_SWINE, OdysseyEnchantments.BANE_OF_THE_ILLAGER -> {
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
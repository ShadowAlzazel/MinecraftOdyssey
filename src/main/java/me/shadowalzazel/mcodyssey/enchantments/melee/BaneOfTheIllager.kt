package me.shadowalzazel.mcodyssey.enchantments.melee

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object BaneOfTheIllager : OdysseyEnchantment("bane_of_the_illager", "Bane of the Illager", 5) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            DAMAGE_ARTHROPODS, IMPALING, DAMAGE_ALL, DAMAGE_UNDEAD, OdysseyEnchantments.BANE_OF_THE_SWINE, OdysseyEnchantments.BANE_OF_THE_SEA, OdysseyEnchantments.VOID_STRIKE -> {
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
            Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
            Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
            Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
            Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL,
            Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE -> {
                true
            }
            else -> {
                false
            }
        }
    }

}
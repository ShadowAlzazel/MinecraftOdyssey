package me.shadowalzazel.mcodyssey.enchantments.melee

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Budding : OdysseyEnchantment(
    "budding",
    "Budding",
    3,
    Rarity.UNCOMMON,
    EnchantmentCategory.DIGGER,
    arrayOf(EquipmentSlot.MAINHAND)
) {
    // HOE ONLY

    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK,
            Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE -> {
                true
            }
            else -> {
                false
            }
        }
    }
}
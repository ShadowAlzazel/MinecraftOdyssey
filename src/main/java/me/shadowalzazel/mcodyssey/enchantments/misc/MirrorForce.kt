package me.shadowalzazel.mcodyssey.enchantments.misc

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object MirrorForce : OdysseyEnchantment(
    "mirror_force",
    "Mirror Force",
    3,
    Rarity.RARE,
    EnchantmentCategory.WEARABLE,
    arrayOf(EquipmentSlot.OFFHAND)
) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.SHIELD -> {
                true
            }
            else -> {
                false
            }
        }
    }
}
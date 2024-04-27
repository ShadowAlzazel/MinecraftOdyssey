package me.shadowalzazel.mcodyssey.enchantments.misc

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ElytraItem
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.Enchantment.*
import org.bukkit.inventory.ItemStack

object VoidJump : OdysseyEnchantment(
    "void_jump",
    "Void Jump",
    3,
    Rarity.UNCOMMON,
    EnchantmentCategory.WEARABLE,
    arrayOf(EquipmentSlot.CHEST)
) {

    override fun canEnchant(itemStack: net.minecraft.world.item.ItemStack): Boolean {
        return itemStack.item is ElytraItem
    }

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
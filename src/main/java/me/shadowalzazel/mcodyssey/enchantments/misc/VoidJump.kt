package me.shadowalzazel.mcodyssey.enchantments.misc

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ElytraItem
import net.minecraft.world.item.enchantment.Enchantment.constantCost
import net.minecraft.world.item.enchantment.Enchantment.dynamicCost
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.Enchantment.*
import org.bukkit.inventory.ItemStack

object VoidJump : OdysseyEnchantment(
    "void_jump",
    "Void Jump",
    3,
    5,
    constantCost(8),
    dynamicCost(8, 10),
    3,
    ItemTags.ARMOR_ENCHANTABLE,
    ItemTags.ARMOR_ENCHANTABLE,
    arrayOf(EquipmentSlot.CHEST)
) {
    override fun canEnchant(itemStack: net.minecraft.world.item.ItemStack): Boolean {
        return itemStack.item is ElytraItem
    }

    override fun checkBukkitConflict(other: Enchantment): Boolean {
        return when (other) {
            PROTECTION, PROJECTILE_PROTECTION, BLAST_PROTECTION, FIRE_PROTECTION -> {
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
package me.shadowalzazel.mcodyssey.enchantments.misc

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ShieldItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object ReversedRecoil : OdysseyEnchantment(
    "reversed_recoil",
    "Reversed Recoil",
    3,
    10,
    constantCost(8),
    dynamicCost(8, 10),
    3,
    ItemTags.EQUIPPABLE_ENCHANTABLE,
    ItemTags.EQUIPPABLE_ENCHANTABLE,
    arrayOf(EquipmentSlot.OFFHAND)
) {
    override fun canEnchant(itemStack: net.minecraft.world.item.ItemStack): Boolean {
        return itemStack.item is ShieldItem
    }

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
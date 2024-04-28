package me.shadowalzazel.mcodyssey.enchantments.misc

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object MirrorForce : OdysseyEnchantment(
    "mirror_force",
    "Mirror Force",
    3,
    10,
    constantCost(8),
    dynamicCost(8, 10),
    3,
    ItemTags.EQUIPPABLE_ENCHANTABLE,
    ItemTags.EQUIPPABLE_ENCHANTABLE,
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
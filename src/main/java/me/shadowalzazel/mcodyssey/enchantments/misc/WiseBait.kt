package me.shadowalzazel.mcodyssey.enchantments.misc

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

// Increase XP Gain from fishing
object WiseBait : OdysseyEnchantment(
    "wise_bait",
    "Wise Bait",
    4,
    10,
    constantCost(8),
    dynamicCost(8, 10),
    3,
    ItemTags.FISHING_ENCHANTABLE,
    ItemTags.FISHING_ENCHANTABLE,
    arrayOf(EquipmentSlot.MAINHAND)
) {
    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            OdysseyEnchantments.O_SHINY.toBukkit() -> {
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

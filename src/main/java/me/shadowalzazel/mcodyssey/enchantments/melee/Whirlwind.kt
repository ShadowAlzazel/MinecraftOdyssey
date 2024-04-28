package me.shadowalzazel.mcodyssey.enchantments.melee

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.Enchantment.SWEEPING_EDGE
import org.bukkit.inventory.ItemStack

object Whirlwind : OdysseyEnchantment(
    "whirlwind",
    "Whirlwind",
    3,
    5,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.WEAPON_ENCHANTABLE,
    ItemTags.WEAPON_ENCHANTABLE,
    arrayOf(EquipmentSlot.MAINHAND)
) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            SWEEPING_EDGE -> {
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
            Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE -> {
                true
            }
            else -> {
                false
            }
        }
    }

}
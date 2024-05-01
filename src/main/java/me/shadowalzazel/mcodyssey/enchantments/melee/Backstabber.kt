package me.shadowalzazel.mcodyssey.enchantments.melee

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment.constantCost
import net.minecraft.world.item.enchantment.Enchantment.dynamicCost
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Backstabber : OdysseyEnchantment(
    "backstabber",
    "Backstabber",
    3,
    10,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.WEAPON_ENCHANTABLE,
    ItemTags.WEAPON_ENCHANTABLE,
    arrayOf(EquipmentSlot.MAINHAND)
) {

    override fun checkBukkitConflict(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK,
            Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD -> {
                true
            }
            else -> {
                false
            }
        }
    }

}
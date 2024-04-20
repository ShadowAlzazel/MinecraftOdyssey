package me.shadowalzazel.mcodyssey.enchantments.misc

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.Enchantment.ARROW_INFINITE
import org.bukkit.inventory.ItemStack

object HookShot : OdysseyEnchantment(
    "hook_shot",
    "Hook Shot",
    2,
    Rarity.UNCOMMON,
    EnchantmentCategory.FISHING_ROD,
    arrayOf(EquipmentSlot.MAINHAND)
) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return false
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
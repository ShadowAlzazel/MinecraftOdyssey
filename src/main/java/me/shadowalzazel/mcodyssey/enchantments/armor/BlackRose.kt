package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object BlackRose : OdysseyEnchantment(
    "black_rose",
    "Black Rose",
    2,
    Rarity.VERY_RARE,
    EnchantmentCategory.ARMOR_CHEST,
    arrayOf(EquipmentSlot.CHEST)
) {

    override fun isTradeable(): Boolean = false
    override fun isDiscoverable(): Boolean = false

    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount = inputLevel
        val text1 = "Applies Wither $amount=[level] for 5 seconds to enemies"
        val text2 = "that attacked the wearer."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
        )
    }



}
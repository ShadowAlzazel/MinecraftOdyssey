package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.Enchantment.DEPTH_STRIDER
import org.bukkit.inventory.ItemStack

object StaticSocks : OdysseyEnchantment(
    "static_socks",
    "Static Socks",
    5,
    Rarity.RARE,
    EnchantmentCategory.ARMOR_FEET,
    arrayOf(EquipmentSlot.FEET)
) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            DEPTH_STRIDER -> {
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
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount1 = inputLevel * 2
        val amount2 = inputLevel
        val text1 = "Gain a static charge every time you sneak maxed at $amount1=[level x 2]."
        val text2 = "Attacking an entity discharges all stacks for $amount2=[level] damage."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2)
        )
    }


}
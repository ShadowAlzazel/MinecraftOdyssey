package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.Enchantment.DEPTH_STRIDER
import org.bukkit.inventory.ItemStack

object RootBoots : OdysseyEnchantment(
    "root_boots",
    "Root Boots",
    3,
    10,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.FOOT_ARMOR_ENCHANTABLE,
    ItemTags.FOOT_ARMOR_ENCHANTABLE,
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
        val amount1 = 0.2 + (inputLevel * 0.1)
        val amount2 = inputLevel
        val text1 = "Reduce Knockback by $amount1%=[0.2 + (level * 0.1) and"
        val text2 = "reduce damage taken by $amount2=[level] when shifting OR"
        val text3 = "on dirt/root related blocks. (These effects can stack)"
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
            getGrayComponentText(text3)
        )
    }

}
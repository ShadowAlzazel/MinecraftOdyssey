package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Beastly : OdysseyEnchantment(
    "beastly",
    "Beastly",
    3,
    10,
    constantCost(5),
    dynamicCost(5, 10),
    3,
    ItemTags.ARMOR_ENCHANTABLE,
    ItemTags.ARMOR_ENCHANTABLE,
    arrayOf(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
) {

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
        val amount1 = 0.5 * inputLevel
        val amount2 = 2 * inputLevel
        val text1 = "Receive $amount1=[0.5 x level] less damage when $amount2=[2 x level]"
        val text2 = "or more enemies are within a 4 block radius."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
        )
    }

}
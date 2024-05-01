package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import net.kyori.adventure.text.Component
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment.constantCost
import net.minecraft.world.item.enchantment.Enchantment.dynamicCost
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Relentless : OdysseyEnchantment(
    "relentless",
    "Relentless",
    3,
    10,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.CHEST_ARMOR_ENCHANTABLE,
    ItemTags.CHEST_ARMOR_ENCHANTABLE,
    arrayOf(EquipmentSlot.CHEST)
) {

    override fun checkOdysseyConflict(other: OdysseyEnchantment): Boolean {
        return when (other) {
            OdysseyEnchantments.RECKLESS -> {
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
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET,
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE,
            Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.LEATHER_LEGGINGS,
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount1 = 0.25 + (inputLevel * 0.25)
        val text1 = "When damaged, gain $amount1=[0.25 + (level x 0.25)] saturation"
        return listOf(
            getGrayComponentText(text1)
        )
    }

}
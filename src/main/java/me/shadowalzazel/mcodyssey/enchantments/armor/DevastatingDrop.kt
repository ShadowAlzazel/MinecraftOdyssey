package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment.constantCost
import net.minecraft.world.item.enchantment.Enchantment.dynamicCost
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object DevastatingDrop : OdysseyEnchantment(
    "devastating_drop",
    "Devastating Drop",
    4,
    10,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.FOOT_ARMOR_ENCHANTABLE,
    ItemTags.FOOT_ARMOR_ENCHANTABLE,
    arrayOf( EquipmentSlot.FEET)
) {

    override fun checkBukkitConflict(other: Enchantment): Boolean {
        return false
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
        val amount = 40 * inputLevel
        val text1 = "Converts fall damage to AOE damage at $amount=[40 x level]%"
        return listOf(
            getGrayComponentText(text1),
        )
    }


}
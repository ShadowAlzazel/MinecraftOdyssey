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

object Vengeful : OdysseyEnchantment(
    "vengeful",
    "Vengeful",
    3,
    5,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.CHEST_ARMOR_ENCHANTABLE,
    ItemTags.CHEST_ARMOR_ENCHANTABLE,
    arrayOf(EquipmentSlot.CHEST)
) {


    override fun checkBukkitConflict(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK,
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount1 = inputLevel
        val text1 = "Deal $amount1=[level] more damage to enemies that have damaged the wearer."
        return listOf(
            getGrayComponentText(text1)
        )
    }

}
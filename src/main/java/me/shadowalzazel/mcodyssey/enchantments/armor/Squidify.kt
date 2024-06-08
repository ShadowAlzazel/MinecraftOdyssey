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

object Squidify : OdysseyEnchantment(
    "squidify",
    "Squidify",
    3,
    8,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.LEG_ARMOR_ENCHANTABLE,
    ItemTags.LEG_ARMOR_ENCHANTABLE,
    arrayOf(EquipmentSlot.LEGS)
) {

    override fun checkBukkitConflict(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK,
            Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.LEATHER_LEGGINGS -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount1 = 2 + (inputLevel * 2)
        val amount2 = inputLevel
        val text1 = "Getting hit applies Blindness 1 for $amount1=[2 + (level x 2)] seconds"
        val text2 = "and Slowness 1 for $amount2=[level] seconds."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2)
        )
    }

}
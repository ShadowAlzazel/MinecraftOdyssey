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

object LeapFrog : OdysseyEnchantment(
    "leapfrog",
    "Leap Frog",
    3,
    10,
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
        val amount1 = 0.4 * inputLevel
        val text1 = "Increases jump velocity by $amount1=[0.4 x level] when the player has sneaked"
        val text2 = "or is on top of lily pad, drip leaf, mud, muddy mangrove root,"
        val text3 = "or a waterlogged leaf. These effects can stack"
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
            getGrayComponentText(text3)
        )
    }


}
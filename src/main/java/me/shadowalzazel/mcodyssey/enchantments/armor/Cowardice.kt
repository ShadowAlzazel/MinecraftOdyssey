package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Cowardice : OdysseyEnchantment(
    "cowardice",
    "Cowardice",
    3,
    10,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.LEG_ARMOR_ENCHANTABLE,
    ItemTags.LEG_ARMOR_ENCHANTABLE,
    arrayOf(EquipmentSlot.LEGS)
) {

    override fun conflictsWith(other: Enchantment): Boolean {
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
        val amount = 1 * inputLevel
        val text1 = "Get knock backed further and get speed $amount=[level] for 6 seconds."
        return listOf(
            getGrayComponentText(text1),
        )
    }

}
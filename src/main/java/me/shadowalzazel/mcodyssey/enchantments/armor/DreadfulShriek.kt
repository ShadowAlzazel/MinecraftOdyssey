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

object DreadfulShriek : OdysseyEnchantment(
    "dreadful_shriek",
    "Dreadful Shriek",
    3,
    10,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.HEAD_ARMOR_ENCHANTABLE,
    ItemTags.HEAD_ARMOR_ENCHANTABLE,
    arrayOf(EquipmentSlot.HEAD)
) {

    override fun checkBukkitConflict(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK,
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount = 2 + (2 * inputLevel)
        val text1 = "Using a goat horn applies weakness and slowness for $amount=[4 + (2 x level)] seconds"
        val text2 = "to enemies within a 16 block radius."
        val text3 = "Changes goat horn cooldown to 6 seconds."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
            getGrayComponentText(text3)
        )
    }

}
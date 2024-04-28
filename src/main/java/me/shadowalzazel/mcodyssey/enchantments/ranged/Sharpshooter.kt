package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Sharpshooter : OdysseyEnchantment(
    "sharpshooter",
    "Sharpshooter",
    5,
    10,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.BOW_ENCHANTABLE,
    ItemTags.BOW_ENCHANTABLE,
    arrayOf(EquipmentSlot.MAINHAND)
) {


    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.CROSSBOW, Material.BOW -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount1 = inputLevel * 0.1
        val amount2 = inputLevel * 0.5
        val text1 = "Critical arrows gain $amount1%=[level * 0.1] speed "
        val text2 = "and do $amount2=[level x 0.5] extra damage."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2)
        )
    }


}
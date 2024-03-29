package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Overcharge : OdysseyEnchantment("overcharge", "Overcharge", 5) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.BOW -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount1 = inputLevel
        val amount2 = inputLevel * 3
        val text1 = "Holding a fully drown applies a charge to a max of $amount1=[level]."
        val text2 = "Each charge increase damage by $amount2=[level x 3] and velocity. "
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2)
        )
    }
}
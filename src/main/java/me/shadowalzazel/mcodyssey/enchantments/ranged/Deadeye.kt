package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Deadeye : OdysseyEnchantment("deadeye", "Deadeye", 4) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.BOW, Material.CROSSBOW -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount1 = 3 + (inputLevel * 2)
        val text1 = "Increase damage by $amount1=[3 + (level x 2)]"
        val text2 = "if the projectile hits the target's eye."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
        )
    }

}


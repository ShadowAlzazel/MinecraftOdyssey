package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object DeathFromAbove : OdysseyEnchantment("death_from_above", "Death From Above", 4) {

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
        val amount1 = 1.5 + (inputLevel * 1.5)
        val amount2 = 4 + (inputLevel * 4)
        val text1 = "Increase damage by $amount1=[1.5+ (level x 2)] if the projectile "
        val text2 = "was launched from $amount2=[4 + (level x 4)] blocks high."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
        )
    }

}
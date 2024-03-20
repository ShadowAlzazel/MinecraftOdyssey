package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Vulnerocity : OdysseyEnchantment("vulnerocity", "Vulnerocity", 3) {

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
        val text1 = "On projectile hit, reduce entity immunity time by $amount1=[level * 0.1] seconds."
        return listOf(
            getGrayComponentText(text1)
        )
    }

}
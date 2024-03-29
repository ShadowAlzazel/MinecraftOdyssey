package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Ricochet : OdysseyEnchantment("ricochet", "Ricochet", 4) {

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
        val amount1 = 15 + (5 * inputLevel)
        val text1 = "Projectiles ricochet on entities/block $amount1=[15 + (level x 5)] seconds."
        val text2 = "Each bounce increases damage by 2."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2)
        )
    }

}
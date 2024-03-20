package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Entanglement : OdysseyEnchantment("entanglement", "Entanglement", 3) {

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
        val amount1 = inputLevel
        val text1 = "On projectile hit, the target is tagged. If another target is tagged "
        val text2 = "and within 10 blocks, they are pulled together and damaged for $amount1=[level]."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
        )
    }

}
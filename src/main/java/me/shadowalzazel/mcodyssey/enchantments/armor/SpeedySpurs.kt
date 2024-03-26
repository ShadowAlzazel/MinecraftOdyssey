package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.Enchantment.THORNS
import org.bukkit.inventory.ItemStack

object SpeedySpurs : OdysseyEnchantment("speedy_spurs", "Speedy Spurs", 3) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            THORNS -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK,
            Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount = inputLevel
        val text1 = "Gives speed $amount=[level] to ridden entities."
        return listOf(
            getGrayComponentText(text1),
        )
    }

}
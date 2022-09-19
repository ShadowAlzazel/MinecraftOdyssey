package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantment
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object SingularityShot : OdysseyEnchantment("singularityshot", "Singularity Shot", 3) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            MENDING -> {
                true
            }
            else -> {
                false
            }
        }
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
    fun todo() {
        TODO("Shoots singularity, replace arrow")
    }
}
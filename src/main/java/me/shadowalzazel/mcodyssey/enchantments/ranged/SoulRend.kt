package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object SoulRend: OdysseyEnchantment(
    "soul_rend",
    "Soul Rend",
    3,
    Rarity.RARE,
    EnchantmentCategory.BOW,
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
        val amount1 = inputLevel
        val text1 = "Marks struck enemies. On item hand swap, damage all "
        val text2 = "marked enemies based on how many arrows they have in them "
        val text3 = "multiplied by $amount1=[level]."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
            getGrayComponentText(text3)
        )
    }

}
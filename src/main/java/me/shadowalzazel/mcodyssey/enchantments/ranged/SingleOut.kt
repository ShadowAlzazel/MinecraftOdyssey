package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object SingleOut : OdysseyEnchantment(
    "single_out",
    "Single Out",
    3,
    Rarity.UNCOMMON,
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
        val amount1 = 1 + (inputLevel * 2.0)
        val text1 = "Isolated targets mor than 16 blocks away"
        val text2 = "take $amount1=[1 + (level x 2] more damage."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2)
        )
    }

}
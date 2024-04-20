package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.Enchantment.ARROW_INFINITE
import org.bukkit.enchantments.Enchantment.MULTISHOT
import org.bukkit.inventory.ItemStack

object BurstBarrage : OdysseyEnchantment(
    "burst_barrage",
    "Burst Barrage",
    5,
    Rarity.RARE,
    EnchantmentCategory.BOW,
    arrayOf(EquipmentSlot.MAINHAND)
) {


    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            ARROW_INFINITE, MULTISHOT -> {
                true
            }
            else -> {
                false
            }
        }
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
        val amount1 = 2 + inputLevel
        val text1 = "Shoot $amount1=[2 + level] consecutive arrows."
        val text2 = "(This does not bypass immunity)"
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
        )
    }

}
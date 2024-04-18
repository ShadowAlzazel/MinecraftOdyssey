package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object MoltenCore : OdysseyEnchantment(
    "molten_core",
    "Molten Core",
    2,
    Rarity.UNCOMMON,
    EnchantmentCategory.ARMOR_CHEST,
    arrayOf(EquipmentSlot.CHEST)
) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK,
            Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount1 = 2 * inputLevel
        val amount2 = 0.5 * inputLevel
        val text1 = "Enemies that attack the wearer are set on fire for $amount1=[2 x level] seconds;"
        val text2 = "this effect is doubled when on fire."
        val text3 = "Taking damage while in lava or on a magma block gives $amount2=[0.5 x level] saturation."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
            getGrayComponentText(text3)
        )
    }

}
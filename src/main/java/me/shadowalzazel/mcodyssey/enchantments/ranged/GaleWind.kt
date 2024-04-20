package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object GaleWind : OdysseyEnchantment(
    "gale_wind",
    "Gale Wind",
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
        val text1 = "0.25 seconds after shooting, blows the"
        val text2 = "player forward in the direction they are facing."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
        )
    }
}
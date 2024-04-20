package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object SingularityShot : OdysseyEnchantment(
    "singularity_shot",
    "Singularity Shot",
    3,
    Rarity.VERY_RARE,
    EnchantmentCategory.BOW,
    arrayOf(EquipmentSlot.MAINHAND)
) {

    override fun isTradeable(): Boolean = false
    override fun isDiscoverable(): Boolean = false

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
        val amount1 = (inputLevel * 0.5)
        val text1 = "A black hole follow the projectile that pulls in targets"
        val text2 = "and damages them for $amount1=[level * 0.5]."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2)
        )
    }


}
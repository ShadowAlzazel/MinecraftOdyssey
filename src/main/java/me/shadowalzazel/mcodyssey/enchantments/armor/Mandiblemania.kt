package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Mandiblemania  : OdysseyEnchantment(
    "mandiblemania",
    "Mandiblemania",
    2,
    Rarity.RARE,
    EnchantmentCategory.ARMOR_HEAD,
    arrayOf(EquipmentSlot.HEAD)
) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK,
            Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount1 = 0.1 * inputLevel
        val text1 = "Getting damaged by an entity or attacking an entity with a lower eye"
        val text2 = "location decreases their immunity time by $amount1=[0.1 x level] seconds."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2)
        )
    }

}
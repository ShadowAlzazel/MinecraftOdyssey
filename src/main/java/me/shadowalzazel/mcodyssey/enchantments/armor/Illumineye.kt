package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object Illumineye : OdysseyEnchantment(
    "illumineye",
    "Illumineye",
    3,
    Rarity.UNCOMMON,
    EnchantmentCategory.ARMOR_HEAD,
    arrayOf(EquipmentSlot.HEAD)
) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return when (item.type) {
            Material.ENCHANTED_BOOK, Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET -> {
                true
            }
            else -> {
                false
            }
        }
    }

    override fun getDescriptionToolTip(inputLevel: Int): List<Component> {
        val amount1 = 3 + (inputLevel * 2)
        val text1 = "Taking damage by an entity within line of sight applies"
        val text2 = "glowing for $amount1=[3 + (level x 2)] seconds."
        val text3 = "Using a spyglass on an entity applies glowing"
        val text4 = "and adds a 2 second cooldown to the spyglass."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
            getGrayComponentText(text3),
            getGrayComponentText(text4),
        )
    }

}
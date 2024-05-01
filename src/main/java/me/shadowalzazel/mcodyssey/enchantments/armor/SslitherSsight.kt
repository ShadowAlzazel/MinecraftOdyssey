package me.shadowalzazel.mcodyssey.enchantments.armor

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment.constantCost
import net.minecraft.world.item.enchantment.Enchantment.dynamicCost
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object SslitherSsight : OdysseyEnchantment(
    "sslither_ssight",
    "Sslither Ssight",
    3,
    5,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.HEAD_ARMOR_ENCHANTABLE,
    ItemTags.HEAD_ARMOR_ENCHANTABLE,
    arrayOf(EquipmentSlot.HEAD)
) {

    override fun checkBukkitConflict(other: Enchantment): Boolean {
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
        val amount1 = 0.5 * inputLevel
        val text1 = "Taking damage by an entity within line of sight disables"
        val text2 = "their movement for $amount1=[0.5 x level] seconds."
        val text3 = "Using a spyglass on an entity disable their movement"
        val text4 = "and adds a 2 second cooldown to the spyglass."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
            getGrayComponentText(text3),
            getGrayComponentText(text4),
        )
    }
}
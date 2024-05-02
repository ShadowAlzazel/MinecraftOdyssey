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

object BrewfulBreath : OdysseyEnchantment(
    "brewful_breath",
    "Brewful Breath",
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
        val amount = 0.5 + (0.5 * inputLevel)
        val text1 = "Drinking a potion creates a lingering potion cloud of the effect"
        val text2 = "with $amount=[0.5 + (0.5 x level)] radius at 15% duration."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
        )
    }

}
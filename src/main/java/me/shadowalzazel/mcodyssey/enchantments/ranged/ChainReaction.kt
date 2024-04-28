package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import net.kyori.adventure.text.Component
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object ChainReaction : OdysseyEnchantment(
    "chain_reaction",
    "Chain Reaction",
    5,
    5,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.BOW_ENCHANTABLE,
    ItemTags.BOW_ENCHANTABLE,
    arrayOf(EquipmentSlot.MAINHAND)
) {


    override fun conflictsWith(other: Enchantment): Boolean {
        return false
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
        val amount = 2 + inputLevel
        val text1 = "On projectile hit, spawn an arrow that targets the closest"
        val text2 = "enemy. This can happen $amount=[2 + level] amount of times"
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2)
        )
    }

}
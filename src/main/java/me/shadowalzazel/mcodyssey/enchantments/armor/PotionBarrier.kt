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

object PotionBarrier : OdysseyEnchantment(
    "potion_barrier",
    "Potion Barrier",
    2,
    10,
    constantCost(8),
    dynamicCost(8, 10),
    5,
    ItemTags.CHEST_ARMOR_ENCHANTABLE,
    ItemTags.CHEST_ARMOR_ENCHANTABLE,
    arrayOf(EquipmentSlot.CHEST)
) {

    override fun checkBukkitConflict(other: Enchantment): Boolean {
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
        val amount1 = inputLevel * 4
        val amount2 = inputLevel * 4
        val text1 = "Drinking a potion gives a Barrier for $amount1=[4 + (level x 4)] seconds that reduces"
        val text2 = "damage by 3. Adds a $amount2=[2 + (level x 2)] second cooldown to potions."
        return listOf(
            getGrayComponentText(text1),
            getGrayComponentText(text2),
        )
    }


}


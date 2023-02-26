package me.shadowalzazel.mcodyssey.enchantments.ranged

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object TemporalTorrent : OdysseyEnchantment("temporal_torrent", "Temporal Torrent", 5) {

    override fun conflictsWith(other: Enchantment): Boolean {
        return when (other) {
            ARROW_INFINITE, OdysseyEnchantments.PERPETUAL_PROJECTILE -> {
                true
            }
            else -> {
                false
            }
        }
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

    fun todo() {
        TODO("Shoots arrows that have slow velocity and ignore gravity, but after 5 shot, all gain double velocity and regain time" +
                "Incompatible with Singularity Shot?")
    }
}
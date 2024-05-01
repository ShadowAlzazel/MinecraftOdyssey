package me.shadowalzazel.mcodyssey.enchantments.util

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import org.bukkit.enchantments.Enchantment

class EnchantContainer(
    val bukkitEnchant: Enchantment? = null,
    val odysseyEnchant: OdysseyEnchantment? = null
) {

    val isBukkit: Boolean
    val isOdyssey: Boolean

    init {
        if (bukkitEnchant != null) {
            isBukkit = true
            isOdyssey = false
        }
        else if (odysseyEnchant != null) {
            isBukkit = false
            isOdyssey = true
        }
        else {
            isBukkit = false
            isOdyssey = false
        }
    }

}
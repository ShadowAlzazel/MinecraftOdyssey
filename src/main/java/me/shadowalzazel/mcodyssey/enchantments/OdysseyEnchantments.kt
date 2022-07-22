package me.shadowalzazel.mcodyssey.enchantments

import org.bukkit.enchantments.Enchantment
import java.util.*
import java.util.stream.Collectors

object OdysseyEnchantments {

    val BANE_OF_THE_SWINE: Enchantment = BaneOfTheSwine("baneoftheswine", "BANE_OF_THE_SWINE", 5)
    val BANE_OF_THE_SEA: Enchantment = BaneOfTheSea

    val enchantmentSet = setOf(BANE_OF_THE_SWINE, BANE_OF_THE_SEA)


    fun register() {
        val registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(BANE_OF_THE_SWINE)
        if (!registered) registerEnchantment(BANE_OF_THE_SWINE)
    }

    fun registerEnchantment(enchantment: Enchantment?) {
        var registered = true
        try {
            val f = Enchantment::class.java.getDeclaredField("acceptingNew")
            f.isAccessible = true
            f[null] = true
            Enchantment.registerEnchantment(enchantment!!)
        } catch (e: Exception) {
            registered = false
            e.printStackTrace()
        }
        if (registered) {
            // Send to console
            println("Registered")
        }
    }
}
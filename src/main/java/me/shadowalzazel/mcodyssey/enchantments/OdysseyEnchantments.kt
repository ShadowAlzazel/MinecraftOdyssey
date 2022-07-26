package me.shadowalzazel.mcodyssey.enchantments

import org.bukkit.enchantments.Enchantment
import java.util.*
import java.util.stream.Collectors

object OdysseyEnchantments {

    // Enchantments
    val BANE_OF_THE_SWINE: Enchantment = BaneOfTheSwine
    val BANE_OF_THE_SEA: Enchantment = BaneOfTheSea
    val BANE_OF_THE_ILLAGER: Enchantment = BaneOfTheIllager
    val EXPLODING: Enchantment = Exploding
    val FREEZING_ASPECT : Enchantment = FreezingAspect
    val VOID_STRIKE: Enchantment = VoidStrike
    val GUARDING_STRIKE: Enchantment = GuardingStrike
    val GILDED_POWER: Enchantment = GildedPower
    val BACKSTABBER: Enchantment = Backstabber

    // Set of all enchantments
    val enchantmentSet = setOf(BANE_OF_THE_SWINE, BANE_OF_THE_SEA, EXPLODING, BANE_OF_THE_ILLAGER, FREEZING_ASPECT, VOID_STRIKE, GILDED_POWER, GUARDING_STRIKE, BACKSTABBER)

    // Register
    fun register() {
        for (odysseyEnchant in enchantmentSet) {
            val registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(odysseyEnchant)
            if (!registered) registerEnchantment(odysseyEnchant)
        }
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
            println("Registered $enchantment")
        }
    }
}
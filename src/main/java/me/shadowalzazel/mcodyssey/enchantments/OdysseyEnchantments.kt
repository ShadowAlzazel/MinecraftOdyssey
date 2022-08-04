package me.shadowalzazel.mcodyssey.enchantments

import me.shadowalzazel.mcodyssey.enchantments.utility.GildedPower
import org.bukkit.enchantments.Enchantment
import java.util.*
import java.util.stream.Collectors

object OdysseyEnchantments {

    // Utility
    val GILDED_POWER: Enchantment = GildedPower
    // Enchantments
    val BACKSTABBER: Enchantment = Backstabber
    val BANE_OF_THE_ILLAGER: Enchantment = BaneOfTheIllager
    val BANE_OF_THE_SEA: Enchantment = BaneOfTheSea
    val BANE_OF_THE_SWINE: Enchantment = BaneOfTheSwine
    val BUZZY_BEES: Enchantment = BuzzyBees
    val DECAYING_TOUCH: Enchantment = DecayingTouch
    val EXPLODING: Enchantment = Exploding
    val FREEZING_ASPECT : Enchantment = FreezingAspect
    val FROG_FRIGHT : Enchantment = FrogFright
    val GUARDING_STRIKE: Enchantment = GuardingStrike
    val POTION_BARRIER: Enchantment = PotionBarrier
    val VOID_STRIKE: Enchantment = VoidStrike
    val WHIRLWIND: Enchantment = Whirlwind

    // Set of all enchantments
    val enchantmentSet = setOf(GILDED_POWER, BACKSTABBER, BANE_OF_THE_ILLAGER, BANE_OF_THE_SEA, BANE_OF_THE_SWINE, BUZZY_BEES, DECAYING_TOUCH,
        EXPLODING, FREEZING_ASPECT, FROG_FRIGHT, GUARDING_STRIKE, POTION_BARRIER, VOID_STRIKE, WHIRLWIND)

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
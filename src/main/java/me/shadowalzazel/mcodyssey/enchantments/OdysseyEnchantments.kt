package me.shadowalzazel.mcodyssey.enchantments

import me.shadowalzazel.mcodyssey.enchantments.armor.*
import me.shadowalzazel.mcodyssey.enchantments.melee.*
import me.shadowalzazel.mcodyssey.enchantments.other.MirrorForce
import me.shadowalzazel.mcodyssey.enchantments.ranged.AlchemyArtillery
import me.shadowalzazel.mcodyssey.enchantments.utility.GildedPower
import org.bukkit.enchantments.Enchantment
import java.util.*
import java.util.stream.Collectors

object OdysseyEnchantments {

    // Utility
    val GILDED_POWER: Enchantment = GildedPower
    // Enchantments
    val ALCHEMYARTILLERY: Enchantment = AlchemyArtillery
    val BACKSTABBER: Enchantment = Backstabber
    val BANE_OF_THE_ILLAGER: Enchantment = BaneOfTheIllager
    val BANE_OF_THE_SEA: Enchantment = BaneOfTheSea
    val BANE_OF_THE_SWINE: Enchantment = BaneOfTheSwine
    val BUZZY_BEES: Enchantment = BuzzyBees
    val COWARDICE: Enchantment = Cowardice
    val DECAYING_TOUCH: Enchantment = DecayingTouch
    val DOUSE: Enchantment = Douse
    val EXPLODING: Enchantment = Exploding
    val FREEZING_ASPECT : Enchantment = FreezingAspect
    val FROG_FRIGHT: Enchantment = FrogFright
    val FRUITFUL_FARE: Enchantment = FruitfulFare
    val GRAVITY_WELL: Enchantment = GravityWell
    val GUARDING_STRIKE: Enchantment = GuardingStrike
    val HEMORRHAGE: Enchantment = Hemorrhage
    val MIRROR_FORCE: Enchantment = MirrorForce
    val POTION_BARRIER: Enchantment = PotionBarrier
    val SPOREFUL: Enchantment = Sporeful
    val SQUIDIFY: Enchantment = Squidify
    val VENGEFUL: Enchantment = Vengeful
    val VOID_STRIKE: Enchantment = VoidStrike
    val WHIRLWIND: Enchantment = Whirlwind

    // Set of all enchantments
    val enchantmentSet = setOf(GILDED_POWER, ALCHEMYARTILLERY, BANE_OF_THE_ILLAGER, BANE_OF_THE_SEA, BANE_OF_THE_SWINE, BUZZY_BEES, COWARDICE, DECAYING_TOUCH, DOUSE,
        EXPLODING, FREEZING_ASPECT, FROG_FRIGHT, FRUITFUL_FARE, GRAVITY_WELL, GUARDING_STRIKE, HEMORRHAGE, MIRROR_FORCE, POTION_BARRIER, SPOREFUL, SQUIDIFY, VOID_STRIKE, WHIRLWIND)

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
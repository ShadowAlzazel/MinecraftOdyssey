package me.shadowalzazel.mcodyssey.enchantments

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.enchantments.armor.*
import me.shadowalzazel.mcodyssey.enchantments.melee.*
import me.shadowalzazel.mcodyssey.enchantments.other.HookShot
import me.shadowalzazel.mcodyssey.enchantments.other.MirrorForce
import me.shadowalzazel.mcodyssey.enchantments.other.WarpJump
import me.shadowalzazel.mcodyssey.enchantments.ranged.*
import me.shadowalzazel.mcodyssey.enchantments.utility.GildedPower
import org.bukkit.enchantments.Enchantment
import java.util.*
import java.util.stream.Collectors

object OdysseyEnchantments {

    // Utility
    val GILDED_POWER: Enchantment = GildedPower
    // Enchantments
    val ALCHEMY_ARTILLERY: Enchantment = AlchemyArtillery
    val BACKSTABBER: Enchantment = Backstabber
    val BANE_OF_THE_ILLAGER: Enchantment = BaneOfTheIllager
    val BANE_OF_THE_SEA: Enchantment = BaneOfTheSea
    val BANE_OF_THE_SWINE: Enchantment = BaneOfTheSwine
    val BURST_BARRAGE: Enchantment = BurstBarrage
    val BUZZY_BEES: Enchantment = BuzzyBees
    val CHAIN_REACTION: Enchantment = ChainReaction
    val COWARDICE: Enchantment = Cowardice
    val DECAYING_TOUCH: Enchantment = DecayingTouch
    val DOUSE: Enchantment = Douse
    val ECHO: Enchantment = Echo
    val EXPLODING: Enchantment = Exploding
    val FREEZING_ASPECT : Enchantment = FreezingAspect
    val FROG_FRIGHT: Enchantment = FrogFright
    val FRUITFUL_FARE: Enchantment = FruitfulFare
    val GRAVITY_WELL: Enchantment = GravityWell
    val GUARDING_STRIKE: Enchantment = GuardingStrike
    val HEMORRHAGE: Enchantment = Hemorrhage
    val HOOK_SHOT: Enchantment = HookShot
    val LUCKY_DRAW: Enchantment = LuckyDraw
    val MIRROR_FORCE: Enchantment = MirrorForce
    val OVERCHARGE: Enchantment = Overcharge
    val POTION_BARRIER: Enchantment = PotionBarrier
    val SOUL_REND: Enchantment = SoulRend
    val SPEEDY_SPURS: Enchantment = SpeedySpurs
    val SPOREFUL: Enchantment = Sporeful
    val SQUIDIFY: Enchantment = Squidify
    val VENGEFUL: Enchantment = Vengeful
    val VOID_STRIKE: Enchantment = VoidStrike
    val WARP_JUMP: Enchantment = WarpJump
    val WHIRLWIND: Enchantment = Whirlwind

    // Set of all enchantments
    val enchantmentSet = setOf(GILDED_POWER, ALCHEMY_ARTILLERY, BACKSTABBER, BANE_OF_THE_ILLAGER, BANE_OF_THE_SEA, BANE_OF_THE_SWINE, BURST_BARRAGE, BUZZY_BEES, CHAIN_REACTION, COWARDICE, DECAYING_TOUCH, DOUSE, ECHO,
        EXPLODING, FREEZING_ASPECT, FROG_FRIGHT, FRUITFUL_FARE, GRAVITY_WELL, GUARDING_STRIKE, HEMORRHAGE, HOOK_SHOT, LUCKY_DRAW, MIRROR_FORCE, OVERCHARGE, POTION_BARRIER, SOUL_REND, SPEEDY_SPURS, SQUIDIFY, VOID_STRIKE, WARP_JUMP, WHIRLWIND)

    // Register
    fun register() {
        for (odysseyEnchant in enchantmentSet) {
            val registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(odysseyEnchant)
            if (!registered) registerEnchantment(odysseyEnchant)
        }
    }

    private fun registerEnchantment(enchantment: Enchantment?) {
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
            MinecraftOdyssey.instance.logger.info("Registered: $enchantment")
        }
    }
}
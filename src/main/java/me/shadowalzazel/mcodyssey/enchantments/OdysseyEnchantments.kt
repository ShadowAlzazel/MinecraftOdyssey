package me.shadowalzazel.mcodyssey.enchantments

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.enchantments.armor.*
import me.shadowalzazel.mcodyssey.enchantments.melee.*
import me.shadowalzazel.mcodyssey.enchantments.other.HookShot
import me.shadowalzazel.mcodyssey.enchantments.other.MirrorForce
import me.shadowalzazel.mcodyssey.enchantments.other.WarpJump
import me.shadowalzazel.mcodyssey.enchantments.ranged.*
import me.shadowalzazel.mcodyssey.enchantments.utility.GildedPower
import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantment
import org.bukkit.enchantments.Enchantment
import java.util.*
import java.util.stream.Collectors

object OdysseyEnchantments {

    // Utility
    val GILDED_POWER: OdysseyEnchantment = GildedPower
    // Enchantments
    val ALCHEMY_ARTILLERY: OdysseyEnchantment = AlchemyArtillery
    val BACKSTABBER: OdysseyEnchantment = Backstabber
    val BANE_OF_THE_ILLAGER: OdysseyEnchantment = BaneOfTheIllager
    val BANE_OF_THE_SEA: OdysseyEnchantment = BaneOfTheSea
    val BANE_OF_THE_SWINE: OdysseyEnchantment = BaneOfTheSwine
    val BURST_BARRAGE: OdysseyEnchantment = BurstBarrage
    val BUZZY_BEES: OdysseyEnchantment = BuzzyBees
    val CHAIN_REACTION: OdysseyEnchantment = ChainReaction
    val COWARDICE: OdysseyEnchantment = Cowardice
    val DECAYING_TOUCH: OdysseyEnchantment = DecayingTouch
    val DOUSE: OdysseyEnchantment = Douse
    val ECHO: OdysseyEnchantment = Echo
    val EXPLODING: OdysseyEnchantment = Exploding
    val FREEZING_ASPECT : OdysseyEnchantment = FreezingAspect
    val FROG_FRIGHT: OdysseyEnchantment = FrogFright
    val FRUITFUL_FARE: OdysseyEnchantment = FruitfulFare
    val GRAVITY_WELL: OdysseyEnchantment = GravityWell
    val GUARDING_STRIKE: OdysseyEnchantment = GuardingStrike
    val HEMORRHAGE: OdysseyEnchantment = Hemorrhage
    val HOOK_SHOT: OdysseyEnchantment = HookShot
    val LUCKY_DRAW: OdysseyEnchantment = LuckyDraw
    val MIRROR_FORCE: OdysseyEnchantment = MirrorForce
    val OVERCHARGE: OdysseyEnchantment = Overcharge
    val POTION_BARRIER: OdysseyEnchantment = PotionBarrier
    val SOUL_REND: OdysseyEnchantment = SoulRend
    val SPEEDY_SPURS: OdysseyEnchantment = SpeedySpurs
    val SPOREFUL: OdysseyEnchantment = Sporeful
    val SQUIDIFY: OdysseyEnchantment = Squidify
    val VENGEFUL: OdysseyEnchantment = Vengeful
    val VOID_STRIKE: OdysseyEnchantment = VoidStrike
    val WARP_JUMP: OdysseyEnchantment = WarpJump
    val WHIRLWIND: OdysseyEnchantment = Whirlwind

    // Set of all enchantments
    val enchantmentSet = setOf(GILDED_POWER, ALCHEMY_ARTILLERY, BACKSTABBER, BANE_OF_THE_ILLAGER, BANE_OF_THE_SEA, BANE_OF_THE_SWINE, BURST_BARRAGE, BUZZY_BEES, CHAIN_REACTION, COWARDICE, DECAYING_TOUCH, DOUSE, ECHO,
        EXPLODING, FREEZING_ASPECT, FROG_FRIGHT, FRUITFUL_FARE, GRAVITY_WELL, GUARDING_STRIKE, HEMORRHAGE, HOOK_SHOT, LUCKY_DRAW, MIRROR_FORCE, OVERCHARGE, POTION_BARRIER, SOUL_REND, SPEEDY_SPURS, SQUIDIFY, VOID_STRIKE, WARP_JUMP, WHIRLWIND)

    // Register
    fun register() {
        for (odysseyEnchant in enchantmentSet) {
            val registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(odysseyEnchant)
            if (!registered) registerEnchantment(odysseyEnchant as Enchantment)
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
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
    val ARCANE_CELL: OdysseyEnchantment = ArcaneCell
    val BACKSTABBER: OdysseyEnchantment = Backstabber
    val BANE_OF_THE_ILLAGER: OdysseyEnchantment = BaneOfTheIllager
    val BANE_OF_THE_SEA: OdysseyEnchantment = BaneOfTheSea
    val BANE_OF_THE_SWINE: OdysseyEnchantment = BaneOfTheSwine
    val BEASTLY_BRAWLER: OdysseyEnchantment = BeastlyBrawler
    val BOLA_SHOT: OdysseyEnchantment = BolaShot
    val BURST_BARRAGE: OdysseyEnchantment = BurstBarrage
    val BUZZY_BEES: OdysseyEnchantment = BuzzyBees
    val CHAIN_REACTION: OdysseyEnchantment = ChainReaction
    val CLUSTER_SHOT: OdysseyEnchantment = ClusterShot
    val COMMITTED: OdysseyEnchantment = Committed
    val COWARDICE: OdysseyEnchantment = Cowardice
    val CULL_THE_WEAK: OdysseyEnchantment = CullTheWeak
    val DECAYING_TOUCH: OdysseyEnchantment = DecayingTouch
    val DOUSE: OdysseyEnchantment = Douse
    val ECHO: OdysseyEnchantment = Echo
    val ENTANGLEMENT: OdysseyEnchantment = Entanglement
    val EXPLODING: OdysseyEnchantment = Exploding
    val FEARFUL_FINISHER: OdysseyEnchantment = FearfulFinisher
    val FREEZING_ASPECT: OdysseyEnchantment = FreezingAspect
    val FROG_FRIGHT: OdysseyEnchantment = FrogFright
    val FROSTY_FUSE: OdysseyEnchantment = FrostyFuse
    val FRUITFUL_FARE: OdysseyEnchantment = FruitfulFare
    val GALE_WIND: OdysseyEnchantment = GaleWind
    val GRAVITY_WELL: OdysseyEnchantment = GravityWell  // drop
    val GUARDING_STRIKE: OdysseyEnchantment = GuardingStrike
    val HEMORRHAGE: OdysseyEnchantment = Hemorrhage
    val HOOK_SHOT: OdysseyEnchantment = HookShot
    val ILLUCIDATION: OdysseyEnchantment = Illucidation
    val IGNORE_PAIN: OdysseyEnchantment = IgnorePain
    val LUCKY_DRAW: OdysseyEnchantment = LuckyDraw
    val MIRROR_FORCE: OdysseyEnchantment = MirrorForce
    val OVERCHARGE: OdysseyEnchantment = Overcharge
    val PERPETUAL_PROJECTILE: OdysseyEnchantment = PerpetualProjectile
    val POTION_BARRIER: OdysseyEnchantment = PotionBarrier
    val RECKLESS: OdysseyEnchantment = Reckless
    val RELENTLESS: OdysseyEnchantment = Relentless
    val RICOCHET: OdysseyEnchantment = Ricochet
    val RUPTURING_STRIKE: OdysseyEnchantment = RupturingStrike
    val SINGULARITY_SHOT: OdysseyEnchantment = SingularityShot // drop
    val SHARPSHOOTER: OdysseyEnchantment = Sharpshooter
    val SOUL_REND: OdysseyEnchantment = SoulRend
    val SPEEDY_SPURS: OdysseyEnchantment = SpeedySpurs
    val SPOREFUL: OdysseyEnchantment = Sporeful
    val SQUIDIFY: OdysseyEnchantment = Squidify
    val STELLAR_SHOWER: OdysseyEnchantment = StellarShower
    val TEMPORAL_TORRENT: OdysseyEnchantment = TemporalTorrent
    val VENGEFUL: OdysseyEnchantment = Vengeful
    val VICIOUS_VIGOR: OdysseyEnchantment = ViciousVigor
    val VOID_STRIKE: OdysseyEnchantment = VoidStrike
    val WARP_JUMP: OdysseyEnchantment = WarpJump
    val WAR_CRY: OdysseyEnchantment = WarCry
    val WHIRLWIND: OdysseyEnchantment = Whirlwind

    // Set of all enchantments
    val registeredSet = setOf(
        GILDED_POWER,
        ALCHEMY_ARTILLERY,
        ARCANE_CELL,
        BACKSTABBER,
        BANE_OF_THE_ILLAGER,
        BANE_OF_THE_SEA,
        BANE_OF_THE_SWINE,
        BEASTLY_BRAWLER,
        BOLA_SHOT,
        BURST_BARRAGE,
        BUZZY_BEES,
        CHAIN_REACTION,
        CLUSTER_SHOT,
        COMMITTED,
        COWARDICE,
        CULL_THE_WEAK,
        DECAYING_TOUCH,
        DOUSE,
        ECHO,
        EXPLODING,
        FREEZING_ASPECT,
        FEARFUL_FINISHER,
        FROG_FRIGHT,
        FROSTY_FUSE,
        FRUITFUL_FARE,
        GALE_WIND,
        GRAVITY_WELL,
        GUARDING_STRIKE,
        HEMORRHAGE,
        HOOK_SHOT,
        IGNORE_PAIN,
        ILLUCIDATION,
        LUCKY_DRAW,
        MIRROR_FORCE,
        OVERCHARGE,
        PERPETUAL_PROJECTILE,
        RECKLESS,
        RELENTLESS,
        RICOCHET,
        RUPTURING_STRIKE,
        POTION_BARRIER,
        SHARPSHOOTER,
        SINGULARITY_SHOT,
        SOUL_REND,
        SPEEDY_SPURS,
        SQUIDIFY,
        STELLAR_SHOWER,
        TEMPORAL_TORRENT,
        VICIOUS_VIGOR,
        VOID_STRIKE,
        WARP_JUMP,
        WAR_CRY,
        WHIRLWIND
    )


    // Set that is available to table
    val meleeSet = setOf(
        ARCANE_CELL,
        BACKSTABBER,
        BANE_OF_THE_ILLAGER,
        BANE_OF_THE_SEA,
        BANE_OF_THE_SWINE,
        BUZZY_BEES,
        COMMITTED,
        CULL_THE_WEAK,
        DECAYING_TOUCH,
        DOUSE,
        ECHO,
        EXPLODING,
        FEARFUL_FINISHER,
        FREEZING_ASPECT,
        FROG_FRIGHT,
        FROSTY_FUSE,
        GUARDING_STRIKE,
        HEMORRHAGE,
        ILLUCIDATION,
        RUPTURING_STRIKE,
        VOID_STRIKE,
        WHIRLWIND
    )
    val armorSet = setOf(
        BEASTLY_BRAWLER,
        COWARDICE,
        FRUITFUL_FARE,
        POTION_BARRIER,
        RECKLESS,
        RELENTLESS,
        SPEEDY_SPURS,
        SPOREFUL,
        SQUIDIFY,
        VENGEFUL,
        VICIOUS_VIGOR,
        WAR_CRY
    )
    val rangedSet = setOf(
        ALCHEMY_ARTILLERY,
        BOLA_SHOT,
        BURST_BARRAGE,
        CLUSTER_SHOT,
        CHAIN_REACTION,
        ENTANGLEMENT,
        GALE_WIND,
        LUCKY_DRAW,
        OVERCHARGE,
        PERPETUAL_PROJECTILE,
        RICOCHET,
        SHARPSHOOTER,
        SOUL_REND,
        STELLAR_SHOWER,
        TEMPORAL_TORRENT,

    )
    val miscSet = setOf(WARP_JUMP)


    // Register
    fun register() {
        for (odysseyEnchant in registeredSet) {
            val registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(odysseyEnchant)
            if (!registered) registerOdysseyEnchantment(odysseyEnchant as Enchantment)
        }
    }

    private fun registerOdysseyEnchantment(enchantment: Enchantment?) {
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
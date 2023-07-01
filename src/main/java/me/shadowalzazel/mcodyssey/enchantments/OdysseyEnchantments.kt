package me.shadowalzazel.mcodyssey.enchantments

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.enchantments.armor.*
import me.shadowalzazel.mcodyssey.enchantments.base.GildedPower
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.enchantments.melee.*
import me.shadowalzazel.mcodyssey.enchantments.misc.*
import me.shadowalzazel.mcodyssey.enchantments.ranged.*
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
    val BOMB_OB: OdysseyEnchantment = BombOb
    val BURST_BARRAGE: OdysseyEnchantment = BurstBarrage
    val BUZZY_BEES: OdysseyEnchantment = BuzzyBees
    val CHAIN_REACTION: OdysseyEnchantment = ChainReaction
    val CLUSTER_SHOT: OdysseyEnchantment = ClusterShot
    val COMMITTED: OdysseyEnchantment = Committed
    val COWARDICE: OdysseyEnchantment = Cowardice
    val CULL_THE_WEAK: OdysseyEnchantment = CullTheWeak
    val DECAYING_TOUCH: OdysseyEnchantment = DecayingTouch
    val DEVASTATING_DROP: OdysseyEnchantment = DevastatingDrop
    val DOUSE: OdysseyEnchantment = Douse
    val ECHO: OdysseyEnchantment = Echo
    val ENTANGLEMENT: OdysseyEnchantment = Entanglement // TODO
    val EXPLODING: OdysseyEnchantment = Exploding
    val FEARFUL_FINISHER: OdysseyEnchantment = FearfulFinisher
    val FREEZING_ASPECT: OdysseyEnchantment = FreezingAspect
    val FROG_FRIGHT: OdysseyEnchantment = FrogFright // FIX
    val FROSTY_FUSE: OdysseyEnchantment = FrostyFuse
    val FRUITFUL_FARE: OdysseyEnchantment = FruitfulFare
    val GALE_WIND: OdysseyEnchantment = GaleWind
    val GRAVITY_WELL: OdysseyEnchantment = GravityWell
    val GUARDING_STRIKE: OdysseyEnchantment = GuardingStrike
    val HEMORRHAGE: OdysseyEnchantment = Hemorrhage
    val HOOK_SHOT: OdysseyEnchantment = HookShot
    val ILLUCIDATION: OdysseyEnchantment = Illucidation
    val IGNORE_PAIN: OdysseyEnchantment = IgnorePain // TODO
    val LENGTHY_LINE: OdysseyEnchantment = LengthyLine
    val LUCKY_DRAW: OdysseyEnchantment = LuckyDraw
    val MIRROR_FORCE: OdysseyEnchantment = MirrorForce // TODO
    val O_SHINY: OdysseyEnchantment = OShiny
    val OVERCHARGE: OdysseyEnchantment = Overcharge
    val PERPETUAL_PROJECTILE: OdysseyEnchantment = PerpetualProjectile
    val POTION_BARRIER: OdysseyEnchantment = PotionBarrier
    val RECKLESS: OdysseyEnchantment = Reckless // TODO
    val RELENTLESS: OdysseyEnchantment = Relentless // TODO
    val RICOCHET: OdysseyEnchantment = Ricochet
    val RUPTURING_STRIKE: OdysseyEnchantment = RupturingStrike
    val SINGULARITY_SHOT: OdysseyEnchantment = SingularityShot // TODO
    val SHARPSHOOTER: OdysseyEnchantment = Sharpshooter
    val SOUL_REND: OdysseyEnchantment = SoulRend
    val SPEEDY_SPURS: OdysseyEnchantment = SpeedySpurs
    val SPOREFUL: OdysseyEnchantment = Sporeful
    val SQUIDIFY: OdysseyEnchantment = Squidify
    val STELLAR_SHOWER: OdysseyEnchantment = StellarShower // TODO
    val TEMPORAL_TORRENT: OdysseyEnchantment = TemporalTorrent // TODO
    val UNTOUCHABLE: OdysseyEnchantment = Untouchable // TODO
    val VENGEFUL: OdysseyEnchantment = Vengeful // TODO
    val VICIOUS_VIGOR: OdysseyEnchantment = ViciousVigor // TODO
    val VOID_JUMP: OdysseyEnchantment = VoidJump
    val VOID_STRIKE: OdysseyEnchantment = VoidStrike
    val WAR_CRY: OdysseyEnchantment = WarCry // TODO
    val WISE_BAIT: OdysseyEnchantment = WiseBait
    val WHIRLWIND: OdysseyEnchantment = Whirlwind
    val YANK: OdysseyEnchantment = Yank

    // Set of all enchantments
    val REGISTERED_SET = setOf(
        GILDED_POWER,
        ALCHEMY_ARTILLERY,
        ARCANE_CELL,
        BACKSTABBER,
        BANE_OF_THE_ILLAGER,
        BANE_OF_THE_SEA,
        BANE_OF_THE_SWINE,
        BEASTLY_BRAWLER,
        BOLA_SHOT,
        BOMB_OB,
        BURST_BARRAGE,
        BUZZY_BEES,
        CHAIN_REACTION,
        CLUSTER_SHOT,
        COMMITTED,
        COWARDICE,
        CULL_THE_WEAK,
        DECAYING_TOUCH,
        DEVASTATING_DROP,
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
        LENGTHY_LINE,
        LUCKY_DRAW,
        MIRROR_FORCE,
        O_SHINY,
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
        UNTOUCHABLE,
        VICIOUS_VIGOR,
        VOID_STRIKE,
        VOID_JUMP,
        WAR_CRY,
        WISE_BAIT,
        WHIRLWIND,
        YANK
    )


    // Set that is available to table
    val MELEE_SET = setOf(
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

    val ARMOR_SET = setOf(
        BEASTLY_BRAWLER,
        COWARDICE,
        DEVASTATING_DROP,
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

    val RANGED_SET = setOf(
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
        TEMPORAL_TORRENT
    )

    val MISC_SET = setOf(
        BOMB_OB,
        LENGTHY_LINE,
        O_SHINY,
        VOID_JUMP,
        WISE_BAIT,
        YANK
    )


    // Register
    fun register() {
        for (odysseyEnchant in REGISTERED_SET) {
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
            Odyssey.instance.logger.info("Registered: $enchantment")
        }
    }
}
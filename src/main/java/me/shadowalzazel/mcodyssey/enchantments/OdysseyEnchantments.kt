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

    // Armor
    val ANTIBONK: OdysseyEnchantment = Antibonk
    val BEASTLY_BRAWLER: OdysseyEnchantment = BeastlyBrawler
    val BLACK_ROSE: OdysseyEnchantment = BlackRose
    val BREWFUL_BREATH: OdysseyEnchantment = BrewfulBreath
    val COWARDICE: OdysseyEnchantment = Cowardice
    val DEVASTATING_DROP: OdysseyEnchantment = DevastatingDrop
    val DREADFUL_SHRIEK: OdysseyEnchantment = DreadfulShriek
    val FRUITFUL_FARE: OdysseyEnchantment = FruitfulFare
    val IGNORE_PAIN: OdysseyEnchantment = IgnorePain // TODO
    val ILLUMINEYE: OdysseyEnchantment = Illumineye // TODO
    val LEAP_FROG: OdysseyEnchantment = LeapFrog
    val MOLTEN_CORE: OdysseyEnchantment = MoltenCore // TODO
    val OPTICALIZATION: OdysseyEnchantment = Opticalization
    val POTION_BARRIER: OdysseyEnchantment = PotionBarrier
    val RAGING_ROAR: OdysseyEnchantment = RagingRoar
    val RECKLESS: OdysseyEnchantment = Reckless
    val RELENTLESS: OdysseyEnchantment = Relentless
    val ROOT_BOOTS: OdysseyEnchantment = RootBoots
    val SCULK_SENSITIVE: OdysseyEnchantment = SculkSensitive // TODO
    val SPEEDY_SPURS: OdysseyEnchantment = SpeedySpurs
    val SPOREFUL: OdysseyEnchantment = Sporeful
    val SQUIDIFY: OdysseyEnchantment = Squidify
    val SSLITHER_SSIGHT: OdysseyEnchantment = SslitherSsight
    val STATIC_SOCKS: OdysseyEnchantment = StaticSocks
    val UNTOUCHABLE: OdysseyEnchantment = Untouchable // TODO
    val VENGEFUL: OdysseyEnchantment = Vengeful // TODO
    val VICIOUS_VIGOR: OdysseyEnchantment = ViciousVigor // TODO
    val WAR_CRY: OdysseyEnchantment = WarCry // TODO

    // Melee
    val ARCANE_CELL: OdysseyEnchantment = ArcaneCell
    val BACKSTABBER: OdysseyEnchantment = Backstabber
    val BANE_OF_THE_ILLAGER: OdysseyEnchantment = BaneOfTheIllager
    val BANE_OF_THE_SEA: OdysseyEnchantment = BaneOfTheSea
    val BANE_OF_THE_SWINE: OdysseyEnchantment = BaneOfTheSwine
    val BUZZY_BEES: OdysseyEnchantment = BuzzyBees
    val COMMITTED: OdysseyEnchantment = Committed
    val CULL_THE_WEAK: OdysseyEnchantment = CullTheWeak
    val DECAYING_TOUCH: OdysseyEnchantment = DecayingTouch
    val DOUSE: OdysseyEnchantment = Douse
    val ECHO: OdysseyEnchantment = Echo
    val EXPLODING: OdysseyEnchantment = Exploding
    val FEARFUL_FINISHER: OdysseyEnchantment = FearfulFinisher
    val FREEZING_ASPECT: OdysseyEnchantment = FreezingAspect
    val FROG_FRIGHT: OdysseyEnchantment = FrogFright // FIX
    val FROSTY_FUSE: OdysseyEnchantment = FrostyFuse
    val GRAVITY_WELL: OdysseyEnchantment = GravityWell
    val GUARDING_STRIKE: OdysseyEnchantment = GuardingStrike
    val HEMORRHAGE: OdysseyEnchantment = Hemorrhage
    val ILLUCIDATION: OdysseyEnchantment = Illucidation
    val RUPTURING_STRIKE: OdysseyEnchantment = RupturingStrike
    val VOID_STRIKE: OdysseyEnchantment = VoidStrike
    val WHIRLWIND: OdysseyEnchantment = Whirlwind

    // Misc
    val BOMB_OB: OdysseyEnchantment = BombOb
    val HOOK_SHOT: OdysseyEnchantment = HookShot
    val LENGTHY_LINE: OdysseyEnchantment = LengthyLine
    val MIRROR_FORCE: OdysseyEnchantment = MirrorForce // TODO
    val REVERSED_RECOIL: OdysseyEnchantment = ReversedRecoil
    val O_SHINY: OdysseyEnchantment = OShiny
    val VOID_JUMP: OdysseyEnchantment = VoidJump
    val WISE_BAIT: OdysseyEnchantment = WiseBait
    val YANK: OdysseyEnchantment = Yank

    // Ranged
    val ALCHEMY_ARTILLERY: OdysseyEnchantment = AlchemyArtillery
    val BOLA_SHOT: OdysseyEnchantment = BolaShot
    val BURST_BARRAGE: OdysseyEnchantment = BurstBarrage
    val CHAIN_REACTION: OdysseyEnchantment = ChainReaction
    val CLUSTER_SHOT: OdysseyEnchantment = ClusterShot
    val DEADEYE: OdysseyEnchantment = Deadeye
    val ENTANGLEMENT: OdysseyEnchantment = Entanglement // TODO
    val GALE_WIND: OdysseyEnchantment = GaleWind
    val HEAVY_BALLISTICS: OdysseyEnchantment = HeavyBallistics
    val LUCKY_DRAW: OdysseyEnchantment = LuckyDraw
    val OVERCHARGE: OdysseyEnchantment = Overcharge
    val PERPETUAL_PROJECTILE: OdysseyEnchantment = PerpetualProjectile
    val RICOCHET: OdysseyEnchantment = Ricochet
    val SHARPSHOOTER: OdysseyEnchantment = Sharpshooter
    val SINGULARITY_SHOT: OdysseyEnchantment = SingularityShot // TODO
    val SOUL_REND: OdysseyEnchantment = SoulRend
    val STELLAR_SHOWER: OdysseyEnchantment = StellarShower // TODO
    val TEMPORAL_TORRENT: OdysseyEnchantment = TemporalTorrent // TODO

    // --------------------------------------------------------------

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
        ANTIBONK,
        BEASTLY_BRAWLER,
        BLACK_ROSE,
        BREWFUL_BREATH,
        COWARDICE,
        DEVASTATING_DROP,
        DREADFUL_SHRIEK,
        FRUITFUL_FARE,
        IGNORE_PAIN,
        ILLUMINEYE,
        LEAP_FROG,
        MOLTEN_CORE,
        OPTICALIZATION,
        POTION_BARRIER,
        RAGING_ROAR,
        RECKLESS,
        RELENTLESS,
        ROOT_BOOTS,
        SCULK_SENSITIVE,
        SPEEDY_SPURS,
        SPOREFUL,
        SQUIDIFY,
        SSLITHER_SSIGHT,
        STATIC_SOCKS,
        UNTOUCHABLE,
        VENGEFUL,
        VICIOUS_VIGOR,
        WAR_CRY
    )

    val MISC_SET = setOf(
        BOMB_OB,
        HOOK_SHOT,
        LENGTHY_LINE,
        MIRROR_FORCE,
        O_SHINY,
        REVERSED_RECOIL,
        VOID_JUMP,
        WISE_BAIT,
        YANK
    )

    val RANGED_SET = setOf(
        ALCHEMY_ARTILLERY,
        BOLA_SHOT,
        BURST_BARRAGE,
        CHAIN_REACTION,
        CLUSTER_SHOT,
        DEADEYE,
        ENTANGLEMENT,
        GALE_WIND,
        HEAVY_BALLISTICS,
        LUCKY_DRAW,
        OVERCHARGE,
        PERPETUAL_PROJECTILE,
        RICOCHET,
        SHARPSHOOTER,
        SINGULARITY_SHOT,
        SOUL_REND,
        STELLAR_SHOWER,
        TEMPORAL_TORRENT
    )

    val REGISTERED_SET = setOf(GILDED_POWER) + ARMOR_SET + MELEE_SET + MISC_SET + RANGED_SET
    val EXOTIC_LIST = setOf(SINGULARITY_SHOT, GRAVITY_WELL, SCULK_SENSITIVE)



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
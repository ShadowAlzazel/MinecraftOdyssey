package me.shadowalzazel.mcodyssey.enchantments

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.enchantments.armor.*
import me.shadowalzazel.mcodyssey.enchantments.base.GildedPower
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.enchantments.melee.*
import me.shadowalzazel.mcodyssey.enchantments.misc.*
import me.shadowalzazel.mcodyssey.enchantments.ranged.*
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import org.bukkit.enchantments.Enchantment

object OdysseyEnchantments : EnchantRegistryManager {

    // Utility
    val GILDED_POWER: OdysseyEnchantment = GildedPower

    // Armor
    val ANTIBONK: OdysseyEnchantment = Antibonk
    val BEASTLY_BRAWLER: OdysseyEnchantment = BeastlyBrawler
    val BLACK_ROSE: OdysseyEnchantment = BlackRose
    val BLURCISE: OdysseyEnchantment = Blurcise
    val BREWFUL_BREATH: OdysseyEnchantment = BrewfulBreath
    val COPPER_CHITIN: OdysseyEnchantment = CopperChitin
    val COWARDICE: OdysseyEnchantment = Cowardice
    val DEVASTATING_DROP: OdysseyEnchantment = DevastatingDrop
    val DREADFUL_SHRIEK: OdysseyEnchantment = DreadfulShriek
    val FRUITFUL_FARE: OdysseyEnchantment = FruitfulFare
    val IGNORE_PAIN: OdysseyEnchantment = IgnorePain
    val ILLUMINEYE: OdysseyEnchantment = Illumineye
    val LEAP_FROG: OdysseyEnchantment = LeapFrog
    val MANDIBLEMANIA: OdysseyEnchantment = Mandiblemania
    val MOLTEN_CORE: OdysseyEnchantment = MoltenCore
    val MOONWARD: OdysseyEnchantment = Moonward
    val OPTICALIZATION: OdysseyEnchantment = Opticalization // TODO
    val POLLEN_GUARD: OdysseyEnchantment = PollenGuard
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
    val UNTOUCHABLE: OdysseyEnchantment = Untouchable
    val VEILED_IN_SHADOW: OdysseyEnchantment = VeiledInShadow
    val VENGEFUL: OdysseyEnchantment = Vengeful // TODO
    val VICIOUS_VIGOR: OdysseyEnchantment = ViciousVigor // TODO
    val WAR_CRY: OdysseyEnchantment = WarCry

    // Melee
    val ASPHYXIATING_ASSAULT: OdysseyEnchantment = AsphyxiatingAssault
    val ARCANE_CELL: OdysseyEnchantment = ArcaneCell
    val BACKSTABBER: OdysseyEnchantment = Backstabber
    val BANE_OF_THE_ILLAGER: OdysseyEnchantment = BaneOfTheIllager
    val BANE_OF_THE_SEA: OdysseyEnchantment = BaneOfTheSea
    val BANE_OF_THE_SWINE: OdysseyEnchantment = BaneOfTheSwine
    val BLITZ_SHIFT: OdysseyEnchantment = BlitzShift
    val BUZZY_BEES: OdysseyEnchantment = BuzzyBees
    val COMMITTED: OdysseyEnchantment = Committed
    val CULL_THE_WEAK: OdysseyEnchantment = CullTheWeak
    val DECAYING_TOUCH: OdysseyEnchantment = DecayingTouch
    val DOUSE: OdysseyEnchantment = Douse
    val ECHO: OdysseyEnchantment = Echo
    val EXPLODING: OdysseyEnchantment = Exploding
    val FEARFUL_FINISHER: OdysseyEnchantment = FearfulFinisher
    val FREEZING_ASPECT: OdysseyEnchantment = FreezingAspect
    val FROG_FRIGHT: OdysseyEnchantment = FrogFright
    val FROSTY_FUSE: OdysseyEnchantment = FrostyFuse
    val GRAVITY_WELL: OdysseyEnchantment = GravityWell
    val GUARDING_STRIKE: OdysseyEnchantment = GuardingStrike
    val HEMORRHAGE: OdysseyEnchantment = Hemorrhage
    val ILLUCIDATION: OdysseyEnchantment = Illucidation
    val RUPTURING_STRIKE: OdysseyEnchantment = RupturingStrike
    val SPORING_ROT: OdysseyEnchantment = SporingRot
    val TAR_N_DIP: OdysseyEnchantment = TarNDip
    val VOID_STRIKE: OdysseyEnchantment = VoidStrike
    val WHIRLWIND: OdysseyEnchantment = Whirlwind

    // Misc
    val BOMB_OB: OdysseyEnchantment = BombOb
    val HOOK_SHOT: OdysseyEnchantment = HookShot
    val LENGTHY_LINE: OdysseyEnchantment = LengthyLine
    val MIRROR_FORCE: OdysseyEnchantment = MirrorForce // TODO
    val REVERSED_RECOIL: OdysseyEnchantment = ReversedRecoil // TODO
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
    val DEATH_FROM_ABOVE: OdysseyEnchantment = DeathFromAbove
    val DOUBLE_TAP: OdysseyEnchantment = DoubleTap
    val FAN_FIRE: OdysseyEnchantment = FanFire
    val ENTANGLEMENT: OdysseyEnchantment = Entanglement // TODO
    val GALE_WIND: OdysseyEnchantment = GaleWind
    val HEAVY_BALLISTICS: OdysseyEnchantment = HeavyBallistics
    val LUXPOSE: OdysseyEnchantment = Luxpose
    val LUCKY_DRAW: OdysseyEnchantment = LuckyDraw
    val OVERCHARGE: OdysseyEnchantment = Overcharge
    val PERPETUAL_PROJECTILE: OdysseyEnchantment = PerpetualProjectile
    val RICOCHET: OdysseyEnchantment = Ricochet
    val SHARPSHOOTER: OdysseyEnchantment = Sharpshooter
    val SINGLE_OUT: OdysseyEnchantment = SingleOut
    val SINGULARITY_SHOT: OdysseyEnchantment = SingularityShot // TODO
    val SOUL_REND: OdysseyEnchantment = SoulRend
    val STELLAR_SHOWER: OdysseyEnchantment = StellarShower // TODO
    val TEMPORAL_TORRENT: OdysseyEnchantment = TemporalTorrent // TODO
    val VULNEROCITY: OdysseyEnchantment = Vulnerocity

    val MELEE_SET = setOf(
        ASPHYXIATING_ASSAULT,
        ARCANE_CELL,
        BACKSTABBER,
        BANE_OF_THE_ILLAGER,
        BANE_OF_THE_SEA,
        BANE_OF_THE_SWINE,
        BLITZ_SHIFT,
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
        GRAVITY_WELL,
        GUARDING_STRIKE,
        HEMORRHAGE,
        ILLUCIDATION,
        RUPTURING_STRIKE,
        SPORING_ROT,
        TAR_N_DIP,
        VOID_STRIKE,
        WHIRLWIND
    )

    val ARMOR_SET = setOf(
        ANTIBONK,
        BEASTLY_BRAWLER,
        BLACK_ROSE,
        BLURCISE,
        BREWFUL_BREATH,
        COPPER_CHITIN,
        COWARDICE,
        DEVASTATING_DROP,
        DREADFUL_SHRIEK,
        FRUITFUL_FARE,
        IGNORE_PAIN,
        ILLUMINEYE,
        LEAP_FROG,
        MANDIBLEMANIA,
        MOLTEN_CORE,
        MOONWARD,
        OPTICALIZATION,
        POLLEN_GUARD,
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
        VEILED_IN_SHADOW,
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
        DEATH_FROM_ABOVE,
        DOUBLE_TAP,
        ENTANGLEMENT,
        FAN_FIRE,
        GALE_WIND,
        HEAVY_BALLISTICS,
        LUCKY_DRAW,
        LUXPOSE,
        OVERCHARGE,
        PERPETUAL_PROJECTILE,
        RICOCHET,
        SHARPSHOOTER,
        SINGLE_OUT,
        SINGULARITY_SHOT,
        SOUL_REND,
        STELLAR_SHOWER,
        TEMPORAL_TORRENT,
        VULNEROCITY
    )

    private const val ODYSSEY_NAMESPACE: String = "odyssey"

    val REGISTERED_SET = ARMOR_SET + MELEE_SET + MISC_SET + RANGED_SET + setOf(GILDED_POWER)
    val EXOTIC_LIST = setOf(SINGULARITY_SHOT, GRAVITY_WELL,
        STELLAR_SHOWER, SCULK_SENSITIVE, BLACK_ROSE) // To exclude for table

    // Register
    fun registerAll() {
        /* ---------- PRE 1.20.4 ----------- */
        /*
        for (odysseyEnchant in REGISTERED_SET) {
            val registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(odysseyEnchant)
            if (!registered) registerOdysseyEnchantment(odysseyEnchant as Enchantment)
        }
         */
        for (odysseyEnchant in REGISTERED_SET) {
            var registered = false
            try {
                net.minecraft.core.Registry.register( // Using own namespace for safety and support
                    BuiltInRegistries.ENCHANTMENT,
                    ResourceLocation(ODYSSEY_NAMESPACE, odysseyEnchant.name),
                    odysseyEnchant
                )
                /*
                net.minecraft.core.Registry.register( // Can change from TO resource key FROM string
                    BuiltInRegistries.ENCHANTMENT,
                    odysseyEnchant.name,
                    odysseyEnchant
                )
                 */
                registered = true
            }
            catch (exception: Exception) {
                exception.printStackTrace()
            }
            // SUCCESS!!
            if (registered) {
                Odyssey.instance.logger.info("Registered: $odysseyEnchant")
            }
        }
    }


    /* ---------- PRE 1.20.4 ----------- */
    private fun registerOdysseyEnchantment(enchantment: Enchantment?) {
        var registered = false
        try {
            if (enchantment != null) {
                val field = Enchantment::class.java.getDeclaredField("acceptingNew")
                field.isAccessible = true
                field[null] = true
                //Odyssey.instance.server.getRegistry()
                //Enchantment.registerEnchantment(enchantment)
            }
            registered = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (registered) {  // Send to console
            Odyssey.instance.logger.info("Registered: $enchantment")
        } else {
            Odyssey.instance.logger.info("Failed to register: $enchantment")
        }
    }

}
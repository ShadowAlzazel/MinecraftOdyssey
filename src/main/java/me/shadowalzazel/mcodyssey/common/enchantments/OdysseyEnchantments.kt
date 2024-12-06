package me.shadowalzazel.mcodyssey.common.enchantments

import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.util.RegistryTagManager
import org.bukkit.enchantments.Enchantment

@Suppress("MemberVisibilityCanBePrivate", "Unused")
object OdysseyEnchantments : EnchantmentManager, RegistryTagManager {

    private val enchantmentRegistry = getPaperRegistry(RegistryKey.ENCHANTMENT)
    private val getRegisteredEnchant: (String) -> (Enchantment) = { enchantmentRegistry.get(createOdysseyKey(it))!!}

    // Armor
    val ANALYSIS: Enchantment = getRegisteredEnchant("analysis")
    val ANTIBONK: Enchantment = getRegisteredEnchant("antibonk")
    val BEASTLY: Enchantment = getRegisteredEnchant("beastly")
    val BLACK_ROSE: Enchantment = getRegisteredEnchant("black_rose")
    val BLURCISE: Enchantment = getRegisteredEnchant("blurcise")
    val BRAWLER: Enchantment = getRegisteredEnchant("brawler")
    val BULWARK: Enchantment = getRegisteredEnchant("bulwark")
    val CLAW_CLIMBING: Enchantment = getRegisteredEnchant("claw_climbing")
    val COWARDICE: Enchantment = getRegisteredEnchant("cowardice")
    val DEVASTATING_DROP: Enchantment = getRegisteredEnchant("devastating_drop")
    val FRUITFUL_FARE: Enchantment = getRegisteredEnchant("fruitful_fare")
    val HEARTENED: Enchantment = getRegisteredEnchant("heartened")
    val IGNORE_PAIN: Enchantment = getRegisteredEnchant("ignore_pain")
    val ILLUMINEYE: Enchantment = getRegisteredEnchant("illumineye")
    val IMPETUS: Enchantment = getRegisteredEnchant("impetus")
    val LEAP_FROG: Enchantment = getRegisteredEnchant("leap_frog")
    val MANDIBLEMANIA: Enchantment = getRegisteredEnchant("mandiblemania")
    val MOLTEN_CORE: Enchantment = getRegisteredEnchant("molten_core")
    val OPTICALIZATION: Enchantment = getRegisteredEnchant("opticalization")
    val POTION_BARRIER: Enchantment = getRegisteredEnchant("potion_barrier")
    val RAGING_ROAR: Enchantment = getRegisteredEnchant("antibonk")
    val RECKLESS: Enchantment = getRegisteredEnchant("reckless")
    val RELENTLESS: Enchantment = getRegisteredEnchant("relentless")
    val ROOT_BOOTS: Enchantment = getRegisteredEnchant("root_boots")
    val SCULK_SENSITIVE: Enchantment = getRegisteredEnchant("sculk_sensitive")
    val SPEEDY_SPURS: Enchantment = getRegisteredEnchant("speedy_spurs")
    val SPOREFUL: Enchantment = getRegisteredEnchant("sporeful")
    val SQUIDIFY: Enchantment = getRegisteredEnchant("squidify")
    val SSLITHER_SSIGHT: Enchantment = getRegisteredEnchant("sslither_ssight")
    val STATIC_SOCKS: Enchantment = getRegisteredEnchant("static_socks")
    val UNTOUCHABLE: Enchantment = getRegisteredEnchant("untouchable")
    val VEILED_IN_SHADOW: Enchantment = getRegisteredEnchant("veiled_in_shadow")
    val VIGOR: Enchantment = getRegisteredEnchant("vigor")

    // Melee
    val AGILE: Enchantment = getRegisteredEnchant("agile")
    val ARCANE_CELL: Enchantment = getRegisteredEnchant("arcane_cell")
    val ASPHYXIATE: Enchantment = getRegisteredEnchant("asphyxiate")
    val BACKSTABBER: Enchantment = getRegisteredEnchant("backstabber")
    val BANE_OF_THE_ILLAGER: Enchantment = getRegisteredEnchant("bane_of_the_illager")
    val BANE_OF_THE_SEA: Enchantment = getRegisteredEnchant("bane_of_the_sea")
    val BANE_OF_THE_SWINE: Enchantment = getRegisteredEnchant("bane_of_the_swine")
    val BUDDING: Enchantment = getRegisteredEnchant("budding")
    val BUZZY_BEES: Enchantment = getRegisteredEnchant("buzzy_bees")
    val COMMITTED: Enchantment = getRegisteredEnchant("committed")
    val CONFLAGRATE: Enchantment = getRegisteredEnchant("conflagrate")
    val CULL_THE_WEAK: Enchantment = getRegisteredEnchant("cull_the_weak")
    val DECAY: Enchantment = getRegisteredEnchant("decay")
    val DOUSE: Enchantment = getRegisteredEnchant("douse")
    val ECHO: Enchantment = getRegisteredEnchant("echo")
    val EXPLODING: Enchantment = getRegisteredEnchant("exploding")
    val FEARFUL_FINISHER: Enchantment = getRegisteredEnchant("fearful_finisher")
    val FREEZING_ASPECT: Enchantment = getRegisteredEnchant("freezing_aspect")
    val FROG_FRIGHT: Enchantment = getRegisteredEnchant("frog_fright")
    val FROSTY_FUSE: Enchantment = getRegisteredEnchant("frosty_fuse")
    val GRASP: Enchantment = getRegisteredEnchant("grasp")
    val GRAVITY_WELL: Enchantment = getRegisteredEnchant("gravity_well")
    val GUARDING_STRIKE: Enchantment = getRegisteredEnchant("guarding_strike")
    val GUST: Enchantment = getRegisteredEnchant("gust")
    val HEMORRHAGE: Enchantment = getRegisteredEnchant("hemorrhage")
    val ILLUCIDATION: Enchantment = getRegisteredEnchant("illucidation")
    val INVOCATIVE: Enchantment = getRegisteredEnchant("invocative")
    val PESTILENCE: Enchantment = getRegisteredEnchant("pestilence")
    val RUPTURE: Enchantment = getRegisteredEnchant("rupture")
    val SWAP: Enchantment = getRegisteredEnchant("swap")
    val VENGEFUL: Enchantment = getRegisteredEnchant("vengeful")
    val VITAL: Enchantment = getRegisteredEnchant("vital")
    val VOID_STRIKE: Enchantment = getRegisteredEnchant("void_strike")
    val WHIRLWIND: Enchantment = getRegisteredEnchant("whirlwind")

    // Ranged
    val ALCHEMY_ARTILLERY: Enchantment = getRegisteredEnchant("alchemy_artillery")
    val BALLISTICS: Enchantment = getRegisteredEnchant("ballistics")
    val BOLA_SHOT: Enchantment = getRegisteredEnchant("bola_shot")
    val BURST_BARRAGE: Enchantment = getRegisteredEnchant("burst_barrage")
    val CHAIN_REACTION: Enchantment = getRegisteredEnchant("chain_reaction")
    val CLUSTER_SHOT: Enchantment = getRegisteredEnchant("cluster_shot")
    val DEADEYE: Enchantment = getRegisteredEnchant("deadeye")
    val DEATH_FROM_ABOVE: Enchantment = getRegisteredEnchant("death_from_above")
    val DOUBLE_TAP: Enchantment = getRegisteredEnchant("double_tap")
    val ENTANGLEMENT: Enchantment = getRegisteredEnchant("entanglement")
    val FAN_FIRE: Enchantment = getRegisteredEnchant("fan_fire")
    val GALE: Enchantment = getRegisteredEnchant("gale")
    val LUCKY_DRAW: Enchantment = getRegisteredEnchant("lucky_draw")
    val LUXPOSE: Enchantment = getRegisteredEnchant("luxpose")
    val OVERCHARGE: Enchantment = getRegisteredEnchant("overcharge")
    val PERPETUAL: Enchantment = getRegisteredEnchant("perpetual")
    val RAIN_OF_ARROWS: Enchantment = getRegisteredEnchant("rain_of_arrows")
    val RICOCHET: Enchantment = getRegisteredEnchant("ricochet")
    val SHARPSHOOTER: Enchantment = getRegisteredEnchant("sharpshooter")
    val SINGLE_OUT: Enchantment = getRegisteredEnchant("single_out")
    val SINGULARITY_SHOT: Enchantment = getRegisteredEnchant("singularity_shot")
    val STEADY_AIM: Enchantment = getRegisteredEnchant("steady_aim")
    val REND: Enchantment = getRegisteredEnchant("rend")
    val TEMPORAL: Enchantment = getRegisteredEnchant("temporal")
    val VULNEROCITY: Enchantment = getRegisteredEnchant("vulnerocity")

    // Misc
    val CHITIN: Enchantment = getRegisteredEnchant("chitin")
    val MOONPATCH: Enchantment = getRegisteredEnchant("moonpatch")
    val O_SHINY: Enchantment = getRegisteredEnchant("o_shiny")
    // Curses
    val ENCUMBERING_CURSE: Enchantment = getRegisteredEnchant("encumbering_curse")
    val PARASITIC_CURSE: Enchantment = getRegisteredEnchant("parasitic_curse")

    // Other
    val BOMB_OB: Enchantment = getRegisteredEnchant("bomb_ob")
    val HOOK_SHOT: Enchantment = getRegisteredEnchant("hook_shot")
    val LENGTHY_LINE: Enchantment = getRegisteredEnchant("lengthy_line")
    val SCOURER: Enchantment = getRegisteredEnchant("scourer")
    val WISDOM_OF_THE_DEEP: Enchantment = getRegisteredEnchant("wisdom_of_the_deep")
    val YANK: Enchantment = getRegisteredEnchant("yank")

    val MIRROR_FORCE: Enchantment = getRegisteredEnchant("mirror_force")
    val VOID_JUMP: Enchantment = getRegisteredEnchant("antibonk")

    /*-----------------------------------------------------------------------------------------------*/
    //getTagFromRegistry(RegistryKey.ENCHANTMENT, "curse") -> returns enchants in datapack with curse


    val inTableMelee = getCollectionFromTag(RegistryKey.ENCHANTMENT, "in_table/melee")
    val nonTableMelee = getCollectionFromTag(RegistryKey.ENCHANTMENT, "non_table/melee")

    val meleeSet = inTableMelee + nonTableMelee
    val rangedSet = getCollectionFromTag(RegistryKey.ENCHANTMENT, "in_table/ranged")
    val armorSet = getCollectionFromTag(RegistryKey.ENCHANTMENT, "non_table/armor")

    val MISC_SET = setOf(
        BOMB_OB,
        HOOK_SHOT,
        LENGTHY_LINE,
        MIRROR_FORCE,
        O_SHINY,
        VOID_JUMP,
        WISDOM_OF_THE_DEEP,
        YANK
    )

    val EXOTIC_LIST = setOf(SINGULARITY_SHOT, GRAVITY_WELL, SCULK_SENSITIVE, BLACK_ROSE) // To exclude from enchantment table

}
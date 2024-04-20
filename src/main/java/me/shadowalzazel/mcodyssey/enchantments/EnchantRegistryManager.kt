package me.shadowalzazel.mcodyssey.enchantments

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.enchantments.Enchantment

interface EnchantRegistryManager  {

    // Extension to easily check if odyssey/not
    fun Enchantment.isOdysseyEnchant(): Boolean {
        val enchant = findOdysseyEnchant(this)
        return enchant != null // IF not Null -> Is odyssey enchantment
    }

    // Extension to easily check if odyssey/not
    fun Enchantment.isNotOdysseyEnchant(): Boolean {
        val enchant = findOdysseyEnchant(this)
        return enchant == null // IF Null -> Is NOT odyssey enchantment
    }

    // Called When enchant needs to be OdysseyEnchantment Class
    fun Enchantment.convertToOdysseyEnchant(): OdysseyEnchantment {
        return getOdysseyEnchantFromString(this.key.key)!!
    }


    // Get bukkit
    fun convertToBukkitEnchant(enchantment: OdysseyEnchantment): Enchantment? {
        //val key = enchantment.getKey()
        //val foundEnchant = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(key.key))
        val enchant = Registry.ENCHANTMENT.get(NamespacedKey(Odyssey.instance, enchantment.name))
        return enchant
    }

    // Call to check enchant is Odyssey
    fun enchantIsOdyssey(enchantment: Enchantment): Boolean {
        val enchant = findOdysseyEnchant(enchantment)
        return enchant != null // IF finds -> Is odyssey enchantment
    }

    // Tries to find OdysseyEnchantment from a bukkit enchantment
    fun findOdysseyEnchant(enchantment: Enchantment): OdysseyEnchantment? {
        val key = enchantment.key.key
        return getOdysseyEnchantFromString(key)
    }

    /*
    fun findEnchantFromString(string: String): OdysseyEnchantment? {
        val enchant = Registry.ENCHANTMENT.get(NamespacedKey(Odyssey.instance, string))
    }
     */

    // MAYBE CHANGE TO DICT??
    fun getOdysseyEnchantFromString(name: String): OdysseyEnchantment? {
        return when(name) {
            "antibonk" -> {
                OdysseyEnchantments.ANTIBONK
            }
            "beastly" -> {
                OdysseyEnchantments.BEASTLY
            }
            "black_rose" -> {
                OdysseyEnchantments.BLACK_ROSE
            }
            "blurcise" -> {
                OdysseyEnchantments.BLURCISE
            }
            "brawler" -> {
                OdysseyEnchantments.BRAWLER
            }
            "brewful_breath" -> {
                OdysseyEnchantments.BREWFUL_BREATH
            }
            "chitin" -> {
                OdysseyEnchantments.CHITIN
            }
            "cowardice" -> {
                OdysseyEnchantments.COWARDICE
            }
            "devastating_drop" -> {
                OdysseyEnchantments.DEVASTATING_DROP
            }
            "dreadful_shriek" -> {
                OdysseyEnchantments.DREADFUL_SHRIEK
            }
            "fruitful_fare" -> {
                OdysseyEnchantments.FRUITFUL_FARE
            }
            "ignore_pain" -> {
                OdysseyEnchantments.IGNORE_PAIN
            }
            "illumineye" -> {
                OdysseyEnchantments.ILLUMINEYE
            }
            "leap_frog" -> {
                OdysseyEnchantments.LEAP_FROG
            }
            "mandiblemania" -> {
                OdysseyEnchantments.MANDIBLEMANIA
            }
            "molten_core" -> {
                OdysseyEnchantments.MOLTEN_CORE
            }
            "moonpatch" -> {
                OdysseyEnchantments.MOONPATCH
            }
            "opticalization" -> {
                OdysseyEnchantments.OPTICALIZATION
            }
            "pollen_guard" -> {
                OdysseyEnchantments.POLLEN_GUARD
            }
            "potion_barrier" -> {
                OdysseyEnchantments.POTION_BARRIER
            }
            "raging_roar" -> {
                OdysseyEnchantments.RAGING_ROAR
            }
            "reckless" -> {
                OdysseyEnchantments.RECKLESS
            }
            "relentless" -> {
                OdysseyEnchantments.RELENTLESS
            }
            "root_boots" -> {
                OdysseyEnchantments.ROOT_BOOTS
            }
            "sculk_sensitive" -> {
                OdysseyEnchantments.SCULK_SENSITIVE
            }
            "speedy_spurs" -> {
                OdysseyEnchantments.SPEEDY_SPURS
            }
            "sporeful" -> {
                OdysseyEnchantments.SPOREFUL
            }
            "squidify" -> {
                OdysseyEnchantments.SQUIDIFY
            }
            "sslither_ssight" -> {
                OdysseyEnchantments.SSLITHER_SSIGHT
            }
            "static_socks" -> {
                OdysseyEnchantments.STATIC_SOCKS
            }
            "untouchable" -> {
                OdysseyEnchantments.UNTOUCHABLE
            }
            "veiled_in_shadow" -> {
                OdysseyEnchantments.VEILED_IN_SHADOW
            }
            "vengeful" -> {
                OdysseyEnchantments.VENGEFUL
            }
            "vicious_vigor" -> {
                OdysseyEnchantments.VICIOUS_VIGOR
            }
            "war_cry" -> {
                OdysseyEnchantments.WAR_CRY
            }
            "asphyxiate" -> {
                OdysseyEnchantments.ASPHYXIATE
            }
            "arcane_cell" -> {
                OdysseyEnchantments.ARCANE_CELL
            }
            "backstabber" -> {
                OdysseyEnchantments.BACKSTABBER
            }
            "bane_of_the_illager" -> {
                OdysseyEnchantments.BANE_OF_THE_ILLAGER
            }
            "bane_of_the_sea" -> {
                OdysseyEnchantments.BANE_OF_THE_SEA
            }
            "bane_of_the_swine" -> {
                OdysseyEnchantments.BANE_OF_THE_SWINE
            }
            "blitz_shift" -> {
                OdysseyEnchantments.BLITZ_SHIFT
            }
            "budding" -> {
                OdysseyEnchantments.BUDDING
            }
            "buzzy_bees" -> {
                OdysseyEnchantments.BUZZY_BEES
            }
            "committed" -> {
                OdysseyEnchantments.COMMITTED
            }
            "cull_the_weak" -> {
                OdysseyEnchantments.CULL_THE_WEAK
            }
            "decaying_touch" -> {
                OdysseyEnchantments.DECAYING_TOUCH
            }
            "douse" -> {
                OdysseyEnchantments.DOUSE
            }
            "echo" -> {
                OdysseyEnchantments.ECHO
            }
            "exploding" -> {
                OdysseyEnchantments.EXPLODING
            }
            "fearful_finisher" -> {
                OdysseyEnchantments.FEARFUL_FINISHER
            }
            "freezing_aspect" -> {
                OdysseyEnchantments.FREEZING_ASPECT
            }
            "frog_fright" -> {
                OdysseyEnchantments.FROG_FRIGHT
            }
            "frosty_fuse" -> {
                OdysseyEnchantments.FROSTY_FUSE
            }
            "gravity_well" -> {
                OdysseyEnchantments.GRAVITY_WELL
            }
            "guarding_strike" -> {
                OdysseyEnchantments.GUARDING_STRIKE
            }
            "gust" -> {
                OdysseyEnchantments.GUST
            }
            "hemorrhage" -> {
                OdysseyEnchantments.HEMORRHAGE
            }
            "illucidation" -> {
                OdysseyEnchantments.ILLUCIDATION
            }
            "rupturing_strike" -> {
                OdysseyEnchantments.RUPTURING_STRIKE
            }
            "tar_n_dip" -> {
                OdysseyEnchantments.TAR_N_DIP
            }
            "vital" -> {
                OdysseyEnchantments.VITAL
            }
            "void_strike" -> {
                OdysseyEnchantments.VOID_STRIKE
            }
            "whirlwind" -> {
                OdysseyEnchantments.WHIRLWIND
            }
            "bomb_ob" -> {
                OdysseyEnchantments.BOMB_OB
            }
            "hook_shot" -> {
                OdysseyEnchantments.HOOK_SHOT
            }
            "lengthy_line" -> {
                OdysseyEnchantments.LENGTHY_LINE
            }
            "mirror_force" -> {
                OdysseyEnchantments.MIRROR_FORCE
            }
            "reversed_recoil" -> {
                OdysseyEnchantments.REVERSED_RECOIL
            }
            "o_shiny" -> {
                OdysseyEnchantments.O_SHINY
            }
            "void_jump" -> {
                OdysseyEnchantments.VOID_JUMP
            }
            "wise_bait" -> {
                OdysseyEnchantments.WISE_BAIT
            }
            "yank" -> {
                OdysseyEnchantments.YANK
            }
            "alchemy_artillery" -> {
                OdysseyEnchantments.ALCHEMY_ARTILLERY
            }
            "bola_shot" -> {
                OdysseyEnchantments.BOLA_SHOT
            }
            "ballistics" -> {
                OdysseyEnchantments.BALLISTICS
            }
            "burst_barrage" -> {
                OdysseyEnchantments.BURST_BARRAGE
            }
            "chain_reaction" -> {
                OdysseyEnchantments.CHAIN_REACTION
            }
            "cluster_shot" -> {
                OdysseyEnchantments.CLUSTER_SHOT
            }
            "deadeye" -> {
                OdysseyEnchantments.DEADEYE
            }
            "death_from_above" -> {
                OdysseyEnchantments.DEATH_FROM_ABOVE
            }
            "double_tap" -> {
                OdysseyEnchantments.DOUBLE_TAP
            }
            "entanglement" -> {
                OdysseyEnchantments.ENTANGLEMENT
            }
            "fan_fire" -> {
                OdysseyEnchantments.FAN_FIRE
            }
            "gale_wind" -> {
                OdysseyEnchantments.GALE_WIND
            }
            "lucky_draw" -> {
                OdysseyEnchantments.LUCKY_DRAW
            }
            "luxpose" -> {
                OdysseyEnchantments.LUXPOSE
            }
            "overcharge" -> {
                OdysseyEnchantments.OVERCHARGE
            }
            "perpetual" -> {
                OdysseyEnchantments.PERPETUAL
            }
            "rain_of_arrows" -> {
                OdysseyEnchantments.RAIN_OF_ARROWS
            }
            "ricochet" -> {
                OdysseyEnchantments.RICOCHET
            }
            "sharpshooter" -> {
                OdysseyEnchantments.SHARPSHOOTER
            }
            "single_out" -> {
                OdysseyEnchantments.SINGLE_OUT
            }
            "singularity_shot" -> {
                OdysseyEnchantments.SINGULARITY_SHOT
            }
            "soul_rend" -> {
                OdysseyEnchantments.SOUL_REND
            }
            "temporal" -> {
                OdysseyEnchantments.TEMPORAL
            }
            "vulnerocity" -> {
                OdysseyEnchantments.VULNEROCITY
            }
            else -> {
                null
            }
        }
    }

}
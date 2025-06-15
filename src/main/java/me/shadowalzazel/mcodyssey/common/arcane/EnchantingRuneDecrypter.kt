package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.ModifierRune
import me.shadowalzazel.mcodyssey.common.enchantments.EnchantmentManager
import org.bukkit.enchantments.Enchantment

object EnchantingRuneDecrypter : EnchantmentManager, RuneDataManager {


    /*
    val enchantmentToRunes = mapOf(
        // All Purpose
        "mending"              to listOf("point", "heal"),
        "unbreaking"           to listOf("self", "convergence"),
        "curse_of_vanishing"   to listOf("next", "break"),

        // Armor
        "aqua_affinity"        to listOf("beam", "range"),
        "blast_protection"     to listOf("zone", "invert"),
        "curse_of_binding"     to listOf("self", "convergence"),
        "depth_strider"        to listOf("zone", "amplify"),
        "feather_falling"      to listOf("zone", "convergence"),
        "fire_protection"      to listOf("zone", "invert"),
        "frost_walker"         to listOf("ball", "origin"),
        "projectile_protection" to listOf("beam", "invert"),
        "protection"           to listOf("zone", "convergence"),
        "respiration"          to listOf("point", "range"),
        "soul_speed"           to listOf("beam", "amplify"),
        "thorns"               to listOf("beam", "trace"),
        "swift_sneak"          to listOf("beam", "wide"),

        // Melee Weapons
        "bane_of_arthropods"   to listOf("beam", "amplify"),
        "breach"               to listOf("point", "invert"),
        "density"              to listOf("ball", "convergence"),
        "efficiency"           to listOf("beam", "amplify"),
        "fire_aspect"          to listOf("beam", "ignite"),      // new rune: ignite
        "looting"              to listOf("zone", "amplify"),
        "impaling"             to listOf("beam", "trace"),
        "knockback"            to listOf("point", "thrust"),     // new rune: thrust
        "sharpness"            to listOf("beam", "amplify"),
        "smite"                to listOf("beam", "amplify"),
        "sweeping_edge"        to listOf("ball", "amplify"),
        "wind_burst"           to listOf("point", "amplify", "self"),

        // Ranged Weapons
        "channeling"           to listOf("beam", "storm", "trace"),  // new rune: storm
        "flame"                to listOf("beam", "ignite"),
        "infinity"             to listOf("self", "infinite"),       // new rune: infinite
        "loyalty"              to listOf("point", "trace", "self"),
        "riptide"              to listOf("ball", "self"),
        "multishot"            to listOf("ball", "amplify", "trace"),
        "piercing"             to listOf("beam", "pierce"),         // new rune: pierce
        "power"                to listOf("beam", "amplify"),
        "punch"                to listOf("point", "thrust"),
        "quick_charge"         to listOf("self", "delay"),

        // Tools
        "efficiency"           to listOf("beam", "amplify"),
        "fortune"              to listOf("zone", "amplify"),
        "luck_of_the_sea"      to listOf("zone", "amplify", "next"),
        "lure"                 to listOf("self", "delay"),
        "silk_touch"           to listOf("point", "pick_up")
    )

     */

    fun getRunesFromEnchantment(enchant: Enchantment): List<ArcaneRune> = when(enchant.getNameId()) {
        "unbreaking" -> listOf(ModifierRune.Amplify(4.0))
        else -> listOf(ModifierRune.Amplify(4.0))
    }


    fun getRuneNamesFromEnchantment(enchant: Enchantment): List<String> = when(enchant.getNameId()) {
        "unbreaking" -> listOf("point", "amplify")
        "sharpness" -> listOf("point", "amplify")
        "power" -> listOf("ball", "trace")
        "efficiency" -> listOf("next", "Convergence")
        "riptide" -> listOf("next", "next")
        "mending" -> listOf("heal", "point", "self")
        "thorns" -> listOf("next", "nearby")
        "sweeping_edge" -> listOf("differ", "nearby")
        "loyalty" -> listOf("invert", "self")
        "protection" -> listOf("amplify", "self", "heal")
        else -> listOf("amplify")
    }


    fun decryptEnchantSimple(enchant: Enchantment): ArcaneRune {
        val runeName = getRuneNamesFromEnchantment(enchant).random()
        return ArcaneRune.fromName(runeName) ?: ModifierRune.Amplify(4.0)
    }


}
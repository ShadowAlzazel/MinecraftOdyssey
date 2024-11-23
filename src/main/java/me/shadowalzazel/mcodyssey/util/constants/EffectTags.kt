package me.shadowalzazel.mcodyssey.util.constants

object EffectTags {

    // For Custom Status Effects and Potion Effects
    const val NO_EFFECT: String = "odyssey.no_effect"

    // TYPES
    const val FREEZING: String = "odyssey.freezing" // Freeze Ticks + Slow
    const val BUDDING: String = "odyssey.budding" // Independent Stackable Damage over time
    const val ABLAZE: String = "odyssey.ablaze" // More damage Fire ticks
    const val MIASMA: String = "odyssey.miasma" // Reduce maximum health
    const val HONEYED: String = "odyssey.honeyed" // Slowed and reduced jump velocity
    const val HEMORRHAGING: String = "odyssey.hemorrhaging" // Damage over time that increases damage based on how much the entity has been hit
    const val IRRADIATED: String = "odyssey.irradiated" // Reduces Healing effectiveness by 15/30/45%
    const val CORRODING: String = "odyssey.corroding" // Armor Effectiveness is reduced by 15/30/45%
    const val ACCURSED: String = "odyssey.accursed" // The next source of damage you take will be increased by 30%
    const val SOUL_DAMAGE: String = "odyssey.soul_damage" // No effect
    const val SHIMMER: String = "odyssey.shimmer" // No effect
    const val VULNERABLE: String = "odyssey.vulnerable" // Reduce immunity time by 0.5 seconds
    const val BARRIER: String = "odyssey.barrier" // Reduce damage

    // Crowd Control
    const val ROOTED: String = "odyssey.rooted" // Can not jump (use entityJumpEvent?)
    const val TETHERED: String = "odyssey.tethered" // Can not teleport away
    const val SHATTERED: String = "odyssey.shattered" // Have less armor (temp -2.0 armor value on player stats) flat
    const val HARDEN: String = "odyssey.harden" // Have more armor (temp 2.0) flat
    const val GROUNDED: String = "odyssey.grounded" // Can not fly away SHEAR ENCHANT
    const val CHARMED: String = "odyssey.charmed" // Attracted to a location
    const val FEARED: String = "odyssey.feared" // Set entity goal away
    const val POLYMORPHED: String = "odyssey.polymorphed" // Change to display entity thingy
    const val INSPIRED: String = "odyssey.inspired" // Support Mechanic ???

    // Special Effects
    const val ARCANE_JAILED: String = "odyssey.arcane_jailed"
    const val FROSTY_FUSED: String = "odyssey.frosty_fused"
    const val GRAVITY_WELLED: String = "odyssey.gravity_welled"

    const val PARTLY_RUPTURED: String = "odyssey.partly_ruptured"
    const val FULLY_RUPTURED: String = "odyssey.fully_ruptured"


    // TYPE MODIFIERS
    const val HEMORRHAGE_MODIFIER: String = "odyssey.hemorrhage_modifier.0"
    const val TARRED_MODIFIER: String = "odyssey.douse_modifier.0"





}
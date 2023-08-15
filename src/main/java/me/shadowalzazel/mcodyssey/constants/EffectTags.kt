package me.shadowalzazel.mcodyssey.constants

object EffectTags {

    const val NO_EFFECT: String = "odyssey.no_effect"

    const val DOUSED: String = "odyssey.doused" //

    // TYPES
    const val FREEZING: String = "odyssey.freezing" // Freeze Ticks + Slow
    const val ROTTING: String = "odyssey.rotting" // give Hunger, do damage, stacking, spores,
    const val TARRED: String = "odyssey.tarred" //
    const val ABLAZE: String = "odyssey.ablaze" // More damage Fire ticks
    const val MIASMA: String = "odyssey.miasma" // TODO: Prevent Hunger Regen
    const val HONEYED: String = "odyssey.honeyed" // Slow, and bees attacks you
    const val HEMORRHAGING: String = "odyssey.hemorrhaging" // Stacking damage
    const val IRRADIATED: String = "odyssey.irradiated" // TODO: Prevent Healing (use entityHealthEvent?)   // Irradiates enemies (Poison + Can not heal)
    const val CORRODING: String = "odyssey.corroding" // Armor Takes damage per 5 ticks
    const val ACCURSED: String = "odyssey.accursed" // The next source of damage you take will be increased by 33.3%
    const val SOUL_DAMAGE: String = "odyssey.soul_damage" // ? No effect
    const val SHIMMER: String = "odyssey.shimmer" // TODO: ?

    // STATUS
    const val ROOTED: String = "odyssey.rooted" // TODO: Can not jump (use entityJumpEvent?)
    const val ASPHYXIATE: String = "odyssey.asphyxiate" // TODO: Give negative breath -> IF not negative ticks then make timed effect that sets max to 0
    const val TETHERED: String = "odyssey.tethered" // TODO: Can not teleport away
    const val SHATTERED: String = "odyssey.shattered" // TODO: Have less armor (temp -2.0 armor value on player stats) flat
    const val HARDEN: String = "odyssey.harden" // Have more armor (temp 2.0) flat
    const val GROUNDED: String = "odyssey.grounded" // TODO: Can not fly away SHEAR ENCHANT

    const val CHARMED: String = "odyssey.charmed" // Attracted to a location
    const val FEARED: String = "odyssey.feared" // Set entity goal away
    const val POLYMORPHED: String = "odyssey.polymorphed" // Change to display entity thingy
    const val INSPIRED: String = "odyssey.inspired" // Support Mechanic ???
    const val INFECTED: String = "odyssey.infected" // TODO: Upon death, turn into a zombie

    // ENCHANT
    const val ARCANE_JAILED: String = "odyssey.arcane_jailed"
    const val FROSTY_FUSED: String = "odyssey.frosty_fused"
    const val GRAVITY_WELLED: String = "odyssey.gravity_welled"

    const val PARTLY_RUPTURED: String = "odyssey.partly_ruptured"
    const val FULLY_RUPTURED: String = "odyssey.fully_ruptured"


    // TYPE MODIFIERS
    const val HEMORRHAGE_MODIFIER: String = "odyssey.hemorrhage_modifier.0"
    const val TARRED_MODIFIER: String = "odyssey.douse_modifier.0"





}
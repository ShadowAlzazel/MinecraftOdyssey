package me.shadowalzazel.mcodyssey.util.constants


object WeaponMaps {

    // WEAPON ARCHETYPE DAMAGERS
    val PIERCING_WEAPONS = setOf(
        "rapier",
        "dagger",
        "kunai",
        "spear",
        "halberd",
        "lance",
        "poleaxe",
    )

    val SLASHING_WEAPONS = setOf(
        "katana",
        "longsword",
        "claymore",
        "cutlass",
        "saber",
        "sickle",
        "chakram",
        "zweihander",
        "kriegsmesser",
        "glaive",
        "longaxe",
        "double_axe",
        "battle_saw",
        "arm_blade",
        "scythe",
        "shuriken"
    )

    val BLUNT_WEAPONS = setOf(
        "warhammer"
    )

    // ACTIVE SKILLS (these are mainly exclusive)
    val CAN_PARRY = setOf(
        "katana",
        "claymore",
        "longsword",
        "rapier",
        "cutlass",
        "saber",
        "sickle",
        "dagger",
        "zweihander",
        "poleaxe",
        "glaive",
    )

    val DUAL_WIELDABLE = setOf(
        "dagger",
        "cutlass",
        "chakram",
        "cutlass"
    )

    val THROWABLE = setOf(
        "kunai",
        "chakram",
        "shuriken"
    )

    val CAN_KINETIC_CHARGE = setOf(
        "pike",
        "lance",
    )

    // PASSIVES
    val DISABLE_SHIELDS = mapOf( // In ticks
        "warhammer" to 60,
        "sickle" to 30
    )

    val CAN_DISMOUNT = setOf(
        "spear",
        "halberd"
    )

    val MINIMUM_CHARGE = mapOf(
        "halberd" to 0.5,
        "longaxe" to 0.5,
        "claymore" to 0.3,
        "kriegsmesser" to 0.3,
        "zweihander" to 0.3,
    )

    val HITBOX_ASSIST = mapOf(
        "katana" to 0.35F,
        "claymore" to 0.4F,
        "longsword" to 0.35F,
        "double_axe" to 0.35F,
        "sickle" to 0.45F,
        "scythe" to 0.7F,
        "battlesaw" to 0.45F,
        "kriegsmesser" to 0.4F,
        "zweihander" to 0.45F,
    )

    val REQUIRES_TWO_HANDS = setOf(
        "zweihander",
        "claymore",
        "halberd"
    )

    val BONUS_CRIT_DAMAGE = mapOf(
        "katana" to 0.1F,
        "longaxe" to 0.25F
    )

    // ANIMATIONS (Defaults to SWING)
    val STAB_ANIMATION = mapOf(
        "rapier" to 8,
        "dagger" to 8,
        "kunai" to 8,
        "spear" to 12,
        "halberd" to 18,
        "lance" to 18,
        "pike" to 18,
        "poleaxe" to 14,
    )

    val WHACK_ANIMATION = mapOf(
        "katana" to 11,
        "longsword" to 13,
        "claymore" to 15,
        "cutlass" to 10,
        "saber" to 10,
        "sickle" to 8,
        "chakram" to 8,
        "zweihander" to 16,
        "kriegsmesser" to 14,
        "glaive" to 12,
        "longaxe" to 14,
    )

    // FOR MAGIC WEAPONS
    val ARCANE_RANGES = mapOf(
        "arcane_wand" to 32.0, // Range
        "arcane_blade" to 8.0,
        "arcane_scepter" to 48.0,
        "warping_wand" to 8.0, // Cone
    )

    // MOVED TO NEW ATTRIBUTE
    val REACH_MAP = mapOf(
        "claymore" to 4.2,
        "longaxe" to 3.7,
        "longsword" to 3.4,
        "cutlass" to 2.8,
        "dagger" to 2.4,
        "sickle" to 2.5,
        "chakram" to 2.3,
        "spear" to 6.0,
        "halberd" to 7.9,
        "lance" to 8.2,
        "poleaxe" to 6.2,
    )

    // Minimum Distance -> Inside attack is reduced
    val MIN_RANGE_MAP = mapOf(
        "spear" to 1.0,
        "halberd" to 1.5,
        "lance" to 1.5,
    )
    // MAYBE ADD PERFECT RANGE MECHANIC?! -> small 10% damage bonus if in sweet spot / perfect range


    // BONUS PASSIVE DAMAGE TYPES
    // Lacerating - more damage to unarmored targets
    // Piercing - damage that ignores armor
    // Bludgeoning - extra damage to armored targets
    // Cleaving - damages armor durability
    // Sweeping - increased sweep damage

    val LACERATE_DAMAGE_MAP = mapOf(
        "katana" to 4.0,
        "claymore" to 3.0,
        "saber" to 2.0,
        "scythe" to 4.0,
        "sickle" to 3.0
    )

    val PIERCE_DAMAGE_MAP = mapOf(
        "dagger" to 1.0,
        "spear" to 1.0,
        "halberd" to 4.0,
        "lance" to 3.0,
        "warhammer" to 1.0,
        "poleaxe" to 2.0,
    )

    val BLUNT_DAMAGE_MAP = mapOf(
        "warhammer" to 4.0,
        "poleaxe" to 1.0,
    )

    // Non-damage
    val CLEAVE_MAP = mapOf(
        "chakram" to 1.0,
        "longaxe" to 3.0,
        "halberd" to 1.0,
        "poleaxe" to 2.0,
    )
    val SWEEP_MAP = mapOf(
        "katana" to 2.0,
        "claymore" to 3.0,
        "kriegsmesser" to 3.0,
        "zweihander" to 4.0,
        "saber" to 2.0,
        "longsword" to 2.0,
        "scythe" to 5.0,
        "chakram" to 3.0,
        "sickle" to 2.0,
        "dagger" to 1.0,
        "cutlass" to 1.0
    )

}
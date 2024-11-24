package me.shadowalzazel.mcodyssey.util.constants


object WeaponMaps {

    // MOVED TO 1.20.5 attribute
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

    val ARCANE_RANGES = mapOf(
        "arcane_wand" to 32.0, // Range
        "arcane_blade" to 8.0,
        "arcane_scepter" to 48.0,
        "warping_wand" to 8.0, // Cone
    )


    // MAYBE ADD PERFECT RANGE?! -> small 10% damage bonus if in sweet spot / perfect range

    // Max Distance -> Outside attack is cancelled
    val MAX_RANGE_MAP = mapOf(
        "dagger" to 2.4,
        "sickle" to 2.5,
        "chakram" to 2.3,
        "rapier" to 2.9,
        "cutlass" to 2.8,
    )

    // Minimum Distance -> Inside attack is reduced
    val MIN_RANGE_MAP = mapOf(
        "spear" to 1.0,
        "halberd" to 1.5,
        "lance" to 1.5,
    )

    // Lacerating - more damage to unarmored targets
    // Piercing - damage that ignores armor
    // Bludgeoning - extra damage to armored targets
    // Cleaving - damages armor durability
    // Sweeping - increased sweep damage

    val LACERATE_MAP = mapOf(
        "katana" to 4.0,
        "claymore" to 3.0,
        "saber" to 2.0,
        "scythe" to 4.0,
        "sickle" to 3.0
    )

    val PIERCE_MAP = mapOf(
        "dagger" to 1.0,
        "spear" to 1.0,
        "halberd" to 4.0,
        "lance" to 3.0,
        "warhammer" to 1.0,
        "poleaxe" to 2.0,
    )

    val BLUDGEON_MAP = mapOf(
        "warhammer" to 4.0,
        "poleaxe" to 1.0,
    )

    val CLEAVE_MAP = mapOf(
        "chakram" to 1.0,
        "longaxe" to 3.0,
        "halberd" to 1.0,
        "poleaxe" to 2.0,
    )
    val SWEEP_MAP = mapOf(
        "katana" to 2.0,
        "claymore" to 4.0,
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
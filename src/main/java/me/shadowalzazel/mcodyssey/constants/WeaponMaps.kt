package me.shadowalzazel.mcodyssey.constants


object WeaponMaps {

    // MOVED TO 1.20.5 attribute
    val REACH_MAP = mapOf(
        "claymore" to 5.2,
        "longsword" to 4.2,
        "cutlass" to 3.7,
        "dagger" to 2.4,
        "sickle" to 2.6,
        "chakram" to 2.2,
        "spear" to 7.3,
        "halberd" to 7.9,
        "lance" to 8.2
    )

    // MAYBE ADD PERFECT RANGE?! -> small 10% damage bonus if in sweet spot / perfect range

    // Max Distance -> Outside attack is cancelled
    val MAX_RANGE_MAP = mapOf(
        "dagger" to 2.4,
        "sickle" to 2.6,
        "chakram" to 2.2,
        "rapier" to 3.9,
        "cutlass" to 3.7,
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
        "sickle" to 2.0
    )

    val PIERCE_MAP = mapOf(
        "dagger" to 1.0,
        "halberd" to 4.0,
        "lance" to 3.0,
        "warhammer" to 1.0,
    )

    val BLUDGEON_MAP = mapOf(
        "warhammer" to 4.0
    )

    val CLEAVE_MAP = mapOf(
        "chakram" to 1.0,
        "longaxe" to 3.0,
        "halberd" to 1.0,
    )

    val SWEEP_MAP = mapOf(
        "katana" to 2.0,
        "claymore" to 4.0,
        "saber" to 2.0,
        "longsword" to 2.0,
        "scythe" to 5.0,
        "chakram" to 3.0,
        "sickle" to 2.0,
        "dagger" to 1.0,
        "cutlass" to 1.0
    )

}
package me.shadowalzazel.mcodyssey.constants


object WeaponMaps {

    // Reach
    val REACH_MAP = mapOf(
        ItemModels.CLAYMORE to 5.2,
        ItemModels.CUTLASS to 3.7,
        ItemModels.RAPIER to 3.9,
        ItemModels.SOUL_STEEL_CLAYMORE to 5.2,
        ItemModels.SOUL_STEEL_CUTLASS to 3.7,
        ItemModels.SOUL_STEEL_RAPIER to 3.9,

        ItemModels.DAGGER to 3.05,
        ItemModels.SICKLE to 3.35,
        ItemModels.CHAKRAM to 2.85,
        ItemModels.SOUL_STEEL_DAGGER to 3.05,
        ItemModels.SOUL_STEEL_SICKLE to 3.35,
        ItemModels.SOUL_STEEL_CHAKRAM to 2.85,

        ItemModels.SPEAR to 7.3,
        ItemModels.HALBERD to 7.9,
        ItemModels.LANCE to 8.4,
        ItemModels.SOUL_STEEL_SPEAR to 7.3,
        ItemModels.SOUL_STEEL_HALBERD to 7.9,
        ItemModels.SOUL_STEEL_LANCE to 8.4,
    )

    // Max Distance -> Outside attack is cancelled
    val MAX_RANGE_MAP = mapOf(
        ItemModels.DAGGER to 2.1,
        ItemModels.SICKLE to 2.0,
        ItemModels.CHAKRAM to 1.8,
        ItemModels.SOUL_STEEL_DAGGER to 2.15,
        ItemModels.SOUL_STEEL_SICKLE to 2.0,
        ItemModels.SOUL_STEEL_CHAKRAM to 1.8,
    )

    // Minimum Distance -> Inside attack is cancelled
    val MIN_RANGE_MAP = mapOf(
        ItemModels.SPEAR to 0.75,
        ItemModels.HALBERD to 1.0,
        ItemModels.LANCE to 1.0,
        ItemModels.SOUL_STEEL_SPEAR to 0.75,
        ItemModels.SOUL_STEEL_HALBERD to 1.0,
        ItemModels.SOUL_STEEL_LANCE to 1.0,
    )

    // Unarmored Damage
    val LACERATE_MAP = mapOf(
        ItemModels.KATANA to 5.0,
        ItemModels.CLAYMORE to 4.0,
        ItemModels.SABER to 3.0,
        ItemModels.SCYTHE to 3.0,
        ItemModels.SOUL_STEEL_KATANA to 5.0,
        ItemModels.SOUL_STEEL_CLAYMORE to 4.0,
        ItemModels.SOUL_STEEL_SABER to 3.0,
        ItemModels.SOUL_STEEL_SCYTHE to 3.0,

        ItemModels.SICKLE to 2.0,
        ItemModels.SOUL_STEEL_SICKLE to 2.0,
    )

    // Damage Ignoring Armor
    val PIERCE_MAP = mapOf(
        ItemModels.DAGGER to 1.0,
        ItemModels.SOUL_STEEL_DAGGER to 1.0,

        ItemModels.SPEAR to 2.0,
        ItemModels.HALBERD to 4.0,
        ItemModels.LANCE to 3.0,
        ItemModels.SOUL_STEEL_SPEAR to 2.0,
        ItemModels.SOUL_STEEL_HALBERD to 4.0,
        ItemModels.SOUL_STEEL_LANCE to 3.0,

        ItemModels.WARHAMMER to 2.0,
        ItemModels.SOUL_STEEL_WARHAMMER to 2.0
    )

    // Extra damage to armored enemies
    val BLUDGEON_MAP = mapOf(
        ItemModels.WARHAMMER to 5.0,
        ItemModels.SOUL_STEEL_WARHAMMER to 5.0
    )

    // Extra damage to durability
    val CLEAVE_MAP = mapOf(
        ItemModels.CLAYMORE to 3.0,
        ItemModels.SOUL_STEEL_CLAYMORE to 3.0,

        ItemModels.CHAKRAM to 1.0,
        ItemModels.SOUL_STEEL_CHAKRAM to 1.0,

        ItemModels.LONG_AXE to 5.0,
        ItemModels.SOUL_STEEL_LONG_AXE to 5.0,

        ItemModels.HALBERD to 2.0,
        ItemModels.SOUL_STEEL_HALBERD to 2.0,
    )

    // AOE Sweep in front if player
    val SWEEP_MAP = mapOf(
        ItemModels.KATANA to 1.0,
        ItemModels.CLAYMORE to 1.5,
        ItemModels.SABER to 1.0,
        ItemModels.CUTLASS to 0.75,
        ItemModels.SCYTHE to 2.75,
        ItemModels.SOUL_STEEL_KATANA to 1.0,
        ItemModels.SOUL_STEEL_CLAYMORE to 1.5,
        ItemModels.SOUL_STEEL_SABER to 1.0,
        ItemModels.SOUL_STEEL_CUTLASS to 0.75,
        ItemModels.SOUL_STEEL_SCYTHE to 2.75,

        ItemModels.CHAKRAM to 0.75,
        ItemModels.SICKLE to 0.5,
        ItemModels.SOUL_STEEL_CHAKRAM to 0.75,
        ItemModels.SOUL_STEEL_SICKLE to 0.5,

        ItemModels.HALBERD to 0.5,
        ItemModels.SOUL_STEEL_HALBERD to 0.5,

        ItemModels.WOODEN_STAFF to 1.85,
        ItemModels.BONE_STAFF to 1.85,
        ItemModels.BAMBOO_STAFF to 1.85,
        ItemModels.BLAZE_ROD_STAFF to 1.85
    )


    // Reach - more range
    // Lacerating - more damage to unarmored targets
    // Piercing - ignores some armor
    // Bludgeoning - more damage to armored targets
    // Cleaving - damages armor more
    // Sweeping - more sweep/aoe

    // Parrying !? Riding !?
    // Slow?
    // BLUNT - POINTED - EDGED

}
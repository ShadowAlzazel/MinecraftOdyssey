package me.shadowalzazel.mcodyssey.constants


object WeaponStats {

    val reachMap = mapOf(
        ItemModels.DIAMOND_SPEAR to 7.3,
        ItemModels.DIAMOND_HALBERD to 7.9,
        ItemModels.DIAMOND_CLAYMORE to 5.2,
        ItemModels.DIAMOND_CUTLASS to 3.7,
        ItemModels.DIAMOND_RAPIER to 3.9,
        ItemModels.DIAMOND_DAGGER to 3.05)

    val lacerateMap = mapOf(
        ItemModels.IRON_KATANA to 2.5,
        ItemModels.SOUL_STEEL_KATANA to 2.5,
        ItemModels.DIAMOND_CLAYMORE to 2.0,
        ItemModels.DIAMOND_SABER to 1.5,
    )

    val pierceMap = mapOf(
        ItemModels.DIAMOND_RAPIER to 2.0,
        ItemModels.DIAMOND_HALBERD to 3.0,
        ItemModels.DIAMOND_SPEAR to 1.0,
    )

    val bludgeonMap = mapOf(
        ItemModels.DIAMOND_WARHAMMER to 4.5
    )

    val cleaveMap = mapOf(
        ItemModels.DIAMOND_CLAYMORE to 1.0
    )

    val sweepMap = mapOf(
        ItemModels.IRON_KATANA to 0.75,
        ItemModels.SOUL_STEEL_KATANA to 0.75,
        ItemModels.DIAMOND_CLAYMORE to 1.25,
        ItemModels.DIAMOND_HALBERD to 0.5,
        ItemModels.DIAMOND_SABER to 0.5,
        ItemModels.DIAMOND_CUTLASS to 0.5,
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


    // BLUNT - POINTED - EDGED


}
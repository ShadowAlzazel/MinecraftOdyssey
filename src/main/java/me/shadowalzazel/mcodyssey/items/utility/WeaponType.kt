package me.shadowalzazel.mcodyssey.items.utility

import me.shadowalzazel.mcodyssey.constants.ItemModels

enum class WeaponType(val baseDamage: Double, val baseAttackSpeed: Double, val baseName: String, val model: Int) {

    // BASE
    AXE(3.0, 1.6, "Axe", 69),
    POLE_ARM(3.0, 1.6, "Pole-Arm", 69),
    SWORD(3.0, 1.6, "Sword", 69),
    CLUB(3.0, 1.6, "Sword", 69),
    SPEAR(3.0, 1.6, "Sword", 69),
    STAFF(2.5, 1.3, "Staff", 0),
    KNIFE(3.0, 1.6, "Sword", 69),
    OTHER(3.0, 1.6, "Sword", 69),

    // COMMON TYPES
    KATANA(4.0, 1.5, "Katana", ItemModels.KATANA),
    CLAYMORE(7.75, 0.7, "Claymore", ItemModels.CLAYMORE),
    RAPIER(1.5, 3.4, "Rapier", ItemModels.RAPIER),
    CUTLASS(2.5, 2.1, "Cutlass", ItemModels.CUTLASS),
    SABER(3.0, 1.8, "Saber", ItemModels.SABER),

    HALBERD(4.5, 0.8, "Halberd", ItemModels.WOODEN_HALBERD),

    DAGGER(4.5, 0.8, "Halberd", ItemModels.WOODEN_SPEAR),

    LONG_AXE(4.5, 0.8, "Halberd", ItemModels.WOODEN_SPEAR),

    WARHAMMER(4.5, 1.2, "Warhammer", ItemModels.WARHAMMER),

    // RARE TYPES
    ZWEIHANDER(6.0, 0.9, "Zweihander", ItemModels.ZWEIHANDER)

}

// TYPE VS CATEGORY
// AXE, POLE_ARM, SWORD, CLUB, SPEAR, STAFF, KNIFE, OTHER
package me.shadowalzazel.mcodyssey.items.utility

import me.shadowalzazel.mcodyssey.constants.ItemModels

enum class WeaponType(
    val baseDamage: Double,
    val baseAttackSpeed: Double,
    val baseName: String,
    val model: Int) {

    SWORD(3.0, 1.6, "Sword", 6900),
    KATANA(4.0, 1.5, "Katana", ItemModels.KATANA),
    CLAYMORE(7.5, 0.7, "Claymore", ItemModels.CLAYMORE),
    RAPIER(1.5, 3.4, "Rapier", ItemModels.RAPIER),
    CUTLASS(2.5, 2.1, "Cutlass", ItemModels.CUTLASS),
    SABER(3.0, 1.8, "Saber", ItemModels.SABER),

    KNIFE(3.0, 1.6, "Knife", 6900),
    DAGGER(1.0, 3.0, "Dagger", ItemModels.DAGGER),
    SICKLE(1.5, 2.3, "Sickle", ItemModels.SICKLE),
    CHAKRAM(1.5, 2.6, "Chakram", ItemModels.CHAKRAM),

    SPEAR(3.0, 1.6, "Spear", ItemModels.SPEAR),
    HALBERD(4.5, 0.8, "Halberd", ItemModels.HALBERD),
    LANCE(3.0, 0.6, "Lance", ItemModels.LANCE),

    AXE(3.0, 1.6, "Axe", 6900),
    LONG_AXE(8.5, 0.7, "Long-Axe", ItemModels.LONG_AXE),

    POLE_ARM(3.0, 1.6, "Pole-Arm", 6900),
    WARHAMMER(4.0, 1.3, "Warhammer", ItemModels.WARHAMMER),
    SCYTHE(3.0, 0.8, "Scythe", ItemModels.SCYTHE),

    CLUB(3.0, 1.6, "Club", 6900),

    STAFF(2.5, 1.3, "Staff", 6900),

    // RARE TYPES
    OTHER(3.0, 1.6, "Sword", 6900),
    ZWEIHANDER(6.0, 0.9, "Zweihander", ItemModels.ZWEIHANDER)

}
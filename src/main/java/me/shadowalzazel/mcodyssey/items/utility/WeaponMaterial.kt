package me.shadowalzazel.mcodyssey.items.utility


enum class WeaponMaterial(val damage: Double, val namePrefix: String) {

    WOOD(1.0, "Wooden"),
    GOLD(1.5, "Golden"),
    STONE(2.0, "Stone"),
    IRON(3.0, "Iron"),
    DIAMOND(4.0, "Diamond"),
    NETHERITE(5.0, "Netherite"),
    SOUL_STEEL(4.0, "Soul Steel"),

    BAMBOO(1.5, "Bamboo"),
    BONE(2.0, "Bone"),
    BLAZE_ROD(2.5, "Blaze Rod")

}

/*
sealed class WeaponMaterial(val damage: Double, val namePrefix: String) {

    object WOOD: WeaponMaterial(1.0, "Wooden")
    object GOLD: WeaponMaterial(1.5, "Golden")
    object STONE: WeaponMaterial(2.0, "Stone")
    object IRON: WeaponMaterial(3.0, "Iron")
    object DIAMOND: WeaponMaterial(4.0, "Diamond")
    object NETHERITE: WeaponMaterial(5.0, "Netherite")
    object SOUL_STEEL: WeaponMaterial(4.25, "Soul Steel")
    class OTHER(damage: Double, namePrefix: String): WeaponMaterial(damage, namePrefix)

}

 */

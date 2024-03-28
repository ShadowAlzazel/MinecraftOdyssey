package me.shadowalzazel.mcodyssey.items.utility


enum class WeaponMaterial(val damage: Double, val namePrefix: String) {

    // MINECRAFT
    WOOD(1.0, "Wooden"),
    GOLD(1.5, "Golden"),
    STONE(2.0, "Stone"),
    IRON(3.0, "Iron"),
    DIAMOND(4.0, "Diamond"),
    NETHERITE(5.0, "Netherite"),

    // ODYSSEY
    // [More Durability than stone], [iron-tier mine]
    COPPER(2.0, "Copper"),
    // [less durability than netherite], [exclusive to edge], [+1 stat], template + copper
    MITHRIL(6.0, "Mithril"),
    // [+15% Exp Drop], [slightly more durability than iron], template + iron
    SOUL_STEEL(4.0, "Soul Steel"),
    // [Way more durability], [-10% speed (block, attack)], [boom immune]
    IRIDIUM(5.0, "Iridium"),
    // [+2 Dmg against monster], [=durability to iron]
    SILVER(3.0, "Silver"),
    // [+10% Damage Life Steal, applies withering to user], wither rose + (silver)
    WITHERGLINT(4.0, "Wither Glint"),
    // []
    RUNE_STONE(4.0, "Rune Stone"),
    // TITANIUM = +5% speed [-diamond durability]

    // MISC
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

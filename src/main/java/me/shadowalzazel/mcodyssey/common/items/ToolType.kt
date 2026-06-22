package me.shadowalzazel.mcodyssey.common.items

import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps

/**
 * All the tool and weapon type classes that can be made
 *
 */
enum class ToolType(
    val toolName: String, // 'item_name' component
    val fullName: String, // 'custom_name' component
    val baseDamage: Double,
    val baseSpeed: Double,
    val vanillaBase: String,  // for finding the item id
    val bonusRange: Double? = null // Default is range 3.0, bonus can be negative to lower range
) {

    // ---------------
    // ACTIVE SKILLS
    // Block/Parry -> mostly for duelist weapons like swords
    // Charge -> for large spears and
    // Throwable -> Like chakram
    // Dual Wield -> Can be offhanded
    // No Skill -> Shield
    // Mineable -> Can be used for mining

    // PASSIVE SKILLS
    // Disable Shields [axes, scythe, sickle]
    // Dismount [spears]
    // AoE (Sweep, Pierce, Smash)
    // Bypass Armor [piercing polearms, rapier, dagger]
    // Bonus Unarmored Damage [Katana, Chakram, Scythe]

    // 3 DAMAGE ARCHETYPES
    // Pierce -> Piercing Weapon
    // Slash -> Sweeping Weapon
    // Blunt -> Single Target Weapon

    // COMBAT STATS
    // Damage
    // Attack Speed
    // Range

    // Vanilla
    SWORD("sword", "Sword", 3.0, 1.6, "sword"),
    PICKAXE("pickaxe", "Pickaxe", 1.0, 1.2,  "pickaxe"),
    AXE("axe", "Axe", 5.0, 1.0, "axe"),
    SHOVEL("shovel", "Shovel", 1.5, 1.0, "shovel"),
    HOE("hoe", "Hoe", 0.0, 1.6, "hoe"),
    SPEAR("spear", "Spear", 0.0, 1.0, "spear"),

    // Sword type weapons
    CLAYMORE("claymore", "Claymore", 6.0, 1.2, "sword", 0.5),
    CUTLASS("cutlass", "Cutlass", 2.5, 2.1, "sword"),
    KATANA("katana", "Katana", 3.0, 1.7, "sword", 0.2),
    LONGSWORD("longsword", "Longsword", 4.0, 1.5, "sword", 0.3),
    RAPIER("rapier", "Rapier", 1.5, 3.5, "sword"),
    SABER("saber", "Saber", 3.0, 1.8, "sword"),

    // Small Slashing Weapons
    CHAKRAM("chakram", "Chakram", 1.5, 2.5, "sword", -0.3),
    DAGGER("dagger", "Dagger", 1.0, 3.0, "sword", -0.3),
    KUNAI("kunai", "Kunai", 1.0, 2.5, "sword"),
    SICKLE("sickle", "Sickle", 1.5, 2.7, "sword", -0.2),
    //ARM_BLADE("arm_blade", "Arm Blade", 2.0, 3.0, "sword", -0.3),

    // Heavy Slashing Weapons
    KRIEGSMESSER("kriegsmesser", "Kriegsmesser", 6.0, 1.0, "sword", 0.6),
    ZWEIHANDER("zweihander", "Zweihander", 7.0, 0.8, "sword", 1.0),

    // Polearms Weapons
    HALBERD("halberd", "Halberd", 4.0, 0.8, "spear",2.0),
    PIKE("pike", "Pike", 6.0, 0.6, "spear", 3.0),
    POLEAXE("poleaxe", "Poleaxe", 4.0, 1.1, "axe", 1.0),
    GLAIVE("glaive", "Glaive", 4.0, 1.3, "axe", 1.0),
    //JAVELIN("pike", "Pike", 3.0, 1.2, "spear", 2.0),
    //LANCE("lance", "Lance", 3.0, 0.8, "spear", 3.0),

    // Axe type Weapons
    DOUBLE_AXE("double_axe", "Double Axe", 5.0, 1.0, "axe", 0.0),
    LONGAXE("longaxe", "Longaxe", 6.0, 0.9, "axe", 0.5),

    // Blunt Type Weapons
    WARHAMMER("warhammer", "Warhammer", 4.0, 1.4, "pickaxe"),

    // Misc
    SCYTHE("scythe", "Scythe", 3.0, 1.1, "hoe", 1.0),
    BATTLESAW("battlesaw", "Battlesaw", 4.0, 1.0, "axe", 1.0),
    //SCISSORS("scissors", "scissors", 5.0, 1.0, "axe", 1.0), // Nom-Nom Shark


    // Others
    SHURIKEN("shuriken", "Shuriken", 0.5, 1.0, "iron_nugget");



    // Use companion object to get lists and maps
    companion object {

        // Types Methods
        fun getVanillaTypes(): List<ToolType> {
            return listOf(
                SWORD,
                PICKAXE,
                SHOVEL,
                HOE,
                AXE,
                SPEAR,
            )
        }

        fun getOdysseyTypes(): List<ToolType> {
            return listOf(
                CLAYMORE,
                CUTLASS,
                KATANA,
                LONGSWORD,
                RAPIER,
                SABER,

                CHAKRAM,
                DAGGER,
                KUNAI,
                SICKLE,

                KRIEGSMESSER,
                ZWEIHANDER,

                HALBERD,
                PIKE,

                DOUBLE_AXE,
                GLAIVE,
                LONGAXE,
                POLEAXE,

                WARHAMMER,
                SCYTHE,
                BATTLESAW,
            )
        }

        fun getFromName(name: String): ToolType? = entries.find { name == it.toolName }
    }


    // Skills, Damage Types, Passives, Maps

    fun canParry(): Boolean {
        return WeaponMaps.CAN_PARRY.contains(this.toolName)
    }

    fun isThrowable(): Boolean {
        return WeaponMaps.THROWABLE.contains(this.toolName)
    }

    fun isDualWieldable(): Boolean {
        return WeaponMaps.DUAL_WIELDABLE.contains(this.toolName)
    }

    fun hasSwingAnimation(): Boolean {
        return WeaponMaps.STAB_ANIMATION.contains(this.toolName) || WeaponMaps.WHACK_ANIMATION.contains(this.toolName)
    }

    fun canKineticCharge(): Boolean {
        return WeaponMaps.CAN_KINETIC_CHARGE.contains(this.toolName)
    }

}


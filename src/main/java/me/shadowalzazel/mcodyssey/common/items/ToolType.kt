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

    // Sword overrides
    KATANA("katana", "Katana", 3.0, 1.7, "sword", 0.2),
    CLAYMORE("claymore", "Claymore", 6.0, 0.9, "sword", 0.5),
    LONGSWORD("longsword", "Longsword", 4.0, 1.5, "sword", 0.3),
    RAPIER("rapier", "Rapier", 1.5, 3.5, "sword"),
    CUTLASS("cutlass", "Cutlass", 2.5, 2.1, "sword"),
    SABER("saber", "Saber", 3.0, 1.8, "sword"),

    // Small Slashing Weapons
    SICKLE("sickle", "Sickle", 1.5, 2.7, "sword", -0.2),
    CHAKRAM("chakram", "Chakram", 1.5, 2.5, "sword", -0.3),
    DAGGER("dagger", "Dagger", 1.0, 3.0, "sword", -0.3),
    KUNAI("kunai", "Kunai", 1.0, 2.5, "sword"),
    //ARM_BLADE("arm_blade", "Arm Blade", 2.0, 3.0, "sword", -0.3),

    // Heavy Slashing Weapons
    ZWEIHANDER("zweihander", "Zweihander", 7.0, 0.7, "sword", 0.8),
    KRIEGSMESSER("kriegsmesser", "Kriegsmesser", 6.0, 0.8, "sword", 0.6),

    // Spear Overrides
    SPEAR("spear", "Heavy Spear", 3.0, 1.2, "spear", 2.0),
    HALBERD("halberd", "Halberd", 5.0, 0.8, "spear",3.0),
    LANCE("lance", "Lance", 3.0, 0.8, "spear", 3.0),
    POLEAXE("poleaxe", "Poleaxe", 4.0, 1.1, "axe", 1.0),
    GLAIVE("glaive", "Glaive", 4.0, 1.3, "axe", 1.0),


    // Axe Overrides
    LONGAXE("longaxe", "Longaxe", 6.0, 0.8, "axe", 0.5),
    // DOUBLEAXE

    //BATTLESAW("battlesaw", "Battlesaw", 5.0, 1.0, "axe", 1.0),
    //SCISSORS("scissors", "scissors", 5.0, 1.0, "axe", 1.0), // Nom-Nom Shark

    // Pickaxe Overrides
    WARHAMMER("warhammer", "Warhammer", 4.0, 1.4, "pickaxe"),

    // Hoe Overrides
    SCYTHE("scythe", "Scythe", 3.0, 1.1, "hoe", 1.0),

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
                AXE
            )
        }

        fun getOdysseyTypes(): List<ToolType> {
            return listOf(
                KATANA,
                CLAYMORE,
                DAGGER,
                RAPIER,
                SABER,
                SICKLE,
                CHAKRAM,
                KUNAI,
                LONGSWORD,
                ZWEIHANDER,
                KRIEGSMESSER,
                // Non-Swords
                LONGAXE,
                POLEAXE,
                GLAIVE,
                SPEAR,
                HALBERD,
                WARHAMMER,
                SCYTHE
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


}


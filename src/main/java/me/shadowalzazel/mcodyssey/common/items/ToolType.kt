package me.shadowalzazel.mcodyssey.common.items

enum class ToolType(
    val toolName: String, // 'item_name' component
    val fullName: String, // 'custom_name' component
    val baseDamage: Double,
    val baseSpeed: Double,
    val itemOverrideName: String,  // for finding the item id
    val bonusRange: Double? = null
) {
    // Vanilla
    SWORD("sword", "Sword", 3.0, 1.6, "sword"),
    PICKAXE("pickaxe", "Pickaxe", 1.0, 1.2,  "pickaxe"),
    AXE("axe", "Axe", 5.0, 1.0, "axe"),
    SHOVEL("shovel", "Shovel", 1.5, 1.0, "shovel"),
    HOE("hoe", "Hoe", 0.0, 1.6, "hoe"),

    // Sword overrides
    KATANA("katana", "Katana", 3.0, 1.7, "sword", 0.2),
    CLAYMORE("claymore", "Claymore", 6.0, 0.9, "sword", 0.5),
    DAGGER("dagger", "Dagger", 1.0, 3.0, "sword", -0.2),
    RAPIER("rapier", "Rapier", 1.5, 3.5, "sword"),
    CUTLASS("cutlass", "Cutlass", 2.5, 2.1, "sword"),
    SABER("saber", "Saber", 3.0, 1.8, "sword"),
    SICKLE("sickle", "Sickle", 1.5, 2.7, "sword", -0.2),
    CHAKRAM("chakram", "Chakram", 1.5, 2.5, "sword", -0.3),
    KUNAI("kunai", "Kunai", 1.0, 2.5, "sword"),
    LONGSWORD("longsword", "Longsword", 4.0, 1.5, "sword", 0.3),
    //ARM_BLADE("arm_blade", "Arm Blade", 2.0, 3.0, "sword", -0.3),
    ZWEIHANDER("zweihander", "Zweihander", 7.0, 0.7, "sword", 0.8),
    KRIEGSMESSER("kriegsmesser", "Kriegsmesser", 6.0, 0.8, "sword", 0.6),
    // Shovel Overrides
    SPEAR("spear", "Spear", 3.0, 1.2, "shovel", 2.0),
    HALBERD("halberd", "Halberd", 5.0, 0.9, "shovel",3.0),
    LANCE("lance", "Lance", 3.0, 0.8, "shovel", 3.0),
    // Axe Overrides
    LONGAXE("longaxe", "Longaxe", 6.0, 0.8, "axe", 0.5),
    POLEAXE("poleaxe", "Poleaxe", 4.0, 1.1, "axe", 1.0),
    GLAIVE("glaive", "Glaive", 4.0, 1.3, "axe", 1.0),
    //BATTLESAW("battlesaw", "Battlesaw", 5.0, 1.0, "axe", 1.0),
    // Pickaxe Overrides
    WARHAMMER("warhammer", "Warhammer", 4.0, 1.4, "pickaxe"),
    // Hoe Overrides
    SCYTHE("scythe", "Scythe", 3.0, 1.1, "hoe", 1.0),
    // Others
    SHURIKEN("shuriken", "Shuriken", 0.5, 1.0, "iron_nugget")

}


package me.shadowalzazel.mcodyssey.items.utility

enum class ToolType(
    val itemName: String, // 'item_name' component
    val customName: String, // 'custom_name' component
    val baseDamage: Double,
    val baseSpeed: Double,
    val itemModelSuf: Int, // 'custom_model_data' component [-----67]
    val itemOverrideSuf: String,  // for finding the item id
    val bonusRange: Double? = null
) {

    // Sword overrides
    KATANA("katana", "Katana", 3.0, 1.7, 44, "sword", 0.2),
    CLAYMORE("claymore", "Claymore", 6.0, 0.85, 45, "sword", 0.5),
    DAGGER("dagger", "Dagger", 1.0, 3.0, 46, "sword", -0.2),
    RAPIER("rapier", "Rapier", 1.5, 3.5, 47, "sword"),
    CUTLASS("cutlass", "Cutlass", 2.5, 2.1, 48, "sword"),
    SABER("saber", "Saber", 3.0, 1.8, 49, "sword"),
    SICKLE("sickle", "Sickle", 1.5, 2.7, 50, "sword", -0.2),
    CHAKRAM("chakram", "Chakram", 1.5, 2.5, 51, "sword", -0.3),
    KUNAI("kunai", "Kunai", 1.0, 2.5, 52, "sword"),
    LONGSWORD("longsword", "Longsword", 4.0, 1.5, 53, "sword", 0.3),
    // Shovel Overrides
    SPEAR("spear", "Spear", 3.0, 1.2, 74, "shovel", 2.0),
    HALBERD("halberd", "Halberd", 5.0, 0.9, 75, "shovel",3.0),
    LANCE("lance", "Lance", 3.0, 0.8, 76, "shovel", 3.0),
    // Axe Overrides
    LONGAXE("longaxe", "Longaxe", 6.0, 0.8, 85, "axe", 0.5),
    POLEAXE("poleaxe", "Poleaxe", 4.0, 1.2, 86, "axe", 1.0),
    // Pickaxe Overrides
    WARHAMMER("warhammer", "Warhammer", 4.0, 1.4, 11, "pickaxe"),
    // Hoe Overrides
    SCYTHE("scythe", "Scythe", 3.0, 1.1, 92, "hoe"),
    // Others
    SHURIKEN("shuriken", "Shuriken", 0.5, 1.0, 1, "iron_nugget")


    // Old
    //CLUB(3.0, 1.6, "Club", 6900),
    //STAFF(2.5, 1.3, "Staff", 6900),
    //ZWEIHANDER(6.0, 0.9, "Zweihander", ItemModels.ZWEIHANDER)
}


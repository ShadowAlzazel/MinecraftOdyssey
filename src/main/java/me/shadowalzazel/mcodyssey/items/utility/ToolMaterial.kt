package me.shadowalzazel.mcodyssey.items.utility

enum class ToolMaterial(
    val itemName: String, // 'item_name' component
    val customName: String, // 'custom_name' component
    val attackDamage: Double,
    val itemModelPre: Int, // 'custom_model_data' component [12345--]
    val itemOverridePre: String, // for finding the item id
    val maxDurability: Int? = null
) {

    // Minecraft
    WOODEN("wooden", "Wooden", 1.0, 69051, "wooden"),
    GOLDEN("golden", "Golden", 1.5, 69052, "golden"),
    STONE("stone", "Stone", 2.0, 69053, "stone"),
    IRON("iron", "Iron", 3.0, 69057, "iron"),
    DIAMOND("diamond", "Diamond", 4.0, 69058, "diamond"),
    NETHERITE("netherite", "Netherite", 5.0, 69059, "netherite"),
    // Odyssey
    COPPER("copper", "Copper", 2.5, 69055, "golden", 198), // OVERWORLD
    SILVER("silver", "Silver", 3.0, 69063, "iron", 337), // OVERWORLD
    SOUL_STEEL("soul_steel", "Soul Steel", 4.0, 69066, "iron", 666), // NETHER
    TITANIUM("titanium", "Titanium", 4.0, 69068, "iron", 1002), // OVERWORLD
    ANODIZED_TITANIUM("anodized_titanium", "Anodized Titanium", 4.0, 69070, "iron", 1002), // OVERWORLD
    IRIDIUM("iridium", "Iridium", 5.0, 69071, "iron", 3108), // OVERWORLD
    MITHRIL("mithril", "Mithril", 6.0, 69076, "iron", 1789), // EDGE

}
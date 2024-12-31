package me.shadowalzazel.mcodyssey.common.items

enum class ToolMaterial(
    val namePre: String, // 'item_name' component
    val customNamePre: String, // 'custom_name' component
    val attackDamage: Double,
    val itemOverridePre: String, // for finding the item id
    val maxDurability: Int? = null
) {

    // Minecraft
    WOODEN("wooden", "Wooden", 1.0, "wooden"),
    GOLDEN("golden", "Golden", 1.5, "golden"),
    STONE("stone", "Stone", 2.0, "stone"),
    IRON("iron", "Iron", 3.0, "iron"),
    DIAMOND("diamond", "Diamond", 4.0, "diamond"),
    NETHERITE("netherite", "Netherite", 5.0, "netherite"),
    // Odyssey
    COPPER("copper", "Copper", 2.5, "golden", 198), // OVERWORLD
    SILVER("silver", "Silver", 3.0, "iron", 337), // OVERWORLD
    SOUL_STEEL("soul_steel", "Soul Steel", 4.0, "iron", 666), // NETHER
    TITANIUM("titanium", "Titanium", 4.0, "iron", 1002), // OVERWORLD
    ANODIZED_TITANIUM("anodized_titanium", "Anodized Titanium", 4.0, "iron", 1002), // OVERWORLD
    IRIDIUM("iridium", "Iridium", 5.0, "diamond", 3108), // OVERWORLD
    MITHRIL("mithril", "Mithril", 6.0, "diamond", 1789), // EDGE
    CRYSTAL_ALLOY("crystal_alloy", "Crystal Alloy", 4.0, "iron", 1123); // EDGE

}
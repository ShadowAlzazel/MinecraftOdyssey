package me.shadowalzazel.mcodyssey.items.utility

enum class ToolType(
    val itemName: String, // 'item_name' component
    val toolName: String, // 'custom_name' component
    val baseDamage: Double,
    val baseSpeed: Double,
    val itemModelSuf: Int, // 'custom_model_data' component [-----67]
    val itemOverrideSuf: String  // for finding the item id
) {
}
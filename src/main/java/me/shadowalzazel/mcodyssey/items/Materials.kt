package me.shadowalzazel.mcodyssey.items

import net.kyori.adventure.text.Component
import org.bukkit.Material
import java.util.*

// Maybe move to parse from json components 1.20.5
enum class Materials(
    val itemName: String,
    val customName: String,
    val customModel: Int,
    val overrideMaterial: Material,
    val lore: List<Component>? = null
) {

    SOUL_STEEL_INGOT("soul_steel_ingot", "Soul Steel Ingot", 6906618, Material.IRON_INGOT),
    SILVER_INGOT("silver_ingot", "Silver Ingot", 6906619, Material.IRON_INGOT),
    MITHRIL_INGOT("mithril_ingot", "Mithril Ingot", 6906777, Material.IRON_INGOT),
    IRIDIUM_INGOT("iridium_ingot", "Iridium Ingot", 6906878, Material.IRON_INGOT),
    TITANIUM_INGOT("titanium_ingot", "Titanium Ingot", 6906879, Material.IRON_INGOT),
    HEATED_TITANIUM_INGOT("heated_titanium_ingot", "Heated Titanium Ingot", 6906880, Material.IRON_INGOT),
    ANDONIZED_TITANIUM_INGOT("andonized_titanium_ingot", "Andonized Titanium Ingot", 6906881, Material.IRON_INGOT),

}
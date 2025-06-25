package me.shadowalzazel.mcodyssey.util

import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

@Suppress("JoinDeclarationAndAssignment")
class EquipmentRandomizer(
    weaponMaterials: List<ToolMaterial>,
    weaponList: List<ToolType>,
    partList: List<String>,
    armorMaterials: List<String>?,
    armorTrimMaterials: List<TrimMaterial>,
    armorTrimPatterns: List<TrimPattern>) {

    val toolMaterial: ToolMaterial
    val toolType: ToolType
    val toolPattern: String
    val armorMaterial: String
    val armorTrim: ArmorTrim

    init {
        toolMaterial = weaponMaterials.random()
        toolType = weaponList.random()
        toolPattern = partList.random()
        armorMaterial = armorMaterials?.random() ?: toolMaterial.nameId
        armorTrim = ArmorTrim(armorTrimMaterials.random(), armorTrimPatterns.random())
    }

}
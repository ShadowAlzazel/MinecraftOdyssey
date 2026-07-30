package me.shadowalzazel.mcodyssey.common.mobs

import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.common.trims.TrimMaterials
import me.shadowalzazel.mcodyssey.common.trims.TrimPatterns
import me.shadowalzazel.mcodyssey.util.EquipmentRandomBuilder
import me.shadowalzazel.mcodyssey.util.constants.EliteMobsData
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

// ──────────────────────────────────────────────────────────────────────────────
// ─────────────────────────── EQUIPMENT CATALOGUE ──────────────────────────────
// ──────────────────────────────────────────────────────────────────────────────

/**
 * Functions, not `val`s: `EquipmentRandomBuilder` is a builder, so sharing one instance across
 * every spawn risks leaking state. If it's genuinely immutable, flip these to `val`.
 */
object EquipmentPresets {

    fun shadowElite() = EquipmentRandomBuilder(
        listOf(ToolMaterial.MITHRIL),
        EliteMobsData.ALL_WEAPONS,
        EliteMobsData.ALL_PARTS,
        null,
        EliteMobsData.ELITE_ARMOR_TRIM_MATS,
        EliteMobsData.SHINY_ARMOR_TRIM_PATTERNS,
    )

    fun shadowBasic() = EquipmentRandomBuilder(
        listOf(ToolMaterial.DIAMOND),
        EliteMobsData.ALL_WEAPONS,
        EliteMobsData.ALL_PARTS,
        listOf("chainmail"),
        listOf(TrimMaterials.OBSIDIAN),
        EliteMobsData.SHADOW_MOB_TRIM_PATTERNS,
    )

    fun vanguard() = EquipmentRandomBuilder(
        listOf(ToolMaterial.IRON),
        listOf(ToolType.GLAIVE),
        listOf("imperial", "fancy", "voyager"),
        null,
        listOf(TrimMaterial.GOLD),
        listOf(TrimPatterns.VOYAGER),
    )

    fun terminalGrid() = EquipmentRandomBuilder(
        listOf(ToolMaterial.COPPER),
        EliteMobsData.ALL_WEAPONS,
        EliteMobsData.ALL_PARTS,
        listOf("copper"),
        listOf(TrimMaterial.RESIN),
        listOf(TrimPatterns.VOYAGER),
    )

    fun hypercubicGuard() = EquipmentRandomBuilder(
        listOf(ToolMaterial.IRON),
        EliteMobsData.ALL_WEAPONS,
        EliteMobsData.ALL_PARTS,
        listOf("iron"),
        listOf(TrimMaterials.NEPTUNIAN, TrimMaterials.JOVIANITE),
        listOf(TrimPatterns.VOYAGER, TrimPattern.BOLT, TrimPattern.RAISER),
    )

    fun sunkenGuard() = EquipmentRandomBuilder(
        listOf(ToolMaterial.SILVER, ToolMaterial.IRON),
        EliteMobsData.ALL_WEAPONS,
        EliteMobsData.ALL_PARTS,
        listOf("silver", "iron"),
        listOf(TrimMaterial.DIAMOND),
        listOf(TrimPatterns.VOYAGER, TrimPattern.BOLT, TrimPattern.RAISER),
    )
}
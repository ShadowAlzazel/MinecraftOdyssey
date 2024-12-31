package me.shadowalzazel.mcodyssey.util.constants

import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.common.trims.TrimMaterials
import me.shadowalzazel.mcodyssey.common.trims.TrimPatterns
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object MobData {

    val DANGER_PREFIXES = setOf(
        "Deadly", "Magnificent", "Terrorizing", "Classic", "Potent", "Dominant", "Forceful", "Mighty",
        "Great", "Cruel", "Dangerous", "Savage", "Lethal", "Fatal", "Whimsy", "Mythic", "Legendary",
        "Shiny", "Glistening", "Sparkling"
    )

    private val conjunctions = listOf("with", "of", "master of", "joined with", "imbued by", "keeper of", "holder of")


    // Weapon Customization
    val ALL_WEAPONS = listOf(
        ToolType.SABER, ToolType.KATANA, ToolType.LONGSWORD, ToolType.CUTLASS, ToolType.RAPIER,
        ToolType.CLAYMORE, ToolType.KRIEGSMESSER,
        ToolType.POLEAXE, ToolType.LONGAXE, ToolType.GLAIVE,
        ToolType.WARHAMMER,  ToolType.SCYTHE, ToolType.SPEAR, ToolType.HALBERD,
        ToolType.DAGGER, ToolType.SICKLE, ToolType.CHAKRAM // Double
    )
    val ALL_PARTS = listOf(
        "crusader", "danger", "fancy", "humble", "imperial", "marauder", "seraph", "vandal", "voyager")

    // Armor Customization
    val ELITE_ARMOR_TRIM_MATS = listOf(
        TrimMaterial.AMETHYST, TrimMaterial.EMERALD, TrimMaterial.DIAMOND,
        TrimMaterial.GOLD, TrimMaterial.RESIN, TrimMaterial.COPPER,
        TrimMaterials.SILVER, TrimMaterials.TITANIUM, TrimMaterials.ANODIZED_TITANIUM, // Metals
        TrimMaterials.RUBY, TrimMaterials.JADE, TrimMaterials.ALEXANDRITE, TrimMaterials.KUNZITE, // Gems
        TrimMaterials.OBSIDIAN, TrimMaterials.JOVIANITE, TrimMaterials.NEPTUNIAN // Special
    )

    val ELITE_ARMOR_TRIM_PATTERNS = listOf(
        TrimPattern.WAYFINDER, TrimPattern.RAISER, TrimPattern.HOST, TrimPattern.SHAPER,
        TrimPattern.SENTRY, TrimPattern.DUNE, TrimPattern.WILD, TrimPattern.COAST
    )

    val SHINY_ARMOR_TRIM_PATTERNS = listOf(
        TrimPatterns.IMPERIAL, TrimPatterns.RING, TrimPatterns.LEAF, TrimPatterns.DANGER, TrimPatterns.VOYAGER
    )

    val SHADOW_MOB_TRIM_PATTERNS = listOf(
        TrimPatterns.IMPERIAL, TrimPattern.FLOW, TrimPattern.BOLT, TrimPatterns.VOYAGER,
    )

    fun newName(name: String): String {
        return "${DANGER_PREFIXES.random()} $name ${conjunctions.random()} "
    }

}
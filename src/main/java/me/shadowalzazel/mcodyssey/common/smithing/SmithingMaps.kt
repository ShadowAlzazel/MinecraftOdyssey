package me.shadowalzazel.mcodyssey.common.smithing

import me.shadowalzazel.mcodyssey.common.trims.TrimMaterials
import me.shadowalzazel.mcodyssey.common.trims.TrimPatterns
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object SmithingMaps {

    val MATERIAL_UPGRADE_MAP = mapOf(
        "soul_steel_ingot" to "soul_steel",
        "mithril_ingot" to "mithril",
        "titanium_ingot" to "titanium",
        "anodized_titanium_ingot" to "titanium",
        "iridium_ingot" to "iridium"
    )

    val TEMPLATE_INPUT_MAP = mapOf(
        "soul_steel_upgrade_template" to "soul_steel_ingot",
        "mithril_upgrade_template" to "mithril_ingot",
        "titanium_upgrade_template" to "titanium_ingot",
        "iridium_upgrade_template" to "iridium_ingot"
    )

    val DURABILITY_MAP = mapOf(
        "iridium" to 3178,
        "mithril" to 1789,
        "titanium" to 1002,
        "anodized_titanium" to 1002,
        "soul_steel" to 666,
        "netherite" to 2031
    )

    val MODEL_DATA_MAP = mapOf(
        "tool_type" to 0, // Not available to modify
        "blade" to 1,
        "handle" to 2,
        "hilt" to 3,
        "pommel" to 4,
        "trim_pattern" to 5
    )

    val PART_FROM_ITEM = mapOf(
        "blade_part_upgrade_template" to "blade",
        "handle_part_upgrade_template" to "handle",
        "hilt_part_upgrade_template" to "hilt",
        "pommel_part_upgrade_template" to "pommel",
    )

    val PATTERN_FROM_ITEM = mapOf(
        "voyager_part_pattern" to "voyager",
        "danger_part_pattern" to "danger",
        "seraph_part_pattern" to "seraph",
        "marauder_part_pattern" to "marauder",
        "crusader_part_pattern" to "crusader",
        "vandal_part_pattern" to "vandal",
        "imperial_part_pattern" to "imperial",
        "fancy_part_pattern" to "fancy",
        "humble_part_pattern" to "humble"
    )

    val TRIM_MATERIAL_FROM_ITEM_MAP = mapOf(
        "alexandrite" to TrimMaterials.ALEXANDRITE,
        "kunzite" to TrimMaterials.KUNZITE,
        "jade" to TrimMaterials.JADE,
        "ruby" to TrimMaterials.RUBY,
        "soul_quartz" to TrimMaterials.SOUL_QUARTZ,
        "soul_steel_ingot" to TrimMaterials.SOUL_STEEL,
        "iridium_ingot" to TrimMaterials.IRIDIUM,
        "mithril_ingot" to TrimMaterials.MITHRIL,
        "titanium_ingot" to TrimMaterials.TITANIUM,
        "anodized_titanium_ingot" to TrimMaterials.ANODIZED_TITANIUM,
        "silver_ingot" to TrimMaterials.SILVER,
        "jovianite" to TrimMaterials.JOVIANITE,
        "neptunian" to TrimMaterials.NEPTUNIAN,
        "iron_ingot" to TrimMaterial.IRON,
        "gold_ingot" to TrimMaterial.GOLD,
        "redstone" to TrimMaterial.REDSTONE,
        "diamond" to TrimMaterial.DIAMOND,
        "emerald" to TrimMaterial.EMERALD,
        "amethyst_shard" to TrimMaterial.AMETHYST,
        "netherite_ingot" to TrimMaterial.NETHERITE,
        "lapis_lazuli" to TrimMaterial.LAPIS,
        "quartz" to TrimMaterial.QUARTZ,
        "copper_ingot" to TrimMaterial.COPPER,
        "obsidian" to TrimMaterials.OBSIDIAN,
        "resin" to TrimMaterial.RESIN,
    )

    val ARMOR_TRIM_PATTERNS_FROM_ITEM = mapOf(
        // Odyssey
        "imperial_armor_trim_smithing_template" to TrimPatterns.IMPERIAL,
        "voyager_armor_trim_smithing_template" to TrimPatterns.VOYAGER,
        "leaf_armor_trim_smithing_template" to TrimPatterns.LEAF,
        "danger_armor_trim_smithing_template" to TrimPatterns.DANGER,
        "ring_armor_trim_smithing_template" to TrimPatterns.RING,
        // Vanilla
        "ward_armor_trim_smithing_template" to TrimPattern.WARD,
        "spire_armor_trim_smithing_template" to TrimPattern.SPIRE,
        "coast_armor_trim_smithing_template" to TrimPattern.COAST,
        "eye_armor_trim_smithing_template" to TrimPattern.EYE,
        "dune_armor_trim_smithing_template" to TrimPattern.DUNE,
        "wild_armor_trim_smithing_template" to TrimPattern.WILD,
        "rib_armor_trim_smithing_template" to TrimPattern.RIB,
        "tide_armor_trim_smithing_template" to TrimPattern.TIDE,
        "sentry_armor_trim_smithing_template" to TrimPattern.SENTRY,
        "vex_armor_trim_smithing_template" to TrimPattern.VEX,
        "snout_armor_trim_smithing_template" to TrimPattern.SNOUT,
        "wayfinder_armor_trim_smithing_template" to TrimPattern.WAYFINDER,
        "shaper_armor_trim_smithing_template" to TrimPattern.SHAPER,
        "silence_armor_trim_smithing_template" to TrimPattern.SILENCE,
        "raiser_armor_trim_smithing_template" to TrimPattern.RAISER,
        "host_armor_trim_smithing_template" to TrimPattern.HOST,
        "flow_armor_trim_smithing_template" to TrimPattern.FLOW,
        "bolt_armor_trim_smithing_template" to TrimPattern.BOLT
    )

    val WEAPON_TRIM_PATTERNS_FROM_ITEM = mapOf(
        "cross_weapon_trim_smithing_template" to TrimPatterns.CROSS,
        "spine_weapon_trim_smithing_template" to TrimPatterns.SPINE,
        "wings_weapon_trim_smithing_template" to TrimPatterns.WINGS,
        "trace_weapon_trim_smithing_template" to TrimPatterns.TRACE,
        "jewel_weapon_trim_smithing_template" to TrimPatterns.JEWEL,
    )

    // LISTS

    val UPGRADE_TEMPLATES = listOf(
        "mithril_upgrade_template",
        "soul_steel_upgrade_template",
        "titanium_upgrade_template",
        "iridium_upgrade_template",
        "netherite_upgrade_template"
    )

    val NOT_UPGRADEABLE = listOf(
        "titanium", "iridium", "mithril", "anodized_titanium", "silver", "soul_steel", "netherite"
    )

    val TRIM_TEMPLATES = listOf(
        // Odyssey
        "imperial_armor_trim_smithing_template",
        "voyager_armor_trim_smithing_template",
        "leaf_armor_trim_smithing_template",
        "danger_armor_trim_smithing_template",
        "ring_armor_trim_smithing_template",
        "cross_weapon_trim_smithing_template",
        "spine_weapon_trim_smithing_template",
        "wings_weapon_trim_smithing_template",
        "trace_weapon_trim_smithing_template",
        "jewel_weapon_trim_smithing_template",
        // Vanilla
        "ward_armor_trim_smithing_template",
        "spire_armor_trim_smithing_template",
        "coast_armor_trim_smithing_template",
        "eye_armor_trim_smithing_template",
        "dune_armor_trim_smithing_template",
        "wild_armor_trim_smithing_template",
        "rib_armor_trim_smithing_template",
        "tide_armor_trim_smithing_template",
        "sentry_armor_trim_smithing_template",
        "vex_armor_trim_smithing_template",
        "snout_armor_trim_smithing_template",
        "wayfinder_armor_trim_smithing_template",
        "shaper_armor_trim_smithing_template",
        "silence_armor_trim_smithing_template",
        "raiser_armor_trim_smithing_template",
        "host_armor_trim_smithing_template",
        "flow_armor_trim_smithing_template",
        "bolt_armor_trim_smithing_template"
    )


}
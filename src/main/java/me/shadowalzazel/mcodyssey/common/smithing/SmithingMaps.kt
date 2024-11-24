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

    val NOT_UPGRADEABLE = listOf(
        "titanium", "iridium", "mithril", "anodized_titanium", "silver", "soul_steel", "netherite"
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
        "obsidian" to TrimMaterials.OBSIDIAN
    )

    val PATTERN_FROM_ITEM_MAP = mapOf(
        "imperial_armor_trim_smithing_template" to TrimPatterns.IMPERIAL,
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

    val UPGRADE_TEMPLATES = listOf(
        "mithril_upgrade_template",
        "soul_steel_upgrade_template",
        "titanium_upgrade_template",
        "iridium_upgrade_template",
        "netherite_upgrade_template"
    )

    val TRIM_TEMPLATES = listOf(
        "imperial_armor_trim_smithing_template",
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
package me.shadowalzazel.mcodyssey.common.recipes

import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.common.recipes.UnlockTrigger.CRAFT
import me.shadowalzazel.mcodyssey.common.recipes.UnlockTrigger.DISCOVER
import me.shadowalzazel.mcodyssey.common.recipes.UnlockTrigger.JOIN
import me.shadowalzazel.mcodyssey.common.recipes.UnlockTrigger.PICKUP

/**
 * THE ONLY FILE YOU EDIT TO ADD UNLOCKS.
 *
 * Call OdysseyUnlocks.register() once in onEnable(), before registering listeners.
 */
object OdysseyUnlocks {

    // ──────────────────────────────────────────────────────────────────────────────
    // ──────────────────────────────── BUNDLING ────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    // --- tiny local helpers; add your own freely -----------------------------
    private fun armorSet(prefix: String) =
        listOf("helmet", "chestplate", "leggings", "boots").map { "${prefix}_$it" }

    private fun toolSet(prefix: String) =
        listOf("sword", "pickaxe", "axe", "shovel", "hoe", "spear").map { "${prefix}_$it" }

    private fun allWeapons(material: ToolMaterial) =
        ToolType.getOdysseyTypes().map { "${material.nameId}_${it.toolName}" }

    fun register() {
        RecipeUnlocks.clear()
        RecipeUnlocks.debug = false            // flip to true while adding content
        RecipeUnlocks.defaultNamespace = "odyssey"

        registerGroups()
        registerToolVariants()
        registerMaterialGates()
        registerSimpleTable()
        registerBaseline()
    }

    /* --------------------------------------------------------------------- */
    /*  Reusable bundles                                                      */
    /* --------------------------------------------------------------------- */

    private fun registerGroups() = with(RecipeUnlocks) {
        group(
            "arcane_basics",
            "crystal_alloy_ingot", "arcane_pen", "arcane_scepter",
            "arcane_wand", "crystal_alloy_trimmers")
        //group("titanium_armor", armorSet("titanium"))
        //group("titanium_tools", toolSet("titanium"))
        group("starter_gear",
            "wooden_dagger", "bundle")
        //
        group("titanium_equipment",
            "tinkered_musket", "builders_gauntlet")

        group("part_upgrade_kit",
            "blade_part_upgrade_template", "hilt_part_upgrade_template",
            "handle_part_upgrade_template", "pommel_part_upgrade_template",
            "blade_part_upgrade_template_replicate", "hilt_part_upgrade_template_replicate",
            "handle_part_upgrade_template_replicate", "pommel_part_upgrade_template_replicate")
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // ────────────────────────────────── RULES ─────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    /* --------------------------------------------------------------------- */
    /*  Your original behaviour: crafting a vanilla tool unlocks every custom  */
    /*  variant that shares its vanilla base, in the same material.           */
    /* --------------------------------------------------------------------- */

    private fun registerToolVariants() = RecipeUnlocks.rule("tool_variants") {
        on(DISCOVER, CRAFT)
        requireToolMaterial()
        requireVanillaTool()
        grantEach { ctx ->
            ToolType.entries
                .filter { it.vanillaBase == ctx.toolType!!.vanillaBase && it != ctx.toolType }
                .map { "${ctx.toolMaterial!!.nameId}_${it.toolName}" }
        }
    }

    /* --------------------------------------------------------------------- */
    /*  Material-gated content                                                */
    /* --------------------------------------------------------------------- */

    private fun registerMaterialGates() = with(RecipeUnlocks) {

        rule("diamond_to_arcana") {
            onAnyTrigger()
            whenToolMaterial(ToolMaterial.DIAMOND)
            grantGroup("arcane_basics")
        }

        rule("silver_gear") {
            on(DISCOVER, PICKUP, CRAFT)
            whenIdIs("silver_ingot", "silver_nugget")
            grant("alchemical_diffuser", "silver_ingot", "silver_nugget")
        }

        rule("mithril_gear") {
            on(DISCOVER, PICKUP, CRAFT)
            whenIdIs("mithril_ingot")
            grant("mithril_upgrade_template")
        }

        rule("titanium_gear") {
            on(DISCOVER, PICKUP, CRAFT)
            whenIdIs("titanium_ingot")
            grant("titanium_upgrade_template")
            grantGroup("titanium_equipment")
        }

        rule("iridium_gear") {
            on( DISCOVER, PICKUP, CRAFT)
            whenIdIs("iridium_ingot", "iridium_nugget")
            grant("iridium_upgrade_template", "iridium_ingot", "iridium_nugget")
        }

        rule("crystal_alloy_gear") {
            on(DISCOVER, PICKUP, CRAFT)
            whenIdIs("crystal_alloy_ingot")
            grant("crystal_alloy_upgrade_template")
            grantGroup("arcane_basics")
        }

        rule("soul_steel_gear") {
            on(DISCOVER, PICKUP, CRAFT)
            whenIdIs("soul_steel_ingot", "soul_sand", "soul_soil", "ectoplasm")
            grant("soul_steel_upgrade_template")
        }

        rule("weapon_part_patterns_and_upgrades") {
            on(DISCOVER, PICKUP, CRAFT)
            whenIdContains("part_pattern")
            grantGroup("part_upgrade_kit")
        }

        // pattern rule — every "*_part_pattern" unlocks its matching replica.
        rule("part_pattern_replicas") {
            on(DISCOVER, PICKUP, CRAFT)
            whenIdMatches(Regex("^(\\w+)_part_pattern$"))
            grantEach { ctx -> listOf(ctx.id + "_replicate") }
        }

        // pattern rule — every "*_part_pattern" unlocks its matching replica.
        rule("armor_trim_smithing_template_replicas") {
            on(DISCOVER, PICKUP, CRAFT)
            whenIdMatches(Regex("^(\\w+)_armor_trim_smithing_template$"))
            // Match only custom trims using clean -> "odyssey:abc"
            grantEach { ctx -> listOf("odyssey:" + ctx.id + "_replicate") }
        }

        /*
        rule("diamond_to_arcana") {
            onAnyTrigger()
            whenToolMaterial(ToolMaterial.DIAMOND)
            grantGroup("arcane_basics")
        }

        rule("titanium_gear") {
            onAnyTrigger()
            whenToolMaterial(ToolMaterial.TITANIUM)
            grant("builders_gauntlet")
            grantGroup("titanium_armor", "titanium_tools")
        }
         */

        /*
        // Example: anything netherite-ish, only for players with a permission.
        rule("netherite_masterwork") {
            on(CRAFT, DISCOVER)
            whenIdContains("netherite")
            whenPermission("odyssey.masterwork")
            grant("masterwork_anvil")
        }

         */
        // Example: a pattern rule — every "*_ingot" unlocks its matching trimmer.
        /*
        rule("ingot_trimmers") {
            on(CRAFT, PICKUP)
            whenIdMatches(Regex("^(\\w+)_ingot$"))
            grantEach { ctx -> listOf(ctx.id.removeSuffix("_ingot") + "_trimmers") }
        }
         */
    }

    /* --------------------------------------------------------------------- */
    /*  The long tail — one line per item. Add hundreds here.                 */
    /*  "when the player crafts/picks up/discovers X, unlock these".          */
    /* --------------------------------------------------------------------- */

    private fun registerSimpleTable() = RecipeUnlocks.table(
        "wheat"   to listOf("tonkatsu", "french_toast"),
        "cooked_beef"      to listOf("steak_and_eggs"),
        "amethyst_shard"   to listOf("crystal_alloy_ingot", "blank_tome", "crystal_candy",
            "crystalline_compost", "prismatic_name_tag"),
        "book"             to listOf("blank_tome"),
        "netherite_ingot"  to listOf("auto_crossbow"),
        "heavy_core"       to listOf("auto_crossbow", "tinkered_musket"),
        "gunpowder"        to listOf("sulfur_powder", "gunpowder_from_sulfur", "explosive_arrow"),
        "cocoa_beans"      to listOf("chocolate_mochi"),
        "cooked_cod"       to listOf("fish_n_chips"),
        "sweet_berries"    to listOf("fruit_bowl", "crystal_candy"),
        /*
        "cooked_beef"      to listOf("beef_wellington", "hearty_stew"),
        "wheat"            to listOf("travel_bread", "seed_cake"),
        "honey_bottle"     to listOf("honeyed_glazed_ham", "mead"),
        "amethyst_shard"   to listOf("resonant_lens", "focusing_prism"),
        "copper_ingot"     to listOf("copper_lantern", "lightning_rod_array"),
        "leather"          to listOf("satchel", "quiver"),
        "blaze_rod"        to listOf("ember_torch", "flame_censer"),

         */
        // ...
    )

    /* --------------------------------------------------------------------- */
    /*  Always-available recipes, granted on join                             */
    /* --------------------------------------------------------------------- */

    private fun registerBaseline() = RecipeUnlocks.rule("baseline") {
        on(JOIN)
        grantGroup("starter_gear")
    }
}
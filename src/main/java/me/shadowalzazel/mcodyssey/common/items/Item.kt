package me.shadowalzazel.mcodyssey.common.items

import me.shadowalzazel.mcodyssey.api.LootTableManager
import me.shadowalzazel.mcodyssey.common.items.custom.Misc
import me.shadowalzazel.mcodyssey.common.items.custom.Potions
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

// This Represents an Item generated from loot/item from datapack

open class Item(
    val name: String,
    val location: String?=null,
) {

    class DataItem(name: String, location: String?=null): Item(name, location)
    class GeneratedItem(name: String, val constructor: ItemConstructor): Item(name)

    open fun newItemStack(amount: Int=1, withBukkitId: Boolean=false): ItemStack {
        val item = when(this) {
            is DataItem -> LootTableManager.createItemStackFromItemTable(this.location ?: this.name)
            is GeneratedItem -> this.constructor.createItemStack(this.name, amount, withBukkitId)
            else -> ItemStack(Material.AIR)
        }
        item.amount = amount
        return item
    }


    @Suppress("unused")
    companion object {
        // ---------------------------------- GENERIC DATA ITEMS -----------------------------------
        val ARCANE_BOOK = DataItem("arcane_book")
        val ARCANE_PEN = DataItem("arcane_pen")
        val ANODIZED_TITANIUM_INGOT = DataItem("anodized_titanium_ingot")
        val COAGULATED_BLOOD = DataItem("coagulated_blood")
        val ECTOPLASM = DataItem("ectoplasm")
        val SOUL_QUARTZ = DataItem("soul_quartz")
        val HEATED_TITANIUM_INGOT = DataItem("heated_titanium_ingot")
        val IRIDIUM_INGOT = DataItem("iridium_ingot")
        val BLANK_TOME = DataItem("blank_tome")
        val ALEXANDRITE = DataItem("alexandrite")
        val JADE = DataItem("jade")
        val JOVIANITE = DataItem("jovianite")
        val KUNZITE = DataItem("kunzite")
        val MITHRIL_INGOT = DataItem("mithril_ingot")
        val NEPTUNIAN = DataItem("neptunian")
        val RUBY = DataItem("ruby")
        val SILVER_INGOT = DataItem("silver_ingot")
        val SILVER_NUGGET = DataItem("silver_nugget")
        val SOUL_STEEL_INGOT = DataItem("soul_steel_ingot")
        val TITANIUM_INGOT = DataItem("titanium_ingot")
        val SHADOW_TRIAL_KEY = DataItem("shadow_trial_key")
        val CRYSTAL_ALLOY_INGOT = DataItem("crystal_alloy_ingot")
        // Smithing
        val IMPERIAL_ARMOR_TRIM_SMITHING_TEMPLATE = DataItem("imperial_armor_trim_smithing_template")
        val VOYAGER_ARMOR_TRIM_SMITHING_TEMPLATE = DataItem("voyager_armor_trim_smithing_template")
        val LEAF_ARMOR_TRIM_SMITHING_TEMPLATE = DataItem("leaf_armor_trim_smithing_template")
        val DANGER_ARMOR_TRIM_SMITHING_TEMPLATE = DataItem("danger_armor_trim_smithing_template")
        val RING_ARMOR_TRIM_SMITHING_TEMPLATE = DataItem("ring_armor_trim_smithing_template")
        val CROSS_WEAPON_TRIM_SMITHING_TEMPLATE = DataItem("cross_weapon_trim_smithing_template")
        val SPINE_WEAPON_TRIM_SMITHING_TEMPLATE = DataItem("spine_weapon_trim_smithing_template")
        val WINGS_WEAPON_TRIM_SMITHING_TEMPLATE = DataItem("wings_weapon_trim_smithing_template")
        val TRACE_WEAPON_TRIM_SMITHING_TEMPLATE = DataItem("trace_weapon_trim_smithing_template")
        val JEWEL_WEAPON_TRIM_SMITHING_TEMPLATE = DataItem("jewel_weapon_trim_smithing_template")
        val MITHRIL_UPGRADE_TEMPLATE = DataItem("mithril_upgrade_template")
        val SOUL_STEEL_UPGRADE_TEMPLATE = DataItem("soul_steel_upgrade_template")
        val TITANIUM_UPGRADE_TEMPLATE = DataItem("titanium_upgrade_template")
        val IRIDIUM_UPGRADE_TEMPLATE = DataItem("iridium_upgrade_template")
        val CRYSTAL_ALLOY_UPGRADE_TEMPLATE = DataItem("crystal_alloy_upgrade_template")
        // Enchanting
        val TOME_OF_DISCHARGE = DataItem("tome_of_discharge")
        val TOME_OF_EXPENDITURE = DataItem("tome_of_expenditure")
        val TOME_OF_EXTRACTION = DataItem("tome_of_extraction")
        val TOME_OF_IMITATION = DataItem("tome_of_imitation")
        val TOME_OF_PROMOTION = DataItem("tome_of_promotion")
        val TOME_OF_AVARICE = DataItem("tome_of_avarice")
        val TOME_OF_HARMONY = DataItem("tome_of_harmony")
        val TOME_OF_REPLICATION = DataItem("tome_of_replication")
        // Tool Parts
        val BLADE_PART_UPGRADE_TEMPLATE = DataItem("blade_part_upgrade_template")
        val HANDLE_PART_UPGRADE_TEMPLATE = DataItem("handle_part_upgrade_template")
        val POMMEL_PART_UPGRADE_TEMPLATE = DataItem("pommel_part_upgrade_template")
        val HILT_PART_UPGRADE_TEMPLATE = DataItem("hilt_part_upgrade_template")
        val EMPTY_PART_UPGRADE_TEMPLATE = DataItem("empty_part_upgrade_template")
        val VOYAGER_PART_PATTERN = DataItem("voyager_part_pattern")
        val DANGER_PART_PATTERN = DataItem("danger_part_pattern")
        val SERAPH_PART_PATTERN = DataItem("seraph_part_pattern")
        val MARAUDER_PART_PATTERN = DataItem("marauder_part_pattern")
        val CRUSADER_PART_PATTERN = DataItem("crusader_part_pattern")
        val VANDAL_PART_PATTERN = DataItem("vandal_part_pattern")
        val IMPERIAL_PART_PATTERN = DataItem("imperial_part_pattern")
        val FANCY_PART_PATTERN = DataItem("fancy_part_pattern")
        val HUMBLE_PART_PATTERN = DataItem("humble_part_pattern")
        val EMPTY_PART_PATTERN = DataItem("empty_part_pattern")
        val MASTERCRAFTED_TOOL_TEMPLATE = DataItem("mastercrafted_tool_template")
        // Glyphic
        val GLAZED_ORB = DataItem("glazed_orb")
        val GLAZED_RODS = DataItem("glazed_rods")
        val GLAZED_KEY = DataItem("glazed_key")
        val GLAZED_SKULL = DataItem("glazed_skull")
        val GLAZED_DOWEL = DataItem("glazed_dowel")
        val GLAZED_TOTEM = DataItem("glazed_totem")
        val CLAY_ORB = DataItem("clay_orb")
        val CLAY_RODS = DataItem("clay_rods")
        val CLAY_KEY = DataItem("clay_key")
        val CLAY_SKULL = DataItem("clay_skull")
        val CLAY_DOWEL = DataItem("clay_dowel")
        val CLAY_TOTEM = DataItem("clay_totem")
        // Glyphsherds
        val ASSAULT_GLYPHSHERD = DataItem("assault_glyphsherd")
        val GUARD_GLYPHSHERD = DataItem("guard_glyphsherd")
        val FINESSE_GLYPHSHERD = DataItem("finesse_glyphsherd")
        val SWIFT_GLYPHSHERD = DataItem("swift_glyphsherd")
        val VITALITY_GLYPHSHERD = DataItem("vitality_glyphsherd")
        val STEADFAST_GLYPHSHERD = DataItem("steadfast_glyphsherd")
        val FORCE_GLYPHSHERD = DataItem("force_glyphsherd")
        val BREAK_GLYPHSHERD = DataItem("break_glyphsherd")
        val GRASP_GLYPHSHERD = DataItem("grasp_glyphsherd")
        val JUMP_GLYPHSHERD = DataItem("jump_glyphsherd")
        val GRAVITY_GLYPHSHERD = DataItem("gravity_glyphsherd")
        val RANGE_GLYPHSHERD = DataItem("range_glyphsherd")
        val SIZE_GLYPHSHERD = DataItem("size_glyphsherd")
        // Food
        val GREEN_APPLE = DataItem("green_apple")
        val BACON = DataItem("bacon")
        val BERRY_TART = DataItem("berry_tart")
        val CHOCOLATE_MOCHI = DataItem("chocolate_mochi")
        val COFFEE = DataItem("coffee")
        val CRYSTAL_CANDY = DataItem("crystal_candy")
        val FISH_N_CHIPS = DataItem("fish_n_chips")
        val FRENCH_TOAST = DataItem("french_toast")
        val FRUIT_BOWL = DataItem("fruit_bowl")
        val SALMON_ROLL = DataItem("salmon_roll")
        val SALMON_NIGIRI = DataItem("salmon_nigiri")
        val SHOYU_RAMEN = DataItem("shoyu_ramen")
        val TONKOTSU_RAMEN = DataItem("tonkotsu_ramen")
        val SPIDER_EYE_BOBA = DataItem("spider_eye_boba")
        val EARL_LILY_BOBA_TEA = DataItem("earl_lily_boba_tea")
        val BRISKET = DataItem("brisket")
        val COOKED_BRISKET = DataItem("cooked_brisket")
        val OOLONG_ORCHID_BOBA_TEA = DataItem("earl_lily_boba_tea")
        val MATCHA_MELON_BOBA_TEA = DataItem("earl_lily_boba_tea")
        val THAI_TULIP_BOBA_TEA = DataItem("earl_lily_boba_tea")
        val ALLIUM_JADE_BOBA_TEA = DataItem("earl_lily_boba_tea")
        val CORNFLOWER_CEYLON_BOBA_TEA = DataItem("earl_lily_boba_tea")
        // Misc
        val EXPLOSIVE_ARROW = DataItem("explosive_arrow")
        val TOTEM_OF_VEXING = DataItem("totem_of_vexing")
        val IRRADIATED_FRUIT = DataItem("irradiated_fruit")
        val SCULK_HEART = DataItem("sculk_heart")
        val SCULK_POINTER = DataItem("sculk_pointer")
        val SOUL_SPICE = DataItem("soul_spice")
        val SOUL_OMAMORI = DataItem("soul_omamori")
        // ---------------------------------- DATA ITEMS with custom  -----------------------------------
        // Exotics
        val ABZU_BLADE = DataItem("abzu_blade")
        val ELUCIDATOR = DataItem("elucidator")
        val EXCALIBUR = DataItem("excalibur")
        val KNIGHT_BREAKER = DataItem("knight_breaker")
        val SHOGUN_LIGHTNING = DataItem("shogun_lightning")
        val FROST_FANG = DataItem("frost_fang")
        // Pet
        val DOG_SPINACH = DataItem("dog_spinach")
        val DOG_SIZZLE_CRISP = DataItem("dog_sizzle_crisp")
        val DOG_MILK_BONE = DataItem("dog_milk_bone")
        // Equipment
        // val HORNED_HELMET = DataItem("horned_helmet")
        val TINKERED_BOW = DataItem("tinkered_bow")
        val CHAIN_HOOK = DataItem("chain_hook")
        val TINKERED_MUSKET = DataItem("tinkered_musket")
        val AUTO_CROSSBOW = DataItem("auto_crossbow")
        val COMPACT_CROSSBOW = DataItem("compact_crossbow")
        val ALCHEMICAL_DRIVER = DataItem("alchemical_driver")
        val ALCHEMICAL_DIFFUSER = DataItem("alchemical_diffuser")
        val ALCHEMICAL_BOLTER = DataItem("alchemical_bolter")
        val ARCANE_WAND = DataItem("arcane_wand")
        val ARCANE_BLADE = DataItem("arcane_blade")
        val ARCANE_SCEPTER = DataItem("arcane_scepter")
        val WARPING_WAND = DataItem("warping_wand")
        val VOID_LINKED_KUNAI = DataItem("void_linked_kunai")
        val SCROLL = DataItem("scroll")
        // ---------------------------------- ODYSSEY GENERATED -----------------------------------
        // Potions
        val POTION_VIAL = GeneratedItem("potion_vial", Potions.POTION_VIAL)
        val CRYSTALLINE_POTION = GeneratedItem("crystalline_potion", Potions.CRYSTALLINE_POTION)
        val POTION_OF_LEVITATION = GeneratedItem("potion_of_levitation", Potions.POTION_OF_LEVITATION)
        val POTION_OF_WITHERING = GeneratedItem("potion_of_withering", Potions.POTION_OF_WITHERING)
        val POTION_OF_DARKNESS = GeneratedItem("potion_of_darkness", Potions.POTION_OF_DARKNESS)
        val POTION_OF_LUCK = GeneratedItem("potion_of_luck", Potions.POTION_OF_LUCK)
        val POTION_OF_RESISTANCE = GeneratedItem("potion_of_resistance", Potions.POTION_OF_RESISTANCE)
        val POTION_OF_HASTE = GeneratedItem("potion_of_haste", Potions.POTION_OF_HASTE)
        val POTION_OF_BIOLUMINESCENCE = GeneratedItem("potion_of_bioluminescence", Potions.POTION_OF_BIOLUMINESCENCE)
        val POTION_OF_CONSTITUTION = GeneratedItem("potion_of_constitution", Potions.POTION_OF_CONSTITUTION)
        val POTION_OF_STONE_SKIN = GeneratedItem("potion_of_stone_skin", Potions.POTION_OF_STONE_SKIN)
        val POTION_OF_WRATH = GeneratedItem("potion_of_wrath", Potions.POTION_OF_WRATH)
        val POTION_OF_RECOUP = GeneratedItem("potion_of_recoup", Potions.POTION_OF_RECOUP)
        val POTION_OF_ZOOM = GeneratedItem("potion_of_zoom", Potions.POTION_OF_ZOOM)
        val POTION_OF_SHIMMER = GeneratedItem("bottle_of_shimmer", Potions.POTION_OF_SHIMMER)
        val ANGLERS_CONCOCTION = GeneratedItem("anglers_concoction", Potions.ANGLERS_CONCOCTION)
        val NETHER_OWL_CONCOCTION = GeneratedItem("nether_owl_concoction", Potions.NETHER_OWL_CONCOCTION)
        val SPELUNKERS_CONCOCTION = GeneratedItem("spelunkers_concoction", Potions.SPELUNKERS_CONCOCTION)
        val CUSTOM_CONCOCTION = GeneratedItem("custom_concoction", Potions.CUSTOM_CONCOCTION)
        // Misc
        val BLAZING_ROCKET = GeneratedItem("blazing_rocket", Misc.BLAZING_ROCKET)


    }

}
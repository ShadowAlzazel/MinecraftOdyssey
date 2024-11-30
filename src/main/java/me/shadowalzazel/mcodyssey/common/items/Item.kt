package me.shadowalzazel.mcodyssey.common.items

import me.shadowalzazel.mcodyssey.api.LootTableManager
import me.shadowalzazel.mcodyssey.common.items.constructors.Potions
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

// This Represents an Item generated from loot/item from datapack

open class Item(
    val name: String,
    val location: String?=null,
) {

    class DataItem(name: String, location: String?=null): Item(name, location)
    class CustomItem(name: String, val constructor: ItemConstructor): Item(name)

    open fun newItemStack(amount: Int=1, withIdTag: Boolean=true): ItemStack {
        val item = when(this) {
            is DataItem -> LootTableManager.getItemFromOdysseyLoot(this.location ?: this.name)
            is CustomItem -> this.constructor.createItemStack(this.name, amount, withIdTag)
            else -> ItemStack(Material.AIR)
        }
        item.amount = amount
        return item
    }


    @Suppress("unused")
    companion object {
        // Generic Data Items
        val ARCANE_BOOK = DataItem("arcane_book")
        val ANODIZED_TITANIUM_INGOT = DataItem("anodized_titanium_ingot")
        val COAGULATED_BLOOD = DataItem("coagulated_blood")
        val ECTOPLASM = DataItem("ectoplasm")
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
        val ARCANE_ARMOR_TRIM_SMITHING_TEMPLATE = DataItem("arcane_armor_trim_smithing_template")
        val DANGER_ARMOR_TRIM_SMITHING_TEMPLATE = DataItem("danger_armor_trim_smithing_template")
        val IMPERIAL_ARMOR_TRIM_SMITHING_TEMPLATE = DataItem("imperial_armor_trim_smithing_template")
        val MITHRIL_UPGRADE_TEMPLATE = DataItem("mithril_upgrade_template")
        val SOUL_STEEL_UPGRADE_TEMPLATE = DataItem("soul_steel_upgrade_template")
        val TITANIUM_UPGRADE_TEMPLATE = DataItem("titanium_upgrade_template")
        val IRIDIUM_UPGRADE_TEMPLATE = DataItem("iridium_upgrade_template")
        val TOME_OF_DISCHARGE = DataItem("tome_of_discharge")
        val TOME_OF_EXPENDITURE = DataItem("tome_of_expenditure")
        val TOME_OF_EXTRACTION = DataItem("tome_of_extraction")
        val TOME_OF_IMITATION = DataItem("tome_of_imitation")
        val TOME_OF_PROMOTION = DataItem("tome_of_promotion")
        val TOME_OF_REPLICATION = DataItem("tome_of_replication")
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
        // Pet
        val DOG_SPINACH = DataItem("dog_spinach")
        val DOG_SIZZLE_CRISP = DataItem("dog_sizzle_crisp")
        val DOG_MILK_BONE = DataItem("dog_milk_bone")
        // Potions
        val CRYSTALLINE_POTION = CustomItem("crystalline_potion", Potions.CRYSTALLINE_POTION)
        val POTION_OF_LEVITATION = CustomItem("potion_of_levitation", Potions.POTION_OF_LEVITATION)
        val POTION_OF_WITHERING = CustomItem("potion_of_withering", Potions.POTION_OF_WITHERING)
        val POTION_OF_DARKNESS = CustomItem("potion_of_darkness", Potions.POTION_OF_DARKNESS)
        val POTION_OF_LUCK = CustomItem("potion_of_luck", Potions.POTION_OF_LUCK)
        val POTION_OF_RESISTANCE = CustomItem("potion_of_resistance", Potions.POTION_OF_RESISTANCE)
        val POTION_OF_HASTE = CustomItem("potion_of_haste", Potions.POTION_OF_HASTE)
        val POTION_OF_BIOLUMINESCENCE = CustomItem("potion_of_bioluminescence", Potions.POTION_OF_BIOLUMINESCENCE)
        val POTION_OF_CONSTITUTION = CustomItem("potion_of_constitution", Potions.POTION_OF_CONSTITUTION)
        val POTION_OF_STONE_SKIN = CustomItem("potion_of_stone_skin", Potions.POTION_OF_STONE_SKIN)
        val POTION_OF_WRATH = CustomItem("potion_of_wrath", Potions.POTION_OF_WRATH)
        val POTION_OF_ZOOM = CustomItem("potion_of_zoom", Potions.POTION_OF_ZOOM)
        val POTION_OF_SHIMMER = CustomItem("bottle_of_shimmer", Potions.POTION_OF_SHIMMER)
        val ANGLERS_CONCOCTION = CustomItem("anglers_concoction", Potions.ANGLERS_CONCOCTION)
        val NETHER_OWL_CONCOCTION = CustomItem("nether_owl_concoction", Potions.NETHER_OWL_CONCOCTION)
        val SPELUNKERS_CONCOCTION = CustomItem("spelunkers_concoction", Potions.SPELUNKERS_CONCOCTION)
        val CUSTOM_CONCOCTION = CustomItem("custom_concoction", Potions.CUSTOM_CONCOCTION)

    }

}
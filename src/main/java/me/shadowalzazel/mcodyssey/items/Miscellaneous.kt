package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.items.creators.ItemCreator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Miscellaneous : ItemCreator {

    private val LORE_GRAY = TextColor.color(85, 85, 85)

    val TOTEM_OF_VEXING = OdysseyItem("totem_of_vexing", Material.PAPER, "Totem of Vexing", ItemModels.TOTEM_OF_VEXING)
    val IRRADIATED_FRUIT = OdysseyItem("irradiated_fruit", Material.APPLE, "Irradiated Fruit", ItemModels.IRRADIATED_FRUIT)
    val SCULK_HEART = OdysseyItem("sculk_heart", Material.ROTTEN_FLESH, "Sculk Heart", ItemModels.SCULK_HEART)
    val ENIGMATIC_OMAMORI = OdysseyItem("enigmatic_omamori", Material.PAPER, "Sculk Heart", ItemModels.ENIGMATIC_OMAMORI)

    val BLAZING_ROCKET = OdysseyItem("blazing_rocket", Material.FIREWORK_ROCKET, "Blazing Rocket", ItemModels.BLAZING_ROCKET,
        lore = listOf(Component.text("This rocket has a chance to explode!", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val SOUL_SPICE = OdysseyItem("soul_spice", Material.GLOWSTONE_DUST, "Soul Spice", ItemModels.SOUL_SPICE,
        lore = listOf(Component.text("Drop this to be able to see nearby enemies.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val SCULK_POINTER = OdysseyItem("sculk_pointer", Material.COMPASS, "Sculk Pointer", ItemModels.SCULK_POINTER,
        lore = listOf(Component.text("This device points in the direction of the nearest Ancient City.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // Books
    val ARCANE_BOOK = OdysseyItem("arcane_book", Material.ENCHANTED_BOOK, "Arcane Book", ItemModels.ARCANE_BOOK)

    val GILDED_BOOK = OdysseyItem("gilded_book", Material.ENCHANTED_BOOK, "Gilded Book", ItemModels.GILDED_BOOK)

    val BLANK_TOME = OdysseyItem("blank_tome", Material.BOOK, "Blank Tome", ItemModels.BLANK_TOME,
        lore = listOf(Component.text("A special book that can be enchanted into tomes.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // Tomes
    val TOME_OF_DISCHARGE = OdysseyItem("tome_of_discharge", Material.ENCHANTED_BOOK, "Tome of Discharge", ItemModels.TOME_OF_DISCHARGE,
        lore = listOf(Component.text("Removes one enchantment from the item.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))) // T1

    val TOME_OF_PROMOTION = OdysseyItem("tome_of_promotion", Material.ENCHANTED_BOOK, "Tome of Promotion", ItemModels.TOME_OF_PROMOTION,
        lore = listOf(Component.text("Increases the level of an enchantment by one, up to the max.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))) // T1

    val TOME_OF_HARMONY = OdysseyItem("tome_of_harmony", Material.ENCHANTED_BOOK, "Tome of Harmony", ItemModels.TOME_OF_HARMONY,
        lore = listOf(Component.text("Resets the repair and combination cost of an item.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))) // T2

    val TOME_OF_BANISHMENT = OdysseyItem("tome_of_banishment", Material.ENCHANTED_BOOK, "Tome of Banishment", ItemModels.TOME_OF_BANISHMENT,
        lore = listOf(Component.text("Removes an empty enchant slot from the item.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))) // T0

    val TOME_OF_EMBRACE = OdysseyItem("tome_of_embrace", Material.ENCHANTED_BOOK, "Tome of Embrace", ItemModels.TOME_OF_EMBRACE,
        lore = listOf(Component.text("Adds an empty enchant slot to the item.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))) // T1

    val TOME_OF_IMITATION = OdysseyItem("tome_of_imitation", Material.ENCHANTED_BOOK, "Tome of Imitation", ItemModels.TOME_OF_IMITATION, // T2
        lore = listOf(Component.text("Copies one enchantment from an enchanted book.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val TOME_OF_EXPENDITURE = OdysseyItem("tome_of_expenditure", Material.ENCHANTED_BOOK, "Tome of Expenditure", ItemModels.TOME_OF_EXPENDITURE, // T2
        lore = listOf(Component.text("Extracts one enchantment from an item (the item is destroyed).", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val TOME_OF_AVARICE = OdysseyItem("tome_of_avarice", Material.ENCHANTED_BOOK, "Tome of Avarice", ItemModels.TOME_OF_AVARICE, // T3
        lore = listOf(Component.text("Converts all enchantments to experience (the item is destroyed).", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val TOME_OF_REPLICATION = OdysseyItem("tome_of_replication", Material.ENCHANTED_BOOK, "Tome of Replication", ItemModels.TOME_OF_REPLICATION, // T3
        lore = listOf(Component.text("Replicates one book.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val TOME_OF_EUPHONY = OdysseyItem("tome_of_euphony", Material.ENCHANTED_BOOK, "Tome of Euphony", ItemModels.TOME_OF_EUPHONY, // T3
        lore = listOf(Component.text("[FINISH]", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val TOME_OF_POLYMERIZATION = OdysseyItem("tome_of_polymerization", Material.ENCHANTED_BOOK, "Tome of Polymerization", ItemModels.TOME_OF_POLYMERIZATION, // T3
        lore = listOf(Component.text("Adds all stored enchantments with no experience cost.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    // Can absorb any non-conflicting enchantment.
    // Applies the stored enchantments to an item with no cost


    // Spirit Items
    // TO DIFFERENT TYPES OF BUFFING ITEMS
    // TOTEMS ARE FOUND -> More Potent / Effects can be spread
    // CHARMS ARE MADE -> Less Potent / Personal

    // Sniffer
    val ASPEN_SEED = OdysseyItem("aspen_seed", Material.WHEAT_SEEDS, "Aspen Seed", ItemModels.ASPEN_SEED)
    val MAPLE_SEED = OdysseyItem("maple_seed", Material.WHEAT_SEEDS, "Aspen Seed", ItemModels.MAPLE_SEED)
    val SAKURA_SEED = OdysseyItem("sakura_seed", Material.WHEAT_SEEDS, "Aspen Seed", ItemModels.SAKURA_SEED)
    val REDWOOD_SEED = OdysseyItem("redwood_seed", Material.WHEAT_SEEDS, "Aspen Seed", ItemModels.REDWOOD_SEED)

    // Other
    val PRIMO_GEM = OdysseyItem( "primogem",Material.EMERALD,"Primogem", ItemModels.PRIMOGEM,
        lore = listOf(
            Component.text("A primordial crystalline gem that's beyond", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("the test world. Shines with the condensed hopes", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("and dreams of universes that once were.", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
    )

    // Unused [WIP]
    val CRYING_GOLD = OdysseyItem("crying_gold", Material.RAW_GOLD, "Crying Gold", ItemModels.CRYING_GOLD)
    val BREEZE_IN_A_BOTTLE = OdysseyItem("breeze_in_a_bottle", Material.GLASS_BOTTLE, "Breeze in a Bottle", ItemModels.BREEZE_IN_A_BOTTLE)
    val SOUL_CATALYST  = OdysseyItem("soul_catalyst", Material.AMETHYST_SHARD, "Soul Catalyst", ItemModels.SOUL_CATALYST)

}


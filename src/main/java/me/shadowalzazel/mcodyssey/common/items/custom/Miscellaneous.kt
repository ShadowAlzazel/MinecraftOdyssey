package me.shadowalzazel.mcodyssey.common.items.custom

import me.shadowalzazel.mcodyssey.common.items.OdysseyItem
import me.shadowalzazel.mcodyssey.datagen.items.ItemCreator
import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Miscellaneous : ItemCreator {

    private val LORE_GRAY = CustomColors.DARK_GRAY.color

    val TOTEM_OF_VEXING = OdysseyItem("totem_of_vexing", Material.PAPER, "Totem of Vexing")
    val IRRADIATED_FRUIT = OdysseyItem("irradiated_fruit", Material.APPLE, "Irradiated Fruit")
    val SCULK_HEART = OdysseyItem("sculk_heart", Material.ROTTEN_FLESH, "Sculk Heart")
    val ENIGMATIC_OMAMORI = OdysseyItem("enigmatic_omamori", Material.PAPER, "Sculk Heart")

    val BLAZING_ROCKET = OdysseyItem("blazing_rocket", Material.FIREWORK_ROCKET, "Blazing Rocket",
        lore = listOf(Component.text("This rocket has a chance to explode!", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val SOUL_SPICE = OdysseyItem("soul_spice", Material.GLOWSTONE_DUST, "Soul Spice",
        lore = listOf(Component.text("Drop this to be able to see nearby enemies.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val SCULK_POINTER = OdysseyItem("sculk_pointer", Material.COMPASS, "Sculk Pointer",
        lore = listOf(Component.text("This device points in the direction of the nearest Ancient City.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // Books
    val ARCANE_BOOK = OdysseyItem("arcane_book", Material.ENCHANTED_BOOK, "Arcane Book")

    val GILDED_BOOK = OdysseyItem("gilded_book", Material.ENCHANTED_BOOK, "Gilded Book")

    val BLANK_TOME = OdysseyItem("blank_tome", Material.BOOK, "Blank Tome",
        lore = listOf(Component.text("A special book that can be enchanted into tomes.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // Tomes
    val TOME_OF_DISCHARGE = OdysseyItem("tome_of_discharge", Material.ENCHANTED_BOOK, "Tome of Discharge",
        lore = listOf(Component.text("Removes one enchantment from the item.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))) // T1

    val TOME_OF_PROMOTION = OdysseyItem("tome_of_promotion", Material.ENCHANTED_BOOK, "Tome of Promotion",
        lore = listOf(Component.text("Increases the level of an enchantment by one, up to the max.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))) // T1

    val TOME_OF_HARMONY = OdysseyItem("tome_of_harmony", Material.ENCHANTED_BOOK, "Tome of Harmony",
        lore = listOf(Component.text("Resets the repair and combination cost of an item.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))) // T2

    val TOME_OF_BANISHMENT = OdysseyItem("tome_of_banishment", Material.ENCHANTED_BOOK, "Tome of Banishment",
        lore = listOf(Component.text("Removes an empty enchant slot from the item.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))) // T0

    val TOME_OF_EMBRACE = OdysseyItem("tome_of_embrace", Material.ENCHANTED_BOOK, "Tome of Embrace",
        lore = listOf(Component.text("Adds an empty enchant slot to the item.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))) // T1

    val TOME_OF_IMITATION = OdysseyItem("tome_of_imitation", Material.ENCHANTED_BOOK, "Tome of Imitation", // T2
        lore = listOf(Component.text("Copies one enchantment from an enchanted book.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val TOME_OF_EXPENDITURE = OdysseyItem("tome_of_expenditure", Material.ENCHANTED_BOOK, "Tome of Expenditure", // T2
        lore = listOf(Component.text("Extracts one enchantment from an item (the item is destroyed).", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val TOME_OF_AVARICE = OdysseyItem("tome_of_avarice", Material.ENCHANTED_BOOK, "Tome of Avarice", // T3
        lore = listOf(Component.text("Converts all enchantments to experience (the item is destroyed).", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val TOME_OF_REPLICATION = OdysseyItem("tome_of_replication", Material.ENCHANTED_BOOK, "Tome of Replication", // T3
        lore = listOf(Component.text("Replicates one book.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val TOME_OF_EUPHONY = OdysseyItem("tome_of_euphony", Material.ENCHANTED_BOOK, "Tome of Euphony", // T3
        lore = listOf(Component.text("[FINISH]", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val TOME_OF_POLYMERIZATION = OdysseyItem("tome_of_polymerization", Material.ENCHANTED_BOOK, "Tome of Polymerization", // T3
        lore = listOf(Component.text("Adds all stored enchantments with no experience cost.", LORE_GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    // Can absorb any non-conflicting enchantment.
    // Applies the stored enchantments to an item with no cost


    // Spirit Items
    // TO DIFFERENT TYPES OF BUFFING ITEMS
    // TOTEMS ARE FOUND -> More Potent / Effects can be spread
    // CHARMS ARE MADE -> Less Potent / Personal

    // Sniffer
    val ASPEN_SEED = OdysseyItem("aspen_seed", Material.WHEAT_SEEDS, "Aspen Seed")
    val MAPLE_SEED = OdysseyItem("maple_seed", Material.WHEAT_SEEDS, "Aspen Seed")
    val SAKURA_SEED = OdysseyItem("sakura_seed", Material.WHEAT_SEEDS, "Aspen Seed")
    val REDWOOD_SEED = OdysseyItem("redwood_seed", Material.WHEAT_SEEDS, "Aspen Seed")

    // Other
    val PRIMO_GEM = OdysseyItem( "primogem",Material.EMERALD,"Primogem",
        lore = listOf(
            Component.text("A primordial crystalline gem that's beyond", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("the test world. Shines with the condensed hopes", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text("and dreams of universes that once were.", TextColor.color(215, 215, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
    )

    // Unused [WIP]
    val CRYING_GOLD = OdysseyItem("crying_gold", Material.RAW_GOLD, "Crying Gold")
    val BREEZE_IN_A_BOTTLE = OdysseyItem("breeze_in_a_bottle", Material.GLASS_BOTTLE, "Breeze in a Bottle")
    val SOUL_CATALYST  = OdysseyItem("soul_catalyst", Material.AMETHYST_SHARD, "Soul Catalyst")

}


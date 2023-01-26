package me.shadowalzazel.mcodyssey.items.miscellaneous

import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.KnowledgeBookMeta


// ARCANE_BOOK
object ArcaneBook : OdysseyItem("Arcane Book",
    Material.BOOK,
    Component.text("Arcane Book", TextColor.color(191, 146, 239), TextDecoration.ITALIC),
    listOf(Component.text("Arcane Book", TextColor.color(191, 146, 239)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.ARCANE_BOOK)


// GILDED BOOK
object GildedBook : OdysseyItem("Gilded Book",
    Material.ENCHANTED_BOOK,
    Component.text("Gilded Book", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
    customModel = OdysseyItemModels.GILDED_BOOK) {
    fun createGildedBook(gildedEnchantment: OdysseyEnchantment, level: Int): ItemStack {
        val newGildedBook = ItemStack(Material.ENCHANTED_BOOK, 1)

        // Assign meta
        newGildedBook.itemMeta = newGildedBook.itemMeta.also {
            it.addEnchant(gildedEnchantment, level, true)
            it.setCustomModelData(OdysseyItemModels.GILDED_BOOK)
            it.displayName(odysseyDisplayName)
            val textLore = gildedEnchantment.enchantLore(level)
            it.lore(listOf(textLore.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
        }

        return newGildedBook
    }
}


// TOME OF DISCHARGE
object TomeOfDischarge : OdysseyItem("Tome Of Discharge",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Discharge", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Discharge", TextColor.color(193, 94, 54)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.TOME_OF_DISCHARGE)

// TOME OF PROMOTION
object TomeOfPromotion : OdysseyItem("Tome Of Promotion",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Promotion", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Promotion", TextColor.color(210, 255, 74)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.TOME_OF_PROMOTION)

// TOME OF HARMONY
object TomeOfHarmony : OdysseyItem("Tome Of Harmony",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Harmony", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Harmony", TextColor.color(237, 165, 247)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.TOME_OF_HARMONY)


// TOME OF HARMONY
object TomeOfEmbrace : OdysseyItem("Tome Of Embrace",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Embrace", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Embrace", TextColor.color(23, 221, 98)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.TOME_OF_EMBRACE)


// TOME OF BANISHMENT
object TomeOfBanishment : OdysseyItem("Tome Of Banishment",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Banishment", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Banishment", TextColor.color(23, 221, 98)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.TOME_OF_BANISHMENT)


// TOME OF REPLICATION
object TomeOfReplication : OdysseyItem("Tome Of Replication",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Replication", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Replication", TextColor.color(64, 128, 234)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.TOME_OF_REPLICATION)


// TOME OF EXPENDITURE
object TomeOfExpenditure : OdysseyItem("Tome Of Expenditure",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Expenditure", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Expenditure", TextColor.color(114, 176, 54)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.TOME_OF_EXPENDITURE)


// TOME OF EUPHONY
object TomeOfEuphony : OdysseyItem("Tome Of Euphony",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Euphony", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Euphony", TextColor.color(94, 210, 215)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.TOME_OF_EUPHONY)


// TOME OF AVARICE
object TomeOfAvarice : OdysseyItem("Tome Of Avarice",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Avarice", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Avarice", TextColor.color(212, 74, 74)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    OdysseyItemModels.TOME_OF_AVARICE)


// RECIPE BOOK
object WeaponEncyclopedia : OdysseyItem( "Weapon Encyclopedia",
    Material.KNOWLEDGE_BOOK,
    Component.text("Tome of Expenditure", TextColor.color(124, 155, 164), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Expenditure", TextColor.color(124, 155, 164)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))) {

    override fun createItemStack(amount: Int): ItemStack {
        val newBook =  super.createItemStack(amount)
        newBook.itemMeta = (newBook.itemMeta as KnowledgeBookMeta).also {
            it.recipes.addAll(setOf())
        }
        return newBook
    }

}
package me.shadowalzazel.mcodyssey.items.misc

import me.shadowalzazel.mcodyssey.enchantments.utility.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.assets.CustomModels
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
    CustomModels.ARCANE_BOOK)


// GILDED BOOK
object GildedBook : OdysseyItem("Gilded Book",
    Material.ENCHANTED_BOOK,
    Component.text("Gilded Book", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
    customModel = CustomModels.GILDED_BOOK) {
    fun createGildedBook(gildedEnchantment: OdysseyEnchantment, level: Int): ItemStack {
        val newGildedBook = ItemStack(Material.ENCHANTED_BOOK, 1)

        // Assign meta
        newGildedBook.itemMeta = newGildedBook.itemMeta.also {
            it.addEnchant(gildedEnchantment, level, true)
            it.setCustomModelData(CustomModels.GILDED_BOOK)
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
    CustomModels.TOME_OF_DISCHARGE)

// TOME OF PROMOTION
object TomeOfPromotion : OdysseyItem("Tome Of Promotion",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Promotion", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Promotion", TextColor.color(210, 255, 74)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.TOME_OF_PROMOTION)

// TOME OF REPLICATION
object TomeOfReplication : OdysseyItem("Tome Of Replication",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Replication", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Replication", TextColor.color(64, 128, 234)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.TOME_OF_REPLICATION)


// TOME OF HARMONY
object TomeOfHarmony : OdysseyItem("Tome Of Harmony",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Harmony", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Harmony", TextColor.color(237, 165, 247)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.TOME_OF_HARMONY)


// TOME OF HARMONY
object TomeOfEmbrace : OdysseyItem("Tome Of Embrace",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Embrace", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Embrace", TextColor.color(23, 221, 98)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.TOME_OF_EMBRACE)


// TOME OF HARMONY
object TomeOfBanishment : OdysseyItem("Tome Of Banishment",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Banishment", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Banishment", TextColor.color(23, 221, 98)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.TOME_OF_BANISHMENT)


// TOME OF INFUSION
object TomeOfExpenditure : OdysseyItem("Tome Of Expenditure",
    Material.ENCHANTED_BOOK,
    Component.text("Tome of Expenditure", TextColor.color(255, 170, 0), TextDecoration.ITALIC),
    listOf(Component.text("Tome of Expenditure", TextColor.color(114, 176, 54)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    CustomModels.TOME_OF_EXPENDITURE)


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
        /*
        NamespacedKey(MinecraftOdyssey.instance, "goldenkatana"),
                NamespacedKey(MinecraftOdyssey.instance, "ironclaymore"),
                NamespacedKey(MinecraftOdyssey.instance, "stonespear"),
                NamespacedKey(MinecraftOdyssey.instance, "woodendagger"),
                NamespacedKey(MinecraftOdyssey.instance, "diamondrapier"),
                NamespacedKey(MinecraftOdyssey.instance, "bonestaff")
         */

        return newBook
    }


}
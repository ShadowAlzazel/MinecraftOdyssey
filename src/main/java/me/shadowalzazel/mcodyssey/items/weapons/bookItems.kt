package me.shadowalzazel.mcodyssey.items.weapons

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.resources.CustomModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

object Necronomicon : OdysseyItem("Necronomicon",
    Material.WRITTEN_BOOK,
    odysseyDisplayName = Component.text("Necronomicon", TextColor.color(39, 19, 92)),
    odysseyLore = null,
    customModel = CustomModels.NECRONOMICON_BOOK) {

    override fun createItemStack(amount: Int): ItemStack {
        val necronomiconBook = super.createItemStack(amount)
        val necronomiconBookMeta: BookMeta = necronomiconBook.itemMeta.clone() as BookMeta
        val necronomiconBookBuilder = necronomiconBookMeta.toBuilder()

        // Author component
        val authorComponent: TextComponent = Component.text("Vail", TextColor.color(255, 170, 0)).decorate(TextDecoration.OBFUSCATED)
        necronomiconBookBuilder.author(authorComponent)

        // Title component
        val titleComponent: TextComponent = Component.text("Necronomicon", TextColor.color(39, 19, 92)).decorate(TextDecoration.ITALIC)
        necronomiconBookBuilder.title(titleComponent)

        // First Page
        val firstPage: TextComponent = Component.text("Summon Undead", TextColor.color(115, 13, 59)).decorate(TextDecoration.UNDERLINED)
            .append(Component.text("A spell capable of summoning the dead"))
            .append(Component.text("At a significant cost to the caster..."))
            .append(Component.text(""))
            .append(Component.text("CAST")
                .color(TextColor.color(115, 13, 59))
                .decorate(TextDecoration.STRIKETHROUGH, TextDecoration.BOLD)
                .clickEvent(ClickEvent.runCommand("/necronomicon summon"))
            )
        necronomiconBookBuilder.addPage(firstPage)

        // Build
        val newBookMeta = necronomiconBookBuilder.build()
        newBookMeta.setCustomModelData(CustomModels.NECRONOMICON_BOOK)
        newBookMeta.generation = BookMeta.Generation.TATTERED
        necronomiconBook.itemMeta = newBookMeta
        return necronomiconBook
    }
}
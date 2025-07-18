package me.shadowalzazel.mcodyssey.common.arcane

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Consumable
import io.papermc.paper.datacomponent.item.ItemLore
import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneRune
import me.shadowalzazel.mcodyssey.common.items.Item
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface RuneInscribing : RuneDataManager {



    fun smithingRuneInscribing(event: PrepareSmithingEvent) {
        // TODO: for now just deletes the pen
        val arcanePen = event.inventory.inputEquipment ?: return
        val ink = event.inventory.inputMineral ?: return
        val scrolls = event.inventory.inputTemplate ?: return
        // Check if items
        if (arcanePen.getItemNameId() != "arcane_pen") return
        if (scrolls.getItemNameId() != "scroll") return
        // Default set to AIR
        val spellScroll = Item.SPELL_SCROLL.newItemStack()
        event.result = ItemStack(Material.AIR)

        // Read runes from pen
        //val bundleContents = arcanePen.getData(DataComponentTypes.BUNDLE_CONTENTS) ?: return
        //val runeItems = bundleContents.contents()

        val penRunes = readBundledRunes(arcanePen) ?: return

        val runesToInscribe = mutableListOf<ArcaneRune>()
        val displayTextLore = mutableListOf<TextComponent>()
        var runeCount = 0
        for (r in penRunes) {
            runesToInscribe.add(r)
            displayTextLore.add(Component.text(r.displayName))
            runeCount++
        }

        val spellScrollCast = spellScroll.getData(DataComponentTypes.CONSUMABLE)!!
        val newCastConsumable = Consumable.consumable()
        newCastConsumable.animation(spellScrollCast.animation())
        newCastConsumable.sound(spellScrollCast.sound())
        newCastConsumable.consumeSeconds(runeCount * 0.4F)

        spellScroll.setData(DataComponentTypes.CONSUMABLE, newCastConsumable.build())


        storeInscribedRunes(spellScroll, runesToInscribe)
        spellScroll.setData(DataComponentTypes.LORE, ItemLore.lore(displayTextLore))

        event.result = spellScroll
        event.inventory.result = spellScroll
    }



}
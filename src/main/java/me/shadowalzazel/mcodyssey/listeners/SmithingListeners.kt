package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.ItemTags.ENGRAVED
import me.shadowalzazel.mcodyssey.constants.ItemTags.addTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.ItemStack

object SmithingListeners : Listener {

    // TODO: Handler upgrading tools using 1.20 smithing table!
    // Handle weapon quality

    private val AMETHYST_COLOR = TextColor.color(1141, 109, 209)

    @EventHandler
    fun smithingHandler(event: PrepareSmithingEvent) {
        val recipe = event.inventory.recipe ?: return

        // Avoid Conflict with enchanting tomes
        if (recipe.result.type == Material.ENCHANTED_BOOK) { return }
        // Check Equipment
        if (event.inventory.inputEquipment == null) { return }
        val equipment = event.inventory.inputEquipment!!
        // Make Sure Recipe has result
        if (event.result == null) { return }
        val eventResult = event.result!!

        // Engraving
        if (eventResult.type == Material.AMETHYST_SHARD) {
            val engraved = equipment.clone()
            if (engraved.hasTag(ENGRAVED)) {
                event.result = ItemStack(Material.AIR)
                return
            }
            engraved.also {
                it.addTag(ENGRAVED)
                val itemLore = it.lore()
                val forgerLore = mutableListOf(Component.text(""))
                for (viewer in event.viewers) {
                    forgerLore.add(Component.text("Created by ${viewer.name}",
                        AMETHYST_COLOR,
                        TextDecoration.ITALIC))
                }
                if (itemLore == null) {
                    it.lore(forgerLore as List<Component>?)
                }
                else {
                    //if (itemLore.contains(Component.text(""))
                    it.lore(itemLore + forgerLore)
                }
            }
            event.result = engraved
            return
        }

        // Check Recipes
        if (!recipe.result.hasItemMeta()) { return }
        if (!recipe.result.itemMeta.hasCustomModelData()) { return }
        // CURRENTLY DOES MC DOES NOT COPY RESULT NBT
        if (!eventResult.itemMeta.hasCustomModelData()) {
            event.inventory.result = recipe.result
            event.result = recipe.result
        }

    }



}
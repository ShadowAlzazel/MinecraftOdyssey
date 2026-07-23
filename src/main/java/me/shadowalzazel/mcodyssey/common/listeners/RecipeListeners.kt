package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.common.recipes.RecipeUnlocks
import me.shadowalzazel.mcodyssey.common.recipes.UnlockContext
import me.shadowalzazel.mcodyssey.common.recipes.UnlockTrigger
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.FurnaceExtractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRecipeDiscoverEvent
import org.bukkit.inventory.ItemStack

/**
 * Pure plumbing. All unlock logic lives in OdysseyUnlocks.
 * Delete any handler you don't want — the engine doesn't care which triggers exist.
 */
object RecipeListeners : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onRecipeDiscover(event: PlayerRecipeDiscoverEvent) {
        RecipeUnlocks.onRecipe(event.player, event.recipe, UnlockTrigger.DISCOVER)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onCraft(event: CraftItemEvent) {
        val player = event.whoClicked as? Player ?: return
        RecipeUnlocks.onItem(player, event.recipe.result, UnlockTrigger.CRAFT, event.recipe)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onSmelt(event: FurnaceExtractEvent) {
        RecipeUnlocks.onItem(
            event.player,
            ItemStack(event.itemType, event.itemAmount),
            UnlockTrigger.SMELT,
        )
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPickup(event: EntityPickupItemEvent) {
        val player = event.entity as? Player ?: return
        RecipeUnlocks.onItem(player, event.item.itemStack, UnlockTrigger.PICKUP)
    }

    /*
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlace(event: BlockPlaceEvent) {
        RecipeUnlocks.onItem(event.player, event.itemInHand, UnlockTrigger.MANUAL)
    }
     */

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        RecipeUnlocks.dispatch(
            UnlockContext(event.player, UnlockTrigger.JOIN, id = "join", namespace = "odyssey")
        )
    }
}
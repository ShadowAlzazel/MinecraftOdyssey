package me.shadowalzazel.mcodyssey.common.alchemy

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.api.AdvancementManager
import me.shadowalzazel.mcodyssey.common.alchemy.utility.CauldronRunner
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.event.block.CauldronLevelChangeEvent

interface CauldronManager : AdvancementManager {

    fun cauldronAlchemyHandler(event: CauldronLevelChangeEvent) {
        // Sentry Block
        val blockUnderneath = event.block.location.clone().subtract(0.0, 1.0, 0.0).block
        val validFuels = listOf(Material.FIRE, Material.CAMPFIRE, Material.SOUL_FIRE, Material.SOUL_CAMPFIRE)
        if (blockUnderneath.type !in validFuels) return
        val entitiesInside = blockUnderneath.world.getNearbyEntities(event.block.boundingBox)
        val entitiesAreItems = entitiesInside.all { it is Item }
        if (!entitiesAreItems) return
        // Items
        val items = entitiesInside.filterIsInstance<Item>().toMutableSet()
        // Advancement
        val players = event.block.location.getNearbyPlayers(3.0).toList()
        rewardAdvancement(players, "odyssey/cauldron_alchemy")
        // Run searcher
        asyncRecipeSearcher(items, blockUnderneath.type)
        return
    }


    private fun recipeFinder(items: MutableCollection<Item>, fuel: Material): CauldronAlchemyRecipe? {
        return CauldronRecipes.allRecipes.find {
            it.validateRecipe(items, fuel)
        }
    }

    // Async function to crete new scope and run check
    @OptIn(DelicateCoroutinesApi::class)
    private fun asyncRecipeSearcher(items: MutableCollection<Item>, fuel: Material) {
        GlobalScope.launch {
            val recipe = recipeFinder(items, fuel)
            if (recipe != null) CauldronRunner(recipe, items).runTask(Odyssey.instance)
        }
    }



}
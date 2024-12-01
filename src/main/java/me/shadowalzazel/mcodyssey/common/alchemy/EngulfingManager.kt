package me.shadowalzazel.mcodyssey.common.alchemy

import me.shadowalzazel.mcodyssey.api.AdvancementManager
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.event.entity.EntityCombustByBlockEvent

interface EngulfingManager : AdvancementManager {


    fun engulfingHandler(event: EntityCombustByBlockEvent) {
        if (event.combuster == null) return
        if (event.entity !is Item) return
        if (event.combuster!!.type != Material.SOUL_FIRE) return
        // Passed Checks
        val fire = event.combuster ?: return
        val item = event.entity as Item
        val recipe = EngulfingRecipes.allRecipes.find { it.validateRecipe(item, fire) }
        recipe?.successHandler(item, fire)
        if (recipe == null) return
        // Advancements
        val players = fire.location.getNearbyPlayers(3.0).toList()
        if (players.isEmpty()) return
        if (recipe == EngulfingRecipes.SOUL_STEEL_INGOT_RECIPE) {
            rewardAdvancement( players,"odyssey/convert_soul_steel")
        }

    }


}
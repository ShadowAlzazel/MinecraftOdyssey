package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.common.mobs.passive.TreasurePig.getItemNameId
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerRecipeDiscoverEvent

object RecipeListeners : Listener {

    @EventHandler
    fun unlockRecipeHandler(event: PlayerRecipeDiscoverEvent) {
        val recipeKey = event.recipe
        val player = event.player
        val namespace = recipeKey.namespace
        val keyStr = recipeKey.key
        val odysseyKey = { name: String -> NamespacedKey("odyssey", name) }

        // Only care about vanilla recipes
        if (namespace != "minecraft") return

        // Try to extract the base tool type from the recipe key
        // vanillaToolType will match for toolName, so the raw 'sword'
        val vanillaToolType = ToolType.getVanillaTypes().find { keyStr.contains(it.toolName) }
        val baseMaterial = ToolMaterial.entries.find { keyStr.contains(it.nameId) }

        // Unlock recipes if not null
        if (vanillaToolType != null && baseMaterial != null) {
            // Unlock all variants of this tool type that override the vanilla base
            ToolType.entries
                .filter { it.vanillaBase == vanillaToolType.vanillaBase && it != vanillaToolType } // matches the base tool
                .forEach { customTool ->
                    val customRecipeKey = "${baseMaterial.nameId}_${customTool.toolName}"
                    val toolRecipe = odysseyKey(customRecipeKey)
                    // Prevent recursive calls
                    if (!player.hasDiscoveredRecipe(toolRecipe)) {
                        player.discoverRecipe(toolRecipe)
                    }

                }
        }

        if (baseMaterial == ToolMaterial.DIAMOND) {
            val crystalAlloyIngotRecipe = odysseyKey("crystal_alloy_ingot")
            if (!player.hasDiscoveredRecipe(crystalAlloyIngotRecipe)) {
                player.discoverRecipe(crystalAlloyIngotRecipe)
            }
            val arcanePenRecipe = odysseyKey("arcane_pen")
            if (!player.hasDiscoveredRecipe(arcanePenRecipe)) {
                player.discoverRecipe(arcanePenRecipe)
            }
            val arcaneScepterIngotRecipe = odysseyKey("arcane_scepter")
            if (!player.hasDiscoveredRecipe(arcaneScepterIngotRecipe)) {
                player.discoverRecipe(arcaneScepterIngotRecipe)
            }
            val arcaneWandIngotRecipe = odysseyKey("arcane_wan")
            if (!player.hasDiscoveredRecipe(arcaneWandIngotRecipe)) {
                player.discoverRecipe(arcaneWandIngotRecipe)
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    fun craftItemRecipeHandler(event: CraftItemEvent) {
        val itemResult = event.recipe.result
        val player = event.whoClicked
        val keyStr = itemResult.getItemNameId()
        val odysseyKey = { name: String -> NamespacedKey("odyssey", name) }

        // Try to extract the base tool type from the recipe key
        // vanillaToolType will match for toolName, so the raw 'sword'
        val vanillaToolType = ToolType.getVanillaTypes().find { keyStr.contains(it.toolName) }
        val baseMaterial = ToolMaterial.entries.find { keyStr.contains(it.nameId) }

        // Unlock recipes if not null
        if (vanillaToolType != null && baseMaterial != null) {
            // Unlock all variants of this tool type that override the vanilla base
            ToolType.entries
                .filter { it.vanillaBase == vanillaToolType.vanillaBase && it != vanillaToolType } // matches the base tool
                .forEach { customTool ->
                    val customRecipeKey = "${baseMaterial.nameId}_${customTool.toolName}"
                    val toolRecipe = odysseyKey(customRecipeKey)
                    // Prevent recursive calls
                    if (!player.hasDiscoveredRecipe(toolRecipe)) {
                        player.discoverRecipe(toolRecipe)
                    }
                }

        }

    }

}
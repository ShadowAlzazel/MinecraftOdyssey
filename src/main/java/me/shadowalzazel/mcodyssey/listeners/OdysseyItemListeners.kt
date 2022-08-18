package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.listeners.utility.UnstableAntimatterTask
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import me.shadowalzazel.mcodyssey.recipes.OdysseyRecipes
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

object OdysseyItemListeners : Listener {

    // Main Function dealing with Odyssey Crafting
    @EventHandler
    fun odysseyCrafting(event: CraftItemEvent) {
        if (event.whoClicked is Player) {
            val somePlayer = event.whoClicked as Player
            if (somePlayer.gameMode != GameMode.SPECTATOR) {
                // Match
                when (event.inventory.result) {
                    OdysseyRecipes.PURE_ANTIMATTER_CRYSTAL_RECIPE.result -> {
                        pureAntiMatterCrystalCrafting(somePlayer)
                    }
                    OdysseyRecipes.FRUIT_OF_ERISHKIGAL_RECIPE.result -> {
                        fruitOfErishkigalCrafting(somePlayer)
                    }
                }
            }
        }
    }

    // Main function when dealing with custom items and their effects and not food
    @EventHandler
    fun consumingItem(event: PlayerItemConsumeEvent) {
        if (event.item.hasItemMeta()) {
            if (event.item.itemMeta.hasLore()) {
                val somePlayer = event.player
                val someStackValue = event.item.amount
                // Match items
                when (event.item) {
                    // Fruit Of Erishkigal Health Boosts
                    OdysseyItems.FRUIT_OF_ERISHKIGAL.createItemStack(someStackValue) -> {
                        // Get
                        val playerHealth = somePlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!
                        var hasExtraHealth = false
                        var someHealthModifier: AttributeModifier? = null
                        // Check modifiers
                        for (someModifier in playerHealth.modifiers) {
                            if (someModifier.name == "odyssey_extra_health_erishkigal") {
                                hasExtraHealth = true
                                someHealthModifier = someModifier
                            }
                        }
                        // Check if same boosts
                        if (hasExtraHealth) {
                            val healthCount = someHealthModifier!!.amount
                            if (healthCount <= 16.0) {
                                val erishkigalHealthBoost = AttributeModifier(UUID.fromString("c994412e-9e72-4881-a55f-1f2d1c95f125"), "odyssey_extra_health_erishkigal", 4.0 + healthCount, AttributeModifier.Operation.ADD_NUMBER)
                                playerHealth.removeModifier(someHealthModifier)
                                playerHealth.addModifier(erishkigalHealthBoost)
                                // Sound
                                somePlayer.playSound(somePlayer.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.5F, 0.25F)
                                somePlayer.playSound(somePlayer.location, Sound.BLOCK_BEACON_ACTIVATE, 2.5F, 0.25F)
                            }
                            else {
                                somePlayer.sendMessage("${ChatColor.GOLD}You can not consume any more of this substance...")
                                event.isCancelled = true
                            }
                        }
                        else {
                            val erishkigalHealthBoost = AttributeModifier(UUID.fromString("c994412e-9e72-4881-a55f-1f2d1c95f125"), "odyssey_extra_health_erishkigal", 4.0, AttributeModifier.Operation.ADD_NUMBER)
                            playerHealth.addModifier(erishkigalHealthBoost)
                            // Sound
                            somePlayer.playSound(somePlayer.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.5F, 0.25F)
                            somePlayer.playSound(somePlayer.location, Sound.BLOCK_BEACON_ACTIVATE, 2.5F, 0.25F)
                        }
                        println(playerHealth.modifiers)
                    }
                    //
                    else -> {
                    }
                }
            }
        }
    }


    @EventHandler
    fun leftCraftingAntimatter(event: PlayerQuitEvent) {
        val somePlayer = event.player
        if ("Unstable_Crafting" in somePlayer.scoreboardTags) {
            // add remove
            somePlayer.scoreboardTags.remove("Unstable_Crafting")
            somePlayer.damage(314.15)
            println("Tried to leave!")
        }
    }

    // PURE_ANTI_MATTER_CRYSTAL_RECIPE_CRAFTING
    private fun pureAntiMatterCrystalCrafting(eventPlayer: Player) {
        eventPlayer.playSound(eventPlayer.location, Sound.ENTITY_WITHER_SPAWN, 1.5F, 0.2F)
        if ("Unstable_Crafting" !in eventPlayer.scoreboardTags) {
            eventPlayer.scoreboardTags.add("Unstable_Crafting")
            val antimatterCraftingTask = UnstableAntimatterTask(eventPlayer)
            antimatterCraftingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 1)
        }
    }

    // FRUIT_OF_ERISHKIGAL_RECIPE_CRAFTING
    private fun fruitOfErishkigalCrafting(eventPlayer: Player) {
        eventPlayer.playSound(eventPlayer.location, Sound.ENTITY_ENDER_EYE_DEATH, 1.5F, 0.15F)
        if ("Unstable_Crafting" in eventPlayer.scoreboardTags) {
            eventPlayer.scoreboardTags.remove("Unstable_Crafting")
            eventPlayer.scoreboardTags.add("Clear_Instability")
        }

    }
}
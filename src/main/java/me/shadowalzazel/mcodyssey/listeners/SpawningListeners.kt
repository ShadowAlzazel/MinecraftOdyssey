package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.mobs.neutral.DubiousDealer
import me.shadowalzazel.mcodyssey.recipe_creators.merchant.Sales
import org.bukkit.entity.EntityType
import org.bukkit.entity.WanderingTrader
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent

object SpawningListeners : Listener {

    @EventHandler
    fun mobSpawningHandler(event: CreatureSpawnEvent) {
        if (event.spawnReason != CreatureSpawnEvent.SpawnReason.NATURAL) { return }

        when(event.entity.type) {
            EntityType.WANDERING_TRADER -> {
                // TEMP
                if (3 >= (1..10).random()) {
                    DubiousDealer.createMob(event.location.world, event.location)
                }
                else {
                    (event.entity as WanderingTrader).apply {
                        setRecipe(recipeCount + 1, Sales.createArcaneBookTrade())
                    }
                }
            }
            else -> {

            }
        }


    }


}
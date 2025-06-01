package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.datagen.RecipeManager
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.VillagerAcquireTradeEvent

object VillagerListeners : Listener {

    @EventHandler
    fun villagerLevelUpHandler(event: VillagerAcquireTradeEvent) {
        val villager = if (event.entity is Villager) event.entity as Villager else return
        println("Villager Level Up ${villager.profession} from" +
                " (${villager.villagerLevel - 1}) to (${villager.villagerLevel})")
        when (villager.profession) {
            Villager.Profession.WEAPONSMITH -> weaponSmithVillagerHandler(event)
        }
    }

    private fun weaponSmithVillagerHandler(event: VillagerAcquireTradeEvent) {
        val villager = event.entity as Villager
        // TEMP TODO -> Make this a weighted method later
        if ((0..10).random() <= 3) {
            println("Changing Recipe to part Upgrade")
            event.recipe = RecipeManager.merchantRecipes.createPartUpgradeTemplateTrade()
        }

        // Rank
        if (villager.villagerLevel == 3) {   // Apprentice -> Journeyman
            if ((0..10).random() <= 5) {
                println("Changing Recipe to Iron Weapon")
                event.recipe = RecipeManager.merchantRecipes.createIronWeaponTrade()
            }
        }
        else if (villager.villagerLevel == 4) { // Journeyman -> Expert
            if ((0..10).random() <= 5) {
                println("Changing Recipe to Customized Iron Weapon")
                val viableParts = listOf(listOf("blade", "hilt").random()) // Just 1 part
                event.recipe = RecipeManager.merchantRecipes.createCustomizedIronWeaponTrade(viableParts)
            }
        }
        else if (villager.villagerLevel == 5) { // Expert -> Master
            if ((0..10).random() <= 6) {
                println("Changing Recipe to Customized Diamond Weapon")
                val viableParts = listOf("blade", "pommel", "hilt")
                event.recipe = RecipeManager.merchantRecipes.createCustomizedDiamondWeaponTrade(viableParts)
            }
        }


    }


}
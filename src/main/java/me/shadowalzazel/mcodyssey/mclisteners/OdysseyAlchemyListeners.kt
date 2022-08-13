package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.alchemy.AlchemyPotions
import me.shadowalzazel.mcodyssey.alchemy.AlchemyRecipes
import me.shadowalzazel.mcodyssey.alchemy.utility.OdysseyPotion
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.data.Levelled
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.CauldronLevelChangeEvent
import org.bukkit.event.entity.PotionSplashEvent
import org.bukkit.event.inventory.BrewEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType

object OdysseyAlchemyListeners : Listener {

    @EventHandler
    fun bottleCauldron(event: CauldronLevelChangeEvent) {
        if (event.entity is Player) {
            val somePlayer = event.entity as Player
            if (event.reason == CauldronLevelChangeEvent.ChangeReason.BOTTLE_EMPTY || event.reason == CauldronLevelChangeEvent.ChangeReason.BUCKET_EMPTY) {
                val someCauldron = event.block
                // Check if Empty
                if (event.newState.type == Material.WATER_CAULDRON) {
                    val cauldronLocation = someCauldron.location
                    val blockUnderneath = cauldronLocation.clone().subtract(0.0, 1.0, 0.0).block.type
                    // Maybe campfire
                    if (blockUnderneath == Material.FIRE || blockUnderneath == Material.SOUL_FIRE) {
                        val nearbyEntities = someCauldron.world.getNearbyEntities(someCauldron.boundingBox)
                        if (nearbyEntities.size < 5) {
                            for (someRecipe in AlchemyRecipes.alchemyRecipeSet) {
                                val validated: Boolean = someRecipe.validateRecipe(nearbyEntities, blockUnderneath)
                                if (validated) {
                                    somePlayer.playSound(somePlayer.location, Sound.ITEM_BOTTLE_FILL, 2.5F, 0.8F)
                                    val newState = event.newState.blockData as Levelled
                                    println(newState.level)
                                    break
                                }
                            }
                        }
                        println(nearbyEntities)
                    }
                }
            }
        }
    }


    @EventHandler
    fun brewingPotion(event: BrewEvent) {
        if (event.contents.ingredient!!.type == Material.GUNPOWDER || event.contents.ingredient!!.type == Material.DRAGON_BREATH) {

            val ingredientType = if (event.contents.ingredient!!.type == Material.GUNPOWDER) Material.SPLASH_POTION else Material.LINGERING_POTION
            val brewerSlots = event.contents.contents!!

            for (x in 0..2) {
                if (brewerSlots[x] != null) {
                    if (brewerSlots[x]!!.type != Material.AIR) {
                        val someBrewedPotion = brewerSlots[x]!!
                        for (someOdysseyPotion: OdysseyPotion in AlchemyPotions.potionSet) {
                            if (someOdysseyPotion.odysseyDisplayName == someBrewedPotion.itemMeta.displayName) {
                                val somePotionMeta = someBrewedPotion.itemMeta as PotionMeta
                                if (somePotionMeta.basePotionData.type != PotionType.UNCRAFTABLE) {
                                    val newPotion = ItemStack(ingredientType, 1)
                                    val newPotionMeta: PotionMeta = someBrewedPotion.itemMeta as PotionMeta
                                    newPotion.itemMeta = newPotionMeta
                                    event.results[x] = newPotion
                                }
                                else {
                                    val newPotion = ItemStack(ingredientType, 1)
                                    val newPotionMeta: PotionMeta = someBrewedPotion.itemMeta as PotionMeta
                                    newPotion.itemMeta = newPotionMeta
                                    event.results[x] = newPotion
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun tippedArrowTableInteract(event: PlayerInteractEvent) {
        if (event.material == Material.FLETCHING_TABLE) {
            println("Arrow Tip")
        }
    }


    @EventHandler
    fun alchemyPotionSplash(event: PotionSplashEvent) {

    }
}
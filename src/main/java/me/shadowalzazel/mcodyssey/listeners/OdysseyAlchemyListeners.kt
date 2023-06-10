package me.shadowalzazel.mcodyssey.listeners

import kotlinx.coroutines.*
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.alchemy.AlchemyManager
import me.shadowalzazel.mcodyssey.alchemy.CauldronRecipes
import me.shadowalzazel.mcodyssey.alchemy.base.AlchemyCauldronRecipe
import me.shadowalzazel.mcodyssey.alchemy.utility.CauldronEventSynchro
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasOdysseyTag
import me.shadowalzazel.mcodyssey.effects.*
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Arrow
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.CauldronLevelChangeEvent
import org.bukkit.event.entity.AreaEffectCloudApplyEvent
import org.bukkit.event.entity.LingeringPotionSplashEvent
import org.bukkit.event.entity.PotionSplashEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.inventory.BrewEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionType
import org.bukkit.scheduler.BukkitRunnable

object OdysseyAlchemyListeners : Listener, AlchemyManager {


    // TODO: Fix bug if multiple types, converts all
    // If Handler for materials and types
    private fun brewingHandler(brewingIngredient: Material, brewerPotions: List<ItemStack?>): Map<Int, ItemStack> {
        val brewedPotions = mutableMapOf<Int, ItemStack>()
        val resultType = if (brewingIngredient == Material.GUNPOWDER) {
            Material.SPLASH_POTION
        } else {
            Material.LINGERING_POTION
        }
        for (x in 0..2) {
            val itemAtX = brewerPotions[x]
            if (itemAtX == null) {
                brewedPotions[x] = ItemStack(Material.AIR)
            } else if (!(itemAtX.itemMeta.persistentDataContainer.hasOdysseyTag())) {
                if (brewingIngredient == Material.REDSTONE) {
                    itemAtX.itemMeta.setCustomModelData(ItemModels.SQUARE_BOTTLE)
                }
            } else if (itemAtX.itemMeta.hasCustomModelData()) {/*
                val potionModel = itemAtX.itemMeta.customModelData
                if (potionModel == ItemModels.CONICAL_POTION) {
                    brewedPotions[x] = createOdysseyEffectPotion(resultType, itemAtX)
                    println("This Happened!")
                    println("$x $brewedPotions")
                }
                else {
                    brewedPotions[x] = createCustomPotion(resultType, itemAtX)
                }
                */
                if (resultType == Material.LINGERING_POTION) {
                    brewedPotions[x] = createLingeringPotion(resultType, itemAtX)
                } else {
                    brewedPotions[x] = createCustomPotion(resultType, itemAtX)
                }
            } else if (brewingIngredient == Material.HONEY_BOTTLE) {
                // FOR STICKY POTION
            } else {
                brewedPotions[x] = createCustomPotion(resultType, itemAtX)
            }
        }
        return brewedPotions
    }


    @EventHandler
    fun brewPotionHandler(event: BrewEvent) {
        if (event.contents.ingredient == null) return
        // Function to async
        val newPotionMap = brewingHandler(event.contents.ingredient!!.type, event.contents.contents.toList())
        //for (brew in newPotionMap) { event.contents.setItem(brew.key, brew.value) }
        for (brew in newPotionMap) {
            event.results[brew.key] = brew.value
        }
    }


    // Simple finder function for async class
    private suspend fun asyncCauldronRecipeFinder(someItems: MutableCollection<Item>, someFuel: Material): AlchemyCauldronRecipe? {
        return CauldronRecipes.CAULDRON_SET.find {
            it.validateRecipe(someItems, someFuel)
        }
    }


    // Async function to crete new scope and run check
    @OptIn(DelicateCoroutinesApi::class)
    private fun asyncCauldronHandler(cauldronItems: MutableCollection<Item>, cauldronFuel: Material) {
        GlobalScope.launch {
            val someRecipe = asyncCauldronRecipeFinder(cauldronItems, cauldronFuel)
            if (someRecipe != null) {
                val synchroCauldronTask: BukkitRunnable = CauldronEventSynchro(someRecipe, cauldronItems)
                synchroCauldronTask.runTask(Odyssey.instance)
            }
        }
    }


    // Handler for cauldron mechanic
    @EventHandler
    fun cauldronWateredHandler(event: CauldronLevelChangeEvent) {
        // Sentry Clauses
        if (event.entity !is Player) return
        if (event.newState.type != Material.WATER_CAULDRON) return
        if (!(event.reason == CauldronLevelChangeEvent.ChangeReason.BOTTLE_EMPTY || event.reason == CauldronLevelChangeEvent.ChangeReason.BUCKET_EMPTY)) return
        val blockUnderneath = event.block.location.clone().subtract(0.0, 1.0, 0.0).block
        if (!(blockUnderneath.type == Material.FIRE || blockUnderneath.type == Material.SOUL_FIRE)) return
        val entitiesInside = blockUnderneath.world.getNearbyEntities(event.block.boundingBox)
        val allItems = entitiesInside.all { it is Item }
        if (!allItems) return
        val itemList = entitiesInside.filterIsInstance<Item>().toMutableSet()
        // Run new async
        asyncCauldronHandler(itemList, blockUnderneath.type)
    }


    // TODO: Reapply effects again
    @EventHandler
    fun alchemyPotionConsumeHandler(event: PlayerItemConsumeEvent) {
        if (event.item.hasItemMeta() && event.item.type == Material.POTION) {
            val potionMeta = event.item.itemMeta as PotionMeta
            if (potionMeta.persistentDataContainer.hasOdysseyTag() && potionMeta.basePotionData.type == PotionType.UNCRAFTABLE) {
                val potionName = potionMeta.persistentDataContainer[NamespacedKey(Odyssey.instance, "item"), PersistentDataType.STRING]!!
                potionEffectAssigner(mutableListOf(event.player), listOf(potionName), false)
            }
            // Check if custom bottle
            if (potionMeta.hasCustomModelData()) {
                event.replacement = ItemStack(Material.GLASS_BOTTLE, 1).also {
                    it.itemMeta.setCustomModelData(potionMeta.customModelData)
                }
            }
        }
    }


    // Main function regarding splash potions
    @EventHandler
    fun alchemyPotionSplashHandler(event: PotionSplashEvent) {
        val potionMeta = event.entity.item.itemMeta as PotionMeta
        if (potionMeta.persistentDataContainer.hasOdysseyTag() && potionMeta.basePotionData.type == PotionType.UNCRAFTABLE) {
            val potionName = potionMeta.persistentDataContainer[NamespacedKey(Odyssey.instance, "item"), PersistentDataType.STRING]!!
            potionEffectAssigner(event.affectedEntities, listOf(potionName), false)
        }
    }


    // Main function regarding lingering potions
    @EventHandler
    fun alchemyLingeringSplashHandler(event: LingeringPotionSplashEvent) {
        val potionMeta = event.entity.item.itemMeta as PotionMeta
        if (potionMeta.persistentDataContainer.hasOdysseyTag() && potionMeta.basePotionData.type == PotionType.UNCRAFTABLE) {
            val potionName = potionMeta.persistentDataContainer[NamespacedKey(Odyssey.instance, "item"), PersistentDataType.STRING]!!
            createEffectCloud(event.entity, event.areaEffectCloud, potionName)
        }
    }


    // Main function for detecting entity clouds from alchemy potions
    @EventHandler
    fun alchemyEffectCloudHandler(event: AreaEffectCloudApplyEvent) {
        if (event.entity.scoreboardTags.contains(EntityTags.TIMED_CLOUD)) {
            potionEffectAssigner(event.affectedEntities, event.entity.scoreboardTags.toList(), true)
        }
    }


    // TODO: FIX
    fun tippedArrowTableInteract(event: PlayerInteractEvent) {
        if (event.clickedBlock != null) {
            if (event.action.isLeftClick && event.clickedBlock!!.type == Material.FLETCHING_TABLE) {
                event.player.equipment.also {
                    if (it.itemInMainHand.type == Material.ARROW && it.itemInOffHand.type == Material.LINGERING_POTION) {
                        val newArrow = ItemStack(Material.TIPPED_ARROW, 1).also { arrow ->
                            arrow.itemMeta = it.itemInOffHand.itemMeta as PotionMeta
                        }
                        event.player.inventory.addItem(newArrow)
                        it.itemInMainHand.subtract(1)

                        if (1 == (1..9).random()) {
                            it.setItemInOffHand(ItemStack(Material.AIR, 1))
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun arrowTableAlchemyHandler(event: PlayerInteractEvent) {
        if (event.clickedBlock == null) return
        if (!event.action.isLeftClick) return
        if (event.clickedBlock!!.type != Material.FLETCHING_TABLE) return
        val playerEquipment = event.player.equipment
        if (playerEquipment.itemInMainHand.type != Material.ARROW) return
        if (playerEquipment.itemInMainHand.amount < 8) return
        if (playerEquipment.itemInOffHand.type != Material.LINGERING_POTION) return // TODO: temp -> change to model data

        val tippedArrows = ItemStack(Material.TIPPED_ARROW, 1)
        tippedArrows.itemMeta = playerEquipment.itemInOffHand.itemMeta as PotionMeta

        playerEquipment.itemInMainHand.subtract(8)
        playerEquipment.itemInOffHand.subtract(1)
        event.player.inventory.addItem(tippedArrows)
        event.player.updateInventory()

    }



    //
    @EventHandler
    fun tippedArrowHit(event: ProjectileHitEvent) {
        // Checks if Arrow
        if (event.entity is Arrow) {
            val someArrow = event.entity as Arrow
            // Checks if meta
            if (someArrow.itemStack.type == Material.TIPPED_ARROW) {
                // checks if lore and display component
                if (someArrow.itemStack.itemMeta.hasLore() && someArrow.itemStack.itemMeta.hasDisplayName()) {
                    // TODO:
                }
            }
        }

    }

}

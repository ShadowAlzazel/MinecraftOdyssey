package me.shadowalzazel.mcodyssey.listeners

import kotlinx.coroutines.*
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.alchemy.AlchemyManager
import me.shadowalzazel.mcodyssey.alchemy.CauldronRecipes
import me.shadowalzazel.mcodyssey.alchemy.base.AlchemyCauldronRecipe
import me.shadowalzazel.mcodyssey.alchemy.utility.BrewingEventSynchro
import me.shadowalzazel.mcodyssey.alchemy.utility.CauldronEventSynchro
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.effects.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
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
import org.bukkit.potion.PotionType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType

object OdysseyAlchemyListeners : Listener, AlchemyManager {

    private val odysseyTimedPotions = listOf("Bottle o' Ablaze", "Bottle o' Frost", "Bottle o' Decay", "Bottle o' Douse", "Potion of Thorns", "Puffy n' Prickly Potion", "Poltergeist Brew")
    private val odysseyTimelessPotions = listOf("Bottled Souls")

    // Helper function that converts a char sequence (M:SS) to time Int
    private fun loreToSecInt(timeSequence: CharSequence): Int {
        val minute = timeSequence[1].toString().toInt()
        val tensSecond = timeSequence[3].toString().toInt()
        val onesSecond = timeSequence[4].toString().toInt()
        return (minute * 60) + (tensSecond * 10) + (onesSecond)
    }

    private fun timeToLoreChar(time: Int): CharSequence {
        val seconds = time % 60
        val minutes = time / 60
        return if (seconds < 9) { "($minutes:0$seconds)" } else { "($minutes:$seconds)" }
    }

    // Helper Coroutine Function for validating cauldron recipes
    private suspend fun validateCauldronRecipe(someItemSet: MutableSet<Item>, someFuelBlock: Material): AlchemyCauldronRecipe? = runBlocking {
        val recipeJob: Deferred<AlchemyCauldronRecipe?> = async {
            for (recipe in CauldronRecipes.CAULDRON_SET) {
                val validated: Boolean = recipe.validateRecipe(someItemSet, someFuelBlock)
                if (validated) {
                    return@async recipe
                }
            }
            return@async null
        }
       recipeJob.await()
    }

    // Helper Coroutine Function for brewing odyssey potions
    private fun brewCustomAlchemyPotion(brewerSlots: Array<ItemStack?>, ingredientMaterial: Material): MutableMap<Int, ItemStack?> = runBlocking {
        // Get Result
        val resultType = if (ingredientMaterial == Material.GUNPOWDER) { Material.SPLASH_POTION } else { Material.LINGERING_POTION }
        val brewingNewResults: MutableMap<Int, ItemStack?> = mutableMapOf()

        // Helper function for creating new potions
        fun createResultingPotion(brewingIndex: Int, oldPotion: ItemStack) {
            val newPotion = ItemStack(resultType, 1)
            newPotion.itemMeta = oldPotion.itemMeta as PotionMeta
            brewingNewResults[brewingIndex] = newPotion
        }

        // Helper function to create lingering timed potions
        fun createLingeringTimedPotion(brewingIndex: Int, oldPotion: ItemStack) {
            val newPotion = ItemStack(resultType, 1)
            // Modify potion meta
            newPotion.itemMeta = (oldPotion.itemMeta as PotionMeta).also {
                val oldTextComponent = it.lore()!!.first() as TextComponent
                val oldColor = oldTextComponent.color()
                val timerLore = oldTextComponent.content()
                val i = timerLore.lastIndex
                // Get the time in seconds form String in "(M:SS)" format
                val oldTime = loreToSecInt(timerLore.subSequence((i - 5)..i))
                // Create lore by getting old effect and adding new time
                val newTimerString = timerLore.subSequence(0..(i - 6)).toString() + timeToLoreChar(oldTime / 4)
                println("Old Time: $timerLore | New time: $newTimerString")

                val newTextComponent = Component.text(newTimerString, oldColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                val newLore = it.lore()!!
                newLore.remove(oldTextComponent)
                newLore.add(0, newTextComponent)
                it.lore(newLore)
            }
            brewingNewResults[brewingIndex] = newPotion
        }


        // Check each slot
        for (x in 0..2) {
            if (brewerSlots[x] != null) {
                val somePotion = brewerSlots[x]!!
                // Checks if potion has display name and display color
                if (somePotion.itemMeta.hasDisplayName() && somePotion.itemMeta.displayName()?.color() != null) {
                    // Get potion Meta
                    val somePotionMeta = somePotion.itemMeta as PotionMeta
                    // Checks if Potion has custom effects via un-craftable and lore
                    if (somePotionMeta.basePotionData.type == PotionType.UNCRAFTABLE && somePotionMeta.hasLore()) {
                        // Get Name
                        val potionName = (somePotionMeta.displayName() as TextComponent).content()
                        // Check if Lingering Potion to modify time values
                        if (resultType == Material.LINGERING_POTION) {
                            // Potions without timers
                            if (potionName in odysseyTimelessPotions) {
                                createResultingPotion(x, somePotion)
                            }
                            // Potions that have timers
                            else {
                                createLingeringTimedPotion(x, somePotion)
                            }
                        }
                        // Creates custom effect splash potion
                        else {
                            createResultingPotion(x, somePotion)
                        }
                    }
                    // Creates odyssey potion without custom effects
                    else {
                        createResultingPotion(x, somePotion)
                    }
                    println("Potion at Slot $x: ${brewingNewResults[x]}")
                }
            }
            else {
                brewingNewResults[x] = null
            }
        }

        //delay(1000)
        brewingNewResults
    }


    /*----------------------------------------------------------------------------------------------------------*/

    @EventHandler
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

                        if (1 == (1..9).random()) { it.setItemInOffHand(ItemStack(Material.AIR, 1)) }
                    }
                }
            }
        }
    }


    @EventHandler
    fun tippedArrowHit(event: ProjectileHitEvent) {
        // Checks if Arrow
        if (event.entity is Arrow) {
            val someArrow = event.entity as Arrow
            // Checks if meta
            if (someArrow.itemStack.type == Material.TIPPED_ARROW) {
                // checks if lore and display component
                if (someArrow.itemStack.itemMeta.hasLore() && someArrow.itemStack.itemMeta.hasDisplayName()) {

                }
            }
        }

    }


    // Main function for Cauldron recipes
    @OptIn(DelicateCoroutinesApi::class)
    @EventHandler
    fun bottleCauldron(event: CauldronLevelChangeEvent) {
        if (event.entity is Player) {
            if (event.reason == CauldronLevelChangeEvent.ChangeReason.BOTTLE_EMPTY || event.reason == CauldronLevelChangeEvent.ChangeReason.BUCKET_EMPTY) {
                val someCauldron = event.block
                // Check if Empty cauldron
                if (event.newState.type == Material.WATER_CAULDRON) {
                    val blockUnderneath = someCauldron.location.clone().subtract(0.0, 1.0, 0.0).block.type
                    // Checks the blocks underneath if fuel source
                    if (blockUnderneath == Material.FIRE || blockUnderneath == Material.SOUL_FIRE) {
                        // Get entities within cauldron
                        val nearbyEntities = someCauldron.world.getNearbyEntities(someCauldron.boundingBox)
                        // Launch Coroutine to check entities and items off main thread
                        GlobalScope.launch {
                            // Create coroutine variables
                            var allItems = true
                            val itemsToCheck: MutableSet<Item> = mutableSetOf()
                            // Checks if size less than 6
                            if (nearbyEntities.size < 6) {
                                // Checks if all entities are Items
                                for (someEntity in nearbyEntities) {
                                    if (someEntity is Item) {
                                        itemsToCheck.add(someEntity)
                                    }
                                    else {
                                        allItems = false
                                        break
                                    }
                                }
                            }
                            // Runs validation if all items
                            if (allItems) {
                                val validRecipe: AlchemyCauldronRecipe? = validateCauldronRecipe(itemsToCheck, blockUnderneath)
                                // If valid recipe then sync back to main thread
                                if (validRecipe != null) {
                                    val synchroCauldronTask: BukkitRunnable = CauldronEventSynchro(validRecipe, itemsToCheck)
                                    synchroCauldronTask.runTask(Odyssey.instance)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    // Main function regarding brewing stand upgrades
    // TODO: Fix
    @OptIn(DelicateCoroutinesApi::class)
    @EventHandler
    fun brewingPotion(event: BrewEvent) {
        if (event.contents.ingredient!!.type == Material.GUNPOWDER || event.contents.ingredient!!.type == Material.DRAGON_BREATH) {

            val brewingContents = event.contents.contents.clone()
            GlobalScope.launch {

                val newBrewingStandSlots = brewCustomAlchemyPotion(brewingContents, event.contents.ingredient!!.type)
                val synchroBrewingTask: BukkitRunnable = BrewingEventSynchro(newBrewingStandSlots, event.contents)
                synchroBrewingTask.runTask(Odyssey.instance)

            }
        }
    }


    // TODO: Reapply effects again
    @EventHandler
    fun alchemyPotionConsumeHandler(event: PlayerItemConsumeEvent) {
        if (event.item.hasItemMeta() && event.item.type == Material.POTION) {
            val potionMeta = event.item.itemMeta as PotionMeta
            if (potionMeta.persistentDataContainer.has(NamespacedKey(Odyssey.instance, "item")) && potionMeta.basePotionData.type == PotionType.UNCRAFTABLE) {
                val potionName = potionMeta.persistentDataContainer[NamespacedKey(Odyssey.instance, "item"), PersistentDataType.STRING]!!
                potionEffectAssigner(mutableListOf(event.player), listOf(potionName), false)
            }
        }
    }


    // Main function regarding splash potions
    @EventHandler
    fun alchemyPotionSplashHandler(event: PotionSplashEvent) {
        val potionMeta = event.entity.item.itemMeta as PotionMeta
        if (potionMeta.persistentDataContainer.has(NamespacedKey(Odyssey.instance, "item")) && potionMeta.basePotionData.type == PotionType.UNCRAFTABLE) {
            val potionName = potionMeta.persistentDataContainer[NamespacedKey(Odyssey.instance, "item"), PersistentDataType.STRING]!!
            potionEffectAssigner(event.affectedEntities, listOf(potionName), false)
        }
    }


    // Main function regarding lingering potions
    @EventHandler
    fun alchemyLingeringSplashHandler(event: LingeringPotionSplashEvent) {
        val potionMeta = event.entity.item.itemMeta as PotionMeta
        if (potionMeta.persistentDataContainer.has(NamespacedKey(Odyssey.instance, "item")) && potionMeta.basePotionData.type == PotionType.UNCRAFTABLE) {
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

}
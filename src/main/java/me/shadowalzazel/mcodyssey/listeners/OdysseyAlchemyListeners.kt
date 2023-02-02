package me.shadowalzazel.mcodyssey.listeners

import kotlinx.coroutines.*
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.alchemy.AlchemyRecipes
import me.shadowalzazel.mcodyssey.alchemy.base.AlchemyCauldronRecipe
import me.shadowalzazel.mcodyssey.effects.*
import me.shadowalzazel.mcodyssey.coroutine_synchronizers.BrewingEventSynchro
import me.shadowalzazel.mcodyssey.coroutine_synchronizers.CauldronEventSynchro
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.Particle
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
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import org.bukkit.scheduler.BukkitRunnable

object OdysseyAlchemyListeners : Listener {

    private val odysseyTimedPotions = listOf("Bottle o' Ablaze", "Bottle o' Frost", "Bottle o' Decay", "Bottle o' Douse", "Potion of Thorns", "Puffy n' Prickly Potion", "Poltergeist Brew")
    private val odysseyTimelessPotions = listOf("Bottled Souls")

    // Helper function that converts a char sequence (M:SS) to time Int
    private fun loreToSeconds(timeSequence: CharSequence): Int {
        val minute = timeSequence[1].toString().toInt()
        val decimalSecond = timeSequence[3].toString().toInt()
        val second = timeSequence[4].toString().toInt()
        return (minute * 60) + (decimalSecond * 10) + (second)
    }

    // Helper function that converts time to lore String
    private fun timeToLore(time: Int): CharSequence {
        val seconds = time % 60
        val minutes = time / 60
        // add 0 if less than 9
        return if (seconds < 9) { "($minutes:0$seconds)" } else { "($minutes:$seconds)" }
    }

    // Helper Coroutine Function for validating cauldron recipes
    private suspend fun validateCauldronRecipe(someItemSet: MutableSet<Item>, someFuelBlock: Material): AlchemyCauldronRecipe? = runBlocking {
        val recipeJob: Deferred<AlchemyCauldronRecipe?> = async {
            for (recipe in AlchemyRecipes.recipeSet) {
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
    private suspend fun brewCustomAlchemyPotion(brewerSlots: Array<ItemStack?>, ingredientMaterial: Material): MutableMap<Int, ItemStack?> = runBlocking {
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
                val oldTime = loreToSeconds(timerLore.subSequence((i - 5)..i))
                // Create lore by getting old effect and adding new time
                val newTimerString = timerLore.subSequence(0..(i - 6)).toString() + timeToLore(oldTime / 4)
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

    // Function to match potions to names
    private fun potionNameMatcher(somePotionMeta: PotionMeta, affectedEntities: MutableCollection<LivingEntity>) {
        // Get Name
        val potionName = (somePotionMeta.displayName() as TextComponent).content()
        // Check custom potion has a timer
        var potionDuration = 0
        if (potionName in odysseyTimedPotions) {
            // Logic
            val potionLore = (somePotionMeta.lore()!![0] as TextComponent).content()
            val i = potionLore.lastIndex
            val potionLoreTimer = potionLore.subSequence((i - 5)..i)
            potionDuration = loreToSeconds(potionLoreTimer)
        }
        // Match Names
        when (potionName) {
            "Bottle o' Decay" -> { OdysseyEffectsHandler.decayingEffect(affectedEntities, potionDuration, 1) }
            "Bottle o' Frost" -> { OdysseyEffectsHandler.freezingEffect(affectedEntities, potionDuration, 1) }
            "Bottle o' Douse" -> { OdysseyEffectsHandler.dousedEffect(affectedEntities, potionDuration, 2) }
            "Bottle o' Ablaze" -> { OdysseyEffectsHandler.ablazeEffect(affectedEntities, potionDuration, 2) }
            "Potion of Thorns" -> { OdysseyEffectsHandler.thornsEffect(affectedEntities, potionDuration) }
            "Puffy n' Prickly Potion" -> { OdysseyEffectsHandler.puffyPricklyEffect(affectedEntities, potionDuration) }
            "Poltergeist Brew" -> { OdysseyEffectsHandler.accursedEffect(affectedEntities, potionDuration) }
            "Bottled Souls" -> { OdysseyEffectsHandler.soulDamageEffect(affectedEntities, 1) }
            else -> {
            }
        }
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


    @EventHandler
    fun potionConsume(event: PlayerItemConsumeEvent) {
        if (event.item.hasItemMeta() && event.item.type == Material.POTION) {
            val somePotion = event.item
            // Checks if potion has lore and display component
            if (somePotion.itemMeta.hasLore() && somePotion.itemMeta.hasDisplayName()) {
                // Get potion Meta
                val somePotionMeta = somePotion.itemMeta as PotionMeta
                // Checks if Potion Meta Un-craftable and Colored Name
                if (somePotionMeta.basePotionData.type == PotionType.UNCRAFTABLE && somePotionMeta.displayName()?.color() != null) {
                    val somePlayer = event.player
                    val potionName = (somePotionMeta.displayName() as TextComponent).content()
                    // Check custom potion has a timer
                    var potionDuration = 0
                    if (potionName in odysseyTimedPotions) {
                        // Logic
                        val potionLore = (somePotionMeta.lore()!![0] as TextComponent).content()
                        val i = potionLore.lastIndex
                        val potionLoreTimer = potionLore.subSequence((i - 5)..i)
                        potionDuration = loreToSeconds(potionLoreTimer)
                    }
                    // Match Names
                    // TODO: Reapply
                    when (potionName) {
                        "Bottle o' Decay" -> { OdysseyEffectsHandler.decayingEffect(mutableListOf(somePlayer), potionDuration, 1) }
                        "Bottle o' Frost" -> { OdysseyEffectsHandler.freezingEffect(mutableListOf(somePlayer), potionDuration, 1) }
                        "Bottle o' Douse" -> { OdysseyEffectsHandler.dousedEffect(mutableListOf(somePlayer), potionDuration, 2) }
                        "Bottle o' Ablaze" -> { OdysseyEffectsHandler.ablazeEffect(mutableListOf(somePlayer), potionDuration, 2) }
                        "Potion of Thorns" -> { OdysseyEffectsHandler.thornsEffect(mutableListOf(somePlayer), potionDuration) }
                        "Puffy n' Prickly Potion" -> { OdysseyEffectsHandler.puffyPricklyEffect(mutableListOf(somePlayer), potionDuration) }
                        "Poltergeist Brew" -> { OdysseyEffectsHandler.accursedEffect(mutableListOf(somePlayer), potionDuration) }
                        "Bottled Souls" -> { OdysseyEffectsHandler.soulDamageEffect(mutableListOf(somePlayer), 1) }
                        else -> {
                        }
                    }


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
                                    synchroCauldronTask.runTask(MinecraftOdyssey.instance)
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
                synchroBrewingTask.runTask(MinecraftOdyssey.instance)

            }
        }
    }


    // Main function regarding splash potions
    @EventHandler
    fun alchemyPotionSplash(event: PotionSplashEvent) {
        if (event.potion.item.hasItemMeta()) {
            val somePotion = event.potion.item
            // Checks if potion has lore and display component
            if (somePotion.itemMeta.hasLore() && somePotion.itemMeta.hasDisplayName()) {
                // Get potion Meta
                val somePotionMeta = somePotion.itemMeta as PotionMeta
                // Checks if Potion Meta Un-craftable and Colored Name
                if (somePotionMeta.basePotionData.type == PotionType.UNCRAFTABLE && somePotionMeta.displayName()?.color() != null) {
                    // Call function
                    potionNameMatcher(somePotionMeta, event.affectedEntities)
                }
            }
        }

    }


    // Main function regarding lingering potions
    @EventHandler
    fun alchemyPotionLingering(event: LingeringPotionSplashEvent) {
        // Checks if potion has item meta and lore
        if (event.entity.item.hasItemMeta() && event.entity.item.itemMeta?.hasLore() == true) {
            // Gets Potion Meta
            val somePotionMeta = event.entity.item.itemMeta as PotionMeta
            // Checks if Potion Meta Un-craftable and Colored Name
            if (somePotionMeta.basePotionData.type == PotionType.UNCRAFTABLE && somePotionMeta.displayName()?.color() != null) {
                // Get Name
                val potionName = (somePotionMeta.displayName() as TextComponent).content()

                // Check if Potion has timer
                var potionDuration = 0
                if (potionName in odysseyTimedPotions) {
                    // Logic
                    val potionLore = (somePotionMeta.lore()!!.first() as TextComponent).content()
                    val i = potionLore.lastIndex
                    val potionLoreTimer = potionLore.subSequence((i - 5)..i)
                    potionDuration = loreToSeconds(potionLoreTimer)
                }

                // Potion Cloud modifiers
                var tagToAdd: String? = null
                var applicationDelay = 20
                var customEffectToAdd: PotionEffect? = null
                // Match names
                when (potionName) {
                    "Bottle o' Decay" -> {
                        customEffectToAdd = PotionEffect(PotionEffectType.HUNGER, potionDuration * 20, 0)
                        applicationDelay = 40
                        tagToAdd = "Decaying_Cloud"
                    }
                    "Bottle o' Frost" -> {
                        customEffectToAdd = PotionEffect(PotionEffectType.SLOW, potionDuration * 20, 0)
                        tagToAdd = "Frost_Cloud"
                    }
                    "Bottle o' Douse" -> {
                        customEffectToAdd = PotionEffect(PotionEffectType.UNLUCK, potionDuration * 20, 0)
                        tagToAdd = "Douse_Cloud"
                    }
                    "Bottle o' Ablaze" -> {
                        customEffectToAdd = PotionEffect(PotionEffectType.WEAKNESS, potionDuration * 20, 0)
                        tagToAdd = "Blazing_Cloud"
                    }
                    "Potion of Thorns" -> {
                        customEffectToAdd = PotionEffect(PotionEffectType.UNLUCK, potionDuration * 20, 0)
                        tagToAdd = "Thorns_Cloud"
                    }
                    "Puffy n' Prickly Potion" -> {
                        customEffectToAdd = PotionEffect(PotionEffectType.UNLUCK, potionDuration * 20, 0)
                        tagToAdd = "Puffy_Prickly_Cloud"
                    }
                    "Bottled Souls" -> {
                        customEffectToAdd = PotionEffect(PotionEffectType.UNLUCK, potionDuration * 20, 0)
                        tagToAdd = "Escaping_Souls_Cloud"
                    }
                    "Poltergeist Brew" -> {
                        customEffectToAdd = PotionEffect(PotionEffectType.UNLUCK, potionDuration * 20, 0)
                        tagToAdd = "Poltergeist_Accursed_Cloud"
                    }
                    else -> {
                    }
                }
                // Add Tags and Effects to potion cloud
                if (tagToAdd != null) {
                    (event.areaEffectCloud).also {
                        somePotionMeta.addCustomEffect(customEffectToAdd!!, true)
                        it.addCustomEffect(customEffectToAdd, true)
                        it.duration = 20 * 20
                        it.reapplicationDelay = applicationDelay
                        it.addScoreboardTag(tagToAdd)
                        it.addScoreboardTag("Timed_Cloud")
                    }
                }
            }
        }
    }


    // Main function for detecting entity clouds from alchemy potions
    @EventHandler
    fun alchemyPotionCloud(event: AreaEffectCloudApplyEvent) {
        val somePotionCloud = event.entity
        // For now base timers then string manipulation
        for (tag in somePotionCloud.scoreboardTags) {
            when (tag) {
                "Decaying_Cloud" -> { OdysseyEffectsHandler.decayingEffect(event.affectedEntities, (30 / 4) / 2, 1) }
                "Frost_Cloud" -> { OdysseyEffectsHandler.freezingEffect(event.affectedEntities, 30 / 4, 1) }
                "Douse_Cloud" -> { OdysseyEffectsHandler.dousedEffect(event.affectedEntities, 40 / 4, 2) }
                "Blazing_Cloud" -> { OdysseyEffectsHandler.ablazeEffect(event.affectedEntities, 30 / 4, 2) }
                "Potion of Thorns" -> { OdysseyEffectsHandler.thornsEffect(event.affectedEntities, 50 / 4) }
                "Puffy_Prickly_Cloud" -> { OdysseyEffectsHandler.puffyPricklyEffect(event.affectedEntities, 40 / 4) }
                "Escaping_Souls_Cloud" -> { OdysseyEffectsHandler.soulDamageEffect(event.affectedEntities, 1)
                    with(somePotionCloud) { world.spawnParticle(Particle.SCULK_SOUL, location, (radius * 40).toInt(), radius.toDouble(), 0.25, radius.toDouble()) }
                }
                "Poltergeist_Accursed_Cloud" -> { OdysseyEffectsHandler.accursedEffect(event.affectedEntities, 30 / 4) }
                else -> {
                }
            }
        }
    }

}
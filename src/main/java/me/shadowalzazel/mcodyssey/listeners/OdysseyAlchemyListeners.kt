package me.shadowalzazel.mcodyssey.listeners

import kotlinx.coroutines.*
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.alchemy.AlchemyRecipes
import me.shadowalzazel.mcodyssey.alchemy.utility.OdysseyAlchemyCauldronRecipe
import me.shadowalzazel.mcodyssey.effects.BlazingTask
import me.shadowalzazel.mcodyssey.effects.DecayingTask
import me.shadowalzazel.mcodyssey.effects.DousedTask
import me.shadowalzazel.mcodyssey.effects.FreezingTask
import me.shadowalzazel.mcodyssey.synchronizers.BrewingEventSynchro
import me.shadowalzazel.mcodyssey.synchronizers.CauldronEventSynchro
import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.BrewingStand
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.CauldronLevelChangeEvent
import org.bukkit.event.entity.AreaEffectCloudApplyEvent
import org.bukkit.event.entity.LingeringPotionSplashEvent
import org.bukkit.event.entity.PotionSplashEvent
import org.bukkit.event.inventory.BrewEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import org.bukkit.scheduler.BukkitRunnable

object OdysseyAlchemyListeners : Listener {

    private val odysseyTimedPotions = listOf("Bottle o' Ablaze", "Bottle o' Frost", "Bottle o' Decay", "Bottle o' Douse")
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
    private suspend fun validateCauldronRecipe(someItemSet: MutableSet<Item>, someFuelBlock: Material): OdysseyAlchemyCauldronRecipe? = runBlocking {
        val recipeJob: Deferred<OdysseyAlchemyCauldronRecipe?> = async {
            for (recipe in AlchemyRecipes.alchemyRecipeSet) {
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
                val timerLore = it.lore!![0]
                val i = timerLore.lastIndex
                // Get the time in seconds form String in "(M:SS)" format
                val oldTime = loreToSeconds(timerLore.subSequence((i - 5)..i))
                // Create lore by getting old effect and adding new time
                val newTimerLore = timerLore.subSequence(0..(i - 6)).toString() + timeToLore(oldTime / 4)
                println("Old Time: $timerLore | New time: $newTimerLore")
                it.lore = mutableListOf(newTimerLore)
            }
            brewingNewResults[brewingIndex] = newPotion
        }


        // Check each slot
        for (x in 0..2) {
            if (brewerSlots[x] != null && brewerSlots[x]?.type != Material.AIR) {
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

    // Main function for Cauldron recipes
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
                                val validRecipe: OdysseyAlchemyCauldronRecipe? = validateCauldronRecipe(itemsToCheck, blockUnderneath)
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
    @EventHandler
    fun brewingPotion(event: BrewEvent) {
        if (event.contents.ingredient!!.type == Material.GUNPOWDER || event.contents.ingredient!!.type == Material.DRAGON_BREATH) {

            val brewingContents = event.contents.contents!!
            GlobalScope.launch {
                val newBrewingStandResults = brewCustomAlchemyPotion(brewingContents, event.contents.ingredient!!.type)
                val synchroBrewingTask: BukkitRunnable = BrewingEventSynchro(newBrewingStandResults, event.contents!!)
                synchroBrewingTask.runTask(MinecraftOdyssey.instance)

            }
        }
    }

    @EventHandler
    fun tippedArrowTableInteract(event: PlayerInteractEvent) {
        if (event.material == Material.FLETCHING_TABLE) {
            println("Arrow Tip")
        }

        fun todo() {
            TODO("Make Click")
            /*
            if clicked with arrow ->
            if potion on top within block bounding box ->
            if applicable ->
            tip arrow, chance to consume potion
             */
        }
    }

    private fun todo() {
        TODO("Make Potions custom particles")
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
                    // Get Name
                    val potionName = (somePotionMeta.displayName() as TextComponent).content()

                    // Check custom potion has a timer
                    var potionDuration = 0
                    if (potionName in odysseyTimedPotions) {
                        // Logic
                        val potionLore = somePotionMeta.lore!![0]
                        val i = potionLore.lastIndex
                        val potionLoreTimer = potionLore.subSequence((i - 5)..i)
                        potionDuration = loreToSeconds(potionLoreTimer)
                    }
                    // Match Names
                    when (potionName) {
                        "Bottle o' Decay" -> {
                            val hungerDecay = PotionEffect(PotionEffectType.HUNGER, potionDuration * 20, 0)
                            somePotionMeta.addCustomEffect(hungerDecay, true)
                            for (decayingEntity: LivingEntity in event.affectedEntities) {
                                val decayingTask = DecayingTask(decayingEntity, 1, potionDuration / 2)
                                decayingEntity.addScoreboardTag("Decaying")
                                decayingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 40)
                            }
                        }
                        "Bottle o' Frost" -> {
                            val freezingSlow = PotionEffect(PotionEffectType.SLOW, potionDuration * 20, 0)
                            somePotionMeta.addCustomEffect(freezingSlow, true)
                            for (freezingEntity: LivingEntity in event.affectedEntities) {
                                if ("Freezing" !in freezingEntity.scoreboardTags) {
                                    val freezingTask = FreezingTask(freezingEntity, 1, potionDuration)
                                    freezingEntity.addPotionEffect(freezingSlow)
                                    freezingEntity.addScoreboardTag("Freezing")
                                    freezingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
                                }
                            }
                        }
                        // Fix for ablaze combo
                        "Bottle o' Douse" -> {
                            for (dousedEntity: LivingEntity in event.affectedEntities) {
                                if ("Doused" !in dousedEntity.scoreboardTags) {
                                    val dousedTask = DousedTask(dousedEntity, potionDuration)
                                    dousedEntity.addScoreboardTag("Doused")
                                    dousedTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
                                }
                            }
                        }
                        "Bottle o' Ablaze" -> {
                            for (blazingEntity: LivingEntity in event.affectedEntities) {
                                if ("Ablaze" !in blazingEntity.scoreboardTags) {
                                    blazingEntity.fireTicks = 20 * potionDuration
                                    val blazingTask = BlazingTask(blazingEntity, 1, potionDuration)
                                    blazingEntity.addScoreboardTag("Ablaze")
                                    blazingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
                                }
                            }
                        }
                        "Bottled Souls" -> {
                            // AOE soul particles
                            event.potion.world.spawnParticle(Particle.SCULK_SOUL, event.potion.location, 145, 2.5, 0.75, 2.55)
                            for (soulDamagedEntity: LivingEntity in event.affectedEntities) {
                                // Souls will not kill only escape
                                if (soulDamagedEntity.health > 3.0) {
                                    soulDamagedEntity.health -= 3.0
                                    soulDamagedEntity.damage(0.01)
                                    soulDamagedEntity.world.spawnParticle(Particle.SCULK_SOUL, soulDamagedEntity.location, 25, 0.25, 0.35, 0.25)
                                }
                            }
                        }
                        else -> {
                        }
                    }
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
                    val potionLore = somePotionMeta.lore!![0]
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
                    "Bottled Souls" -> {
                        customEffectToAdd = PotionEffect(PotionEffectType.UNLUCK, potionDuration * 20, 0)
                        tagToAdd = "Escaping_Souls_Cloud"
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
        val someEntities = event.affectedEntities
        // For now base timers then string manipulation
        for (tag in somePotionCloud.scoreboardTags) {
            when (tag) {
                "Decaying_Cloud" -> {
                    for (decayingEntity: LivingEntity in someEntities) {
                        val decayingTask = DecayingTask(decayingEntity, 1, (20 / 4) / 2)
                        decayingEntity.addScoreboardTag("Decaying")
                        decayingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20 * 2)
                    }
                }
                "Frost_Cloud" -> {
                    for (freezingEntity: LivingEntity in someEntities) {
                        if ("Freezing" !in freezingEntity.scoreboardTags) {
                            val freezingTask = FreezingTask(freezingEntity, 1, 20 / 4)
                            freezingEntity.addScoreboardTag("Freezing")
                            freezingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
                        }
                    }
                }
                "Douse_Cloud" -> {
                    for (dousedEntity: LivingEntity in someEntities) {
                        if ("Doused" !in dousedEntity.scoreboardTags) {
                            val dousedTask = DousedTask(dousedEntity,  30 / 4)
                            dousedEntity.addScoreboardTag("Doused")
                            dousedTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
                        }
                    }
                }
                "Blazing_Cloud" -> {
                    for (blazingEntity: LivingEntity in someEntities) {
                        if ("Ablaze" !in blazingEntity.scoreboardTags) {
                            val blazingTask = BlazingTask(blazingEntity, 1, 20 / 4)
                            blazingEntity.addScoreboardTag("Ablaze")
                            blazingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
                        }
                    }
                }
                "Escaping_Souls_Cloud" -> {
                    // AOE soul particles
                    somePotionCloud.world.spawnParticle(Particle.SCULK_SOUL, somePotionCloud.location, (somePotionCloud.radius * 40).toInt(), somePotionCloud.radius.toDouble(), 0.25, somePotionCloud.radius.toDouble())
                    for (soulDamagedEntity: LivingEntity in event.affectedEntities) {
                        // Souls will not kill only escape
                        if (soulDamagedEntity.health > 3.0) {
                            soulDamagedEntity.health - 3.0
                            soulDamagedEntity.damage(0.01)
                            soulDamagedEntity.world.spawnParticle(Particle.SCULK_SOUL, soulDamagedEntity.location, 25, 0.25, 0.35, 0.25)
                        }
                    }
                }
                else -> {

                }
            }
        }
    }

}
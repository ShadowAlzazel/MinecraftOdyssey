package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.alchemy.AlchemyPotions
import me.shadowalzazel.mcodyssey.alchemy.AlchemyRecipes
import me.shadowalzazel.mcodyssey.alchemy.utility.OdysseyPotion
import me.shadowalzazel.mcodyssey.effects.DecayingTask
import me.shadowalzazel.mcodyssey.effects.FreezingTask
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.data.Levelled
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

object OdysseyAlchemyListeners : Listener {

    private val alchemyPotionNames = listOf("§bBottle o' Decay", "§bBottle o' Frost", "§bBottle o' Douse", "§bBottle o' Ablaze")

    // Get time in seconds
    private fun loreToSeconds(timeSequence: CharSequence): Int {
        val minute = timeSequence[1].toString().toInt()
        val decimalSecond = timeSequence[3].toString().toInt()
        val second = timeSequence[4].toString().toInt()
        return (minute * 60) + (decimalSecond * 10) + (second)
    }

    // time to char
    private fun timeToLore(time: Int): CharSequence {
        val seconds = time % 60
        val minutes = time / 60
        // add 0 if less than 9
        return if (seconds < 9) { "($minutes:0$seconds)" } else { "($minutes:$seconds)" }
    }

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
                        if (nearbyEntities.size <= 5) {
                            var allItems = true
                            val itemsToCheck: MutableSet<Item> = mutableSetOf()
                            for (someEntity in nearbyEntities) {
                                if (someEntity is Item) {
                                    itemsToCheck.add(someEntity)
                                }
                                else {
                                    allItems = false
                                    break
                                }
                            }
                            if (allItems) {
                                for (someRecipe in AlchemyRecipes.alchemyRecipeSet) {
                                    val validated: Boolean = someRecipe.validateRecipe(itemsToCheck, blockUnderneath)
                                    if (validated) {
                                        somePlayer.playSound(somePlayer.location, Sound.ITEM_BOTTLE_FILL, 2.5F, 0.8F)
                                        val newState = event.newState.blockData as Levelled
                                        println(newState.level)
                                        break
                                    }
                                }
                            }
                        }
                        //println(nearbyEntities)
                    }
                }
            }
        }
    }


    @EventHandler
    fun brewingPotion(event: BrewEvent) {
        if (event.contents.ingredient!!.type == Material.GUNPOWDER || event.contents.ingredient!!.type == Material.DRAGON_BREATH) {
            val resultType = if (event.contents.ingredient!!.type == Material.GUNPOWDER) Material.SPLASH_POTION else Material.LINGERING_POTION
            val brewerSlots = event.contents.contents!!
            for (x in 0..2) {
                if (brewerSlots[x] != null) {
                    if (brewerSlots[x]!!.type != Material.AIR) {
                        val someBrewedPotion = brewerSlots[x]!!
                        for (someOdysseyPotion: OdysseyPotion in AlchemyPotions.potionSet) {
                            if (someOdysseyPotion.odysseyDisplayName == someBrewedPotion.itemMeta.displayName) {
                                val somePotionMeta = someBrewedPotion.itemMeta as PotionMeta
                                if (somePotionMeta.basePotionData.type == PotionType.UNCRAFTABLE) {
                                    // Dragon Breath
                                    if (resultType == Material.LINGERING_POTION) {
                                        val newPotion = ItemStack(resultType, 1)
                                        val newPotionMeta: PotionMeta = someBrewedPotion.itemMeta as PotionMeta
                                        // Logic
                                        val potionLore = newPotionMeta.lore!![0]
                                        val i = potionLore.lastIndex
                                        val potionLoreTimer = potionLore.subSequence((i - 5)..i)
                                        //println(potionLoreTimer)
                                        val potionTimerSeconds = loreToSeconds(potionLoreTimer)
                                        //println(potionTimerSeconds)
                                        val newPotionTimer = potionTimerSeconds / 4
                                        val newPotionLoreTimer = timeToLore(newPotionTimer)
                                        //println(newPotionTimer)
                                        val potionLoreEffect = potionLore.subSequence(0..(i - 6))
                                        val newPotionLore = potionLoreEffect.toString() + newPotionLoreTimer
                                        val newLore = newPotionMeta.lore
                                        newLore!!.remove(potionLore)
                                        newLore.add(0, newPotionLore)
                                        newPotionMeta.lore = newLore
                                        newPotion.itemMeta = newPotionMeta
                                        event.results[x] = newPotion
                                    }
                                    // Gunpowder
                                    else {
                                        val newPotion = ItemStack(resultType, 1)
                                        val newPotionMeta: PotionMeta = someBrewedPotion.itemMeta as PotionMeta
                                        newPotion.itemMeta = newPotionMeta
                                        event.results[x] = newPotion
                                    }
                                }
                                else {
                                    val newPotion = ItemStack(resultType, 1)
                                    val newPotionMeta: PotionMeta = someBrewedPotion.itemMeta as PotionMeta
                                    newPotion.itemMeta = newPotionMeta
                                    event.results[x] = newPotion
                                }
                                break
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


    @Suppress("DEPRECATION")
    @EventHandler
    fun alchemyPotionSplash(event: PotionSplashEvent) {
        if (event.potion.item.hasItemMeta()) {
            val somePotion = event.potion.item
            if (somePotion.itemMeta.hasLore()) {
                val somePotionMeta = somePotion.itemMeta as PotionMeta
                if (somePotionMeta.basePotionData.type == PotionType.UNCRAFTABLE) {
                    val potionName = somePotionMeta.displayName
                    var potionDuration = 0
                    if (potionName in alchemyPotionNames) {
                        // Logic
                        val potionLore = somePotionMeta.lore!![0]
                        val i = potionLore.lastIndex
                        val potionLoreTimer = potionLore.subSequence((i - 5)..i)
                        potionDuration = loreToSeconds(potionLoreTimer)
                    }
                    when (potionName) {
                        "§bBottle o' Decay" -> {
                            val hungerDecay = PotionEffect(PotionEffectType.HUNGER, potionDuration, 0)
                            somePotionMeta.addCustomEffect(hungerDecay, true)
                            for (decayingEntity: LivingEntity in event.affectedEntities) {
                                val freezingTask = DecayingTask(decayingEntity, 1, potionDuration)
                                decayingEntity.addScoreboardTag("Decaying")
                                freezingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 40)
                            }
                        }
                        "§bBottle o' Frost" -> {
                            val freezingSlow = PotionEffect(PotionEffectType.SLOW, potionDuration, 0)
                            somePotionMeta.addCustomEffect(freezingSlow, true)
                            for (freezingEntity: LivingEntity in event.affectedEntities) {
                                if ("Freezing" !in freezingEntity.scoreboardTags) {
                                    freezingEntity.addPotionEffect(freezingSlow)
                                    val freezingTask = FreezingTask(freezingEntity, 1, potionDuration)
                                    freezingEntity.addScoreboardTag("Freezing")
                                    freezingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
                                }
                            }
                        }
                        "§bBottle o' Douse" -> {

                        }
                        "§bBottle o' Ablaze" -> {

                        }
                        else -> {

                        }
                    }
                    println(event.affectedEntities)
                }
            }
        }

    }

    @EventHandler
    fun alchemyPotionLingering(event: LingeringPotionSplashEvent) {
        if (event.entity.item.hasItemMeta()) {
            val somePotion = event.entity.item
            if (somePotion.itemMeta.hasLore()) {
                val somePotionMeta = somePotion.itemMeta as PotionMeta
                if (somePotionMeta.basePotionData.type == PotionType.UNCRAFTABLE) {
                    val potionName = somePotionMeta.displayName
                    var potionDuration = 0
                    if (potionName in alchemyPotionNames) {
                        // Logic
                        val potionLore = somePotionMeta.lore!![0]
                        val i = potionLore.lastIndex
                        val potionLoreTimer = potionLore.subSequence((i - 5)..i)
                        potionDuration = loreToSeconds(potionLoreTimer) * 20
                    }
                    val potionCloud = event.areaEffectCloud

                    var tagToAdd: String? = null
                    var applicationDelay = 20
                    var customEffectToAdd: PotionEffect? = null
                    when (potionName) {
                        "§bBottle o' Decay" -> {
                            customEffectToAdd = PotionEffect(PotionEffectType.HUNGER, potionDuration, 0)
                            applicationDelay = 40
                            tagToAdd = "Decaying_Cloud"
                        }
                        "§bBottle o' Frost" -> {
                            customEffectToAdd = PotionEffect(PotionEffectType.SLOW, potionDuration, 0)
                            tagToAdd = "Frost_Cloud"
                        }
                        "§bBottle o' Douse" -> {

                        }
                        "§bBottle o' Ablaze" -> {

                        }
                        else -> {

                        }
                    }
                    if (tagToAdd != null) {
                        somePotionMeta.addCustomEffect(customEffectToAdd!!, true)
                        potionCloud.addCustomEffect(customEffectToAdd, true)
                        potionCloud.duration = 20 * 20
                        potionCloud.reapplicationDelay = applicationDelay
                        potionCloud.addScoreboardTag(tagToAdd)
                    }
                }
            }
        }
    }

    @EventHandler
    fun alchemyPotionCloud(event: AreaEffectCloudApplyEvent) {
        val somePotionCloud = event.entity
        val someEntities = event.affectedEntities
        println(someEntities)
        for (tag in somePotionCloud.scoreboardTags) {
            when (tag) {
                "Decaying_Cloud" -> {
                    for (decayinEntity: LivingEntity in someEntities) {
                        val decayingTask = DecayingTask(decayinEntity, 1, 5)
                        decayinEntity.addScoreboardTag("Decaying")
                        decayingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 40)
                    }
                }
                "Frost_Cloud" -> {
                    for (freezingEntity: LivingEntity in someEntities) {
                        if ("Freezing" !in freezingEntity.scoreboardTags) {
                            // Temp 5 do string tags later
                            val freezingTask = FreezingTask(freezingEntity, 1, 5)
                            freezingEntity.addScoreboardTag("Freezing")
                            freezingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
                        }
                    }
                }
            }
        }
    }

}
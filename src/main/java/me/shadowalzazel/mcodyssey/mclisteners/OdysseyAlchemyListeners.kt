package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.alchemy.AlchemyPotions
import me.shadowalzazel.mcodyssey.alchemy.AlchemyRecipes
import me.shadowalzazel.mcodyssey.alchemy.utility.OdysseyPotion
import me.shadowalzazel.mcodyssey.effects.BlazingTask
import me.shadowalzazel.mcodyssey.effects.DecayingTask
import me.shadowalzazel.mcodyssey.effects.DousedTask
import me.shadowalzazel.mcodyssey.effects.FreezingTask
import org.bukkit.Material
import org.bukkit.Particle
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

    private val alchemyCustomEffectPotions = listOf("§eBottle o' Decay", "§eBottle o' Frost", "§eBottle o' Douse", "§eBottle o' Ablaze")

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
            // function for final potion result


            fun createResultingPotion(brewingIndex: Int) {
                val oldPotion = brewerSlots[brewingIndex]!!
                val newPotion = ItemStack(resultType, 1)
                val newPotionMeta: PotionMeta = oldPotion.itemMeta as PotionMeta
                newPotion.itemMeta = newPotionMeta
                event.results[brewingIndex] = newPotion
            }


            // get per slot in result
            for (x in 0..2) {
                if (brewerSlots[x] != null) {
                    if (brewerSlots[x]!!.type != Material.AIR) {
                        val someBrewedPotion = brewerSlots[x]!!
                        // Check for all names
                        for (someOdysseyPotion: OdysseyPotion in AlchemyPotions.potionSet) {
                            // Name Check
                            if (someOdysseyPotion.odysseyDisplayName == someBrewedPotion.itemMeta.displayName) {
                                val somePotionMeta = someBrewedPotion.itemMeta as PotionMeta
                                // If Custom Effects
                                if (somePotionMeta.basePotionData.type == PotionType.UNCRAFTABLE) {
                                    // Dragon Breath
                                    if (resultType == Material.LINGERING_POTION) {
                                        if (somePotionMeta.displayName == "§3Bottled Souls") {
                                            createResultingPotion(x)
                                        }
                                        else {
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
                                    }
                                    // Gunpowder
                                    else {
                                        createResultingPotion(x)
                                    }
                                }
                                // If not custom potion
                                else {
                                    createResultingPotion(x)
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
                    // Check if in class list
                    if (potionName in alchemyCustomEffectPotions) {
                        // Logic
                        val potionLore = somePotionMeta.lore!![0]
                        val i = potionLore.lastIndex
                        val potionLoreTimer = potionLore.subSequence((i - 5)..i)
                        potionDuration = loreToSeconds(potionLoreTimer)
                    }
                    when (potionName) {
                        "§eBottle o' Decay" -> {
                            val hungerDecay = PotionEffect(PotionEffectType.HUNGER, potionDuration * 20, 0)
                            somePotionMeta.addCustomEffect(hungerDecay, true)
                            for (decayingEntity: LivingEntity in event.affectedEntities) {
                                val decayingTask = DecayingTask(decayingEntity, 1, potionDuration / 2)
                                decayingEntity.addScoreboardTag("Decaying")
                                decayingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 40)
                            }
                        }
                        "§eBottle o' Frost" -> {
                            val freezingSlow = PotionEffect(PotionEffectType.SLOW, potionDuration * 20, 0)
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
                        "§eBottle o' Douse" -> {
                            for (dousedEntity: LivingEntity in event.affectedEntities) {
                                if ("Doused" !in dousedEntity.scoreboardTags) {
                                    dousedEntity.fireTicks = 20 * potionDuration
                                    val dousedTask = DousedTask(dousedEntity, potionDuration)
                                    dousedEntity.addScoreboardTag("Doused")
                                    dousedTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
                                }
                            }
                        }
                        "§eBottle o' Ablaze" -> {
                            for (blazingEntity: LivingEntity in event.affectedEntities) {
                                if ("Ablaze" !in blazingEntity.scoreboardTags) {
                                    blazingEntity.fireTicks = 20 * potionDuration
                                    val blazingTask = BlazingTask(blazingEntity, 1, potionDuration)
                                    blazingEntity.addScoreboardTag("Ablaze")
                                    blazingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
                                }
                            }
                        }
                        "§3Bottled Souls" -> {
                            // AOE soul particles
                            event.potion.world.spawnParticle(Particle.SCULK_SOUL, event.potion.location, 145, 2.5, 0.75, 2.55)
                            for (soulDamagedEntity: LivingEntity in event.affectedEntities) {
                                // Souls will not kill only escape
                                if (soulDamagedEntity.health > 3.0) {
                                    soulDamagedEntity.health -= 3.0
                                    soulDamagedEntity.world.spawnParticle(Particle.SCULK_SOUL, soulDamagedEntity.location, 25, 0.25, 0.35, 0.25)
                                }
                            }
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
                    if (potionName in alchemyCustomEffectPotions) {
                        // Logic
                        val potionLore = somePotionMeta.lore!![0]
                        val i = potionLore.lastIndex
                        val potionLoreTimer = potionLore.subSequence((i - 5)..i)
                        potionDuration = loreToSeconds(potionLoreTimer)
                    }
                    val potionCloud = event.areaEffectCloud

                    var tagToAdd: String? = null
                    var applicationDelay = 20
                    var customEffectToAdd: PotionEffect? = null
                    when (potionName) {
                        "§eBottle o' Decay" -> {
                            customEffectToAdd = PotionEffect(PotionEffectType.HUNGER, potionDuration * 20, 0)
                            applicationDelay = 40
                            tagToAdd = "Decaying_Cloud"
                        }
                        "§eBottle o' Frost" -> {
                            customEffectToAdd = PotionEffect(PotionEffectType.SLOW, potionDuration * 20, 0)
                            tagToAdd = "Frost_Cloud"
                        }
                        "§eBottle o' Douse" -> {
                            customEffectToAdd = PotionEffect(PotionEffectType.UNLUCK, potionDuration * 20, 0)
                            tagToAdd = "Douse_Cloud"
                        }
                        "§eBottle o' Ablaze" -> {
                            customEffectToAdd = PotionEffect(PotionEffectType.WEAKNESS, potionDuration * 20, 0)
                            tagToAdd = "Blazing_Cloud"
                        }
                        "§3Bottled Souls" -> {
                            customEffectToAdd = PotionEffect(PotionEffectType.UNLUCK, potionDuration * 20, 0)
                            tagToAdd = "Escaping_Souls_Cloud"
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
                        potionCloud.addScoreboardTag("Timed_Cloud")
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
        // For now base timers then string manipulation
        for (tag in somePotionCloud.scoreboardTags) {
            when (tag) {
                "Decaying_Cloud" -> {
                    for (decayinEntity: LivingEntity in someEntities) {
                        val decayingTask = DecayingTask(decayinEntity, 1, (20 / 4) / 2)
                        decayinEntity.addScoreboardTag("Decaying")
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
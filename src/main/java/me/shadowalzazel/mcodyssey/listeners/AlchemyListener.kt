package me.shadowalzazel.mcodyssey.listeners

import kotlinx.coroutines.*
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.alchemy.AlchemyManager
import me.shadowalzazel.mcodyssey.alchemy.OldCauldronRecipes
import me.shadowalzazel.mcodyssey.alchemy.base.OldCauldronRecipe
import me.shadowalzazel.mcodyssey.alchemy.utility.CauldronEventSynchro
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasOdysseyTag
import me.shadowalzazel.mcodyssey.effects.*
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

object AlchemyListener : Listener, AlchemyManager, EffectsManager {


    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/
    // Effects
    // TODO: Reapply effects again

    // Main Consumption Handler
    @EventHandler
    fun potionConsumeHandler(event: PlayerItemConsumeEvent) {
        if (!event.item.hasItemMeta()) return
        if (event.item.type != Material.POTION) return
        // Potion Item Tag Getters
        if (!event.item.isCustomEffect()) return
        if (!event.item.hasOdysseyTag()) return
        val effect = event.item.getCustomEffectTag()
        val duration = event.item.getCustomEffectTimeInTicks()
        val amplifier = event.item.getCustomEffectAmplifier()
        // Apply to Player
        event.player.addOdysseyEffect(effect, duration, amplifier)
        /*
         // Check if custom bottle
         if (potionMeta.hasCustomModelData()) {
             if (event.replacement == null) { return }
             event.replacement!!.itemMeta = event.replacement!!.itemMeta.also {
                 it.setCustomModelData(potionMeta.customModelData)
             }
         }
          */
    }

    // Main Splash Handler
    @EventHandler
    fun splashPotionHandler(event: PotionSplashEvent) {
        // Potion Item Tag Getters
        if (!event.potion.item.isCustomEffect()) return
        if (!event.potion.item.hasOdysseyTag()) return
        val effect = event.potion.item.getCustomEffectTag()
        val duration = event.potion.item.getCustomEffectTimeInTicks()
        val amplifier = event.potion.item.getCustomEffectAmplifier()
        // Assign Per Entity for Intensity
        for (entity in event.affectedEntities) {
            // TODO: For Vials -> Lower Intensity
            // For Large Potions -> Increase Intensity
            entity.addOdysseyEffect(effect, duration, amplifier, event.getIntensity(entity))
        }
    }

    // Main function regarding lingering potions
    @EventHandler
    fun lingeringSplashHandler(event: LingeringPotionSplashEvent) {
        // Potion Item Tag Getters
        if (!event.entity.item.isCustomEffect()) return
        if (!event.entity.item.hasOdysseyTag()) return
        // Create Cloud
        createCustomEffectCloud(event.entity, event.areaEffectCloud)
    }

    // Main function for detecting entity clouds from alchemy potions
    @EventHandler
    fun effectCloudApplyHandler(event: AreaEffectCloudApplyEvent) {
        if (!event.entity.scoreboardTags.contains(EntityTags.CUSTOM_EFFECT_CLOUD)) return
        val effect = event.entity.getCloudEffectTag()
        val duration = event.entity.getCloudEffectTimeInTicks()
        val amplifier = event.entity.getCloudEffectAmplifier()
        //if (!event.entity.scoreboardTags.contains(EntityTags.TIMED_CLOUD)) return
        // Assign per Entity
        for (entity in event.affectedEntities) {
            entity.addOdysseyEffect(effect, duration, amplifier)
        }
    }

    // Main function for potion arrows application
    @EventHandler
    fun tippedArrowApplyHandler(event: ProjectileHitEvent) {
        if (event.entity !is Arrow) return
        if (event.hitEntity == null) return
        if (event.hitEntity !is LivingEntity) return
        val arrow = event.entity as Arrow
        if (arrow.itemStack.type != Material.TIPPED_ARROW) return
        // Check Custom Effect Tags
        val item = arrow.itemStack
        if (!item.isCustomEffect()) return
        // Apply effect
        val effect = item.getCustomEffectTag()
        val duration = item.getCustomEffectTimeInTicks()
        val amplifier = item.getCustomEffectAmplifier()
        // Apply to Player
        (event.hitEntity as LivingEntity).addOdysseyEffect(effect, duration, amplifier)
    }

    // Main function for potion arrow creation
    @EventHandler
    fun fletchingTableTippedArrowHandler(event: PlayerInteractEvent) {
        if (event.clickedBlock == null) return
        if (!event.action.isLeftClick) return
        if (event.clickedBlock!!.type != Material.FLETCHING_TABLE) return
        val equipment = event.player.equipment
        if (equipment.itemInMainHand.type != Material.ARROW) return
        if (equipment.itemInMainHand.amount < 1) return
        if (equipment.itemInOffHand.type != Material.LINGERING_POTION) return
        // Get Effect
        val potion = equipment.itemInOffHand
        val effect = potion.getCustomEffectTag()
        val duration = potion.getCustomEffectTimeInTicks()
        val amplifier = potion.getCustomEffectAmplifier()

        val arrows = ItemStack(Material.TIPPED_ARROW, 1).apply {
           itemMeta = (equipment.itemInOffHand.itemMeta as PotionMeta)
            setCustomEffectTag(effect)
            setCustomEffectTime(duration)
            setCustomEffectAmplifier(amplifier)
        }

        equipment.itemInMainHand.subtract(1)
        //equipment.itemInOffHand.subtract(1)
        event.player.inventory.addItem(arrows)
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/
    // Cauldron

    // Simple finder function for async class
    private fun asyncCauldronRecipeFinder(items: MutableCollection<Item>, fuel: Material): OldCauldronRecipe? {
        return OldCauldronRecipes.CAULDRON_SET.find {
            it.validateRecipe(items, fuel)
        }
    }

    // Async function to crete new scope and run check
    @OptIn(DelicateCoroutinesApi::class)
    private fun asyncCauldronHandler(items: MutableCollection<Item>, fuel: Material) {
        GlobalScope.launch {
            val recipe = asyncCauldronRecipeFinder(items, fuel)
            if (recipe != null) CauldronEventSynchro(recipe, items).runTask(Odyssey.instance)
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

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    // TODO: Fix bug if multiple types, converts all
    // If Handler for materials and types
    private fun brewingHandler(brewingIngredient: Material, brewerPotions: List<ItemStack?>): Map<Int, ItemStack> {
        val newPotions = mutableMapOf<Int, ItemStack>()
        val resultMaterial = when (brewingIngredient) {
            Material.GUNPOWDER -> {
                Material.SPLASH_POTION
            }
            Material.DRAGON_BREATH -> {
                Material.LINGERING_POTION
            }
            else -> {
                Material.POTION
            }
        }
        for (x in 0..2) {
            val itemAtX = brewerPotions[x]
            if (itemAtX == null) {
                newPotions[x] = ItemStack(Material.AIR)
            } else if (itemAtX.type == Material.AIR) {
                continue
            } else if (!(itemAtX.itemMeta.persistentDataContainer.hasOdysseyTag())) {
                continue
            } else if (itemAtX.itemMeta.hasCustomModelData()) {
                if (resultMaterial == Material.LINGERING_POTION) {
                    //newPotions[x] = oldCreateLingeringPotion(resultMaterial, itemAtX)
                } else {
                    //newPotions[x] = createCustomPotion(resultMaterial, itemAtX)
                }
            } else if (brewingIngredient == Material.HONEY_BOTTLE) {
                // FOR STICKY POTION
            } else {
                //newPotions[x] = createCustomPotion(resultMaterial, itemAtX)
            }
        }
        return newPotions
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
        // Get Potion Model
        val potionModel = when(event.contents.ingredient!!.type) {
            Material.REDSTONE -> {
                ItemModels.VOLUMETRIC_BOTTLE
            }
            Material.GLOWSTONE_DUST -> {
                ItemModels.SQUARE_BOTTLE
            }
            else -> {
                0
            }
        }
        if (potionModel == 0) return
        for (result in event.results) {
            if (result == null) continue
            if (result.type == Material.AIR) continue
            result.itemMeta = result.itemMeta.also {
                it.setCustomModelData(potionModel)
            }
        }
    }

}

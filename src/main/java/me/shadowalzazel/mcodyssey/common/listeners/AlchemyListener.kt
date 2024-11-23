package me.shadowalzazel.mcodyssey.common.listeners

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent
import kotlinx.coroutines.*
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.alchemy.PotionEffectsManager
import me.shadowalzazel.mcodyssey.common.alchemy.CauldronRecipes
import me.shadowalzazel.mcodyssey.common.alchemy.base.AlchemyCauldronRecipe
import me.shadowalzazel.mcodyssey.common.alchemy.utility.CauldronEventSynchro
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.ItemModels
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.common.effects.*
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Arrow
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.ThrownPotion
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

object AlchemyListener : Listener, PotionEffectsManager, EffectsManager, DataTagManager {

    /*-----------------------------------------------------------------------------------------------*/
    // Effects
    // Reapply effects again

    // Main Consumption Handler
    @EventHandler
    fun potionConsumeHandler(event: PlayerItemConsumeEvent) {
        if (!event.item.hasItemMeta()) return
        if (event.item.itemMeta !is PotionMeta) return
        when(event.item.type) {
            Material.POTION -> {
                potionDrinkHandler(event)
            }
            else -> {
                return
            }
        }
        return
    }

    // Detect when potion is thrown
    @EventHandler
    fun potionThrowHandler(event: PlayerLaunchProjectileEvent) {
        if (event.itemStack.type != Material.SPLASH_POTION  && event.itemStack.type != Material.LINGERING_POTION ) return
        val thrownPotion = event.projectile
        if (thrownPotion !is ThrownPotion) return
        val potion = event.itemStack
        if (potion.hasTag(ItemDataTags.IS_POTION_VIAL)) {
            event.projectile.velocity = event.projectile.velocity.multiply(2.0)
        }
    }

    private fun potionDrinkHandler(event: PlayerItemConsumeEvent) {
        if (event.item.type != Material.POTION) return
        // Potion Item Tag Getters
        val item = event.item
        val isOdysseyEffect = item.hasOdysseyEffectTag() && item.hasOdysseyItemTag()
        if (isOdysseyEffect) {
            val effect = item.getCustomEffectTag()
            val duration = item.getCustomEffectTimeInTicks()
            val amplifier = item.getCustomEffectAmplifier()
            // Apply to Player
            event.player.addOdysseyEffect(effect, duration, amplifier)
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Main Splash Handler
    @EventHandler
    fun splashPotionHandler(event: PotionSplashEvent) {
        // Potion Item Tag Getters
        if (!event.potion.item.hasItemMeta()) return
        if (!event.potion.item.hasOdysseyEffectTag()) return
        if (!event.potion.item.hasOdysseyItemTag()) return
        val effect = event.potion.item.getCustomEffectTag()
        val duration = event.potion.item.getCustomEffectTimeInTicks()
        val amplifier = event.potion.item.getCustomEffectAmplifier()
        // Assign Per Entity for Intensity
        for (entity in event.affectedEntities) {
            // For Vials -> Lower Intensity
            // For Large Potions -> Increase Intensity
            entity.addOdysseyEffect(effect, duration, amplifier, event.getIntensity(entity))
        }
    }

    // Main function regarding lingering potions
    @EventHandler
    fun lingeringSplashHandler(event: LingeringPotionSplashEvent) {
        // Potion Item Tag Getters
        val potionItem = event.entity.item
        if (!potionItem.hasItemMeta()) return
        val potionMeta = potionItem.itemMeta as PotionMeta
        if (potionMeta.hasCustomEffects()) {
            event.entity.potionMeta = potionMeta
            for (effect in potionMeta.customEffects) {
                val newEffect = PotionEffect(effect.type, effect.duration / 4, effect.amplifier)
                event.areaEffectCloud.addCustomEffect(newEffect, true)
            }
        }
        if (!potionItem.hasOdysseyItemTag()) return
        if (!potionItem.hasOdysseyEffectTag()) return
        // Huge buff to lingering if made sticky
        val isSticky = potionItem.hasTag(ItemDataTags.IS_LINGERING_STICKY)
        // Create Cloud
        event.areaEffectCloud.also {
            it.addCustomEffect(PotionEffect(PotionEffectType.UNLUCK, 0, 0), false)
            it.radius = 3F
            it.radiusOnUse = -0.5F
            it.radiusPerTick = -0.005F
            it.color = Color.fromRGB(155, 155, 155)
            it.reapplicationDelay = 20
            it.durationOnUse = 0
            it.duration = 600
            it.waitTime = 10
            if (isSticky) {
                it.radiusOnUse = -0.05F
                it.radiusPerTick = 0.001F
            }
        }
        createOdysseyEffectCloud(event.entity, event.areaEffectCloud)
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

    /*-----------------------------------------------------------------------------------------------*/

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
        if (!item.hasOdysseyEffectTag()) return
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
    // Cauldron

    // Simple finder function for async class
    private fun asyncCauldronRecipeFinder(items: MutableCollection<Item>, fuel: Material): AlchemyCauldronRecipe? {
        return CauldronRecipes.CAULDRON_RECIPE_SET.find {
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
        val alchemyFuels = CauldronRecipes.fireFuels + CauldronRecipes.soulFireFuels
        if (blockUnderneath.type !in alchemyFuels) return
        val entitiesInside = blockUnderneath.world.getNearbyEntities(event.block.boundingBox)
        val allItems = entitiesInside.all { it is Item }
        if (!allItems) return
        val itemList = entitiesInside.filterIsInstance<Item>().toMutableSet()
        // Advancement
        val players = event.block.location.getNearbyPlayers(5.0)
        players.forEach {
            val advancement = it.server.getAdvancement(NamespacedKey.fromString("odyssey:odyssey/cauldron_alchemy")!!)
            if (advancement != null) {
                it.getAdvancementProgress(advancement).awardCriteria("requirement")
            }
        }
        // Run new async
        asyncCauldronHandler(itemList, blockUnderneath.type)
    }
    
    /*-----------------------------------------------------------------------------------------------*/

    // Sea Crystals -> Vials
    // Popped Chorus Fruit -> Concoctions
    // Honey Bottle -> Extended+
    // Glow-berries -> Upgraded+
    // EXP Bottle -> Changes to ???

    @EventHandler
    fun brewingPotionHandler(event: BrewEvent) {
        val ingredient = event.contents.ingredient ?: return
        val ingredientMaterial = ingredient.type
        // Get Potion Model
        val resultModel: Int? = when (ingredientMaterial) {
            Material.REDSTONE -> ItemModels.VOLUMETRIC_BOTTLE
            Material.GLOWSTONE_DUST -> ItemModels.SQUARE_BOTTLE
            Material.GLOW_BERRIES -> ItemModels.SQUARE_BOTTLE
            Material.HONEY_BOTTLE -> ItemModels.VOLUMETRIC_BOTTLE
            Material.PRISMARINE_CRYSTALS -> ItemModels.VIAL_CHARGE_1
            else -> null
        }
        // Get Result from Ingredient
        val resultMaterial = when(ingredientMaterial) {
            Material.GUNPOWDER -> Material.SPLASH_POTION
            Material.DRAGON_BREATH -> Material.LINGERING_POTION
            else -> Material.POTION
        }
        val contents = event.contents.contents.toList()
        for (x in 0..2) {
            val item = contents[x] ?: continue
            if (item.type == Material.AIR) continue
            val itemMeta = item.itemMeta
            if (itemMeta !is PotionMeta) continue
            var isUpgradePlus = true
            // -------------------------------------------------
            when(ingredientMaterial) {
                // For Upgraded Plus
                Material.GLOW_BERRIES -> {
                    event.results[x] = makeUpgradedPlusPotion(item)
                }
                // For Extended Plus
                Material.HONEY_BOTTLE -> {
                    event.results[x] = makeExtendedPlusPotion(item)
                }
                // For Vials
                Material.PRISMARINE_CRYSTALS -> {
                    event.results[x] = makeVialPotion(item)
                }
                // For Lingering/ Splash
                else -> {
                    isUpgradePlus = false
                }
            }
            var result = event.results[x]
            // Get vars

            val isBasePotion = itemMeta.basePotionType != null
            val hasCustomEffects = (itemMeta.hasCustomEffects() && itemMeta.customEffects.isNotEmpty()) || item.hasCustomEffectTag()
            val hasOdysseyEffects = item.hasOdysseyItemTag() && item.hasOdysseyEffectTag()
            val hasCustomModel = itemMeta.hasCustomModelData()
            // --------------------------------------------------
            // For Odyssey Effects
            if (hasOdysseyEffects) {
                result = if (resultMaterial == Material.LINGERING_POTION) {
                    createOdysseyLingeringPotion(item)
                } else {
                    makeModeledPotion(resultMaterial, item)
                }
            }
            // For custom effects -> Lingering/Splash [copies item]
            else if (hasCustomEffects && !isUpgradePlus) {
                result = if (resultMaterial == Material.LINGERING_POTION) {
                    makeCustomLingeringPotion(item)
                } else {
                    makeModeledPotion(resultMaterial, item)
                }
            }
            // For standard brewing [copies result]
            else if (isBasePotion && !isUpgradePlus) {
                result = makeModeledPotion(resultMaterial, result, resultModel)
            }
            // For keeping models  [copies result]
            else if (hasCustomModel && !isUpgradePlus) {
                result = makeModeledPotion(resultMaterial, result, itemMeta.customModelData)
            }

            // --------------------------------------------------
            // Set final
            val resultMeta = result.itemMeta
            val stackSize = if (result.hasTag(ItemDataTags.IS_POTION_VIAL)) 64 else 8
            resultMeta.setMaxStackSize(stackSize)
            result.itemMeta = resultMeta
            event.results[x] = result
        }
    }

    private fun makeModeledPotion(material: Material, potion: ItemStack, model: Int? = null): ItemStack {
        val newPotion = ItemStack(material, 1)
        val meta = potion.itemMeta
        if (model != null) meta.setCustomModelData(model)
        newPotion.itemMeta = meta
        return newPotion
    }

    private fun makeCustomLingeringPotion(potion: ItemStack, model: Int? = null): ItemStack {
        val lingeringPotion = ItemStack(Material.LINGERING_POTION, 1)
        val lingeringMeta = lingeringPotion.itemMeta as PotionMeta
        if (model != null) lingeringMeta.setCustomModelData(model)
        // Add lingering potion effects
        val meta = potion.itemMeta as PotionMeta
        for (effect in meta.customEffects) {
            val newEffect = PotionEffect(effect.type, effect.duration, effect.amplifier)
            lingeringMeta.addCustomEffect(newEffect, true)
        }
        lingeringPotion.itemMeta = lingeringMeta
        return lingeringPotion
    }


    // Fix for all: odyssey effects, custom effects,
    private fun makeExtendedPlusPotion(potion: ItemStack): ItemStack {
        if (potion.itemMeta !is PotionMeta) return potion
        if (potion.hasTag(ItemDataTags.IS_EXTENDED_PLUS)) return potion
        if (potion.hasTag(ItemDataTags.IS_UPGRADED_PLUS)) return potion
        val meta = (potion.itemMeta as PotionMeta)
        val potionType = meta.basePotionType ?: return potion
        // Get Old effects from custom effects or base type effects
        val oldEffects = mutableListOf<PotionEffect>()
        potionType.potionEffects.forEach { oldEffects.add(it) }
        for (effect in oldEffects) {
            val newEffect = PotionEffect(effect.type, (effect.duration * 1.5).toInt(), effect.amplifier)
            meta.addCustomEffect(newEffect, true)
        }
        meta.basePotionType = PotionType.THICK
        return potion.clone().also {
            it.itemMeta = meta
            it.addTag(ItemDataTags.IS_EXTENDED_PLUS)
        }
    }

    private fun makeUpgradedPlusPotion(potion: ItemStack): ItemStack {
        if (potion.itemMeta !is PotionMeta) return potion
        if (potion.hasTag(ItemDataTags.IS_EXTENDED_PLUS)) return potion
        if (potion.hasTag(ItemDataTags.IS_UPGRADED_PLUS)) return potion
        val meta = (potion.itemMeta as PotionMeta)
        val potionType = meta.basePotionType ?: return potion
        // Get Old effects from custom effects or base type effects
        val oldEffects = mutableListOf<PotionEffect>()
        potionType.potionEffects.forEach { oldEffects.add(it) }
        for (effect in oldEffects) {
            val newEffect = PotionEffect(effect.type, (effect.duration * 0.6).toInt(), effect.amplifier + 1)
            meta.addCustomEffect(newEffect, true)
        }
        meta.basePotionType = PotionType.THICK
        return potion.clone().also {
            it.itemMeta = meta
            it.addTag(ItemDataTags.IS_UPGRADED_PLUS)
        }
    }

    private fun makeVialPotion(potion: ItemStack): ItemStack {
        if (potion.itemMeta !is PotionMeta) return potion
        if (potion.hasTag(ItemDataTags.IS_POTION_VIAL)) return potion
        val vial = createPotionVialStack(potion.clone())
        vial.amount = 4
        return vial
    }

}

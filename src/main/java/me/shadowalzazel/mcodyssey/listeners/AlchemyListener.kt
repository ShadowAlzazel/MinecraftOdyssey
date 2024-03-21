package me.shadowalzazel.mcodyssey.listeners

import kotlinx.coroutines.*
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.alchemy.AlchemyManager
import me.shadowalzazel.mcodyssey.alchemy.CauldronRecipes
import me.shadowalzazel.mcodyssey.alchemy.base.AlchemyCauldronRecipe
import me.shadowalzazel.mcodyssey.alchemy.utility.CauldronEventSynchro
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.addTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasOdysseyItemTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.setIntTag
import me.shadowalzazel.mcodyssey.effects.*
import org.bukkit.Color
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
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

object AlchemyListener : Listener, AlchemyManager, EffectsManager {

    /*-----------------------------------------------------------------------------------------------*/
    // Effects
    // TODO: Reapply effects again
    // TODO: Vials Can Be thrown farther

    // Main Consumption Handler
    @EventHandler
    fun potionConsumeHandler(event: PlayerItemConsumeEvent) {
        if (!event.item.hasItemMeta()) return
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
        // Potion Vials (also have charges when thrown)
        if (item.hasTag(ItemTags.IS_POTION_VIAL)) {
            val charges = item.getIntTag(ItemTags.POTION_CHARGES_LEFT) ?: 0
            if (charges <= 1) return
            // Run charge
            val meta = item.itemMeta.clone()
            if (meta.hasCustomModelData()) {
                meta.setCustomModelData(meta.customModelData - 1)
            }
            event.replacement = item.also {
                it.itemMeta = meta
                it.setIntTag(ItemTags.POTION_CHARGES_LEFT, charges - 1)
            }
        }
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
        if (!event.potion.item.hasItemMeta()) return
        if (!event.potion.item.hasOdysseyEffectTag()) return
        if (!event.potion.item.hasOdysseyItemTag()) return
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
        if (!event.entity.item.hasItemMeta()) return
        val potionMeta = event.entity.item.itemMeta as PotionMeta
        if (potionMeta.hasCustomEffects()) {
            event.entity.potionMeta = potionMeta
            for (effect in potionMeta.customEffects) {
                val newEffect = PotionEffect(effect.type, effect.duration / 4, effect.amplifier)
                event.areaEffectCloud.addCustomEffect(newEffect, true)
            }
        }
        if (!event.entity.item.hasOdysseyItemTag()) return
        if (!event.entity.item.hasOdysseyEffectTag()) return
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
            it.ingredientValidateHandler(items, fuel)
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
        // Run new async
        asyncCauldronHandler(itemList, blockUnderneath.type)
    }
    
    /*-----------------------------------------------------------------------------------------------*/
    // TODO: Fix bug if multiple types, converts all


    // Sea Crystals -> Vials
    // Popped Chorus Fruit -> Concoctions
    // Honeycomb OR Honey Bottle -> Extended+
    // Glow-berries -> III
    // EXP Bottle -> ???

    @EventHandler
    fun brewingPotionHandler(event: BrewEvent) {
        val ingredient = event.contents.ingredient ?: return
        val ingredientMaterial = ingredient.type
        // Get Potion Model
        var potionModel: Int? = when (ingredientMaterial) {
            Material.REDSTONE -> {
                ItemModels.VOLUMETRIC_BOTTLE
            }
            Material.GLOWSTONE_DUST -> {
                ItemModels.SQUARE_BOTTLE
            }
            Material.GLOW_BERRIES -> {
                ItemModels.SQUARE_BOTTLE
            }
            Material.HONEY_BOTTLE -> {
                ItemModels.VOLUMETRIC_BOTTLE
            }
            Material.PRISMARINE_CRYSTALS -> {
                ItemModels.VIAL
            }
            else -> {
                null
            }
        }
        // Get Result from Ingredient
        val resultMaterial = when(ingredientMaterial) {
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
        val contents = event.contents.contents.toList()
        for (x in 0..2) {
            val item = contents[x] ?: continue
            if (item.type == Material.AIR) continue
            if (item.itemMeta !is PotionMeta) continue
            // --------------------------------------------------
            // For Upgraded Plus
            if (ingredientMaterial == Material.GLOW_BERRIES) {
                event.results[x] = makeUpgradedPlusPotion(item)
            }
            // For Upgraded Plus
            else if (ingredientMaterial == Material.HONEY_BOTTLE) {
                event.results[x] = makeExtendedPlusPotion(item)
            }
            // For Vials
            else if (ingredientMaterial == Material.PRISMARINE_CRYSTALS) {
                event.results[x] = makeVialPotion(item)
                potionModel = ItemModels.VIAL_CHARGE_5
            }
            // --------------------------------------------------
            // For Handling Converting Odyssey Effects into Splash or Lingering
            if (item.hasOdysseyItemTag() && item.hasOdysseyEffectTag()) {
                if (resultMaterial == Material.LINGERING_POTION) { event.results[x] = createOdysseyLingeringPotion(item) }
                else { event.results[x] = createCustomBrewedPotion(resultMaterial, item) }
            }
            // For Custom Minecraft Potion Effects
            else if ((item.itemMeta as PotionMeta).hasCustomEffects() || item.hasCustomEffectTag()) {
                if (resultMaterial == Material.LINGERING_POTION) { event.results[x] = createCustomLingeringPotion(item) }
                else { event.results[x] = createCustomBrewedPotion(resultMaterial, item) }
            }
            // For Handling Changing Item Models
            else if (!item.hasOdysseyEffectTag() && potionModel != null) {
                event.results[x].itemMeta = event.results[x].itemMeta.also {
                    it.setCustomModelData(potionModel)
                }
            }
            // For Handling Changing Materials AND saving previous model FIX as it chooses one for all 3 slots
            else if (item.itemMeta.hasCustomModelData()) {
                event.results[x].itemMeta = event.results[x].itemMeta.also {
                    it.setCustomModelData(item.itemMeta.customModelData)
                }
            }
        }
    }

    private fun createCustomBrewedPotion(material: Material, oldPotion: ItemStack, model: Int? = null): ItemStack {
        return ItemStack(material, 1).apply {
            itemMeta = (oldPotion.itemMeta as PotionMeta).clone().also {
                if (model != null) it.setCustomModelData(model)
            }
        }
    }

    private fun createCustomLingeringPotion(oldPotion: ItemStack, model: Int? = null): ItemStack {
        return ItemStack(Material.LINGERING_POTION, 1).apply {
            itemMeta = (oldPotion.itemMeta as PotionMeta).clone().also {
                if (model != null) it.setCustomModelData(model)
                for (effect in it.customEffects) {
                    val newEffect = PotionEffect(effect.type, effect.duration, effect.amplifier)
                    it.addCustomEffect(newEffect, true)
                }
            }
        }
    }


    // (TODO: Fix for all: odyssey effects, custom effects,
    private fun makeExtendedPlusPotion(potion: ItemStack): ItemStack {
        if (potion.itemMeta !is PotionMeta) return potion
        if (potion.hasTag(ItemTags.IS_EXTENDED_PLUS)) return potion
        if (potion.hasTag(ItemTags.IS_UPGRADED_PLUS)) return potion
        val meta = (potion.itemMeta as PotionMeta)
        // (WIX)
        if (meta.basePotionData.isUpgraded) return potion
        if (!meta.basePotionData.isExtended) return potion
        val effect = potion.getEffectFromData()
        val newEffect = PotionEffect(effect.type, (effect.duration * 1.5).toInt(), effect.amplifier)
        meta.addCustomEffect(newEffect, true)
        meta.basePotionData = PotionData(PotionType.THICK)
        return potion.clone().also {
            it.itemMeta = meta
            it.addTag(ItemTags.IS_EXTENDED_PLUS)
        }
    }

    private fun makeUpgradedPlusPotion(potion: ItemStack): ItemStack {
        if (potion.itemMeta !is PotionMeta) return potion
        if (potion.hasTag(ItemTags.IS_EXTENDED_PLUS)) return potion
        if (potion.hasTag(ItemTags.IS_UPGRADED_PLUS)) return potion
        val meta = (potion.itemMeta as PotionMeta)
        // (WIX)
        if (meta.basePotionData.isExtended) return potion
        if (!meta.basePotionData.isUpgraded) return potion
        val effect = potion.getEffectFromData()
        val newEffect = PotionEffect(effect.type, effect.duration, effect.amplifier + 1)
        meta.addCustomEffect(newEffect, true)
        meta.basePotionData = PotionData(PotionType.THICK)
        return potion.clone().also {
            it.itemMeta = meta
            it.addTag(ItemTags.IS_UPGRADED_PLUS)
        }
    }

    private fun makeVialPotion(potion: ItemStack): ItemStack {
        if (potion.itemMeta !is PotionMeta) return potion
        if (potion.hasTag(ItemTags.IS_POTION_VIAL)) return potion
        // (WIX)
        val vial = createPotionVials(potion.clone())
        return vial
    }

}

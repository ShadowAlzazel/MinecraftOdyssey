package me.shadowalzazel.mcodyssey.common.listeners

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent
import me.shadowalzazel.mcodyssey.common.alchemy.BrewingManager
import me.shadowalzazel.mcodyssey.common.alchemy.CauldronManager
import me.shadowalzazel.mcodyssey.common.effects.EffectsManager
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Arrow
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

object AlchemyListener : Listener, EffectsManager, BrewingManager, CauldronManager {

    /*-----------------------------------------------------------------------------------------------*/
    // Sea Crystals -> Vials
    // Honey Bottle -> Extended+
    // Glow-berries -> Upgraded+
    // EXP Bottle -> Changes to Aura
    // Popped Chorus Fruit -> Concoctions

    @EventHandler
    fun brewingPotionHandler(event: BrewEvent) {
        event.contents.ingredient ?: return
        customBrewingHandler(event)
    }

    // Handler for cauldron mechanic
    @EventHandler
    fun cauldronWateredHandler(event: CauldronLevelChangeEvent) {
        // Sentry Clauses
        if (event.entity !is Player) return
        if (event.newState.type != Material.WATER_CAULDRON) return
        if (!(event.reason == CauldronLevelChangeEvent.ChangeReason.BOTTLE_EMPTY || event.reason == CauldronLevelChangeEvent.ChangeReason.BUCKET_EMPTY)) return
        // Run cauldron
        cauldronAlchemyHandler(event)
    }

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


}

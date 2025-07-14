package me.shadowalzazel.mcodyssey.common.listeners

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.effects.EffectsManager
import me.shadowalzazel.mcodyssey.common.tasks.item_tasks.AuraPotionEffect
import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.setIntTag
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.*
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Suppress("UnstableApiUsage")
object EffectListeners : Listener, EffectsManager {

    @EventHandler
    fun mainDamageHandler(event: EntityDamageEvent) {
        if (event.entity !is LivingEntity) return
        val entity = event.entity as LivingEntity
        // Accursed
        if (!entity.isDead) {
            if (entity.scoreboardTags.contains(EffectTags.ACCURSED)) event.damage *= 1.30
        }

        if (entity.scoreboardTags.contains(EffectTags.BARRIER)) {
            // maybe make a PDC
            event.damage = maxOf(event.damage - 3.0, 0.0)
        }

        if (entity.getIntTag(EntityTags.POLLEN_GUARD_STACKS) != null) {
            val pollen = entity.getIntTag(EntityTags.POLLEN_GUARD_STACKS) ?: 0
            event.damage = maxOf(event.damage - pollen, 0.0)
            entity.setIntTag(EntityTags.POLLEN_GUARD_STACKS, maxOf(pollen - 1, 0))
        }

    }


    @EventHandler
    fun hungerRegenHandler(event: FoodLevelChangeEvent) {
        if (event.entity.scoreboardTags.contains(EffectTags.MIASMA)) {
            event.isCancelled = true
            return
        }
    }

    @EventHandler
    fun healthRegenHandler(event: EntityRegainHealthEvent) {
        if (event.entity.scoreboardTags.contains(EffectTags.IRRADIATED)) {
            event.isCancelled = true
            return
        }
    }


    /*-----------------------------------------------------------------------------------------------*/

    // Main Consumption Handler
    @EventHandler
    fun potionConsumeHandler(event: PlayerItemConsumeEvent) {
        if (!event.item.hasItemMeta()) return
        if (event.item.itemMeta !is PotionMeta) return
        when(event.item.type) {
            Material.POTION -> {
                //potionDrinkHandler(event)
                auraPotionDrinking(event)
            }
            else -> {
                return
            }
        }
        return
    }

    fun getPotionEffects(item: ItemStack): List<PotionEffect>? {
        val potionData = item.getData(DataComponentTypes.POTION_CONTENTS) ?: return null
        return (potionData.potion()?.potionEffects ?: listOf<PotionEffect>()) + potionData.customEffects()
    }


    private fun auraPotionDrinking(event: PlayerItemConsumeEvent) {
        if (event.item.type != Material.POTION) return
        val item = event.item
        if (item.getItemIdentifier() != "aura_potion") return
        if (item.getData(DataComponentTypes.POTION_CONTENTS) == null) return
        // Task
        val effects = getPotionEffects(item) ?: return
        val task = AuraPotionEffect(event.player, effects, 20, Color.GREEN)
        // TODO: Stacking Aura Effects
        task.runTaskTimer(Odyssey.instance, 0, 20)
    }



    private fun potionDrinkHandler(event: PlayerItemConsumeEvent) {
        if (event.item.type != Material.POTION) return
        // Potion Item Tag Getters
        val item = event.item
        val isOdysseyEffect = item.hasOdysseyEffectTag() && item.hasItemIdTag()
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
        if (!event.potion.item.hasItemIdTag()) return
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
        if (!potionItem.hasItemIdTag()) return
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
        val equipment = event.player.equipment!!
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
package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.OdysseyUUIDs
import me.shadowalzazel.mcodyssey.listeners.tasks.UnstableAntimatterTask
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import me.shadowalzazel.mcodyssey.listeners.tasks.TemporalStasisTask
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

object OdysseyItemListeners : Listener {

    // Main Function dealing with Odyssey Crafting
    @EventHandler
    fun odysseyCraftingHandler(event: CraftItemEvent) {
        if (event.whoClicked is Player) {
            val somePlayer = event.whoClicked as Player
            if (somePlayer.gameMode != GameMode.SPECTATOR) {
                // Match
                when (event.inventory.result) {
                    OdysseyItems.PURE_ANTIMATTER_CRYSTAL.createItemStack(1) -> {
                        pureAntiMatterCrystalCrafting(somePlayer)
                        // TODO: REMOVE ITEM ON DEATH
                    }
                    OdysseyItems.FRUIT_OF_ERISHKIGAL.createItemStack(1) -> {
                        fruitOfErishkigalCrafting(somePlayer)
                    }
                    OdysseyItems.IRRADIATED_FRUIT.createItemStack(1) -> {
                        irradiatedFruitCrafting(somePlayer)
                    }
                    //OdysseyRecipes.IRRADIATED_FRUIT_RECIPE.result
                }
            }
        }
    }

    // Main function when dealing with custom items and their effects and not food
    @EventHandler
    fun consumingItemHandler(event: PlayerItemConsumeEvent) {
        if (event.item.hasItemMeta()) {
            if (event.item.itemMeta.hasLore()) {
                val somePlayer = event.player
                val someStackValue = event.item.amount
                // Match items
                when (event.item) {
                    // Fruit Of Erishkigal Health Boosts
                    OdysseyItems.FRUIT_OF_ERISHKIGAL.createItemStack(someStackValue) -> {
                        if (!extraHealthCalculator(somePlayer, OdysseyUUIDs.EXTRA_HEALTH_ERISHKIGAL_FRUIT)) { event.isCancelled }
                    }
                    OdysseyItems.IRRADIATED_FRUIT.createItemStack(someStackValue) -> {
                        if (!extraHealthCalculator(somePlayer, OdysseyUUIDs.EXTRA_HEALTH_IRRADIATED_FRUIT)) { event.isCancelled }
                        else {
                            somePlayer.addPotionEffects(listOf(
                                PotionEffect(PotionEffectType.HUNGER, 20 * 30, 1),
                                PotionEffect(PotionEffectType.WITHER, 20 * 30, 0),
                                PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 30, 1)
                            ))
                        }
                    }
                    OdysseyItems.SCULK_HEART.createItemStack(someStackValue) -> {
                        if (!extraHealthCalculator(somePlayer, OdysseyUUIDs.EXTRA_HEALTH_SCULK_HEART)) { event.isCancelled }
                        else {
                            somePlayer.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 20 * 30, 1))
                            somePlayer.damage(2.0)
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }


    // Main handler for events related to taking damage
    @EventHandler(priority = EventPriority.HIGH)
    fun takingDamageItemHandler(event: EntityDamageEvent) {
        if (event.entity is LivingEntity) {
            val someEntity = event.entity as LivingEntity
            if (someEntity.equipment?.itemInOffHand != null) {
                if (someEntity.equipment?.itemInOffHand == OdysseyItems.TOTEM_OF_VEXING.createItemStack(1)) {
                    event.damage = 0.0
                    if ((1..3).random() != 1) { someEntity.equipment!!.setItemInOffHand(ItemStack(Material.AIR, 1)) }
                    someEntity.world.playSound(someEntity.location, Sound.ITEM_TOTEM_USE, 2.5F, 0.2F)
                    someEntity.world.spawnParticle(Particle.CRIT_MAGIC, someEntity.location, 100, 0.5, 0.5, 0.5)
                }
            }
        }
    }

    // Main handler for events related to dropped items
    @EventHandler(priority = EventPriority.HIGH)
    fun itemDropHandler(event: PlayerDropItemEvent) {
        when (event.itemDrop.itemStack) {
            OdysseyItems.HOURGLASS_FROM_BABEL.createItemStack(1) -> {
                event.itemDrop.remove()
                hourglassDrop(event.player)
            }
        }
    }


    @EventHandler
    fun jumpHandler(event: PlayerJumpEvent) {
        if (event.player.scoreboardTags.contains("Temporal_Stasis")) {
            event.isCancelled = true
        }
    }


    @EventHandler
    fun playerLeaveHandler(event: PlayerQuitEvent) {
        val somePlayer = event.player

        if (somePlayer.scoreboardTags.contains("Temporal_Stasis")) {
            somePlayer.scoreboardTags.remove("Temporal_Stasis")
            somePlayer.isInvulnerable = false
        }
        else if (somePlayer.scoreboardTags.contains("Unstable_Crafting")) {
            somePlayer.scoreboardTags.remove("Unstable_Crafting")
            somePlayer.damage(314.15)
            println("Tried to leave!")
        }
    }

    /*------------------------------------------------------------------------*/
    // Functions for dropped items

    private fun hourglassDrop(eventPlayer: Player) {
        with(eventPlayer.world) {
            val blockLight = Particle.DustOptions(Color.fromBGR(231, 166, 95), 1.0F)
            val blockBreak = Material.GOLD_BLOCK.createBlockData()
            val blockDust = Material.GOLD_BLOCK.createBlockData()
            val someLocation = eventPlayer.location.clone().add(0.0, 0.35, 0.0)
            spawnParticle(Particle.REDSTONE, someLocation, 75, 0.95, 0.75, 0.95, blockLight)
            spawnParticle(Particle.BLOCK_CRACK, someLocation, 95, 0.95, 0.8, 0.95, blockBreak)
            spawnParticle(Particle.FALLING_DUST, someLocation, 35, 0.75, 0.25, 0.75, blockDust)
            playSound(someLocation, Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0F, 5.0F)
        }
            eventPlayer.also {
            it.isInvulnerable = true
            it.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20 * 8, 100))
            it.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 20 * 8, 100))
            it.addScoreboardTag("Temporal_Stasis")
        }
        val temporalStasisTask = TemporalStasisTask(eventPlayer)
        temporalStasisTask.runTaskLater(Odyssey.instance, 20 * 8)
    }


    /*------------------------------------------------------------------------*/
    // Helper functions for consuming items

    private fun extraHealthCalculator(eventPlayer: Player, healthUUID: UUID): Boolean {
        val healthMap = mapOf(
            OdysseyUUIDs.EXTRA_HEALTH_ERISHKIGAL_FRUIT to Pair("odyssey_extra_health_erishkigal", 4.0), // * 4
            OdysseyUUIDs.EXTRA_HEALTH_IRRADIATED_FRUIT to Pair("odyssey_extra_health_erishkigal", 2.0),
            OdysseyUUIDs.EXTRA_HEALTH_SCULK_HEART to Pair("odyssey_extra_health_sculk_heart", 2.0))

        // Get health
        val playerHealth = eventPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!

        var healthModifier: AttributeModifier? = null
        for (someModifier in playerHealth.modifiers) {
            if (someModifier.uniqueId == healthUUID) {
                healthModifier = someModifier
                break
            }
        }

        // Add health if new
        if (healthModifier != null) {
            // Currently 5 times regular amount
            return if (healthModifier.amount <= (healthMap[healthUUID]!!.second * 4.0)) {
                val newHealthModifier = AttributeModifier(healthUUID, healthMap[healthUUID]!!.first, healthModifier.amount + healthMap[healthUUID]!!.second, AttributeModifier.Operation.ADD_NUMBER)
                playerHealth.removeModifier(healthModifier)
                playerHealth.addModifier(newHealthModifier)
                // Sounds
                eventPlayer.playSound(eventPlayer.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.5F, 0.25F)
                eventPlayer.playSound(eventPlayer.location, Sound.BLOCK_BEACON_ACTIVATE, 2.5F, 0.25F)
                true
            } else {
                eventPlayer.sendActionBar(Component.text("You can not consume any more of this substance.", TextColor.color(255, 255, 85)))
                false
            }
        }
        else {
            val newHealthModifier = AttributeModifier(healthUUID, healthMap[healthUUID]!!.first, healthMap[healthUUID]!!.second, AttributeModifier.Operation.ADD_NUMBER)
            playerHealth.addModifier(newHealthModifier)
            // Sounds
            eventPlayer.playSound(eventPlayer.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.5F, 0.25F)
            eventPlayer.playSound(eventPlayer.location, Sound.BLOCK_BEACON_ACTIVATE, 2.5F, 0.25F)
            return true
        }
    }


    /*------------------------------------------------------------------------*/
    // Helper functions for completing crafting recipe

    private fun pureAntiMatterCrystalCrafting(eventPlayer: Player) {
        eventPlayer.playSound(eventPlayer.location, Sound.ENTITY_WITHER_SPAWN, 1.5F, 0.2F)
        if ("Unstable_Crafting" !in eventPlayer.scoreboardTags) {
            eventPlayer.scoreboardTags.add("Unstable_Crafting")
            val antimatterCraftingTask = UnstableAntimatterTask(eventPlayer)
            antimatterCraftingTask.runTaskTimer(Odyssey.instance, 0, 1)
        }
    }

    private fun irradiatedFruitCrafting(eventPlayer: Player) {
        with(eventPlayer) {
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.HUNGER, 20 * 30, 1),
                PotionEffect(PotionEffectType.WITHER, 20 * 30, 0),
                PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 30, 1))
            )
        }
    }

    private fun fruitOfErishkigalCrafting(eventPlayer: Player) {
        eventPlayer.playSound(eventPlayer.location, Sound.ENTITY_ENDER_EYE_DEATH, 1.5F, 0.15F)
        if ("Unstable_Crafting" in eventPlayer.scoreboardTags) {
            eventPlayer.scoreboardTags.remove("Unstable_Crafting")
            eventPlayer.scoreboardTags.add("Clear_Instability")
        }

    }
}
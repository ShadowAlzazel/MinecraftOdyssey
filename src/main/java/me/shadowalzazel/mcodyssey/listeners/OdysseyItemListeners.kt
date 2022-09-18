package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.listeners.tasks.UnstableAntimatterTask
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import me.shadowalzazel.mcodyssey.listeners.tasks.TemporalStasisTask
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
                    }
                    OdysseyItems.FRUIT_OF_ERISHKIGAL.createItemStack(1) -> {
                        fruitOfErishkigalCrafting(somePlayer)
                    }
                    OdysseyItems.IRRADIATED_FRUIT.createItemStack(1) -> {
                        with(somePlayer) {
                            addPotionEffects(listOf(
                                PotionEffect(PotionEffectType.HUNGER, 20 * 30, 1),
                                PotionEffect(PotionEffectType.WITHER, 20 * 30, 0),
                                PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 30, 1)
                            ))
                        }
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
                        // Get
                        val playerHealth = somePlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!
                        var hasExtraHealth = false
                        var someHealthModifier: AttributeModifier? = null
                        // Check modifiers
                        for (someModifier in playerHealth.modifiers) {
                            if (someModifier.name == "odyssey_extra_health_erishkigal") {
                                hasExtraHealth = true
                                someHealthModifier = someModifier
                            }
                        }
                        // Check if same boosts
                        if (hasExtraHealth) {
                            val healthCount = someHealthModifier!!.amount
                            if (healthCount <= 16.0) {
                                val erishkigalHealthBoost = AttributeModifier(UUID.fromString("c994412e-9e72-4881-a55f-1f2d1c95f125"), "odyssey_extra_health_erishkigal", 4.0 + healthCount, AttributeModifier.Operation.ADD_NUMBER)
                                playerHealth.removeModifier(someHealthModifier)
                                playerHealth.addModifier(erishkigalHealthBoost)
                                // Sound
                                somePlayer.playSound(somePlayer.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.5F, 0.25F)
                                somePlayer.playSound(somePlayer.location, Sound.BLOCK_BEACON_ACTIVATE, 2.5F, 0.25F)
                            }
                            else {
                                somePlayer.sendMessage("${ChatColor.GOLD}You can not consume any more of this substance...")
                                event.isCancelled = true
                            }
                        }
                        else {
                            val erishkigalHealthBoost = AttributeModifier(UUID.fromString("c994412e-9e72-4881-a55f-1f2d1c95f125"), "odyssey_extra_health_erishkigal", 4.0, AttributeModifier.Operation.ADD_NUMBER)
                            playerHealth.addModifier(erishkigalHealthBoost)
                            // Sound
                            somePlayer.playSound(somePlayer.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.5F, 0.25F)
                            somePlayer.playSound(somePlayer.location, Sound.BLOCK_BEACON_ACTIVATE, 2.5F, 0.25F)
                        }
                        println(playerHealth.modifiers)
                    }
                    OdysseyItems.IRRADIATED_FRUIT.createItemStack(someStackValue) -> {
                        // Get
                        val playerHealth = somePlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!
                        var hasExtraHealth = false
                        var someHealthModifier: AttributeModifier? = null
                        // Check modifiers
                        for (someModifier in playerHealth.modifiers) {
                            if (someModifier.name == "odyssey_extra_health_irradiated_fruit") {
                                hasExtraHealth = true
                                someHealthModifier = someModifier
                            }
                        }
                        // Check if same boosts
                        if (hasExtraHealth) {
                            val healthCount = someHealthModifier!!.amount
                            if (healthCount <= 8.0) {
                                val irradiatedHealthBoost = AttributeModifier(UUID.fromString("c994412e-9e72-4881-a55f-1f2d1c95f129"), "odyssey_extra_health_irradiated_fruit", 2.0 + healthCount, AttributeModifier.Operation.ADD_NUMBER)
                                playerHealth.removeModifier(someHealthModifier)
                                playerHealth.addModifier(irradiatedHealthBoost)
                                // Sound
                                somePlayer.playSound(somePlayer.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.5F, 0.25F)
                                somePlayer.playSound(somePlayer.location, Sound.BLOCK_BEACON_ACTIVATE, 2.5F, 0.25F)
                            }
                            else {
                                somePlayer.sendMessage("${ChatColor.GOLD}You can not consume any more of this substance...")
                                event.isCancelled = true
                            }
                        }
                        else {
                            val irradiatedHealthBoost = AttributeModifier(UUID.fromString("c994412e-9e72-4881-a55f-1f2d1c95f129"), "odyssey_extra_health_irradiated_fruit", 2.0, AttributeModifier.Operation.ADD_NUMBER)
                            playerHealth.addModifier(irradiatedHealthBoost)
                            // Sound
                            somePlayer.playSound(somePlayer.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.5F, 0.25F)
                            somePlayer.playSound(somePlayer.location, Sound.BLOCK_BEACON_ACTIVATE, 2.5F, 0.25F)
                        }
                        println(playerHealth.modifiers)
                        with(somePlayer) {
                            addPotionEffects(listOf(
                                PotionEffect(PotionEffectType.HUNGER, 20 * 30, 1),
                                PotionEffect(PotionEffectType.WITHER, 20 * 30, 0),
                                PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 30, 1)
                            ))
                        }
                    }
                    //
                    else -> {
                    }
                }
            }
        }
    }


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

    @EventHandler
    fun itemDropHandler(event: PlayerDropItemEvent) {
        when (event.itemDrop.itemStack) {
            OdysseyItems.HOURGLASS_FROM_BABEL.createItemStack(1) -> {
                event.itemDrop.remove()
                with(event.player.world) {
                    val blockLight = Particle.DustOptions(Color.fromBGR(231, 166, 95), 1.0F)
                    val blockBreak = Material.GOLD_BLOCK.createBlockData()
                    val blockDust = Material.GOLD_BLOCK.createBlockData()
                    val someLocation = event.player.location.clone().add(0.0, 0.35, 0.0)
                    spawnParticle(Particle.REDSTONE , someLocation, 75, 0.95, 0.75, 0.95, blockLight)
                    spawnParticle(Particle.BLOCK_CRACK, someLocation, 95, 0.95, 0.8, 0.95, blockBreak)
                    spawnParticle(Particle.FALLING_DUST, someLocation, 35, 0.75, 0.25, 0.75, blockDust)
                    playSound(someLocation, Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0F, 5.0F)
                }
                event.player.also {
                    it.isInvulnerable = true
                    it.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20 * 5, 100))
                    it.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 20 * 5, 100))
                    it.addScoreboardTag("Temporal_Stasis")
                }
                val temporalStasisTask = TemporalStasisTask(event.player)
                temporalStasisTask.runTaskLater(MinecraftOdyssey.instance, 20 * 5)
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

    // PURE_ANTI_MATTER_CRYSTAL_RECIPE_CRAFTING
    private fun pureAntiMatterCrystalCrafting(eventPlayer: Player) {
        eventPlayer.playSound(eventPlayer.location, Sound.ENTITY_WITHER_SPAWN, 1.5F, 0.2F)
        if ("Unstable_Crafting" !in eventPlayer.scoreboardTags) {
            eventPlayer.scoreboardTags.add("Unstable_Crafting")
            val antimatterCraftingTask = UnstableAntimatterTask(eventPlayer)
            antimatterCraftingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 1)
        }
    }

    // FRUIT_OF_ERISHKIGAL_RECIPE_CRAFTING
    private fun fruitOfErishkigalCrafting(eventPlayer: Player) {
        eventPlayer.playSound(eventPlayer.location, Sound.ENTITY_ENDER_EYE_DEATH, 1.5F, 0.15F)
        if ("Unstable_Crafting" in eventPlayer.scoreboardTags) {
            eventPlayer.scoreboardTags.remove("Unstable_Crafting")
            eventPlayer.scoreboardTags.add("Clear_Instability")
        }

    }
}
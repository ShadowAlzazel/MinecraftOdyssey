package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.weather.LightningStrikeEvent
import org.bukkit.inventory.meta.FireworkMeta

object OtherListeners : Listener {



    @EventHandler
    fun preventHelmetPlace(event: BlockPlaceEvent) {
        if (!event.itemInHand.hasItemMeta()) return
        if (event.blockPlaced.type != Material.CARVED_PUMPKIN) return
        if (event.itemInHand.itemMeta!!.hasCustomModelData()) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun chargedCreeperDeath(event: EntityDeathEvent) {

    }

    //EventHandler
    fun elytraBoost(event: PlayerElytraBoostEvent) {
        if ((event.itemStack.itemMeta!! as FireworkMeta).power > 3) {
            var boostFailureChance = 0.05 + ((event.itemStack.itemMeta as FireworkMeta).power * 0.05) // FOR DURATION
            var boostFailureDamage = 3.0
            var dudChance = 0.1
            val loreComponent = listOf(Component.text("Danger!", TextColor.color(255, 55, 55)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))

            // Maybe
            if (event.itemStack.lore()?.contains(Component.text("Safe!")) == true) { // Change to detect component var
                boostFailureDamage -= 2.0
            }
            if ((boostFailureChance * 100) > (0..100).random()) {
                event.firework.detonate()
                createDetonatingFirework(event.player.location)
                event.player.damage(boostFailureDamage)
            }
        }
    }


    // WHEN CRAFTING
    // DETECT IF ROCKET

    private fun createDetonatingFirework(targetLocation: Location): Firework {
        val randomColors = listOf(Color.BLUE, Color.RED, Color.YELLOW, Color.FUCHSIA, Color.AQUA)
        val superFirework: Firework = (targetLocation.world.spawnEntity(targetLocation, EntityType.FIREWORK) as Firework).apply {
            fireworkMeta = fireworkMeta.clone().also {
                it.addEffect(
                    FireworkEffect.builder()
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .withColor(randomColors.random())
                    .withFade(randomColors.random())
                    .trail(true)
                    .flicker(true)
                    .build()
                )
            }
            fireworkMeta.power = 1
            ticksToDetonate = 0
        }
        return superFirework
    }

}
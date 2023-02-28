package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Snowman
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.weather.LightningStrikeEvent
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object OdysseyMiscListeners : Listener {

    // Check if Ender Dragon Dies
    @EventHandler
    fun onDefeatEnderDragon(event: EntityDeathEvent) {
        val dragon = event.entity
        val endWorld = event.entity.world
        if (dragon.type == EntityType.ENDER_DRAGON) {
            for (aPlayer in endWorld.players) {
                aPlayer.sendMessage("${ChatColor.DARK_PURPLE}The end has just begun ...")

            }
        }
    }

    @EventHandler
    fun chargeAmethyst(event: LightningStrikeEvent) {
        if (event.cause != LightningStrikeEvent.Cause.TRIDENT) {
            // DO CHARGE? ->
        }
    }

    // TEST
    // Elytra Mechanics
    @EventHandler
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


    // WHEN CRAFTING
    // DETECT IF ROCKET

}
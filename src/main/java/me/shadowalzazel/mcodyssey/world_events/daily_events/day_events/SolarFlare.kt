package me.shadowalzazel.mcodyssey.world_events.daily_events.day_events

import me.shadowalzazel.mcodyssey.world_events.daily_events.ActivationTime
import me.shadowalzazel.mcodyssey.world_events.daily_events.DailyWorldEvent
import me.shadowalzazel.mcodyssey.world_events.utility.EntityConditions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class SolarFlare : DailyWorldEvent(
    "solar_flare",
    4,
    4,
    ActivationTime.Day,
    listOf(EntityConditions.AlwaysTrue),
) {

    override fun activationHandler(world: World) {
        val text = "The sun shimmers and flashes extremely for a moment!"
        val message = Component.text(text, TextColor.color(226, 217, 122))
        world.messagePlayers(message)
        // Effects
        val potionEffects = listOf(PotionEffect(PotionEffectType.WEAKNESS, 20 * 120, 0))
        world.players.forEach {
            it.spawnParticle(Particle.LAVA, it.location, 14, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.ENTITY_BLAZE_BURN, 2.5F, 1.5F)
            if (it.location.block.lightFromSky >= 12) {
                it.addPotionEffects(potionEffects)
            }
        }

    }


}
package me.shadowalzazel.mcodyssey.phenomenon.utuPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.utility.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.utility.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object CometSighting : OdysseyPhenomenon("Comet Sighting",
    PhenomenonTypes.UTU,
    40,
    5,
    25,
    55,
    Component.text("A faint object is seen in the sky...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("A comet can be seen at ${someWorld.name}!")

        // Effects
        val cometLightEffect = PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 0)
        val cometInspireEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 12000, 0)
        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                when((0..2).random()) {
                    in 0..1 -> {
                        somePlayer.addPotionEffect(cometLightEffect)
                        sendMessage(Component.text("A bright comet is seen in the sky...", TextColor.color(167, 222, 255)))
                    }
                    else -> {
                        somePlayer.addPotionEffects(listOf(cometInspireEffect, cometLightEffect))
                        sendMessage(Component.text("The bright comet in the sky inspires you...", TextColor.color(167, 222, 255)))
                    }
                }
                spawnParticle(Particle.CRIT, location, 15, 0.5, 0.5, 0.5)
                playSound(location, Sound.ENTITY_ARROW_HIT_PLAYER, 2.5F, 1.5F)
            }
        }
    }

}
package me.shadowalzazel.mcodyssey.phenomenon.solar_phenomena

import me.shadowalzazel.mcodyssey.phenomenon.base.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.base.PhenomenonType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object CometSighting : OdysseyPhenomenon("Comet Sighting",
    PhenomenonType.SOLAR,
    40,
    5,
    25,
    55,
    Component.text("A faint object is seen in the sky...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)

        // Effects
        val cometLightEffect = PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 0)
        val cometInspireEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 12000, 0)
        someWorld.players.forEach {
            when((0..2).random()) {
                in 0..1 -> {
                    it.addPotionEffect(cometLightEffect)
                    it.sendMessage(Component.text("A bright comet is seen in the sky...", TextColor.color(167, 222, 255)))
                }
                else -> {
                    it.addPotionEffects(listOf(cometInspireEffect, cometLightEffect))
                    it.sendMessage(Component.text("The bright comet in the sky inspires you...", TextColor.color(167, 222, 255)))
                }
            }
            it.spawnParticle(Particle.CRIT, it.location, 15, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.ENTITY_ARROW_HIT_PLAYER, 2.5F, 1.5F)
        }
    }

}
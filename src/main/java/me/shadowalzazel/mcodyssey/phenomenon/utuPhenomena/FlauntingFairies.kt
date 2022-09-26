package me.shadowalzazel.mcodyssey.phenomenon.utuPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.odyssey.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object FlauntingFairies : OdysseyPhenomenon("Flaunting Fairies",
    PhenomenonTypes.SUEN,
    45,
    5,
    15,
    55,
    Component.text("You think you spot a fairy whizzing through the air but it soon vanishes...")) {


    // TODO: Sine Wave fairy particle

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("The fairies escape at ${someWorld.name}!")

        // Effects for Fairy Follow Day
        val followEffect = PotionEffect(PotionEffectType.REGENERATION, 12000, 0)
        val luckEffect = PotionEffect(PotionEffectType.LUCK, 12000, 0)
        val loveEffect = PotionEffect(PotionEffectType.HEALTH_BOOST, 12000, 0)
        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                when((0..3).random()) {
                    3 -> {
                        addPotionEffects(listOf(loveEffect, luckEffect, followEffect))
                        sendMessage(Component.text("A beautiful fairy has fallen in with you!", TextColor.color(167, 222, 255)))
                    }
                    in 1..2 -> {
                        addPotionEffects(listOf(luckEffect, followEffect))
                        sendMessage(Component.text("A fairy has taken quite a liking to you!", TextColor.color(167, 222, 255)))
                    }
                    else -> {
                        sendMessage(Component.text("A couple fairies flaunt with you but soon disappear...", TextColor.color(167, 222, 255)))
                    }
                }
            }
            with(somePlayer.world) {
                spawnParticle(Particle.CRIT, somePlayer.location, 15, 0.5, 0.5, 0.5)
                playSound(somePlayer.location, Sound.ENTITY_ARROW_HIT_PLAYER, 2.5F, 1.5F)
            }
        }
    }

}

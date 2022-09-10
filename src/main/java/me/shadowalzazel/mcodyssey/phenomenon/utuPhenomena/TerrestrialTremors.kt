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

object TerrestrialTremors : OdysseyPhenomenon("Terrestrial Tremors",
    PhenomenonTypes.ANKI,
    5,
    3,
    5) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("A large tremor has hit ${someWorld.name}!")
        val tremorMagnitude = (1..10).random()

        if (someWorld.players.size >= 1) {
            // Tremors Effects
            val shakeEffect = PotionEffect(PotionEffectType.SLOW, 20 * (3 * tremorMagnitude), 1)
            val tremorEffect = PotionEffect(PotionEffectType.SLOW, 20 * (5 * tremorMagnitude), 2)
            val trembleFatigueEffect = PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * (5 * tremorMagnitude), 1)
            val trembleWeaknessEffect = PotionEffect(PotionEffectType.WEAKNESS, 20 * (5 * tremorMagnitude), 0)
            val trembleConfusionEffect = PotionEffect(PotionEffectType.CONFUSION, 20 * (4 * tremorMagnitude), 1)

            // Get epicenter
            val randomPlayer = someWorld.players.random()
            val randomEpicenter = randomPlayer.location.clone().add((-64..64).random().toDouble(), -30.0, (-64..64).random().toDouble())
            for (somePlayer in someWorld.players) {
                with(somePlayer) {
                    if (!somePlayer.isFlying) {
                        // Math
                        val distanceFromEpicenter = randomEpicenter.distance(somePlayer.location)
                        val distanceFactor: Double = tremorMagnitude.div((((distanceFromEpicenter - 1000) / 100) + 1))

                        if (distanceFromEpicenter < 40) {
                            damage(tremorMagnitude.toDouble())
                            addPotionEffects(listOf(tremorEffect, trembleConfusionEffect, trembleWeaknessEffect, trembleFatigueEffect))
                            sendMessage(Component.text("THE GROUND TREMBLES UNDER YOU!!!", TextColor.color(67, 67, 67)))
                            spawnParticle(Particle.CRIT, somePlayer.location, 65, 0.5, 0.5, 0.5)
                            playSound(somePlayer.location, Sound.BLOCK_BASALT_BREAK, 5.5F, 1.2F)
                        }
                        else if (distanceFromEpicenter < 800) {
                            damage(tremorMagnitude.toDouble() * distanceFactor)
                            addPotionEffects(listOf(shakeEffect, trembleConfusionEffect))
                            sendMessage(Component.text("Your at the mercy of a tremor!", TextColor.color(67, 67, 67)))
                            spawnParticle(Particle.CRIT, somePlayer.location, 15, 0.5, 0.5, 0.5)
                        }
                        else {
                            sendMessage(Component.text("You hear tremors from afar", TextColor.color(67, 67, 67)))
                        }
                    }
                    else {
                        sendMessage(Component.text("The ground seems to ripple from this height...", TextColor.color(67, 67, 67)))
                    }
                }
            }
        }
    }

}


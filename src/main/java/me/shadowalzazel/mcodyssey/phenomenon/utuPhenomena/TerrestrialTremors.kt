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

object TerrestrialTremors : OdysseyPhenomenon("Starry Night",
    PhenomenonTypes.ANKI,
    20,
    4,
    null,
    Component.text("There is a faint glow emanating from the flora...")) {

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
                        val distanceFactor = tremorMagnitude / ((distanceFromEpicenter / 100) + 1)



                        if (true) {


                        }
                        else {
                            sendMessage(Component.text("The ground seems to ripple from this height...", TextColor.color(67, 67, 67)))
                        }
                    }
                    else {
                        sendMessage(Component.text("The ground seems to ripple from this height...", TextColor.color(67, 67, 67)))
                    }
                }
                with(somePlayer.world) {
                    spawnParticle(Particle.CRIT, somePlayer.location, 15, 0.5, 0.5, 0.5)
                    playSound(somePlayer.location, Sound.ENTITY_ARROW_HIT_PLAYER, 2.5F, 1.5F)
                }

            }

        }

    }

}


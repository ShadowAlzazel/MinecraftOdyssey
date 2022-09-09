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

object SpiritsRelease : OdysseyPhenomenon("Spirits Release",
    PhenomenonTypes.SUEN,
    45,
    5,
    9,
    55,
    Component.text("The animals seem restless...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("The spirits have chosen champions at ${someWorld.name}!")

        // Effects for Fairy Follow Day
        val bearSpiritEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 12000, 1)
        val ramSpiritEffect = PotionEffect(PotionEffectType.SPEED, 12000, 1)
        val phoenixSpiritEffect = PotionEffect(PotionEffectType.SATURATION, 12000, 1)
        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                when((0..7).random()) {
                    in 6..7 -> {
                        addPotionEffect(bearSpiritEffect)
                        sendMessage(Component.text("A bear spirit has decided to shadow you today...", TextColor.color(167, 222, 255)))
                        spawnParticle(Particle.CRIT, somePlayer.location, 15, 0.5, 0.5, 0.5)
                        playSound(somePlayer.location, Sound.ENTITY_ARROW_HIT_PLAYER, 2.5F, 1.5F)
                    }
                    in 4..5 -> {
                        addPotionEffect(ramSpiritEffect)
                        sendMessage(Component.text("The boar spirits roars from within!", TextColor.color(167, 222, 255)))
                        spawnParticle(Particle.CRIT, somePlayer.location, 15, 0.5, 0.5, 0.5)
                        playSound(somePlayer.location, Sound.ENTITY_ARROW_HIT_PLAYER, 2.5F, 1.5F)
                    }
                    in 2..3 -> {
                        addPotionEffect(phoenixSpiritEffect)
                        sendMessage(Component.text("The phoenix erupts from thyself...", TextColor.color(167, 222, 255)))
                        spawnParticle(Particle.CRIT, somePlayer.location, 15, 0.5, 0.5, 0.5)
                        playSound(somePlayer.location, Sound.ENTITY_ARROW_HIT_PLAYER, 2.5F, 1.5F)
                    }
                    else -> {
                        sendMessage(Component.text("You sense animals become restless...", TextColor.color(167, 222, 255)))
                    }
                }
            }
        }
    }

}
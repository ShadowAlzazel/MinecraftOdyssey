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

object BreezyWinds : OdysseyPhenomenon("Breezy Winds",
    PhenomenonTypes.UTU,
    45,
    5,
    30,
    55,
    Component.text("The winds seem to howl...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("A swift wind is happening at ${someWorld.name}!")

        // Player Effects
        val breezeStrength = (0..2).random()
        val breezeEffect = PotionEffect(PotionEffectType.SPEED, 12000, breezeStrength)
        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                addPotionEffect(breezeEffect)
                sendMessage(Component.text("A swift wind follows your side...", TextColor.color(56, 127, 232)))
                spawnParticle(Particle.SPIT, location, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.EXPLOSION_NORMAL, location, 5, 0.5, 0.5, 0.5)
                playSound(location, Sound.ITEM_TRIDENT_RIPTIDE_3, 2.5F, 1.5F)
            }
        }
    }

}
package me.shadowalzazel.mcodyssey.phenomenon.suenPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.utility.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.utility.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object DanceOfTheBioluminescent : OdysseyPhenomenon("Dance of the Bioluminescent",
    PhenomenonTypes.SUEN,
    45,
    5,
    55,
    Component.text("There is a faint glow emanating from the flora...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("The bioluminescent creatures glow at ${someWorld.name}!")

        // Player Effects
        val bioluminescentEffect = PotionEffect(PotionEffectType.GLOWING, 12000, 1)
        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                addPotionEffect(bioluminescentEffect)
                sendMessage(Component.text("Small life forms cling to you and glow...", TextColor.color(46, 181, 204)))
            }
            with(somePlayer.world) {
                val someLocation = somePlayer.location
                spawnParticle(Particle.GLOW_SQUID_INK, someLocation, 14, 0.5, 0.5, 0.5)
                playSound(someLocation, Sound.ENTITY_SILVERFISH_AMBIENT, 2.5F, 0.5F)
            }
        }

    }

}
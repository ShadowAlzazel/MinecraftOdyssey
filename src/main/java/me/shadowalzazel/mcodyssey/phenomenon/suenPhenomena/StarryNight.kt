package me.shadowalzazel.mcodyssey.phenomenon.suenPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.odyssey.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object StarryNight : OdysseyPhenomenon("Starry Night",
    PhenomenonTypes.SUEN,
    15,
    5,
    30,
    55,
    Component.text("The stars seem much closer...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("The star falls at ${someWorld.name}!")

        // Player Effects
        val starryNightEffect = PotionEffect(PotionEffectType.LUCK, 12000, (0..1).random())
        someWorld.players.forEach {
            it.addPotionEffect(starryNightEffect)
            it.sendMessage(Component.text("The stars are falling from the sky!?", TextColor.color(156, 127, 192)))
            it.spawnParticle(Particle.SCRAPE, it.location, 5, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.BLOCK_NOTE_BLOCK_CHIME, 2.5F, 1.5F)
        }

        // TODO: Star falling
    }
}
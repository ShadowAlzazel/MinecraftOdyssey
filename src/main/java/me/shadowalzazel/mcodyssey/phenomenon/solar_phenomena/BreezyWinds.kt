package me.shadowalzazel.mcodyssey.phenomenon.solar_phenomena

import me.shadowalzazel.mcodyssey.phenomenon.base.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.base.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BreezyWinds : OdysseyPhenomenon("Breezy Winds",
    PhenomenonTypes.SOLAR,
    45,
    5,
    30,
    55,
    Component.text("The winds seem to howl...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)

        // Player Effects
        val breezeStrength = (0..2).random()
        val breezeEffect = PotionEffect(PotionEffectType.SPEED, 12000, breezeStrength)

        someWorld.players.forEach {
            it.addPotionEffect(breezeEffect)
            it.sendMessage(Component.text("A swift wind follows your side...", TextColor.color(56, 127, 232)))
            it.spawnParticle(Particle.SPIT, it.location, 15, 0.5, 0.5, 0.5)
            it.spawnParticle(Particle.EXPLOSION_NORMAL, it.location, 5, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.ITEM_TRIDENT_RIPTIDE_3, 2.5F, 1.5F)
        }

    }

}
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

object SpiritsRelease : OdysseyPhenomenon("Spirits Release",
    PhenomenonType.LUNAR,
    45,
    5,
    9,
    55,
    Component.text("The animals seem restless...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)

        // Effects for Fairy Follow Day
        val bearSpiritEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 12000, 1)
        val ramSpiritEffect = PotionEffect(PotionEffectType.SPEED, 12000, 1)
        val phoenixSpiritEffect = PotionEffect(PotionEffectType.SATURATION, 12000, 1)

       someWorld.players.forEach {
           when((0..7).random()) {
               in 6..7 -> {
                   it.addPotionEffect(bearSpiritEffect)
                   it.sendMessage(Component.text("A bear spirit has decided to shadow you today...", TextColor.color(167, 222, 255)))
               }
               in 4..5 -> {
                   it.addPotionEffect(ramSpiritEffect)
                   it.sendMessage(Component.text("The boar spirits roars from within!", TextColor.color(227, 142, 145)))
               }
               in 2..3 -> {
                   it.addPotionEffect(phoenixSpiritEffect)
                   it.sendMessage(Component.text("The phoenix erupts from thyself...", TextColor.color(127, 172, 255)))
               }
               else -> {
                   it.sendMessage(Component.text("You sense animals become restless...", TextColor.color(255, 255, 255)))
               }
           }
           it.spawnParticle(Particle.CRIT, it.location, 35, 0.5, 0.5, 0.5)
           it.playSound(it.location, Sound.ENTITY_ARROW_HIT_PLAYER, 2.5F, 1.5F)
       }

    }

}
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

object FlauntingFairies : OdysseyPhenomenon("Flaunting Fairies",
    PhenomenonType.LUNAR,
    45,
    5,
    15,
    55,
    Component.text("You think you spot a fairy whizzing through the air but it soon vanishes...")) {


    // TODO: Sine Wave fairy particle

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)

        // Effects for Fairy Follow Day
        val followEffect = PotionEffect(PotionEffectType.REGENERATION, 12000, 0)
        val luckEffect = PotionEffect(PotionEffectType.LUCK, 12000, 0)
        val loveEffect = PotionEffect(PotionEffectType.HEALTH_BOOST, 12000, 0)

        someWorld.players.forEach {
            when((0..3).random()) {
                3 -> {
                    it.addPotionEffects(listOf(loveEffect, luckEffect, followEffect))
                    it.sendMessage(Component.text("A beautiful fairy has fallen in with you!", TextColor.color(167, 112, 255)))
                    it.spawnParticle(Particle.HEART, it.location, 14, 0.5, 0.5, 0.5)
                }
                in 1..2 -> {
                    it.addPotionEffects(listOf(luckEffect, followEffect))
                    it.sendMessage(Component.text("A fairy has taken quite a liking to you!", TextColor.color(167, 112, 255)))
                    it.spawnParticle(Particle.HAPPY_VILLAGER, it.location, 14, 0.5, 0.5, 0.5)
                }
                else -> {
                    it.sendMessage(Component.text("A couple fairies flaunt with you but soon disappear...", TextColor.color(167, 112, 255)))
                }
            }
            it.spawnParticle(Particle.CRIT, it.location, 14, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.ENTITY_ARROW_HIT_PLAYER, 2.5F, 1.5F)

        }

    }

}

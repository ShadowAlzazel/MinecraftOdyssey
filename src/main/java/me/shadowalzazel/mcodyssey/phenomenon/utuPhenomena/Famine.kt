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

object Famine : OdysseyPhenomenon("Famine",
    PhenomenonTypes.SUEN,
    15,
    4,
    7,
    40,
    Component.text("The food tastes much more sour...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("A famine is happening at ${someWorld.name}!")

        // Effects
        val famineEffect = PotionEffect(PotionEffectType.HUNGER, 2000, 0)
        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                addPotionEffect(famineEffect)
                foodLevel = 5
                sendMessage(Component.text("The food is rotten and spoiled...", TextColor.color(127, 122, 55)))
                spawnParticle(Particle.CRIT, location, 15, 0.5, 0.5, 0.5)
                playSound(location, Sound.ENTITY_ARROW_HIT_PLAYER, 2.5F, 1.5F)
            }
        }
    }



}
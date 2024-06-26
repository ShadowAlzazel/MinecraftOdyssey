package me.shadowalzazel.mcodyssey.phenomenon.lunar_phenomena

import me.shadowalzazel.mcodyssey.phenomenon.base.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.base.PhenomenonType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BlueMoon : OdysseyPhenomenon("Blood Moon",
    PhenomenonType.LUNAR,
    13,
    4,
    10) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("A blue moon is happening at ${someWorld.name}!")

        // Blood Moon Effects
        val blueMoonEffects = listOf(PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 0))

        someWorld.players.forEach {
            it.addPotionEffects(blueMoonEffects)
            it.sendMessage(Component.text("A blue moon illuminates the night...", TextColor.color(136, 143, 255)))
            it.spawnParticle(Particle.ENTITY_EFFECT, it.location, 15, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 2.5F, 0.5F)
        }

    }

}
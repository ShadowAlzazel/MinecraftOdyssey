package me.shadowalzazel.mcodyssey.phenomenon.suenPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.odyssey.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object AcidicDownpour : OdysseyPhenomenon("Acidic Downpour",
    PhenomenonTypes.SUEN,
    6,
    3,
    5) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("Acid rain down pours on ${someWorld.name}!")

        // Player Effects
        val acidicEffect = PotionEffect(PotionEffectType.WEAKNESS, 20 * 20, 0)

        someWorld.weatherDuration = 20 * 700

        someWorld.players.forEach {
            it.addPotionEffect(acidicEffect)
            it.sendMessage(Component.text("An acid rain pours from the sky...", TextColor.color(26, 37, 22)))
            it.spawnParticle(Particle.SQUID_INK, it.location, 15, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.BLOCK_SLIME_BLOCK_PLACE, 2.5F, 1.5F)
            it.playSound(it.location, Sound.ENTITY_FOX_SCREECH, 2.5F, 0.5F)
            it.spawnParticle(Particle.BLOCK_CRACK, it.location, 95, 0.95, 0.8, 0.95, Material.GREEN_CONCRETE.createBlockData())
        }

    }

    override fun persistentSpawningActives(someEntity: LivingEntity) {
        if (someEntity.location.block.lightFromSky > 3) {
            someEntity.addPotionEffects(listOf(
                PotionEffect(PotionEffectType.WEAKNESS, 20 * 60, 0),
                PotionEffect(PotionEffectType.POISON, 20 * 60, 0)) )
        }
    }


    override fun persistentPlayerActives(someWorld: World) {
        val acidicEffects = listOf(
            PotionEffect(PotionEffectType.WEAKNESS, 20 * 10, 0),
            PotionEffect(PotionEffectType.POISON, 20 * 10, 0),
            PotionEffect(PotionEffectType.HUNGER, 20 * 10, 0))

        someWorld.players.forEach {
            if (it.location.block.lightFromSky > 5 && it.isInRain) {
                if (!allayMitigation(it)) {
                    it.addPotionEffects(acidicEffects)
                }
            }
        }
    }

}
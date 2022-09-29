package me.shadowalzazel.mcodyssey.phenomenon.utuPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.odyssey.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Slime
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object SlimeShower : OdysseyPhenomenon("Slime Shower",
    PhenomenonTypes.SUEN,
    70,
    5,
    15,
    55,
    Component.text("There are strange flashes in the sky...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("Slime is falling from the Sky at ${someWorld.name}!")

        someWorld.players.forEach {
            it.sendMessage(Component.text("Slime is falling from the sky?", TextColor.color(107, 162, 105)))
        }

    }

    override fun persistentPlayerActives(someWorld: World) {
        // Effects
        val fallingSlimeEffects = listOf(
            PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 30, 1),
            PotionEffect(PotionEffectType.REGENERATION, 20 * 600, 1),
            PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 600, 1),
            PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 600, 1))
        //
        someWorld.players.forEach {
            if (it.location.block.lightFromSky > 9 && it.location.y > 63.0) {
                (it.world.spawnEntity(it.location.clone().add((-16..16).random().toDouble(), 32.0, (-16..16).random().toDouble()), EntityType.SLIME) as Slime).also { slime ->
                    slime.addPotionEffects(fallingSlimeEffects)
                    slime.health += 8.0
                }
            }
        }
    }

}
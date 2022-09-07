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

object GravityShift : OdysseyPhenomenon("Starry Night",
    PhenomenonTypes.SUEN,
    30,
    4,
    55,
    Component.text("It is as the world is pulling on you less...")) {

    // Make spheres

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("The gravity shifts at ${someWorld.name}!")

        // Low Gravity Effects
        val lowGravityEffects = listOf(
            PotionEffect(PotionEffectType.JUMP, 12000, 2),
            PotionEffect(PotionEffectType.SLOW_FALLING, 12000, 1))
        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                addPotionEffects(lowGravityEffects)
                sendMessage(Component.text("${server.name} experiencing a relativistic shift and unstable gravity zones!", TextColor.color(56, 127, 232)))
                spawnParticle(Particle.SPIT, location, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.EXPLOSION_NORMAL, location, 5, 0.5, 0.5, 0.5)
                playSound(location, Sound.ITEM_TRIDENT_RIPTIDE_3, 2.5F, 1.5F)
            }
        }
    }

    // create bubbles that if near do more crazy stuff

}
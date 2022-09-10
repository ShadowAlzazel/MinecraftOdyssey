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

object SolarFlare : OdysseyPhenomenon("Starry Night",
    PhenomenonTypes.SUEN,
    25,
    4,
    7,
    55,
    Component.text("The day star seems brighter than usual...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("A Solar Flare has hit at ${someWorld.name}!")

        // Solar Flare Effects
        val solarFlareEffects = listOf(
            PotionEffect(PotionEffectType.WEAKNESS, 12000, 0),
            PotionEffect(PotionEffectType.FIRE_RESISTANCE, 12000, 0))
        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                addPotionEffects(solarFlareEffects)
                sendMessage(Component.text("A minor solar flare just hit the world!", TextColor.color(176, 147, 82)))
                spawnParticle(Particle.SPIT, location, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.EXPLOSION_NORMAL, location, 5, 0.5, 0.5, 0.5)
                playSound(location, Sound.ITEM_TRIDENT_RIPTIDE_3, 2.5F, 1.5F)
            }
        }
    }

}
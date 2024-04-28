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

object SolarFlare : OdysseyPhenomenon("Solar Flare",
    PhenomenonType.LUNAR,
    25,
    4,
    7,
    55,
    Component.text("The day star seems brighter than usual...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)

        // Solar Flare Effects
        val solarFlareEffects = listOf(
            PotionEffect(PotionEffectType.WEAKNESS, 12000, 0),
            PotionEffect(PotionEffectType.FIRE_RESISTANCE, 12000, 0))

        someWorld.players.forEach {
            it.sendMessage(Component.text("A minor solar flare just hit the world!", TextColor.color(176, 147, 82)))
            it.spawnParticle(Particle.EXPLOSION, it.location, 35, 0.5, 0.5, 0.5)
            it.spawnParticle(Particle.LAVA, it.location, 14, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.ITEM_TRIDENT_RIPTIDE_3, 2.5F, 1.5F)
            it.playSound(it.location, Sound.BLOCK_LAVA_AMBIENT, 2.5F, 0.5F)

            if (it.location.block.lightFromSky >= 14) {
                it.fireTicks = 30
                it.addPotionEffect(solarFlareEffects[0])
                it.damage(0.5)
            }
            it.addPotionEffect(solarFlareEffects[1])
        }
    }

}
package me.shadowalzazel.mcodyssey.phenomenon.suenPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.utility.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.utility.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.*
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BlueMoon : OdysseyPhenomenon("Blood Moon",
    PhenomenonTypes.SUEN,
    12,
    3) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("A blue moon is happening at ${someWorld.name}!")

        // Blood Moon Effects
        val blueMoonEffects = listOf(
            PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 0),
            PotionEffect(PotionEffectType.LUCK, 12000, 1))
        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                addPotionEffects(blueMoonEffects)
                sendMessage(Component.text("A blue moon illuminates the night...", TextColor.color(136, 143, 255)))
            }
            with(somePlayer.world) {
                val someLocation = somePlayer.location
                spawnParticle(Particle.SPELL_MOB_AMBIENT, someLocation, 15, 0.5, 0.5, 0.5)
                playSound(someLocation, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 2.5F, 0.5F)
            }
        }
    }

}
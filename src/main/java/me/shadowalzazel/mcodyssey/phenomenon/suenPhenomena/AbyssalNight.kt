package me.shadowalzazel.mcodyssey.phenomenon.suenPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.utility.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.utility.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object AbyssalNight : OdysseyPhenomenon("Abyssal Night",
    PhenomenonTypes.SUEN,
    15,
    4,
    10) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("The Abyss falls at ${someWorld.name}!")

        // Player Effects
        val abyssalNightEffect = PotionEffect(PotionEffectType.DARKNESS, 12000, (0..1).random())
        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                addPotionEffect(abyssalNightEffect)
                sendMessage(Component.text("The black abyss drops from the sky...", TextColor.color(26, 27, 52)))
            }
            with(somePlayer.world) {
                val someLocation = somePlayer.location
                spawnParticle(Particle.SNOWFLAKE, someLocation, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.SQUID_INK, someLocation, 14, 0.5, 0.5, 0.5)
                playSound(someLocation, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 2.5F, 1.5F)
                playSound(someLocation, Sound.ENTITY_WARDEN_EMERGE, 2.5F, 0.5F)
            }
        }

    }

}
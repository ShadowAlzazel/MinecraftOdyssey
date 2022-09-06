package me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena.utilty.SolarPhenomenon
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.ChatColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BreezyWinds : SolarPhenomenon("Breezy_Winds", 45, 5, 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A swift wind is happening at ${phenomenonWorld.name}!")

        val breezeStrength = (0..2).random()
        val breezeEffect = PotionEffect(PotionEffectType.SPEED, 12000, breezeStrength)

        // Player Effects
        for (somePlayer in phenomenonWorld.players) {
            with(somePlayer) {
                addPotionEffect(breezeEffect)
                sendMessage(Component.text("A swift wind follows your side...", TextColor.color(56, 127, 232)))
            }
            with(somePlayer.world) {
                spawnParticle(Particle.SPIT, somePlayer.location, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.EXPLOSION_NORMAL, somePlayer.location, 5, 0.5, 0.5, 0.5)
                playSound(somePlayer.location, Sound.ITEM_TRIDENT_RIPTIDE_3, 2.5F, 1.5F)
            }
        }

    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "The winds seem to howl..."
    }

}
package me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena.utilty.SolarPhenomenon
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BreezyWinds : SolarPhenomenon("Breezy_Winds", 45, 5, 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A swift wind is happening at ${phenomenonWorld.name}!")
        // Effect for Breezy Day
        val breezeStrength = (0..2).random()
        val breezeEffect = PotionEffect(PotionEffectType.SPEED, 12000, breezeStrength)
        val messageComponent: TextComponent = Component.text("A swift wind follows your side...", TextColor.color(6, 57, 112))

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffect(breezeEffect)
            aPlayer.sendMessage(messageComponent)
        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "The winds seem to howl..."
    }

}
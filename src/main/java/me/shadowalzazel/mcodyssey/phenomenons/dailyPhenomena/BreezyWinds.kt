package me.shadowalzazel.mcodyssey.phenomenons.dailyPhenomena

import me.shadowalzazel.mcodyssey.phenomenons.dailyPhenomena.utilty.DailyPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BreezyWinds : DailyPhenomenon("Breezy_Winds", 45, 5, 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A swift wind is happening at ${phenomenonWorld.name}!")
        // Effect for Breezy Day
        val breezeStrength = (0..2).random()
        val breezeEffect = PotionEffect(PotionEffectType.SPEED, 12000, breezeStrength)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffect(breezeEffect)
            aPlayer.sendMessage("${ChatColor.BLUE}A swift winds follows your side...")
        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "The winds seem to howl..."
    }

}
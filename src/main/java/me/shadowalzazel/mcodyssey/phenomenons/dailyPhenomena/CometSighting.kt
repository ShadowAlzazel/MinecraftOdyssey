package me.shadowalzazel.mcodyssey.phenomenons.dailyPhenomena

import me.shadowalzazel.mcodyssey.phenomenons.dailyPhenomena.utilty.DailyPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object CometSighting : DailyPhenomenon("Comet_Sighting", 40, 5, 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A comet can be seen at ${phenomenonWorld.name}!")
        // Effect for Comet Sighting
        val cometLightEffect = PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 0)
        val cometInspireEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 12000, 0)

        for (aPlayer in phenomenonWorld.players) {
            when((0..2).random()) {
                in 0..1 -> {
                    aPlayer.addPotionEffect(cometLightEffect)
                    aPlayer.sendMessage("${ChatColor.AQUA}A bright comet is seen in the sky...")
                }
                else -> {
                    aPlayer.addPotionEffect(cometLightEffect)
                    aPlayer.addPotionEffect(cometInspireEffect)
                    aPlayer.sendMessage("${ChatColor.AQUA}The bright comet in the sky inspires you...")
                }
            }
        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "A faint object is seen in the sky..."
    }

}
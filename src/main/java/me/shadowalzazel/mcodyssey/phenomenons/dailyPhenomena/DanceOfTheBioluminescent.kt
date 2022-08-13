package me.shadowalzazel.mcodyssey.phenomenons.dailyPhenomena

import me.shadowalzazel.mcodyssey.phenomenons.dailyPhenomena.utilty.DailyPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object DanceOfTheBioluminescent : DailyPhenomenon("Dance_Of_The_Bioluminescent", 45, 5, 55) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("The nano-lifeforms glow at ${phenomenonWorld.name}!")
        // Effect for Bioluminescent Day
        val bioluminescentEffect = PotionEffect(PotionEffectType.GLOWING, 12000, 1)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffect(bioluminescentEffect)
            aPlayer.sendMessage("${ChatColor.DARK_AQUA}The smallest of beings have cluttered around you and begin to glow...")
        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "The ground and foliage faintly glow..."
    }

}
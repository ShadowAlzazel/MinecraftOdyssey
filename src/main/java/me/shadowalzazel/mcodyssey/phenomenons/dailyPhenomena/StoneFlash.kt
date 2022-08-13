package me.shadowalzazel.mcodyssey.phenomenons.dailyPhenomena

import me.shadowalzazel.mcodyssey.phenomenons.dailyPhenomena.utilty.DailyPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object StoneFlash : DailyPhenomenon("Stone_Flash", 35, 5, 60) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A flash stones ${phenomenonWorld.name}!")
        // Stone Flash Effects
        val petrifyEffect = PotionEffect(PotionEffectType.SLOW, 12000, 0)
        val stoneSkinEffect = PotionEffect(PotionEffectType.ABSORPTION, 12000, 4)
        val stoneSkinEffects = listOf<PotionEffect>(stoneSkinEffect, petrifyEffect)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffects(stoneSkinEffects)
            aPlayer.sendMessage("${ChatColor.DARK_GRAY}After a super luminous flash of light your skin has turned into stone?")
        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "There are random stones scattered throughout the floor..."
    }

}
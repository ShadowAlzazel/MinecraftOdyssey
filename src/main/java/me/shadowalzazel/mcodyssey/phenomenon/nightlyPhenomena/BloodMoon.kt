package me.shadowalzazel.mcodyssey.phenomenon.nightlyPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.nightlyPhenomena.utilty.NightlyPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BloodMoon : NightlyPhenomenon("Blood_Moon", 12, 3, 0) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A blood moon is happening at ${phenomenonWorld.name}!")
        // Blue Moon Effects
        val bloodMoonEffect = PotionEffect(PotionEffectType.UNLUCK, 12000, 0)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffect(bloodMoonEffect)
            aPlayer.sendMessage("${ChatColor.DARK_RED}A blood moon is happening!")
        }


    }

}
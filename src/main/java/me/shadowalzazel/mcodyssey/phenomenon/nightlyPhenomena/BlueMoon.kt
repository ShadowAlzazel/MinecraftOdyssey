package me.shadowalzazel.mcodyssey.phenomenon.nightlyPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.nightlyPhenomena.utilty.NightlyPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BlueMoon : NightlyPhenomenon("Blue_Moon", 14, 3, 0) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A blue moon is happening at ${phenomenonWorld.name}!")
        // Blood Moon Effects
        val blueMoonEffect = PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 1)
        val moonLuckEffect = PotionEffect(PotionEffectType.LUCK, 12000, 1)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffect(blueMoonEffect)
            aPlayer.addPotionEffect(moonLuckEffect)
            aPlayer.sendMessage("${ChatColor.BLUE}A blue moon illuminates the night...")
        }

    }

}
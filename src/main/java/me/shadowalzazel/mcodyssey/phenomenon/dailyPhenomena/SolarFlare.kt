package me.shadowalzazel.mcodyssey.phenomenon.dailyPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.dailyPhenomena.utilty.DailyPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object SolarFlare : DailyPhenomenon("Solar_Flare", 25, 4, 40) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A Solar Flare has hit $serverName!")
        // Effects for Solar Flare
        val solarFlareEffect = PotionEffect(PotionEffectType.WEAKNESS, 12000, 0)
        val solarResistanceEffect = PotionEffect(PotionEffectType.FIRE_RESISTANCE, 12000, 0)
        val solarEffects = listOf<PotionEffect>(solarFlareEffect, solarResistanceEffect)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffects(solarEffects)
            aPlayer.sendMessage("${ChatColor.GOLD}A Minor Solar Flare has hit $serverName!!")
        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "The day star seems brighter than usual..."
    }

}
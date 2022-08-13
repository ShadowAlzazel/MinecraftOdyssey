package me.shadowalzazel.mcodyssey.phenomenons

import me.shadowalzazel.mcodyssey.phenomenons.dailyPhenomena.utilty.DailyPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object DrawOfFortunes : DailyPhenomenon("Draw_Of_Fortunes", 100, 0, 0) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("Fortunes have been drawn at $serverName")
        // Luck effects
        val dailyLuckEffect = PotionEffect(PotionEffectType.LUCK, 12000,1)
        val dailyUnluckEffect = PotionEffect(PotionEffectType.UNLUCK, 12000,1)
        // Draw players
        val luckyPlayer = phenomenonWorld.players.random()
        val unluckyPlayer = phenomenonWorld.players.random()

        // Lucky and Unlucky Check
        if (luckyPlayer.uniqueId == unluckyPlayer.uniqueId){
            luckyPlayer.sendMessage("${ChatColor.ITALIC}Your fortune as well as your fate are in your hands...")
        }
        else {
            // Lucky Player
            luckyPlayer.addPotionEffect(dailyLuckEffect)
            luckyPlayer.sendMessage("${ChatColor.YELLOW}Fortune favors you today...")
            //Unlucky Player
            unluckyPlayer.addPotionEffect(dailyUnluckEffect)
            unluckyPlayer.sendMessage("${ChatColor.RED}The odds are stacked against you today...")
        }
    }

}
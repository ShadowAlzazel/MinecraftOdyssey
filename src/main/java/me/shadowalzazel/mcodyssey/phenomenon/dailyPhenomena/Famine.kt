package me.shadowalzazel.mcodyssey.phenomenon.dailyPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.dailyPhenomena.utilty.DailyPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object Famine : DailyPhenomenon("Famine", 15, 4, 40) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A famine is happening at ${phenomenonWorld.name}!")
        // Effect for famine
        val famineEffect = PotionEffect(PotionEffectType.HUNGER, 2000, 0)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffect(famineEffect)
            aPlayer.foodLevel = 5
            aPlayer.sendMessage("${ChatColor.GREEN}A famine has hit ${phenomenonWorld.name}!")
            aPlayer.sendMessage("${ChatColor.ITALIC}Milk sounds very tasty right around now...")
        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "There is slightly less food to go around than yesterday..."
    }

}
package me.shadowalzazel.mcodyssey.phenomenon.ankiPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.odyssey.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object DrawOfFortunes : OdysseyPhenomenon("Draw Of Fortunes",
    PhenomenonTypes.ANKI,
    100,
    100,
    100) {
    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("Fortunes have been drawn at ${someWorld.name}!")
        // Luck effects
        val dailyLuckEffect = PotionEffect(PotionEffectType.LUCK, 12000,1)
        val dailyUnluckEffect = PotionEffect(PotionEffectType.UNLUCK, 12000,1)

        // Draw players
        val luckyPlayer = someWorld.players.random()
        val unluckyPlayer = someWorld.players.random()

        // Lucky and Unlucky Check
        if (luckyPlayer.uniqueId == unluckyPlayer.uniqueId){
            luckyPlayer.sendMessage(Component.text("Seems like there was an anomaly...", TextColor.color(127, 122, 55)))
        }
        else {
            // Lucky Player
            luckyPlayer.addPotionEffect(dailyLuckEffect)
            luckyPlayer.sendMessage(Component.text("Fortune Favors you today...", TextColor.color(127, 122, 55)))
            //Unlucky Player
            unluckyPlayer.addPotionEffect(dailyUnluckEffect)
            unluckyPlayer.sendMessage(Component.text("The odds are stacked against you...", TextColor.color(127, 122, 55)))
        }
    }

}
package me.shadowalzazel.mcodyssey.events

import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
//import org.bukkit.scheduler.BukkitRunnable

open class DailyPhenomenon(name: String, rate: Int) {

    val phenomenonName = name
    private val occurrenceRate = rate

    fun phenomenonActivation(phenomenonWorld: World, rollRate: Int){
        if (rollRate < occurrenceRate)
            phenomenonEffect(phenomenonWorld)
        else {
            println("An uneventful day...")
            for (aPlayer in phenomenonWorld.players) {
                aPlayer.sendMessage("An uneventful day proceeds...")
            }

        }


    }

    open fun phenomenonEffect(phenomenonWorld: World) {
        return
    }

}


class DrawOfFortunes : DailyPhenomenon("Draw of Fortunes", 100) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("Fortunes have been drawn at ${phenomenonWorld.name}")
        val luckyPlayer = phenomenonWorld.players.random()
        val dailyLuckEffect = PotionEffect(PotionEffectType.LUCK, 12000,1)
        luckyPlayer.addPotionEffect((dailyLuckEffect))
        luckyPlayer.sendMessage("Fortune favors you today...")
    }
}

class SlimeDay : DailyPhenomenon("Slime Day", 80) {

    override fun phenomenonEffect(phenomenonWorld: World){
        println("Slime is falling from the Sky at ${phenomenonWorld.name}!")

    }

}

class SolarEclipse : DailyPhenomenon("Solar Eclipse", 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A solar eclipse is happening at ${phenomenonWorld.name}!")

    }
}

class BloodMoon : DailyPhenomenon("Blood Moon", 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A blood moon is happening at ${phenomenonWorld.name}!")

    }
}

class BlueMoon : DailyPhenomenon("Blue Moon", 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A blue moon is happening at ${phenomenonWorld.name}!")

    }
}



class BreezyDay : DailyPhenomenon("Breezy Day", 80) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A swift wind is happening at ${phenomenonWorld.name}!")
        val worldPlayers = phenomenonWorld.players
        val breezeEffect = PotionEffect(PotionEffectType.SPEED, 12000, 1)
        for (breezePlayer in worldPlayers) {
            breezePlayer.addPotionEffect(breezeEffect)
            breezePlayer.sendMessage("A swift winds follows your side...")
        }

    }
}

class Earthquake : DailyPhenomenon("Earthquake", 20) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A large tremor has hit ${phenomenonWorld.name}!")
        val worldPlayers = phenomenonWorld.players
        val breezeEffect = PotionEffect(PotionEffectType.CONFUSION, 160, 1)
        for (breezePlayer in worldPlayers) {
            breezePlayer.addPotionEffect(breezeEffect)
            breezePlayer.sendMessage("The ground trembles profoundly!!!")
        }

        //someOdysseyRunnable = BukkitRunnable {

       //     @override
        //    fun tremblePlayers() {
        //        val trembleEffect = PotionEffect(PotionEffectType.SLOW, 80, 1)
        //        for (tPlayers in worldPlayers) {
        //            tPlayers.addPotionEffect(trembleEffect)
        //        }
        //    }
        //}
        //open class TrembleTask : BukkitRunnable() {
        //    override fun run() {
        //        //val trembleEffect = PotionEffect(PotionEffectType.SLOW, 80, 1)
        //        //for (tPlayers in worldPlayers) {
        //        //    tPlayers.addPotionEffect(trembleEffect)
        //         //}
        //        println("Hello World!")



        //}


        //val scheduler = Bukkit.getScheduler()
        //.runTaskLater(this, TrembleTask(), 60L)
        //scheduler.runTaskLaterAsynchronously(this, TrembleTask(), 60L)


    }
}



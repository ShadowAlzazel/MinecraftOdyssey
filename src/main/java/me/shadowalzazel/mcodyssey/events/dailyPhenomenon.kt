package me.shadowalzazel.mcodyssey.events

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
//import org.bukkit.scheduler.BukkitRunnable

open class DailyPhenomenon(name: String, rate: Int) {

    val phenomenonName = name
    private val occurrenceRate = rate
    val serverName: String? = MinecraftOdyssey.instance.config.getString("names.server-name")

    fun phenomenonActivation(phenomenonWorld: World, rollRate: Int){
        if (rollRate < occurrenceRate)
            phenomenonEffect(phenomenonWorld)
        else {
            println("An uneventful day...")
            for (aPlayer in phenomenonWorld.players) {
                aPlayer.sendMessage("${ChatColor.ITALIC}An uneventful day proceeds...")
            }
        }
    }

    open fun phenomenonEffect(phenomenonWorld: World) {
        return
    }
}


class DrawOfFortunes : DailyPhenomenon("Draw of Fortunes", 100) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("Fortunes have been drawn at $serverName")
        val luckyPlayer = phenomenonWorld.players.random()
        val dailyLuckEffect = PotionEffect(PotionEffectType.LUCK, 12000,1)
        val unluckyPlayer = phenomenonWorld.players.random()
        val dailyUnluckEffect = PotionEffect(PotionEffectType.UNLUCK, 12000,1)

        luckyPlayer.addPotionEffect(dailyLuckEffect)
        luckyPlayer.sendMessage("${ChatColor.YELLOW}Fortune favors you today...")

        if (luckyPlayer.name !=  unluckyPlayer.name){
            luckyPlayer.addPotionEffect(dailyUnluckEffect)
            luckyPlayer.sendMessage("${ChatColor.RED}The odds are stacked against you today...")
        }
        else {
            luckyPlayer.removePotionEffect(PotionEffectType.LUCK)
            luckyPlayer.sendMessage("${ChatColor.ITALIC}Your fortune as well as your fate are in your hands...")
        }
    }
}

//Make drinking milk cancellabel event to prevetn players from disabling potion effects and add new item to prevent phenonmenon

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

class GravityShift : DailyPhenomenon("GravityShift", 75) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("The gravity shifts ${phenomenonWorld.name}!")
        val worldPlayers = phenomenonWorld.players
        val lowGravityEffect = PotionEffect(PotionEffectType.JUMP, 12000, 1)
        for (aPlayer in worldPlayers) {
            aPlayer.addPotionEffect(lowGravityEffect)
            aPlayer.sendMessage("${ChatColor.AQUA} $serverName! is experiencing a relativistic shift and unstable gravity zones!")
        }
    }
}

class WorldFamine : DailyPhenomenon("World Famine", 30) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A famine is happening at ${phenomenonWorld.name}!")
        val worldPlayers = phenomenonWorld.players
        val famineEffect = PotionEffect(PotionEffectType.HUNGER, 12000, 1)
        for (aPlayer in worldPlayers) {
            aPlayer.addPotionEffect(famineEffect)
            aPlayer.sendMessage("${ChatColor.GREEN}A famine has hit ${phenomenonWorld.name}!")
        }
    }
}

class BlueMoon : DailyPhenomenon("Blue Moon", 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A blue moon is happening at ${phenomenonWorld.name}!")

    }
}

class SolarFlare : DailyPhenomenon("Solar Flare", 25) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A Solar Flare has hit $serverName!")
        val worldPlayers = phenomenonWorld.players
        val solarFlareEffect = PotionEffect(PotionEffectType.WEAKNESS, 12000, 1)
        for (aPlayer in worldPlayers) {
            aPlayer.addPotionEffect(solarFlareEffect)
            aPlayer.sendMessage("${ChatColor.GOLD}A Solar Flare has hit $serverName!!")
        }
    }
}



class BreezyDay : DailyPhenomenon("Breezy Day", 80) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A swift wind is happening at ${phenomenonWorld.name}!")
        val worldPlayers = phenomenonWorld.players
        val breezeEffect = PotionEffect(PotionEffectType.SPEED, 12000, 1)
        for (breezePlayer in worldPlayers) {
            breezePlayer.addPotionEffect(breezeEffect)
            breezePlayer.sendMessage("${ChatColor.BLUE}A swift winds follows your side...")
        }
    }
}

class Earthquake : DailyPhenomenon("Earthquake", 20) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A large tremor has hit ${phenomenonWorld.name}!")
        val worldPlayers = phenomenonWorld.players
        val breezeEffect = PotionEffect(PotionEffectType.CONFUSION, 150, 1)
        for (breezePlayer in worldPlayers) {
            breezePlayer.addPotionEffect(breezeEffect)
            breezePlayer.sendMessage("${ChatColor.BOLD}The ground trembles profoundly!!!")
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



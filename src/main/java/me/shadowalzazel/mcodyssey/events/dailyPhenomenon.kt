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
        // Luck effects
        val dailyLuckEffect = PotionEffect(PotionEffectType.LUCK, 12000,1)
        val dailyUnluckEffect = PotionEffect(PotionEffectType.UNLUCK, 12000,1)
        // Draw players
        val luckyPlayer = phenomenonWorld.players.random()
        val unluckyPlayer = phenomenonWorld.players.random()

        if (luckyPlayer.name ==  unluckyPlayer.name){
            luckyPlayer.removePotionEffect(PotionEffectType.LUCK)
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

class GravityShift : DailyPhenomenon("GravityShift", 55) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("The gravity shifts ${phenomenonWorld.name}!")
        // Low Gravity Effects
        val lowGravityEffect = PotionEffect(PotionEffectType.JUMP, 12000, 1)
        val lowGravityEffect2 = PotionEffect(PotionEffectType.SLOW_FALLING, 12000, 1)
        val lowGravityEffects = mutableListOf<PotionEffect>(lowGravityEffect, lowGravityEffect2)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffects(lowGravityEffects)
            aPlayer.sendMessage("${ChatColor.BLUE}$serverName is experiencing a relativistic shift and unstable gravity zones!")
        }
    }
}

class ShimmerIntoxication : DailyPhenomenon("ShimmerIntoxication", 55) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("The gravity shifts ${phenomenonWorld.name}!")
        val worldPlayers = phenomenonWorld.players
        // Shimmer effects
        val shimmerStrength = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 12000, 0)
        val shimmerResistance = PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 12000, 0)
        val shimmerHangover = PotionEffect(PotionEffectType.CONFUSION, 120, 0)
        val shimmerEffects = mutableListOf<PotionEffect>(shimmerStrength, shimmerResistance, shimmerHangover)

        // Apply shimmer for every player
        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffects(shimmerEffects)
            aPlayer.sendMessage("${ChatColor.DARK_PURPLE}You don't remember what happened, but there are empty bottles of shimmer in you hand")
        }
    }
}


class WorldFamine : DailyPhenomenon("World Famine", 10) {

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
}

class BlueMoon : DailyPhenomenon("Blue Moon", 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A blue moon is happening at ${phenomenonWorld.name}!")

    }
}

class SolarFlare : DailyPhenomenon("Solar Flare", 25) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A Solar Flare has hit $serverName!")
        // Effects for Solar Flare
        val solarFlareEffect = PotionEffect(PotionEffectType.WEAKNESS, 12000, 0)
        val solarResistanceEffect = PotionEffect(PotionEffectType.FIRE_RESISTANCE, 12000, 0)
        val solarEffects = mutableListOf<PotionEffect>(solarFlareEffect, solarResistanceEffect)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffects(solarEffects)
            aPlayer.sendMessage("${ChatColor.GOLD}A Minor Solar Flare has hit $serverName!!")
        }
    }
}


class BreezyDay : DailyPhenomenon("Breezy Day", 80) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A swift wind is happening at ${phenomenonWorld.name}!")
        // Effect for Breezy Day
        val breezeEffect = PotionEffect(PotionEffectType.SPEED, 12000, 1)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffect(breezeEffect)
            aPlayer.sendMessage("${ChatColor.BLUE}A swift winds follows your side...")
        }
    }
}

class BioluminescentDay : DailyPhenomenon("Bioluminescent Day", 70) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A swift wind is happening at ${phenomenonWorld.name}!")
        // Effect for Bioluminescent Day
        val bioluminescentEffect = PotionEffect(PotionEffectType.GLOWING, 12000, 1)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffect(bioluminescentEffect)
            aPlayer.sendMessage("${ChatColor.DARK_AQUA}The smallest of beings have cluttered around you and begin to glow...")
        }
    }
}

class FairyFollowDay : DailyPhenomenon("Fairy Follow Day", 60) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("The fairies escape at ${phenomenonWorld.name}!")
        // Effects for Fairy Follow Day
        val followEffect = PotionEffect(PotionEffectType.REGENERATION, 12000, 0)
        val luckEffect = PotionEffect(PotionEffectType.LUCK, 12000, 0)
        val loveEffect = PotionEffect(PotionEffectType.HEALTH_BOOST, 12000, 1)

        // Randomizer for fairy events per player
        for (aPlayer in phenomenonWorld.players) {
            when ((0..4).random()) {
                4 -> {
                    val loveEffects = mutableListOf<PotionEffect>(loveEffect, luckEffect, followEffect)
                    aPlayer.addPotionEffects(loveEffects)
                    aPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}A beautiful fairy has fallen in LOVE with you!")
                }
                in 2..3 -> {
                    val likeEffects = mutableListOf<PotionEffect>(luckEffect, followEffect)
                    aPlayer.addPotionEffects(likeEffects)
                    aPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}A fairy has taken quite a liking to you!")
                }
                else -> {
                    aPlayer.sendMessage("${ChatColor.ITALIC}A couple fairies mess with you but soon disappear")
                }
            }

        }
    }
}

class SpiritsAwaken : DailyPhenomenon("Spirits Awaken", 55) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("The spirits have chosen champions at ${phenomenonWorld.name}!")
        // Guardian Effects
        val bearSpiritEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 12000, 0)
        val falconSpiritEffect = PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 0)
        val turtleSpiritEffect = PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 12000, 0)

        for (aPlayer in phenomenonWorld.players) {
            when ((0..10).random()) {
                in 9..10 -> {
                    aPlayer.addPotionEffect(bearSpiritEffect)
                    aPlayer.sendMessage("${ChatColor.AQUA}A bear spirit has decided to protect you today...")
                }
                in 7..8 -> {
                    aPlayer.addPotionEffect(falconSpiritEffect)
                    aPlayer.sendMessage("${ChatColor.YELLOW}A falcon spirit is seen following you high above...")
                }
                in 5..6 -> {
                    aPlayer.addPotionEffect(turtleSpiritEffect)
                    aPlayer.sendMessage("${ChatColor.GREEN}A turtle spirit has taken you under their shell...")
                }
                else -> {
                    aPlayer.sendMessage("${ChatColor.ITALIC}You sense spirits around you")
                }
            }

        }
    }
}

class Earthquake : DailyPhenomenon("Earthquake", 15) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A large tremor has hit ${phenomenonWorld.name}!")
        // Earthquake Effects
        val trembleConfusionEffect = PotionEffect(PotionEffectType.CONFUSION, 150, 2)
        val trembleSlowEffect = PotionEffect(PotionEffectType.SLOW, 190, 2)
        val trembleEffects = mutableListOf<PotionEffect>(trembleConfusionEffect, trembleSlowEffect)
        val shakeEffect = PotionEffect(PotionEffectType.SLOW, 100, 1)

        for (aPlayer in phenomenonWorld.players) {
            when ((0..3).random()) {
                3 -> {
                    if (!aPlayer.isFlying){
                        aPlayer.addPotionEffects(trembleEffects)
                        aPlayer.sendMessage("${ChatColor.BOLD}The ground trembles profoundly under you!!!")
                    }
                }
                2 -> {
                    if (!aPlayer.isFlying){
                        aPlayer.addPotionEffect(shakeEffect)
                        aPlayer.sendMessage("${ChatColor.WHITE}The ground shakes under you!")
                    }
                }
                else -> {
                    if (!aPlayer.isFlying){
                        aPlayer.sendMessage("${ChatColor.ITALIC}Tremors are heard from afar...")
                    }

                }

            }
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



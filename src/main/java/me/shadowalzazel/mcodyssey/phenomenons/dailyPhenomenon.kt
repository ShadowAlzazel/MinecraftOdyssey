package me.shadowalzazel.mcodyssey.phenomenons

import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
//import org.bukkit.scheduler.BukkitRunnable


open class DailyPhenomenon(name: String, rate: Int, growthRate: Int, warning: Int) : Phenomenon(name, rate, growthRate, warning) {

    private val dayMessages = listOf<String>("An uneventful day proceeds...", "Looks like nothing is happening today...", "MULTIPLE HOSTILES INCOM.. False alarm. Nothing is going on.",
        "There are rumors that the Ambassador is on route to this test world...", "Hello World!", "Just another ordinary day...", "The forecast predicts... Nothing.",
        "No distinct events are Schedu-Predicted for today.", "The standard cycle was not disturbed...", "A normal day follows...", "Just plain Today...", "The only thing that happens is nothing at all")

    override val hasWarning: Boolean = true

    override fun phenomenonActivation(phenomenonWorld: World, rollRate: Int): Boolean {

        //val dayMessages = MinecraftOdyssey.instance.config.getStringList("day-messages")
        val randomMessage = dayMessages.random()

        if (rollRate < occurrenceRate) {
            phenomenonEffect(phenomenonWorld)
            occurrenceRate = occurranceRestartRate
            return true
        }
        else {
            println("$phenomenonName Daily Phenomenon Did Not Occur")

            // Send daily fail message
            for (aPlayer in phenomenonWorld.players) {
                aPlayer.sendMessage("${ChatColor.ITALIC}$randomMessage")
            }
            occurrenceRate += occurranceFailGrowthRate

            // Check if event has a warning
            if (hasWarning){
                if (occurrenceRate >= warningThreshold) {
                    // Warning Message
                    val failMessage: String = phenomenonWarning(phenomenonWorld)
                    for (aPlayer in phenomenonWorld.players) {
                        aPlayer.sendMessage("${ChatColor.ITALIC}$failMessage")
                    }
                }
            }
            return false
        }
    }
    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "Found an Exception! :D"
    }

}


class DrawOfFortunes : DailyPhenomenon("DrawOfFortunes", 100, 0, 0) {

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

//Make drinking milk cancellable event to prevent players from disabling potion effects and add new item to prevent phenomenon

class SlimeDay : DailyPhenomenon("SlimeDay", 80, 5, 50) {

    override fun phenomenonEffect(phenomenonWorld: World){
        println("Slime is falling from the Sky at ${phenomenonWorld.name}!")

    }

}

class SolarEclipse : DailyPhenomenon("SolarEclipse", 50, 4, 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A solar eclipse is happening at ${phenomenonWorld.name}!")

    }

}

class BlazingSoul : DailyPhenomenon("BlazingSoul", 40, 5, 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("The players soul's are ablaze at ${phenomenonWorld.name}!")
        // Blazing Soul Effects
        val blazingBodyEffect = PotionEffect(PotionEffectType.FIRE_RESISTANCE, 12000, 0)
        val blazingBodyEffect2 = PotionEffect(PotionEffectType.SPEED, 12000, 0)
        val blazingMindEffect  = PotionEffect(PotionEffectType.FAST_DIGGING, 12000, 0)
        val blazingSoulEffect = PotionEffect(PotionEffectType.GLOWING, 12000, 1)
        val blazingSoulEffect2 = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 12000, 1)


        for (aPlayer in phenomenonWorld.players) {
            aPlayer.sendMessage("${ChatColor.ITALIC}You sense the crackling embers...")
            // roll for each effect
            val blazingSoulRandom = (0..3).random()
            val blazingMindRandom = (0..3).random()
            val blazingBodyRandom = (0..3).random()

            if (blazingBodyRandom == 2) {
                aPlayer.addPotionEffect(blazingBodyEffect)
                aPlayer.addPotionEffect(blazingBodyEffect2)
                aPlayer.sendMessage("${ChatColor.GOLD}Your body is set ablaze!")
            }
            if (blazingMindRandom == 2) {
                aPlayer.addPotionEffect(blazingMindEffect)
                aPlayer.sendMessage("${ChatColor.GOLD}The flames of progress burn strong!")
            }
            if (blazingSoulRandom == 2) {
                aPlayer.addPotionEffect(blazingSoulEffect)
                aPlayer.addPotionEffect(blazingSoulEffect2)
                aPlayer.sendMessage("${ChatColor.GOLD}The inferno illuminates from within!")
            }

        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "You have visions of burning embers..."
    }

}

class CometSighting : DailyPhenomenon("CometSighting", 40, 5, 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A comet can be seen at ${phenomenonWorld.name}!")
        // Effect for Comet Sighting
        val cometLightEffect = PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 0)
        val cometInspireEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 12000, 0)

        for (aPlayer in phenomenonWorld.players) {
            when((0..2).random()) {
                in 0..1 -> {
                    aPlayer.addPotionEffect(cometLightEffect)
                    aPlayer.sendMessage("${ChatColor.AQUA}A bright comet is seen in the sky...")
                }
                else -> {
                    aPlayer.addPotionEffect(cometLightEffect)
                    aPlayer.addPotionEffect(cometInspireEffect)
                    aPlayer.sendMessage("${ChatColor.AQUA}The bright comet in the sky inspires you...")
                }
            }
        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "A faint object is seen in the sky..."
    }

}

class StoneFlash : DailyPhenomenon("StoneFlash", 35, 5, 60) {

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

class GravityShift : DailyPhenomenon("GravityShift", 32, 4, 40) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("The gravity shifts ${phenomenonWorld.name}!")
        // Low Gravity Effects
        val lowGravityEffect = PotionEffect(PotionEffectType.JUMP, 12000, 1)
        val lowGravityEffect2 = PotionEffect(PotionEffectType.SLOW_FALLING, 12000, 1)
        val lowGravityEffects = listOf<PotionEffect>(lowGravityEffect, lowGravityEffect2)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffects(lowGravityEffects)
            aPlayer.sendMessage("${ChatColor.BLUE}$serverName is experiencing a relativistic shift and unstable gravity zones!")
        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "It is as the world is pulling on you less..."
    }

}

class ShimmerIntoxication : DailyPhenomenon("ShimmerIntoxication", 35, 6, 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("There is shimmer appearing at ${phenomenonWorld.name}!")
        // Shimmer effects
        val shimmerStrength = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 12000, 0)
        val shimmerResistance = PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 12000, 0)
        val shimmerHangover = PotionEffect(PotionEffectType.CONFUSION, 120, 0)
        val shimmerEffects = listOf<PotionEffect>(shimmerStrength, shimmerResistance, shimmerHangover)

        // Apply shimmer for every player
        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffects(shimmerEffects)
            aPlayer.sendMessage("${ChatColor.DARK_PURPLE}You don't remember what happened but there are empty bottles of shimmer in you hand")
        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "There are rumors of Shimmer being found nearby..."
    }

}

class WorldFamine : DailyPhenomenon("WorldFamine", 15, 4, 40) {

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

class SolarFlare : DailyPhenomenon("SolarFlare", 25, 4, 40) {

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

class BreezyDay : DailyPhenomenon("BreezyDay", 45, 5, 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A swift wind is happening at ${phenomenonWorld.name}!")
        // Effect for Breezy Day
        val breezeStrength = (0..2).random()
        val breezeEffect = PotionEffect(PotionEffectType.SPEED, 12000, breezeStrength)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffect(breezeEffect)
            aPlayer.sendMessage("${ChatColor.BLUE}A swift winds follows your side...")
        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "The winds seem to howl..."
    }

}

class BioluminescentDay : DailyPhenomenon("BioluminescentDay", 45, 5, 55) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("The nano-lifeforms glow at ${phenomenonWorld.name}!")
        // Effect for Bioluminescent Day
        val bioluminescentEffect = PotionEffect(PotionEffectType.GLOWING, 12000, 1)

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffect(bioluminescentEffect)
            aPlayer.sendMessage("${ChatColor.DARK_AQUA}The smallest of beings have cluttered around you and begin to glow...")
        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "The ground and foliage faintly glow..."
    }

}

class FairyFollowDay : DailyPhenomenon("FairyFollowDay", 45, 5, 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("The fairies escape at ${phenomenonWorld.name}!")
        // Effects for Fairy Follow Day
        val followEffect = PotionEffect(PotionEffectType.REGENERATION, 12000, 0)
        val luckEffect = PotionEffect(PotionEffectType.LUCK, 12000, 0)
        val loveEffect = PotionEffect(PotionEffectType.HEALTH_BOOST, 12000, 0)

        // Randomizer for fairy events per player
        for (aPlayer in phenomenonWorld.players) {
            when ((0..3).random()) {
                3 -> {
                    val loveEffects = mutableListOf<PotionEffect>(loveEffect, luckEffect, followEffect)
                    aPlayer.addPotionEffects(loveEffects)
                    aPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}A beautiful fairy has fallen in ${ChatColor.ITALIC}LOVE ${ChatColor.RESET}${ChatColor.LIGHT_PURPLE}with you!")
                }
                in 1..2 -> {
                    val likeEffects = mutableListOf<PotionEffect>(luckEffect, followEffect)
                    aPlayer.addPotionEffects(likeEffects)
                    aPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}A fairy has taken quite a liking to you!")
                }
                else -> {
                    aPlayer.sendMessage("${ChatColor.ITALIC}A couple fairies mess with you but soon disappear...")
                }
            }

        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "You think you spot a fairy whizzing through the air but it soon vanishes..."
    }

}

class SpiritsAwaken : DailyPhenomenon("SpiritsAwaken", 45, 5, 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("The spirits have chosen champions at ${phenomenonWorld.name}!")
        // Guardian Effects
        val bearSpiritEffect = PotionEffect(PotionEffectType.SATURATION, 12000, 0)
        val tigerSpiritEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 12000, 0)
        val turtleSpiritEffect = PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 12000, 0)

        for (aPlayer in phenomenonWorld.players) {
            when ((0..10).random()) {
                in 9..10 -> {
                    aPlayer.addPotionEffect(bearSpiritEffect)
                    aPlayer.sendMessage("${ChatColor.YELLOW}A bear spirit has decided to shadow you today...")
                }
                in 7..8 -> {
                    aPlayer.addPotionEffect(tigerSpiritEffect)
                    aPlayer.sendMessage("${ChatColor.GOLD}A tiger spirit roars within...")
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

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "The animals seem restless..."
    }
}

class Earthquake : DailyPhenomenon("Earthquake", 20, 4, 0) {

    // No Warning
    override val hasWarning: Boolean = false

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A large tremor has hit ${phenomenonWorld.name}!")
        // Earthquake Effects
        val trembleConfusionEffect = PotionEffect(PotionEffectType.CONFUSION, 150, 2)
        val trembleSlowEffect = PotionEffect(PotionEffectType.SLOW, 190, 2)
        val trembleEffects = listOf<PotionEffect>(trembleConfusionEffect, trembleSlowEffect)
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
                    aPlayer.sendMessage("${ChatColor.ITALIC}Tremors are heard from afar...")
                }
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
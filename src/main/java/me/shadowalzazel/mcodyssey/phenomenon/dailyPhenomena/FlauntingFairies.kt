package me.shadowalzazel.mcodyssey.phenomenon.dailyPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.dailyPhenomena.utilty.DailyPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object FlauntingFairies : DailyPhenomenon("Flaunting_Fairies", 45, 5, 50) {

    fun todo() {
        TODO("Sine Wave fairy particle")
    }

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
                    if ("Fairy_Charmed" !in aPlayer.scoreboardTags) aPlayer.scoreboardTags.add("Fairy_Charmed")
                }
                in 1..2 -> {
                    val likeEffects = mutableListOf<PotionEffect>(luckEffect, followEffect)
                    aPlayer.addPotionEffects(likeEffects)
                    aPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}A fairy has taken quite a liking to you!")
                }
                else -> {
                    aPlayer.sendMessage("${ChatColor.ITALIC}A couple fairies flaunt with you but soon disappear...")
                }
            }

        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "You think you spot a fairy whizzing through the air but it soon vanishes..."
    }

}

package me.shadowalzazel.mcodyssey.phenomenon.dailyPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.dailyPhenomena.utilty.DailyPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ShimmerIntoxication : DailyPhenomenon("Shimmer_Intoxication", 35, 6, 50) {

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
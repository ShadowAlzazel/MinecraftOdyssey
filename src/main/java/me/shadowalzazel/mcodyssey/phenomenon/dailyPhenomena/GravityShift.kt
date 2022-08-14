package me.shadowalzazel.mcodyssey.phenomenon.dailyPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.dailyPhenomena.utilty.DailyPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object GravityShift : DailyPhenomenon("Gravity_Shift", 32, 4, 40) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("The gravity shifts ${phenomenonWorld.name}!")
        // Low Gravity Effects
        val lowGravityEffects = listOf(
            PotionEffect(PotionEffectType.JUMP, 12000, 1),
            PotionEffect(PotionEffectType.SLOW_FALLING, 12000, 1)
        )

        for (aPlayer in phenomenonWorld.players) {
            aPlayer.addPotionEffects(lowGravityEffects)
            aPlayer.sendMessage("${ChatColor.BLUE}$serverName is experiencing a relativistic shift and unstable gravity zones!")
        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "It is as the world is pulling on you less..."
    }

}
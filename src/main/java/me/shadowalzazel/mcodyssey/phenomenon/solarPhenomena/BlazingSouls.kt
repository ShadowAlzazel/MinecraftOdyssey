package me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena.utilty.SolarPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BlazingSouls : SolarPhenomenon("Blazing_Souls", 40, 5, 50) {

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
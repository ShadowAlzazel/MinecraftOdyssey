package me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena.utilty.SolarPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object SpiritsRelease : SolarPhenomenon("Spirits_Release", 45, 5, 50) {

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
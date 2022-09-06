package me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.solarPhenomena.utilty.SolarPhenomenon
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.ChatColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BlazingSouls : SolarPhenomenon("Blazing_Souls", 40, 5, 50) {

    override fun phenomenonEffect(phenomenonWorld: World) {
        phenomenonWorld.sendMessage(Component.text("The players soul's are ablaze!"))
        println("The players soul's are ablaze at ${phenomenonWorld.name}!")

        val blazingBodyEffects = listOf(
            PotionEffect(PotionEffectType.FIRE_RESISTANCE, 12000, 0),
            PotionEffect(PotionEffectType.SPEED, 12000, 0))
        val blazingMindEffects = listOf(
            PotionEffect(PotionEffectType.FAST_DIGGING, 12000, 0))
        val blazingSoulEffects = listOf(
            PotionEffect(PotionEffectType.GLOWING, 12000, 1),
            PotionEffect(PotionEffectType.INCREASE_DAMAGE, 12000, 1))

        // Player Effects
        for (somePlayer in phenomenonWorld.players) {
            with(somePlayer) {
                sendMessage(Component.text("You sense the crackling embers...", TextColor.color(255, 110, 30)))
                // Blazing Body
                if ((0..3).random() == 2) {
                    addPotionEffects(blazingBodyEffects)
                    sendMessage(Component.text("Your body is set ablaze!", TextColor.color(255, 110, 30)))
                }
                // Blazing Mind
                if ((0..3).random() == 3) {
                    addPotionEffects(blazingMindEffects)
                    sendMessage(Component.text("The flames of progress burn strong!", TextColor.color(255, 110, 30)))
                }
                // Blazing Soul
                if ((0..3).random() == 0) {
                    addPotionEffects(blazingSoulEffects)
                    sendMessage(Component.text("The inferno illuminates from within!", TextColor.color(255, 110, 30)))
                }
            }
            with(somePlayer.world) {
                spawnParticle(Particle.ELECTRIC_SPARK, somePlayer.location, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.SMALL_FLAME, somePlayer.location, 15, 0.5, 0.5, 0.5)
                playSound(somePlayer.location, Sound.BLOCK_FURNACE_FIRE_CRACKLE, 2.5F, 1.2F)
            }
        }
    }

    override fun phenomenonWarning(phenomenonWorld: World): String {
        return "You have visions of burning embers..."
    }

}
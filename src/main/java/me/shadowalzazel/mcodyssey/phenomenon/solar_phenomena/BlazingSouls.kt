package me.shadowalzazel.mcodyssey.phenomenon.solar_phenomena

import me.shadowalzazel.mcodyssey.phenomenon.base.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.base.PhenomenonType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BlazingSouls : OdysseyPhenomenon("Blazing Souls",
    PhenomenonType.SOLAR,
    35,
    5,
    20,
    55,
    Component.text("There is a faint glow emanating from the flora...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)

        val blazingBodyEffects = listOf(
            PotionEffect(PotionEffectType.FIRE_RESISTANCE, 12000, 0),
            PotionEffect(PotionEffectType.SPEED, 12000, 0))
        val blazingMindEffects = listOf(
            PotionEffect(PotionEffectType.HASTE, 12000, 0))
        val blazingSoulEffects = listOf(
            PotionEffect(PotionEffectType.GLOWING, 12000, 1),
            PotionEffect(PotionEffectType.STRENGTH, 12000, 1))

        // Player Effects
        someWorld.players.forEach {
            it.sendMessage(Component.text("You sense the crackling embers...", TextColor.color(255, 110, 30)))
            // Blazing Body
            if ((0..3).random() == 2) {
                it.addPotionEffects(blazingBodyEffects)
                it.sendMessage(Component.text("Your body is set ablaze!", TextColor.color(255, 110, 30)))
            }
            // Blazing Mind
            if ((0..3).random() == 3) {
                it.addPotionEffects(blazingMindEffects)
                it.sendMessage(Component.text("The flames of progress burn strong!", TextColor.color(255, 110, 30)))
            }
            // Blazing Soul
            if ((0..3).random() == 0) {
                it.addPotionEffects(blazingSoulEffects)
                it.sendMessage(Component.text("The inferno illuminates from within!", TextColor.color(255, 110, 30)))
            }

            it.spawnParticle(Particle.SMALL_FLAME, it.location, 14, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.BLOCK_FURNACE_FIRE_CRACKLE, 2.5F, 1.5F)

        }
    }

}
package me.shadowalzazel.mcodyssey.phenomenon.lunar_phenomena

import me.shadowalzazel.mcodyssey.phenomenon.base.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.base.PhenomenonType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ChillingNight: OdysseyPhenomenon("Chilling Night",
    PhenomenonType.LUNAR,
    7,
    3,
    7) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("The atmosphere freezes ${someWorld.name}!")

        // Player Effects
        val chillingNight = PotionEffect(PotionEffectType.SLOWNESS, 20 * 20, 1)

        someWorld.players.forEach {
            it.addPotionEffect(chillingNight)
            it.sendMessage(Component.text("The air rapidly becomes icy cold...", TextColor.color(106, 177, 242)))
            it.spawnParticle(Particle.SNOWFLAKE, it.location, 35, 0.5, 0.5, 0.5)
            it.spawnParticle(Particle.ELECTRIC_SPARK, it.location, 14, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 2.5F, 1.5F)
            it.playSound(it.location, Sound.ENTITY_PLAYER_HURT_FREEZE, 2.5F, 0.5F)

        }

    }

    override fun persistentPlayerActives(someWorld: World) {
        val chillingNight = PotionEffect(PotionEffectType.SLOWNESS, 20 * 5, 1)
        someWorld.players.forEach {
            val hasLight: Boolean = it.equipment.itemInOffHand.type == Material.LANTERN || it.equipment.itemInOffHand.type == Material.TORCH

            if (((it.location.block.lightFromBlocks < 8) && !hasLight) || (it.isInWaterOrRain)) {
                if (!allayMitigation(it)) {
                    it.freezeTicks += 20 * 7
                    it.addPotionEffect(chillingNight)
                }
            }
        }
    }


}
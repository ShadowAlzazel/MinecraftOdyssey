package me.shadowalzazel.mcodyssey.phenomenon.suenPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.odyssey.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ChillingNight: OdysseyPhenomenon("Chilling Night",
    PhenomenonTypes.SUEN,
    10,
    4,
    10) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("The atmosphere freezes ${someWorld.name}!")

        // Player Effects
        val chillingNight = PotionEffect(PotionEffectType.SLOW, 20 * 20, 1)

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
        val chillingNight = PotionEffect(PotionEffectType.SLOW, 20 * 11, 1)
        someWorld.players.forEach {
            if (it.location.block.lightFromBlocks < 12 || it.isInWaterOrRain) {
                if (it.hasPotionEffect(PotionEffectType.SLOW)) {
                    it.freezeTicks += 20 * 15
                }
                else {
                    it.freezeTicks += 20 * 5
                }
                it.addPotionEffect(chillingNight)
            }
        }
    }


}
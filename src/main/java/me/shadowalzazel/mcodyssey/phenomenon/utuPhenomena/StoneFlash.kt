package me.shadowalzazel.mcodyssey.phenomenon.utuPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.odyssey.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object StoneFlash : OdysseyPhenomenon("Stone Flash",
    PhenomenonTypes.SUEN,
    35,
    5,
    7,
    55,
    Component.text("There are random stones scattered throughout the floor...")) {


    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("A flash stones ${someWorld.name}!")

        // Solar Flare Effects
        val stoneFlashEffects = listOf(
            PotionEffect(PotionEffectType.SLOW, 12000, 0),
            PotionEffect(PotionEffectType.ABSORPTION, 12000, 4))
        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                addPotionEffects(stoneFlashEffects)
                sendMessage(Component.text("After a super luminous flash of light your skin has turned into stone?", TextColor.color(56, 127, 232)))
                spawnParticle(Particle.SPIT, location, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.EXPLOSION_NORMAL, location, 5, 0.5, 0.5, 0.5)
                playSound(location, Sound.BLOCK_STONE_BREAK, 2.5F, 1.5F)
                playSound(location, Sound.BLOCK_STONE_FALL, 2.5F, 1.5F)
                val bloodBlockBreak = Material.STONE.createBlockData()
                spawnParticle(Particle.BLOCK_CRACK, location, 95, 0.95, 0.8, 0.95, bloodBlockBreak)
            }
        }
    }


}
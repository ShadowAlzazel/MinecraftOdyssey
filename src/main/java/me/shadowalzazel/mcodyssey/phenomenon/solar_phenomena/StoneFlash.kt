package me.shadowalzazel.mcodyssey.phenomenon.solar_phenomena

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

object StoneFlash : OdysseyPhenomenon("Stone Flash",
    PhenomenonType.LUNAR,
    35,
    5,
    7,
    55,
    Component.text("There are random stones scattered throughout the floor...")) {


    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)

        // Solar Flare Effects
        val stoneFlashEffects = listOf(
            PotionEffect(PotionEffectType.SLOW, 12000, 0),
            PotionEffect(PotionEffectType.ABSORPTION, 12000, 4))

        someWorld.players.forEach {
            it.addPotionEffects(stoneFlashEffects)
            it.sendMessage(Component.text("After a super luminous flash of light your skin has turned into stone?", TextColor.color(56, 87, 55)))
            it.spawnParticle(Particle.SPELL_MOB_AMBIENT, it.location, 15, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.BLOCK_STONE_BREAK, 2.5F, 0.5F)
            it.playSound(it.location, Sound.BLOCK_STONE_FALL, 2.5F, 0.5F)
            it.spawnParticle(Particle.BLOCK_CRACK, it.location, 95, 0.95, 0.8, 0.95, Material.STONE.createBlockData())
        }

    }


}
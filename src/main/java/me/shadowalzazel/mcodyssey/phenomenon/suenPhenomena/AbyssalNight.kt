package me.shadowalzazel.mcodyssey.phenomenon.suenPhenomena

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

object AbyssalNight : OdysseyPhenomenon("Abyssal Night",
    PhenomenonTypes.SUEN,
    8,
    3,
    8) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("The Abyss falls at ${someWorld.name}!")

        // Player Effects
        val abyssalNightEffect = PotionEffect(PotionEffectType.DARKNESS, 20 * 20, 0)

        someWorld.players.forEach {
            it.addPotionEffect(abyssalNightEffect)
            it.sendMessage(Component.text("The black abyss drops from the sky...", TextColor.color(26, 27, 52)))
            it.spawnParticle(Particle.SQUID_INK, it.location, 15, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 2.5F, 1.5F)
            it.playSound(it.location, Sound.ENTITY_WARDEN_EMERGE, 2.5F, 0.5F)
            it.spawnParticle(Particle.BLOCK_CRACK, it.location, 95, 0.95, 0.8, 0.95, Material.COAL_BLOCK.createBlockData())
        }

    }

    override fun persistentPlayerActives(someWorld: World) {
        val abyssalNightEffect = PotionEffect(PotionEffectType.DARKNESS, 20 * 11, 1)
        someWorld.players.forEach {
            val hasLight: Boolean = it.equipment.itemInOffHand.type == Material.LANTERN || it.equipment.itemInOffHand.type == Material.TORCH

            if ((it.location.block.lightFromBlocks < 8) && !hasLight) {
                if (!allayMitigation(it)) {
                    it.addPotionEffect(abyssalNightEffect)
                }
            }
        }
    }

}
package me.shadowalzazel.mcodyssey.phenomenon.suenPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.utility.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.utility.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BloodMoon : OdysseyPhenomenon("Blood Moon",
    PhenomenonTypes.SUEN,
    12,
    3) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("A blood moon is happening at ${someWorld.name}!")

        // Blood Moon Effects
        val bloodMoonEffect = PotionEffect(PotionEffectType.UNLUCK, 12000, 0)
        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                addPotionEffect(bloodMoonEffect)
                sendMessage(Component.text("The blood moon rises...", TextColor.color(96, 27, 32)))
            }
            with(somePlayer.world) {
                val someLocation = somePlayer.location
                spawnParticle(Particle.SPELL_MOB_AMBIENT, someLocation, 15, 0.5, 0.5, 0.5)
                val bloodBlockBreak = Material.REDSTONE_BLOCK.createBlockData()
                spawnParticle(Particle.BLOCK_CRACK, someLocation, 95, 0.95, 0.8, 0.95, bloodBlockBreak)
                playSound(someLocation, Sound.ENTITY_WITHER_AMBIENT, 2.5F, 0.5F)
            }
        }
    }

}
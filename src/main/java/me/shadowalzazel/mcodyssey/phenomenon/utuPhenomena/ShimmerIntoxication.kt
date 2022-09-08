package me.shadowalzazel.mcodyssey.phenomenon.utuPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.utility.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.utility.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ShimmerIntoxication : OdysseyPhenomenon("Starry Night",
    PhenomenonTypes.SUEN,
    35,
    5,
    55,
    Component.text("There are rumors of Shimmer being traded nearby...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("There is shimmer appearing at ${someWorld.name}!")

        // Low Gravity Effects
        val shimmerEffects = listOf(
            PotionEffect(PotionEffectType.INCREASE_DAMAGE, 12000, 1),
            PotionEffect(PotionEffectType.CONFUSION, 120, 0),
            PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 12000, 1))
        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                addPotionEffects(shimmerEffects)
                sendMessage(Component.text("You don't remember what happened but there are empty bottles of shimmer in you hand", TextColor.color(56, 127, 232)))
                spawnParticle(Particle.SPIT, location, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.EXPLOSION_NORMAL, location, 5, 0.5, 0.5, 0.5)
                playSound(location, Sound.ITEM_TRIDENT_RIPTIDE_3, 2.5F, 1.5F)
            }
        }
    }


}
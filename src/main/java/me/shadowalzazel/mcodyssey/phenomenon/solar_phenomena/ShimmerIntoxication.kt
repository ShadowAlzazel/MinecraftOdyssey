package me.shadowalzazel.mcodyssey.phenomenon.solar_phenomena

import me.shadowalzazel.mcodyssey.phenomenon.base.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.base.PhenomenonType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ShimmerIntoxication : OdysseyPhenomenon("Shimmer Intoxication",
    PhenomenonType.LUNAR,
    35,
    5,
    15,
    55,
    Component.text("There are rumors of Shimmer being traded nearby...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)

        // Low Gravity Effects
        val shimmerEffects = listOf(
            PotionEffect(PotionEffectType.STRENGTH, 12000, 1),
            PotionEffect(PotionEffectType.NAUSEA, 120, 0),
            PotionEffect(PotionEffectType.RESISTANCE, 12000, 1))

        someWorld.players.forEach {
            it.addPotionEffects(shimmerEffects)
            it.sendMessage(Component.text("You don't remember what happened but there are empty bottles of shimmer in you hand", TextColor.color(78, 0, 161)))
            it.inventory.addItem(ItemStack(Material.GLASS_BOTTLE, 2))
            it.spawnParticle(Particle.WITCH, it.location, 55, 0.5, 0.5, 0.5)
            it.spawnParticle(Particle.ENTITY_EFFECT, it.location, 35, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.ENTITY_GENERIC_DRINK, 2.5F, 1.5F)
        }
    }


}
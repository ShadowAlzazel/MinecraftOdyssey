package me.shadowalzazel.mcodyssey.phenomenon.utuPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.odyssey.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.PhenomenonTypes
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
    PhenomenonTypes.SUEN,
    35,
    5,
    15,
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

        someWorld.players.forEach {
            it.addPotionEffects(shimmerEffects)
            it.sendMessage(Component.text("You don't remember what happened but there are empty bottles of shimmer in you hand", TextColor.color(78, 0, 161)))
            it.inventory.addItem(ItemStack(Material.GLASS_BOTTLE, 2))
            it.spawnParticle(Particle.SPELL_WITCH, it.location, 55, 0.5, 0.5, 0.5)
            it.spawnParticle(Particle.SPELL_MOB_AMBIENT, it.location, 35, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.ENTITY_GENERIC_DRINK, 2.5F, 1.5F)
        }
    }


}
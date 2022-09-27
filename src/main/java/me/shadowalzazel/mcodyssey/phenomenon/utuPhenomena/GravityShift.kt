package me.shadowalzazel.mcodyssey.phenomenon.utuPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.odyssey.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object GravityShift : OdysseyPhenomenon("Gravity shift",
    PhenomenonTypes.SUEN,
    30,
    4,
    10,
    55,
    Component.text("It is as the world is pulling on you less...")) {

    // Make spheres
    // create bubbles that if near do more crazy stuff

    // Low Gravity Effects
    private val lowGravityEffects = listOf(
        PotionEffect(PotionEffectType.JUMP, 12000, 2),
        PotionEffect(PotionEffectType.SLOW_FALLING, 12000, 1))

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("The gravity shifts at ${someWorld.name}!")

        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                addPotionEffects(lowGravityEffects)
                sendMessage(Component.text("${server.name} experiencing a relativistic shift and unstable gravity zones!", TextColor.color(56, 127, 232)))
                spawnParticle(Particle.SPIT, location, 15, 0.5, 0.5, 0.5)
                spawnParticle(Particle.EXPLOSION_NORMAL, location, 5, 0.5, 0.5, 0.5)
                playSound(location, Sound.ITEM_TRIDENT_RIPTIDE_3, 2.5F, 1.5F)
            }
        }
    }

    override fun persistentSpawningActives(someEntity: LivingEntity) {
        if ((0..1).random() == 1) { someEntity.addPotionEffects(lowGravityEffects) }
    }

}
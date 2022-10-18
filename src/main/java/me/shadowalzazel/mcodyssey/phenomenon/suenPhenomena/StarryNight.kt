package me.shadowalzazel.mcodyssey.phenomenon.suenPhenomena

import me.shadowalzazel.mcodyssey.phenomenon.odyssey.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

object StarryNight : OdysseyPhenomenon("Starry Night",
    PhenomenonTypes.SUEN,
    11,
    5,
    11,
    55,
    Component.text("The stars seem much closer...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("The star falls at ${someWorld.name}!")

        // Player Effects
        val starryNightEffect = PotionEffect(PotionEffectType.LUCK, 12000, (0..1).random())
        someWorld.players.forEach {
            it.addPotionEffect(starryNightEffect)
            it.sendMessage(Component.text("The stars are falling from the sky!?", TextColor.color(156, 127, 192)))
            it.spawnParticle(Particle.SCRAPE, it.location, 5, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.BLOCK_NOTE_BLOCK_CHIME, 2.5F, 1.5F)
        }
    }

    override fun persistentSpawningActives(someEntity: LivingEntity) {
        if (someEntity.location.block.lightFromSky > 5) {
            // Firework
            (someEntity.world.spawnEntity(someEntity.location.clone().add(0.0, 40.0, 0.0), EntityType.FIREWORK) as Firework).also {
                val newMeta = it.fireworkMeta
                newMeta.power = 1
                newMeta.addEffect(
                    FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.WHITE)
                        .withFade(Color.WHITE).trail(true).flicker(true).build()
                )
                it.fireworkMeta = newMeta
                it.velocity = Vector(0.0, -3.0, 0.0)
                it.ticksToDetonate = 5 * 20
            }
        }
        someEntity.remove()
    }
}
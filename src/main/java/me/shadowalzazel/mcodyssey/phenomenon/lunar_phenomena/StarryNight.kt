package me.shadowalzazel.mcodyssey.phenomenon.lunar_phenomena

import me.shadowalzazel.mcodyssey.phenomenon.base.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.base.PhenomenonType
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
    PhenomenonType.LUNAR,
    11,
    5,
    11,
    55,
    Component.text("The stars seem much closer...")) {

    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)

        // Player Effects
        val starryNightEffect = PotionEffect(PotionEffectType.LUCK, 12000, (0..1).random())
        someWorld.players.forEach {
            it.addPotionEffect(starryNightEffect)
            it.sendMessage(Component.text("The stars light up the sky!?", TextColor.color(156, 127, 192)))
            it.spawnParticle(Particle.SCRAPE, it.location, 5, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.BLOCK_NOTE_BLOCK_CHIME, 2.5F, 1.5F)
        }
    }

    override fun persistentSpawningActives(someEntity: LivingEntity) {
        if (someEntity.location.block.lightFromSky > 5) {
            // Firework
            (someEntity.world.spawnEntity(someEntity.location.clone().add(0.0, 3.0, 0.0), EntityType.FIREWORK_ROCKET) as Firework).also {
                val newMeta = it.fireworkMeta
                newMeta.power = 1
                newMeta.addEffect(
                    FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.WHITE)
                        .withFade(Color.AQUA).trail(true).flicker(true).build()
                )
                it.fireworkMeta = newMeta
                it.velocity = Vector((0..10).random().toDouble() * 0.1, 3.0, (0..10).random().toDouble() * 0.1)
                it.ticksToDetonate = 5 * 20
            }
        }
        someEntity.remove()
    }
}
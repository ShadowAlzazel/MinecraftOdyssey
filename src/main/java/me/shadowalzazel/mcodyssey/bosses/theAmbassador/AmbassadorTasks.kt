package me.shadowalzazel.mcodyssey.bosses.theAmbassador

import org.bukkit.entity.EntityType
import org.bukkit.entity.Illusioner
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class AmbassadorHijackTasks(private val ambassadorEntity: Illusioner) : BukkitRunnable() {

    private var counter = 0

    override fun run() {
        val playersNearAmbassador = ambassadorEntity.world.getNearbyPlayers(ambassadorEntity.location, 18.5)

        for (somePlayer in playersNearAmbassador) {
            // arrow
            val someArrow = ambassadorEntity.world.spawnEntity(ambassadorEntity.location.add(0.0, 2.5, 0.0), EntityType.ARROW)
            someArrow.velocity = somePlayer.location.subtract(someArrow.location).toVector().multiply(1.0)
        }

        counter += 1
        if (counter > 6) {
            ambassadorEntity.addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 10, 1))
            ambassadorEntity.teleport(ambassadorEntity.location.add(0.0, 5.0, 0.0))
            this.cancel()
        }
    }

}
package me.shadowalzazel.mcodyssey.bosses.the_ambassador

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Illusioner
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class AmbassadorHijackTasks(private val entity: Illusioner) : BukkitRunnable() {

    private var counter = 0
    override fun run() {
        counter += 1
        if (counter > 6) {
            entity.addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 10, 1))
            entity.teleport(entity.location.add(0.0, 8.0, 0.0))
            this.cancel()
        }
    }
}

class RemoveSingularityStand(private val stand: ArmorStand) : BukkitRunnable() {
    override fun run() {
        stand.remove()
    }
}

class AmbassadorDepartTask : BukkitRunnable() {
    override fun run() {
        val manager = Odyssey.instance.bossManager
        if (manager.currentBoss !is TheAmbassador) {
            this.cancel()
            return
        }
        val ambassador = manager.currentBoss as TheAmbassador
        if (ambassador.isActive) {
            ambassador.departBoss()
            manager.run {
                hasBossActive = false
                isAmbassadorDefeated = true
                currentBoss = null
            }
            this.cancel()
            return
        }
        if (ambassador.hasRemove) {
            this.cancel()
            return
        }
    }
}

class AmbassadorAttackCycle : BukkitRunnable() {

    override fun run() {
        val manager = Odyssey.instance.bossManager
        if (manager.currentBoss !is TheAmbassador) {
            this.cancel()
            return
        }
        val ambassador = manager.currentBoss as TheAmbassador
        if (ambassador.illusioner.isDead) {
            this.cancel()
            return
        }
        // Run Attack
        ambassador.runAttackPatterns()
    }
}
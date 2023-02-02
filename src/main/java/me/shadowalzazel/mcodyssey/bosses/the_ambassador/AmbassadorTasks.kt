package me.shadowalzazel.mcodyssey.bosses.the_ambassador

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Illusioner
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class AmbassadorHijackTasks(private val ambassadorEntity: Illusioner) : BukkitRunnable() {

    private var counter = 0
    override fun run() {
        counter += 1
        if (counter > 6) {
            ambassadorEntity.addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 10, 1))
            ambassadorEntity.teleport(ambassadorEntity.location.add(0.0, 8.0, 0.0))
            this.cancel()
        }
    }
}


class AmbassadorSingularity(private val someStand: ArmorStand) : BukkitRunnable() {
    override fun run() {
        someStand.remove()
    }

}


class AmbassadorDepartTask : BukkitRunnable() {
    override fun run() {
        if (MinecraftOdyssey.instance.currentBoss is AmbassadorBoss) {
            val ambassadorBoss = MinecraftOdyssey.instance.currentBoss as AmbassadorBoss
            if (ambassadorBoss.bossActive && ambassadorBoss.bossEntity != null) {
                ambassadorBoss.departBoss()
                MinecraftOdyssey.instance.also {
                    it.isBossActive = false
                    it.isAmbassadorDefeated = true
                    it.currentBoss = null
                }
                this.cancel()
            }
            else if (ambassadorBoss.removeBoss) {
                this.cancel()
            }
        }
    }
}

class AmbassadorAttackCycle(private val ambassadorEntity: Illusioner) : BukkitRunnable() {

    override fun run() {
        if (MinecraftOdyssey.instance.currentBoss is AmbassadorBoss) {
            val ambassadorBoss = MinecraftOdyssey.instance.currentBoss as AmbassadorBoss
            if (!ambassadorEntity.isDead && ambassadorEntity == ambassadorBoss.bossEntity!! && ambassadorBoss.bossActive) {
                ambassadorBoss.attackPatterns()
            }
            else {
                this.cancel()
            }
        }
        else {
            this.cancel()
        }
    }

}
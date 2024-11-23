package me.shadowalzazel.mcodyssey.common.mobs.other

import me.shadowalzazel.mcodyssey.common.mobs.base.OdysseyMob
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Zombie
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object RottingSummon : OdysseyMob("Rotting Summon", "rotting_summon",EntityType.ZOMBIE, -15.0) {

    override fun createMob(someWorld: World, spawningLocation: Location): Zombie {
        val newSummon = (super.createMob(someWorld, spawningLocation) as Zombie).apply {
            addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 20 * 9999, 1))
        }
        return newSummon
    }


}
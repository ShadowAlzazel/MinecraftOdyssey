package me.shadowalzazel.mcodyssey.common.mobs.other

import me.shadowalzazel.mcodyssey.common.mobs.base.OdysseyMob
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.HumanEntity


object Doppelganger : OdysseyMob("Doppelganger", "doppelganger", EntityType.PLAYER, 20.0) {

    fun createPlayerMob(someWorld: World, someLocation: Location) {
        val someDoppelganger = (createMob(someWorld, someLocation) as HumanEntity).also {
            it.wakeup(true)
        }
    }



}
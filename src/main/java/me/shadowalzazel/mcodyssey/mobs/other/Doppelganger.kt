package me.shadowalzazel.mcodyssey.mobs.other

import me.shadowalzazel.mcodyssey.mobs.utility.OdysseyMob
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player


object Doppelganger : OdysseyMob("Doppelganger", EntityType.PLAYER, 20.0) {

    fun createPlayerMob(someWorld: World, someLocation: Location) {
        val someDoppelganger = createMob(someWorld, someLocation).also {

        }
    }



}
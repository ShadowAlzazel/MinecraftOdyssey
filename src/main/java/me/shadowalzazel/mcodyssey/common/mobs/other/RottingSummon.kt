package me.shadowalzazel.mcodyssey.common.mobs.other

import me.shadowalzazel.mcodyssey.common.mobs.base.OdysseyMob
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Zombie
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object RottingSummon : OdysseyMob(
    "Rotting Summon",
    "rotting_summon",
    EntityType.ZOMBIE) {

    override fun spawn(world: World, location: Location): Zombie {
        val rottingSummon = (spawnBase(world, location) as Zombie).apply {
            addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 20 * 300, 0))
        }
        return rottingSummon
    }


}
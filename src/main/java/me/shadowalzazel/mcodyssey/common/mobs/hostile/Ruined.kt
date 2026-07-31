package me.shadowalzazel.mcodyssey.common.mobs.hostile

import me.shadowalzazel.mcodyssey.common.mobs.MobArchetypes
import me.shadowalzazel.mcodyssey.common.mobs.MobFactory
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Stray

object Ruined {

    fun spawn(world: World, location: Location): Stray {
        val mob = world.spawnEntity(location, EntityType.STRAY) as Stray
        mob.addScoreboardTag(EntityTags.RUINED) // <- required: RUINED.predicate matches on this
        mob.customName(Component.text("Ruined", TextColor.color(220, 216, 75)))
        MobFactory.factory.applyArchetype(mob, MobArchetypes.RUINED)
        return mob
    }
}
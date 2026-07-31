package me.shadowalzazel.mcodyssey.common.mobs.hostile

import me.shadowalzazel.mcodyssey.common.mobs.MobArchetypes
import me.shadowalzazel.mcodyssey.common.mobs.MobFactory
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.WitherSkeleton

object Preacher {

    fun spawn(world: World, location: Location): WitherSkeleton {
        val mob = world.spawnEntity(location, EntityType.WITHER_SKELETON) as WitherSkeleton
        mob.addScoreboardTag(EntityTags.PREACHER) // <- required: PREACHER.predicate matches on this
        mob.customName(Component.text("Preacher", TextColor.color(40, 6, 25)))
        MobFactory.factory.applyArchetype(mob, MobArchetypes.PREACHER)
        return mob
    }
}
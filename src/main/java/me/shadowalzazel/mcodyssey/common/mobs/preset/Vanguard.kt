package me.shadowalzazel.mcodyssey.common.mobs.preset

import me.shadowalzazel.mcodyssey.common.mobs.MobArchetypes
import me.shadowalzazel.mcodyssey.common.mobs.MobFactory
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Skeleton
import org.bukkit.entity.SkeletonHorse

object Vanguard {

    fun spawn(world: World, location: Location): Skeleton {
        val mob = world.spawnEntity(location, EntityType.SKELETON) as Skeleton
        mob.addScoreboardTag(EntityTags.VANGUARD) // <- required: VANGUARD.predicate matches on this
        MobFactory.factory.applyArchetype(mob, MobArchetypes.VANGUARD)
        return mob
    }

    /** The horse-riding bit is real bespoke behavior, so it's layered on top rather than baked into the archetype. */
    fun spawnKnight(world: World, location: Location): Pair<Skeleton, SkeletonHorse> {
        val knight = spawn(world, location).apply {
            customName(Component.text("Vanguard Knight"))
        }
        val mount = (world.spawnEntity(location, EntityType.SKELETON_HORSE) as SkeletonHorse).apply {
            isTamed = false
            addPassenger(knight)
            MobFactory.factory.setHealthAttribute(this, 100.0)
            MobFactory.factory.addSpeedAttribute(this, 0.13)
            MobFactory.factory.addStepAttribute(this, 2.5)
            heal(100.0)
        }
        return knight to mount
    }
}
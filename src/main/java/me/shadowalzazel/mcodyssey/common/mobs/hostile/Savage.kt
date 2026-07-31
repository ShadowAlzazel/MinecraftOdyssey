package me.shadowalzazel.mcodyssey.common.mobs.hostile

import me.shadowalzazel.mcodyssey.common.mobs.MobArchetypes
import me.shadowalzazel.mcodyssey.common.mobs.MobArchetypes.buildSavageKnightWeapon
import me.shadowalzazel.mcodyssey.common.mobs.MobFactory
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Zombie
import org.bukkit.entity.ZombieHorse
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object Savage {

    fun spawn(world: World, location: Location): Zombie {
        val mob = world.spawnEntity(location, EntityType.ZOMBIE) as Zombie
        mob.addScoreboardTag(EntityTags.SAVAGE) // <- required: SAVAGE.predicate matches on this
        mob.customName(Component.text("Savage", TextColor.color(220, 216, 75)))
        MobFactory.factory.applyArchetype(mob, MobArchetypes.SAVAGE)
        return mob
    }

    /** The horse-riding bit is bespoke, so it's layered on top rather than baked into the archetype. */
    fun spawnKnight(world: World, location: Location): Pair<Zombie, ZombieHorse> {
        val knight = spawn(world, location).apply {
            customName(Component.text("Savage Knight", TextColor.color(220, 216, 75)))
            clearActiveItem()
            equipment.setItemInOffHand(ItemStack(Material.AIR))
            equipment.setItemInMainHand(MobFactory.factory.buildSavageKnightWeapon())
        }
        val mount = (world.spawnEntity(location, EntityType.ZOMBIE_HORSE) as ZombieHorse).apply {
            isTamed = false
            addPassenger(knight)
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 300, 25),
                PotionEffect(PotionEffectType.SPEED, 20 * 300, 2)
            ))
            health = 100.0
        }
        return knight to mount
    }
}
package me.shadowalzazel.mcodyssey.common.effects

import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity

/** A named, reusable status effect. Add attribute changes + optional per-tick behaviour. */
class StatusEffect(
    val id: String,
    val changes: List<AttributeChange> = emptyList(),
    val tickInterval: Int = 0,
    val cancelIf: (LivingEntity) -> Boolean = { false },   // <-- expire early when true// 0 = no per-tick callback
    val onTick: (entity: LivingEntity, amplifier: Double) -> Unit = { _, _ -> },  // now gets amplifier
    val onRemove: (LivingEntity) -> Unit = {}                                  // cleanup hook
) {
    fun keyFor(attribute: Attribute): NamespacedKey =
        NamespacedKey("odyssey", "status.$id.${attribute.key.value()}")


    /*
    Lift, Knock Down, Crush, and Breach

    Vulnerability
     */

    companion object {

        val AEROSION = StatusEffect(
            id = "aerosion",
            changes = listOf(
                // -4% armor per level
                AttributeChange(Attribute.ARMOR, Operation.ADD_SCALAR) { level -> -0.04 * level }
            ),
            tickInterval = 30,
            onTick = { entity, amplifier ->
                //val stacks = entity.getIntTag(EntityTags.AEROSION_STACKS) ?: 1

                entity.world.spawnParticle(
                    Particle.SMALL_GUST,
                    entity.location.clone().add(0.0, 0.5, 0.0),
                    (8 * amplifier).toInt(), 0.5, 0.25, 0.5, 0.01
                )

                val damageSource = DamageSource.builder(DamageType.WIND_CHARGE).build()
                entity.damage(1.0, damageSource)

                // Suspend velocity
                entity.velocity = org.bukkit.util.Vector(0.0, 0.0, 0.0)
            },
            onRemove = { entity ->
                //entity.setIntTag(EntityTags.AEROSION_STACKS, 0)
                entity.removeScoreboardTag(EffectTags.AEROSION)  // keep for any external checks
            }
        )

        val LOW_GRAVITY = StatusEffect(
            id = "low_gravity",
            changes = listOf(
                // halve gravity per level (base gravity 0.08)
                AttributeChange(Attribute.GRAVITY, Operation.ADD_SCALAR) { level -> -0.5 * level }
            )
        )

        val HONEYED = StatusEffect(
            id = "honeyed",
            changes = listOf(
                // Have friction to be sticky
                AttributeChange(Attribute.FRICTION_MODIFIER, Operation.ADD_SCALAR) { level -> 0.2 * level }
            ),
            tickInterval = 10,
            onTick = { entity, amplifier ->
                // VFX
                val location = entity.location.clone().add(0.0, 0.35, 0.0)
                entity.world.spawnParticle(Particle.DRIPPING_HONEY, location, 5, 0.35, 1.0, 0.35)
                entity.world.spawnParticle(Particle.FALLING_HONEY, location, 2, 0.35, 1.0, 0.35)
                entity.world.spawnParticle(Particle.LANDING_HONEY, location, 2, 0.35, 0.4, 0.35)
                entity.world.playSound(location, Sound.BLOCK_HONEY_BLOCK_STEP, 1.5F, 0.9F)

                // Suspend velocity
                entity.velocity = org.bukkit.util.Vector(0.0, 0.0, 0.0)
            },
            onRemove = { entity ->
                entity.removeScoreboardTag(EffectTags.HONEYED)  // keep for any external checks
            }
        )

        val HEMORRHAGE = StatusEffect(
            id = "hemorrhage",
            changes = listOf(),
            tickInterval = 20,
            onTick = { entity, amplifier ->
                // VFX/SFX
                val color = EffectColors.HEMORRHAGING.toItemColor()
                val bloodDust = Particle.DustOptions(color, 1.0F)
                val bloodBlockBreak = Material.STRIPPED_MANGROVE_LOG.createBlockData()
                val bloodBlockDust = Material.NETHERRACK.createBlockData()
                val location = entity.location.clone().add(0.0, 0.35, 0.0)
                entity.world.spawnParticle(Particle.DUST , location, 20, 0.35, 0.25, 0.35, bloodDust)
                entity.world.spawnParticle(Particle.BLOCK, location, 20, 0.35, 0.25, 0.35, bloodBlockBreak)
                entity.world.spawnParticle(Particle.FALLING_DUST, location, 20, 0.35, 0.25, 0.35, bloodBlockDust)

                // Amplifier = original damage x 0.03 x level
                val damage = amplifier * 1.0
                val damageSource = DamageSource.builder(DamageType.GENERIC).build() // Stopped by Protection
                entity.damage(damage, damageSource)
            },
            onRemove = { entity ->
                entity.removeScoreboardTag(EffectTags.HEMORRHAGING)  // keep for any external checks
            }
        )

        val FREEZING = StatusEffect(
            id = "freezing",
            changes = listOf(
                // -5% movement speeds
                AttributeChange(Attribute.MOVEMENT_SPEED, Operation.ADD_SCALAR) { level -> -0.05 * level },
                AttributeChange(Attribute.FLYING_SPEED, Operation.ADD_SCALAR) { level -> -0.05 * level }
            ),
            tickInterval = 20,
            // Fire and ice don't coexist — expire the moment the entity is burning.
            cancelIf = { entity -> entity.fireTicks > 0 },
            onTick = { entity, amplifier ->
                // VFX/SFX
                val freezingBlock = Material.BLUE_ICE.createBlockData()
                val location = entity.location.clone().add(0.0, 0.5, 0.0)
                entity.world.spawnParticle(Particle.BLOCK, location, 10, 0.05, 0.2, 0.05, freezingBlock)
                entity.world.spawnParticle(Particle.SNOWFLAKE, location, 5, 0.05, 0.05, 0.05)

                // Logic
                entity.freezeTicks += 20
                val value = 1.0
                val damageSource = DamageSource.builder(DamageType.FREEZE).build()
                entity.damage(value, damageSource)
            },
            onRemove = { entity ->
                entity.removeScoreboardTag(EffectTags.FREEZING)
            }
        )


        /** Every effect that exists — used to know which attributes to sweep. */
        val all = listOf(
            AEROSION,
            LOW_GRAVITY,
            HONEYED,
            HEMORRHAGE,
            FREEZING,
            )

    }


}
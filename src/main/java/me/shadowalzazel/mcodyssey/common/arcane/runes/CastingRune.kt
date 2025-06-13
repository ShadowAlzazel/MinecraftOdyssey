package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import me.shadowalzazel.mcodyssey.common.arcane.util.RayTracerAndDetector
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingBuilder
import me.shadowalzazel.mcodyssey.common.arcane.util.ArcaneBallTimer
import me.shadowalzazel.mcodyssey.common.combat.AttackHelper
import me.shadowalzazel.mcodyssey.util.VectorParticles
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Snowball
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

// Category sealed class for "Casting Runes"
// These runes determine the fundamental way magic is expressed
// As such, these `call` the cast.

@Suppress("UnstableApiUsage")
sealed class CastingRune : ArcaneRune(), RayTracerAndDetector,
    AttackHelper, VectorParticles {

    // Helper class to run async calls later for runes
    class ManifestAsyncRunner(
        val rune: CastingRune,
        val context: CastingContext,
        val build: CastingBuilder) : BukkitRunnable() {
        override fun run() {
            rune.manifest(context, build)
        }
    }

    // Abstract Methods for runes and their implementation

    // A function that prepares modifiers and other runes before the spell is manifested
    abstract fun build(builder: CastingBuilder)

    abstract fun manifest(context: CastingContext, builder: CastingBuilder)


    /**
     * Class entry points for usage across systems
     */
    fun cast(context: CastingContext, builder: CastingBuilder) {
        // Delay
        if (builder.delayInTicks > 0) {
            val runner = ManifestAsyncRunner(this, context, builder)
            runner.runTaskLater(Odyssey.instance, builder.delayInTicks)
        } else {
            this.manifest(context, builder)
        }

    }

    /**
     * Assembles the rune using the builder or creates a new one
     */
    fun assemble(provided: CastingBuilder) {
        // Get a builder from the default builder or build with provided
        build(provided)
    }

    // List of all Casting runes

    class Point : CastingRune() {
        override val name = "point"
        override val displayName = "point"

        override fun build(builder: CastingBuilder) {
            // DEFAULT build parameters
            val damage = 0.0
            // Modify the builder
            builder.also {
                it.damage = damage
            }
        }
        override fun manifest(context: CastingContext, builder: CastingBuilder) {
            // Unpack build
            val damage = builder.damage
            val damageType = builder.damageType
            val particle = builder.particle

            val filterEntities = mutableListOf(context.caster)
            for (e in context.ignoredTargets) {
                if (e is LivingEntity) filterEntities.add(e)
            }


            //  Point Logic
            val pointLocation: Location
            val target = context.target
            if (target is LivingEntity) {
                // Damage Logic
                val damageSource = createEntityDamageSource(context.caster, null, damageType)
                target.damage(damage, damageSource)
                // Set point to target
                pointLocation = target.eyeLocation
            } else {
                val targetLocation = context.targetLocation
                pointLocation = targetLocation ?: context.castingLocation
            }
            // Particles
            spawnPointParticles(
                particle,
                pointLocation,
                10,
                0.05
            )

        }
    }


    class Zone : CastingRune() {
        override val name = "zone"
        override val displayName = "Zone"


        override fun build(builder: CastingBuilder) {
            // DEFAULT build parameters
            val damage = 1.0
            val range = 16.0
            val radius = 3.0
            val aimAssist = 0.1
            // Modify the builder
            builder.also {
                it.damage = damage
                it.range = range
                it.radius = radius
                it.aimAssist = aimAssist
            }
        }

        override fun manifest(context: CastingContext, builder: CastingBuilder) {
            // Unpack build
            val range = builder.range
            val aimAssist = builder.aimAssist
            val radius = builder.radius
            val damage = builder.damage
            val damageType = builder.damageType
            val particle = builder.particle

            // Circle Location and Detection Logic
            /*
            val traceLocation = getPathTraceLocation(
                context.castingLocation,
                context.direction,
                listOf(context.caster),
                range,
                raySize = aimAssist)
            val circleCenter = traceLocation ?: context.targetLocation
            circleCenter ?: return
            // Change context

            context.targetLocation = circleCenter
             */
            val circleCenter = context.targetLocation ?: context.castingLocation

            val filterEntities = mutableListOf(context.caster)
            for (e in context.ignoredTargets) {
                if (e is LivingEntity) filterEntities.add(e)
            }

            // Damage Logic
            val damageSource = createEntityDamageSource(context.caster, null, damageType)
            circleCenter.getNearbyLivingEntities(radius).forEach {
                if (it !in filterEntities) {
                    // Detect if damage or heal
                    it.damage(damage, damageSource)
                    context.target = it
                }
            }
            // Particles and effects
            spawnCircleParticles(
                particle = particle,
                center = circleCenter,
                upDirection = Vector(0, 1, 0),
                radius = radius,
                heightOffset = 0.25,
                count = (radius * Math.PI * 7).toInt())
            context.world.playSound(context.castingLocation, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2F, 2F)
        }

    }


    class Beam : CastingRune() {
        override val name = "beam"
        override val displayName = "Beam"

        override fun build(builder: CastingBuilder) {
            // DEFAULT build parameters
            val damage = 2.0
            val range = 16.0
            val aimAssist = 0.25
            // Modify the builder
            builder.also {
                it.damage = damage
                it.range = range
                it.aimAssist = aimAssist
            }
        }

        override fun manifest(context: CastingContext, builder: CastingBuilder) {
            // Unpack build
            val totalRange = builder.range
            val aimAssist = builder.aimAssist
            val damageType = builder.damageType
            val damage = builder.damage
            val particle = builder.particle

            // Temporary locations for beam
            val startLocation: Location = context.castingLocation
            val endLocation: Location

            //val target = getRayTraceEntity(context.caster, totalRange, aimAssist)
            val filterEntities = mutableListOf(context.caster)
            for (e in context.ignoredTargets) {
                if (e is LivingEntity) filterEntities.add(e)
            }

            val target = getEntityRayTrace(
                startLocation,
                context.direction,
                filterEntities,
                totalRange,
                aimAssist)

            // After running target checks
            if (target is LivingEntity) {
                val damageSource = createEntityDamageSource(context.caster, null, damageType)
                target.damage(damage, damageSource)
                endLocation = target.eyeLocation
            }
            else {
                val rayTraceBlock = context.world.rayTraceBlocks(
                    context.castingLocation,
                    context.direction,
                    totalRange,
                    FluidCollisionMode.NEVER)?.hitBlock
                endLocation = if (rayTraceBlock != null) {
                    rayTraceBlock.location.toCenterLocation()
                } else {
                    context.castingLocation.clone().add(context.direction.clone().normalize().multiply(totalRange))
                }
            }

            // Particles in Line
            val particleCount = endLocation.distance(context.castingLocation) * 6
            spawnLineParticles(
                particle = particle,
                start = context.castingLocation,
                end = endLocation,
                count = particleCount.toInt()
            )
            context.targetLocation = endLocation
            context.world.playSound(context.castingLocation, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2F, 2F)
        }

    }

    class Ball : CastingRune() {
        override val name = "ball"
        override val displayName = "ball"

        override fun build(builder: CastingBuilder) {
            // DEFAULT build parameters
            val damage = 5.0
            val range = 16.0
            val aimAssist = 0.1
            val speed = 0.5
            // Modify the builder
            builder.also {
                it.damage = damage
                it.range = range
                it.aimAssist = aimAssist
                it.speed = speed
            }

        }
        override fun manifest(context: CastingContext, builder: CastingBuilder) {
            // Unpack build
            val totalRange = builder.range
            val aimAssist = builder.aimAssist
            val damageType = builder.damageType
            val damage = builder.damage
            val particle = builder.particle
            val speed = builder.speed

            // Set vectors and velocity
            val direction = context.direction
            val velocity = direction.clone().normalize().multiply(speed)

            val ball = context.world.spawnEntity(context.castingLocation, EntityType.SNOWBALL) as Snowball
            ball.also {
                it.item = ItemStack(Material.ENDER_PEARL)
                it.addScoreboardTag(EntityTags.MAGIC_BALL)
                it.velocity = velocity
                it.shooter = context.caster
                it.setHasLeftShooter(false)
                it.setGravity(false)
            }

            // Particles and Timer
            val ballEffects = ArcaneBallTimer(ball, particle)
            ballEffects.runTaskTimer(Odyssey.instance, 1, 2)

        }
    }

    class Missile : CastingRune() {
        override val name = "missile"
        override val displayName = "missile"

        override fun build(builder: CastingBuilder) {
            TODO("Not yet implemented")
        }
        override fun manifest(context: CastingContext, builder: CastingBuilder) {
            TODO("Not yet implemented")
        }
    }

    class Slice : CastingRune() {
        override val name = "slice"
        override val displayName = "Slice"

        override fun build(builder: CastingBuilder) {
            TODO("Not yet implemented")
        }
        override fun manifest(context: CastingContext, builder: CastingBuilder) {
            TODO("Not yet implemented")
        }
    }

    class Aura : CastingRune() {
        override val name = "aura"
        override val displayName = "aura"

        override fun build(builder: CastingBuilder) {
            TODO("Not yet implemented")
        }
        override fun manifest(context: CastingContext, builder: CastingBuilder) {
            TODO("Not yet implemented")
        }
    }



}
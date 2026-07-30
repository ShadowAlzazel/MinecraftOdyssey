package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.CastingContext
import me.shadowalzazel.mcodyssey.common.arcane.ArcaneSource
import me.shadowalzazel.mcodyssey.common.arcane.ArcaneTarget
import me.shadowalzazel.mcodyssey.common.arcane.CastingBuilder
import me.shadowalzazel.mcodyssey.common.arcane.RuneDataManager
import me.shadowalzazel.mcodyssey.common.arcane.runes.DomainRune.Kernel.getItemNameId
import me.shadowalzazel.mcodyssey.common.arcane.runes.DomainRune.Kernel.getStringTag
import me.shadowalzazel.mcodyssey.common.arcane.util.*
import me.shadowalzazel.mcodyssey.common.combat.AttackHelper
import me.shadowalzazel.mcodyssey.util.VectorParticles
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.damage.DamageType
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Snowball
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

// =====================================================================================
//  ARCANE RUNE  --  Base sealed class for all runes
// =====================================================================================
// Notes:
/*
    For magic circle: Runes are read in a CLOCK-WISE ORDER!
 */

sealed class ArcaneRune : RuneDataManager {
    abstract val name: String
    abstract val displayName: String

    companion object {
        fun fromNameID(name: String, value: Double? = null): ArcaneRune? = when (name) {
            // Casting (The form of the spell, a ray or a zone)
            "beam" -> CastingRune.Beam()
            "zone" -> CastingRune.Zone()
            "ball" -> CastingRune.Ball()
            "point" -> CastingRune.Point()

            // Domain (change the casting context like location or target)
            "next" -> DomainRune.Next
            "nearby" -> DomainRune.Nearby
            "origin" -> DomainRune.Origin
            "kernel" -> DomainRune.Kernel
            "swap" -> DomainRune.Swap
            "differ" -> DomainRune.Differ
            "direct" -> DomainRune.Direct
            "self" -> DomainRune.Self

            // Augment (effects like breaking blocks or TP)
            "break" -> AugmentRune.Break(value ?: 2.0)
            "coda" -> AugmentRune.Coda
            "pick_up" -> AugmentRune.PickUp
            "teleport" -> AugmentRune.Teleport
            "heal" -> AugmentRune.Heal(value ?: 4.0)
            // "levitation" -> If target is a block, move 1 up! if target is entity -> levitate?

            // Modifier (stat modifiers for other runes)
            "amplify" -> ModifierRune.Amplify(4.0)
            "wide" -> ModifierRune.Wide(value ?: 1.0)
            "delay" -> ModifierRune.Delay(2.0)
            "convergence" -> ModifierRune.Convergence(1.0)
            "range" -> ModifierRune.Range(16.0)


            else -> null
        }

        fun fromRawItem(item: ItemStack): ArcaneRune? = when (item.getItemNameId()) {
            // Casting
            "alexandrite" -> CastingRune.Beam()
            "snowball" -> CastingRune.Zone()
            "arrow" -> CastingRune.Ball()
            "iron_nugget" -> CastingRune.Point()
            // Domain
            "heart_of_the_sea" -> DomainRune.Next
            "ender_eye" -> DomainRune.Nearby
            "nether_star" -> DomainRune.Origin
            "oak_sapling" -> DomainRune.Kernel
            "popped_chorus_fruit" -> DomainRune.Swap
            "coal" -> DomainRune.Differ
            "stick" -> DomainRune.Direct
            "paper" -> DomainRune.Self
            // Augment
            "cactus" -> AugmentRune.Break(2.0)
            "gold_ingot" -> AugmentRune.Coda
            "iron_ingot" -> AugmentRune.PickUp
            "ender_pearl" -> AugmentRune.Teleport
            "honeycomb" -> AugmentRune.Heal(4.0)
            // Modifier
            "diamond" -> ModifierRune.Amplify(4.0)
            "emerald" -> ModifierRune.Wide(1.0)
            "clock" -> ModifierRune.Delay(2.0)
            "kunzite" -> ModifierRune.Convergence(1.0)
            "amethyst_shard" -> ModifierRune.Range(16.0)
            // Modifier Special
            "ruby" -> ModifierRune.Source(DamageType.IN_FIRE, Particle.FLAME)
            "echo_shard" -> ModifierRune.Source(DamageType.SONIC_BOOM, Particle.SONIC_BOOM)
            "neptunian" -> ModifierRune.Source(DamageType.FREEZE, Particle.SNOWFLAKE)
            "jovianite" -> ModifierRune.Source(DamageType.MAGIC, Particle.WAX_OFF)
            else -> null
        }

        /**
         * This gets an arcane runes from an item
         */
        fun getRuneFromItem(item: ItemStack): ArcaneRune? {
            val runeName = item.getStringTag(ItemDataTags.STORED_ARCANE_RUNE)
            val readRune = fromNameID(runeName ?: "none")
            // -------------------------------------
            if (readRune == null) {
                val directRune = fromRawItem(item)
                return directRune
            }
            return readRune
        }


    }


}

// =====================================================================================
//  CASTING RUNE  --  The fundamental way magic is expressed (they `call` the cast)
// =====================================================================================
// Category sealed class for "Casting Runes"
// These runes determine the fundamental way magic is expressed
// As such, these `call` the cast.

sealed class CastingRune : ArcaneRune(), RayTracerAndDetector,
    AttackHelper, VectorParticles {

    // Helper class to run async calls later for runes
    class DelayedCastRunner(
        val rune: CastingRune,
        val source: ArcaneSource,
        val context: CastingContext,
        val build: CastingBuilder
    ) : BukkitRunnable() {
        override fun run() {
            rune.manifest(source, context, build)
        }
    }

    // Abstract Methods for runes and their implementation

    // A function that prepares modifiers and other runes before the spell is manifested
    abstract fun build(builder: CastingBuilder)

    abstract fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder)


    /**
     * Class entry points for usage across systems
     */
    fun cast(source: ArcaneSource, context: CastingContext, builder: CastingBuilder) {
        // Delay
        if (builder.delayInTicks > 0) {
            val runner = DelayedCastRunner(this, source, context, builder)
            runner.runTaskLater(Odyssey.instance, builder.delayInTicks)
        } else {
            this.manifest(source, context, builder)
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
        override val displayName = "Point"

        override fun build(builder: CastingBuilder) {
            // DEFAULT build parameters
            val damage = 0.0
            // Modify the builder
            builder.also {
                it.damage = damage
            }
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder) {
            val target = context.target
            val caster = context.caster
            // Unpack build
            val damage = builder.damage
            val damageType = builder.damageType
            val particle = builder.particle
            //  Point Logic
            val pointLocation: Location

            if (target?.entityTarget is LivingEntity) {
                // DO Effect
                source.invoke(
                    target = target,
                    caster = caster,
                    direction = context.direction,
                    bonus = builder.damage
                )
                // Set point to target
                pointLocation = target.entityTarget.eyeLocation
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
            val damage = 0.0
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

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder) {
            // Context
            val caster = context.caster

            // Unpack build
            val range = builder.range
            val aimAssist = builder.aimAssist
            val radius = builder.radius
            val damage = builder.damage
            val damageType = builder.damageType
            val particle = builder.particle

            val circleCenter = context.targetLocation ?: context.castingLocation

            // Filter out caster and ignore list
            val filterEntities: MutableList<LivingEntity> = mutableListOf()
            //if (caster.entityCaster is LivingEntity) filterEntities.add(caster.entityCaster)
            for (e in context.ignoredTargets) {
                if (e.entityTarget is LivingEntity) filterEntities.add(e.entityTarget)
            }

            // DO EFFECT,
            circleCenter.getNearbyLivingEntities(radius).forEach {
                if (it !in filterEntities) {
                    val zoneDirection = context.direction // TODO: Different directions from center or from outside
                    source.invoke(
                        target = ArcaneTarget(it),
                        caster = caster,
                        direction = zoneDirection,
                        bonus = damage
                    )
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
            val damage = 1.0
            val range = 16.0
            val aimAssist = 0.25
            // Modify the builder
            builder.also {
                it.damage = damage
                it.range = range
                it.aimAssist = aimAssist
            }
        }

        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder) {
            // Context
            val caster = context.caster

            // Unpack build
            val totalRange = builder.range
            val aimAssist = builder.aimAssist
            val damageType = builder.damageType
            val damage = builder.damage
            val particle = builder.particle

            // Temporary locations for beam
            val castLocation: Location = context.castingLocation
            val targetLocation: Location? = context.targetLocation
            val beamDirection = targetLocation?.clone()?.subtract(castLocation)?.toVector() ?: context.direction

            // What the NEW target location will be
            val endLocation: Location

            // Filter out caster and ignore list
            val filterEntities: MutableList<LivingEntity> = mutableListOf()
            //if (caster.entityCaster is LivingEntity) filterEntities.add(caster.entityCaster)
            for (e in context.ignoredTargets) {
                if (e.entityTarget is LivingEntity) filterEntities.add(e.entityTarget)
            }

            val rayTraceEntity = getEntityRayTrace(
                castLocation,
                beamDirection,
                filterEntities,
                totalRange,
                aimAssist)

            // After running target checks
            if (rayTraceEntity is LivingEntity) {
                val newTarget = ArcaneTarget(entityTarget = rayTraceEntity)
                context.target = newTarget
                source.invoke(
                    target = newTarget,
                    caster = caster,
                    direction = beamDirection,
                    bonus = damage
                )
                endLocation = rayTraceEntity.eyeLocation
            }
            // Is Not a Living Entity
            else {
                val rayTraceBlock = context.world.rayTraceBlocks(
                    context.castingLocation,
                    context.direction,
                    totalRange,
                    FluidCollisionMode.NEVER)?.hitBlock
                // Set end location
                if (rayTraceBlock != null) {
                    val newTarget = ArcaneTarget(blockTarget = rayTraceBlock)
                    context.target = newTarget
                    source.invoke(
                        target = newTarget,
                        caster = caster,
                        direction = beamDirection,
                        bonus = damage
                    )
                    endLocation = rayTraceBlock.location.toCenterLocation()
                }
                // Fallback is just a line to max range
                else {
                    endLocation = context.castingLocation.clone().add(context.direction.clone().normalize().multiply(totalRange))
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
            context.world.playSound(endLocation, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2F, 2F)
        }

    }

    class Ball : CastingRune() {
        override val name = "ball"
        override val displayName = "Ball"

        override fun build(builder: CastingBuilder) {
            // DEFAULT build parameters
            val damage = 3.0
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
        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder) {
            val caster = context.caster
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
                if (caster.entityCaster is LivingEntity) it.shooter = caster.entityCaster
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
        override val displayName = "Missile"

        override fun build(builder: CastingBuilder) {
            TODO("Not yet implemented")
        }
        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder) {
            TODO("Not yet implemented")
        }
    }

    class Slice : CastingRune() {
        override val name = "slice"
        override val displayName = "Slice"

        override fun build(builder: CastingBuilder) {
            TODO("Not yet implemented")
        }
        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder) {
            TODO("Not yet implemented")
        }
    }

    class Aura : CastingRune() {
        override val name = "aura"
        override val displayName = "Aura"

        override fun build(builder: CastingBuilder) {
            TODO("Not yet implemented")
        }
        override fun manifest(source: ArcaneSource, context: CastingContext, builder: CastingBuilder) {
            TODO("Not yet implemented")
        }
    }



}

// =====================================================================================
//  DOMAIN RUNE  --  Change the casting context (location / origin / target)
// =====================================================================================

sealed class DomainRune : ArcaneRune(), RayTracerAndDetector {
    // Domain runes change the casting context of the spell.
    // Changes like the location or entity where it originates or targets

    fun change(original: CastingContext, context: CastingContext) {

        val domain = context.clone()
        var successful = true

        // Want to change context
        when (this) {
            is Kernel -> {
                domain.castingLocation = original.castingLocation
            }
            is Origin -> {
                //val originLocation = context.caster
                val originLocation = context.caster.getLocation()
                domain.castingLocation = originLocation
            }
            is Direct -> {
                val range = 16.0
                val traceEntity = getEntityRayTrace(
                    domain.castingLocation,
                    domain.direction,
                    context.caster.toEntityList(),
                    range,
                    0.05)

                // Check if target
                if (traceEntity is LivingEntity) {
                    domain.targetLocation = traceEntity.location
                    domain.target = ArcaneTarget(entityTarget = traceEntity)
                }
                // If not try ray trace again
                else {
                    val traceLocation = getHitLocationRayTrace(
                        domain.castingLocation,
                        domain.direction,
                        context.caster.toEntityList(),
                        range,
                        0.05)
                    if (traceLocation != null) {
                        domain.targetLocation = traceLocation
                    } else {
                        domain.targetLocation = domain.castingLocation.clone().add(domain.direction.clone().normalize().multiply(range))
                    }
                }

            }
            is Self -> {
                // Add self to target-able entities
                val ignoredTargets = domain.ignoredTargets.toList()
                for (n in ignoredTargets) {
                    // Look for self in ignoredTargets, then remove
                    if (n.entityTarget == domain.caster.entityCaster) {
                        domain.ignoredTargets.remove(n)
                        break
                    }
                    if (n.blockTarget == domain.caster.blockCaster) {
                        domain.ignoredTargets.remove(n)
                        break
                    }
                }
                // Convert to target and set as new
                domain.target = domain.caster.convertToTarget()
            }
            is Next -> {
                //val target = domain.target
                if (domain.target != null) {
                    // Move to eye height
                    val entityTarget = domain.target!!.entityTarget
                    if (entityTarget is LivingEntity) {
                        domain.castingLocation = entityTarget.eyeLocation
                    } else {
                        domain.castingLocation = domain.target!!.getLocation()
                    }
                }
                else if (domain.targetLocation != null) {
                    domain.castingLocation = domain.targetLocation!!
                }
                else {
                    successful = false
                }
            }
            // Sets TARGET to NEAREST entity from the CastingLocation
            is Nearby -> {
                // Get Nearby entities if not target or ignored
                val nearby = domain.castingLocation.getNearbyLivingEntities(6.0)
                // Remove most recent target to prevent recursive calls
                if (domain.target?.entityTarget is LivingEntity) {
                    val entityTarget = domain.target!!.entityTarget as LivingEntity
                    nearby.remove(entityTarget)
                }
                // Remove if in the ignored list
                for (n in domain.ignoredTargets) {
                    if (n.entityTarget is LivingEntity) {
                        nearby.remove(n.entityTarget)
                    }
                }
                // Continue without errors
                if (nearby.isNotEmpty()) {  // Ignore empty list
                    // Sort list to nearest
                    val sortedNearby = nearby.sortedBy { it.location.distance(domain.castingLocation) }
                    val nearestEntity = sortedNearby.first()
                    // Set Target, Location and Direction
                    domain.target = ArcaneTarget(entityTarget = nearestEntity)
                    //domain.targetLocation = nearestEntity.eyeLocation
                    //domain.direction = nearestEntity.eyeLocation.clone().subtract(domain.castingLocation).toVector()
                }
                else {
                    successful = false
                }
            }
            is Trace -> {
                // New Direction?
            }
            is Swap -> {
                // Swap domains
                if (domain.targetLocation != null) {
                    val temp = domain.castingLocation
                    domain.castingLocation = domain.targetLocation!!
                    domain.targetLocation = temp
                }
                else {
                    successful = false
                }
            }
            is Differ -> {
                if (domain.target != null) domain.ignoredTargets.add(domain.target!!)
                else successful = false
            }
            else -> successful = false
        }
        // Apply changes to the context
        if (successful) {
            context.also {
                // apply domain changes
                it.castingLocation = domain.castingLocation
                it.direction = domain.direction
                it.target = domain.target
                it.targetLocation = domain.targetLocation
                // ignore targets
                for (e in domain.ignoredTargets) {
                    if (e !in it.ignoredTargets) it.ignoredTargets.add(e)
                }
            }
        }

    }

    // Sets the cast location back to the ORIGINAL cast
    data object Origin : DomainRune() {
        override val name = "origin"
        override val displayName = "Origin"
    }

    data object Kernel : DomainRune() {
        override val name = "kernel"
        override val displayName = "Kernel"
    }

    // This changes the `castingLocation` to the CURRENT `targetLocation`
    data object Next : DomainRune() {
        override val name = "next"
        override val displayName = "Next"
    }

    data object Direct : DomainRune() {
        override val name = "direct"
        override val displayName = "Direct"
    }

    data object Link : DomainRune() {
        override val name = "link"
        override val displayName = "Link"
    }

    data object Self : DomainRune() {
        override val name = "self"
        override val displayName = "Self"
    }

    // This `returns` the nearest entity. Can stack with other variable runes
    // Sets the `target` to the nearest `entity`
    data object Nearby : DomainRune() {
        override val name = "nearby"
        override val displayName = "Nearby"
    }

    data object Trace : DomainRune() {
        override val name = "trace"
        override val displayName = "Trace"
    }

    // Looks for a new entity/location that is NOT the same
    data object Differ : DomainRune() {
        override val name = "differ"
        override val displayName = "Differ"
    }

    // Looks for a new entity/location that is NOT the same
    data object Omni : DomainRune() {
        override val name = "omni"
        override val displayName = "Omni"
    }

    // Switches the `cast` and `target` locations/entities in the context
    data object Swap : DomainRune() {
        override val name = "swap"
        override val displayName = "Swap"
    }

}

// =====================================================================================
//  AUGMENT RUNE  --  Effects like breaking blocks, healing, teleporting
// =====================================================================================

sealed class AugmentRune : ArcaneRune() {
    // Variable runes CHANGE how the sequence is READ
    // How the loop/run time behaves

    fun effect(context: CastingContext) {
        val target = context.target
        val caster = context.caster

        when (this) {
            is Coda -> {
                // TODO: Special Case
            }
            is Break -> {
                val block = context.targetLocation?.block
                if (block != null) {
                    // Use the wiki to find the values to break
                    // https://minecraft.wiki/w/Module:Blast_resistance_values
                    block.breakNaturally()
                }
            }
            is PickUp -> {
                val pickUpLocation = context.targetLocation ?: return
                val nearby = pickUpLocation.getNearbyEntities(1.0, 1.0, 1.0)
                if (nearby.isEmpty()) return
                val items = nearby.filter { it is Item }
                if (items.isEmpty()) return
                val itemToPickUp = items.first()
                if (itemToPickUp is Item) {
                    itemToPickUp.teleport(context.castingLocation)
                }
            }
            is Heal -> {
                if (target?.entityTarget is LivingEntity) {
                    target.entityTarget.heal(this.value)
                }
            }
            is Teleport -> {
                if (target?.entityTarget is LivingEntity) {
                    target.entityTarget.teleport(context.castingLocation)
                }
            }
            else -> {}
        }
    }

    // Mimics/Clones the original casting context conditions
    // different from the ORIGIN rune, as that can have the `target` change
    data object Repeat : AugmentRune() {
        override val name = "repeat"
        override val displayName = "Repeat"
    }

    // BEHAVES like a music CODA
    // Goes back to the beginning, IGNORES codas
    data object Coda : AugmentRune() {
        override val name = "coda"
        override val displayName = "coda"
    }

    data object PickUp : AugmentRune() {
        override val name = "pick_up"
        override val displayName = "pick_up"
    }

    class Vulnerability() : AugmentRune() {
        override val name = "vulnerability"
        override val displayName = "vulnerability"
    }

    // ENVIRONMENT RUNES
    class Light()  : AugmentRune() {
        override val name = "light"
        override val displayName = "light"
    }

    class Heal(val value: Double)  : AugmentRune() {
        override val name = "heal"
        override val displayName = "heal"
    }

    class Break(val value: Double = 0.0)  : AugmentRune() {
        override val name = "break"
        override val displayName = "break"
    }

    data object Teleport : AugmentRune() {
        override val name = "teleport"
        override val displayName = "teleport"
    }


    // MAYBE ITEM runes
    // detects if ITEM

    // OR pick up ITEM

}

// =====================================================================================
//  MODIFIER RUNE  --  Stat modifiers for other runes
// =====================================================================================

@Suppress("UnstableApiUsage")
sealed class ModifierRune : ArcaneRune() {
    abstract val value: Double

    class Wide(value: Double?) : ModifierRune() {
        override val name = "wide"
        override val displayName = "Wide"
        override val value = value ?: 0.0
    }

    class Speed(value: Double?) : ModifierRune() {
        override val name = "speed"
        override val displayName = "speed"
        override val value = value ?: 0.0
    }


    // delay
    class Delay(value: Double?) : ModifierRune() {
        override val name = "delay"
        override val displayName = "delay"
        override val value = value ?: 0.0
    }

    class Source(
        val damageType: DamageType,
        val particle: Particle
    ) : ModifierRune() {
        override val name = "source"
        override val displayName = "source"
        override val value = 0.0
    }

    // Is how potent the manifestation is i.e. DAMAGE
    class Amplify(value: Double?) : ModifierRune() {
        override val name = "amplify"
        override val displayName = "amplify"
        override val value = value ?: 0.0
    }

    // How Precise the manifestation is
    class Convergence(value: Double?) : ModifierRune() {
        override val name = "convergence"
        override val displayName = "Convergence"
        override val value = value ?: 0.0
    }

    class Range(value: Double?) : ModifierRune() {
        override val name = "range"
        override val displayName = "range"
        override val value = value ?: 0.0
    }


}
package me.shadowalzazel.mcodyssey.common.arcane

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.ManifestationRune
import me.shadowalzazel.mcodyssey.common.arcane.runes.ModifierRune
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import me.shadowalzazel.mcodyssey.common.arcane.util.RayTracerAndDetector
import me.shadowalzazel.mcodyssey.common.combat.AttackHelper
import me.shadowalzazel.mcodyssey.util.VectorParticles
import me.shadowalzazel.mcodyssey.common.tasks.arcane_tasks.MagicMissileLauncher
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps.ARCANE_RANGES
import org.bukkit.*
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.Damageable
import org.bukkit.util.Vector

@Suppress("UnstableApiUsage")
interface ArcaneEquipmentManager : VectorParticles, AttackHelper, DataTagManager, RayTracerAndDetector {

    private fun checkOffhandRunes(caster: LivingEntity, runes: MutableList<ArcaneRune>) {
        if (caster !is Player) return
        val equipment = caster.equipment ?: return

        // read
        // TODO: Bundle reader
        // JUST TO SHOWCASE
        if (equipment.itemInOffHand.type == Material.BUNDLE) { // Temp bundle reader
            // TODO: Temporary reader
            val bundleContents = equipment.itemInOffHand.getData(DataComponentTypes.BUNDLE_CONTENTS) ?: return
            if (bundleContents.contents().isEmpty()) return
            val items = bundleContents.contents()
            for (item in items) {
                when (item.getItemNameId()) {
                    "ruby" -> runes.add(ModifierRune.Source(DamageType.IN_FIRE, Particle.FLAME))
                    "diamond" -> runes.add(ModifierRune.Amplify(4.0))
                    "emerald" -> runes.add(ModifierRune.Wide(2.0))
                    "echo_shard" -> runes.add(ModifierRune.Source(DamageType.SONIC_BOOM, Particle.SONIC_BOOM))
                    "neptunian" -> runes.add(ModifierRune.Source(DamageType.FREEZE, Particle.SNOWFLAKE))
                    "alexandrite" -> runes.add(ManifestationRune.Beam()) // TODO: overwrite or ADD!!!
                    "kunzite" -> runes.add(ModifierRune.Convergence(1.0))
                    "amethyst_shard" -> runes.add(ModifierRune.Range(16.0))
                    "jovianite" -> runes.add(ModifierRune.Source(DamageType.MAGIC, Particle.WAX_OFF))
                    "clock" -> runes.addAll(listOf(
                        ModifierRune.Delay(3.0),
                        ModifierRune.Source(DamageType.MAGIC, Particle.WAX_ON))
                    )
                }
            }

        }// TEMP offhand reader
        else if (equipment.itemInOffHand.type != Material.AIR) {
            when (equipment.itemInOffHand.getItemNameId()) {
                "ruby" -> runes.add(ModifierRune.Source(DamageType.IN_FIRE, Particle.FLAME))
                "diamond" -> runes.add(ModifierRune.Amplify(2.0))
                "emerald" -> runes.add(ModifierRune.Wide(2.0))
                "echo_shard" -> runes.add(ModifierRune.Source(DamageType.SONIC_BOOM, Particle.SONIC_BOOM))
                "neptunian" -> runes.add(ModifierRune.Source(DamageType.FREEZE, Particle.SNOWFLAKE))
                "alexandrite" -> runes.add(ManifestationRune.Beam()) // TODO: overwrite or ADD!!!
                "kunzite" -> runes.add(ModifierRune.Convergence(1.0))
                "amethyst_shard" -> runes.add(ModifierRune.Range(16.0))
                "jovianite" -> runes.add(ModifierRune.Source(DamageType.MAGIC, Particle.WAX_OFF))
                "clock" -> runes.addAll(listOf(
                    ModifierRune.Delay(3.0),
                    ModifierRune.Source(DamageType.MAGIC, Particle.WAX_ON))
                )

            }
        }

    }

    fun arcanePenCastingHandler(caster: LivingEntity) {
        if (caster !is Player) return
        // ----------- BUILDING SPELL ----------
        val equipment = caster.equipment ?: return
        val arcanePen = equipment.itemInOffHand ?: return
        val scrolls = equipment.itemInMainHand ?: return
        val bundleContents = arcanePen.getData(DataComponentTypes.BUNDLE_CONTENTS) ?: return

        // Check if we can build spell
        val spellBuilder = ArcaneSpellBuilder(itemSource = arcanePen, additionalItems = bundleContents.contents())
        val canBuildSpell = spellBuilder.canBuildSpell()
        if (!canBuildSpell) return

        // Context params based on initial conditions
        val direction = caster.eyeLocation.direction.clone()
        val target = getRayTraceEntity(caster, 16.0, 0.15)
        val targetLocation: Location = if (target != null) {
            if (target is LivingEntity) target.eyeLocation else target.location
        } else {
            caster.eyeLocation.clone().add(direction.clone().normalize().multiply(32.0))
        }

        // Form the starting spell context
        val spellContext = CastingContext(
            caster = caster,
            world = caster.world,
            castingLocation = caster.location,
            direction = direction,
            target = target,
            targetLocation = targetLocation,
            runes = spellBuilder.runeSequence
        )

        val spell = spellBuilder.formSpell(spellContext)

    }

    fun arcaneSpellHandler(caster: LivingEntity) {
        if (caster !is Player) return
        val equipment = caster.equipment ?: return
        val arcaneHand = equipment.itemInMainHand
        if (caster.getCooldown(arcaneHand) > 0) return
        //equipment.itemInOffHand.damage(1, caster)
        // ----------- BUILDING SPELL ----------
        // CONTEXT DETECTION
        // TODO: Read from items

        // Spell and modifier building
        val runes = mutableListOf<ArcaneRune>()

        // SCROLL DEFAULT
        var manifestRune: ManifestationRune = ManifestationRune.Zone()
        runes.add(ModifierRune.Range(10.0))

        // Get modifiers
        checkOffhandRunes(caster, runes)
        for (r in runes) {
            if (r is ManifestationRune) {
                manifestRune = r
            }
        }
        /*
        val context = CastingContext(
            caster = caster,
            world = caster.world,
            castingLocation = caster.location,
            direction = ,
            target = null,
            targetLocation = null,
            runes = runes
        )

        manifestRune.cast(context)

         */
    }


    fun arcaneWandHandler(caster: LivingEntity) {
        if (caster !is Player) return
        val equipment = caster.equipment ?: return
        val arcaneHand = equipment.itemInMainHand
        if (caster.getCooldown(arcaneHand) > 0) return
        equipment.itemInMainHand.damage(1, caster)

        // DEFAULT WAND
        val manifestRune = ManifestationRune.Beam()
        val wandRunes = mutableListOf<ArcaneRune>(
            ModifierRune.Range(32.0),
            ModifierRune.Amplify(4.0),
            ModifierRune.Convergence(0.5))

        // Check runes
        checkOffhandRunes(caster, wandRunes)
        // TODO: Set to get from gem quality

        /*
        val context = CastingContext(
            caster = caster,
            world = caster.world,
            castingLocation = caster.location,
            target = null,
            targetLocation = null,
            runes = wandRunes
        )
        manifestRune.cast(context)
        caster.setCooldown(equipment.itemInMainHand, 20)

         */
    }

    fun arcaneScepterHandler(caster: LivingEntity) {
        if (caster !is Player) return
        val equipment = caster.equipment ?: return
        val arcaneHand = equipment.itemInMainHand
        if (caster.getCooldown(arcaneHand) > 0) return
        equipment.itemInMainHand.damage(1, caster)

        // DEFAULT SCEPTER
        val manifestRune = ManifestationRune.Zone()
        val scepterRunes = mutableListOf<ArcaneRune>(
            ModifierRune.Range(16.0),
            ModifierRune.Convergence(0.1))

        // Check runes
        checkOffhandRunes(caster, scepterRunes)

        /*
        val context = CastingContext(
            caster = caster,
            world = caster.world,
            castingLocation = caster.location,
            target = null,
            targetLocation = null,
            runes = scepterRunes
        )
        manifestRune.cast(context)
        caster.setCooldown(equipment.itemInMainHand, 30)

         */
    }


    fun arcaneBladeHandler(event: PlayerInteractEvent) {
        val attacker = event.player
        val equipment = attacker.equipment ?: return
        val arcaneHand = equipment.itemInOffHand
        val bookHand = equipment.itemInMainHand
        // Cooldown
        if (attacker.getCooldown(arcaneHand) > 0) return
        arcaneHand.damage(2, attacker)
        attacker.setCooldown(arcaneHand, 20)
        // Vars
        val range = 6.0
        val angle = 70.0
        // Logic
        getEntitiesInArc(attacker, angle, range).forEach {
            val damageSource = createEntityDamageSource(attacker, null, type = DamageType.MAGIC)
            it.damage(4.0, damageSource)
        }
        // Spawn particles in Arc
        spawnArcParticles(
            particle = Particle.WITCH,
            center = attacker.eyeLocation,
            directionVector = attacker.eyeLocation.direction,
            pitchAngle = 0.0,
            radius = range,
            arcLength = Math.toRadians(angle),
            height = 0.1,
            count = 55)
    }


    fun arcaneCircle(user: LivingEntity, damage: Double, range: Double, radius: Double, aimAssist: Double) {
        // Logic
        val circleLocation = getRayTraceLocation(user, range, aimAssist) ?: return
        val damageSource = createEntityDamageSource(user, null, type = DamageType.MAGIC)
        circleLocation.getNearbyLivingEntities(radius).forEach {
            it.damage(damage, damageSource)
        }
        // Particles
        spawnCircleParticles(
            particle = Particle.WITCH,
            center = circleLocation,
            upDirection = Vector(0, 1, 0),
            radius = radius,
            heightOffset = 0.25,
            count = 55)
        user.world.playSound(user.location, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 2F, 2F)
    }

    fun arcaneMissileHandler(event: PlayerInteractEvent) {
        // Basic Variables
        val player = event.player
        val equipment = player.equipment ?: return
        val offHand = equipment.itemInOffHand
        val mainHand = equipment.itemInMainHand
        val model = offHand.itemMeta!!.itemModel?.key ?: return
        if (mainHand.type != Material.BOOK && mainHand.type != Material.ENCHANTED_BOOK) return
        player.setCooldown(offHand.type, 3 * 20)
        if (offHand.itemMeta is Damageable) {
            offHand.damage(1, player)
        }
        // Get Directions
        val eyeDirection = player.eyeLocation.direction.clone().normalize()
        val launchDirection = eyeDirection.multiply(1.3)
        //val arcDirection = eyeDirection.clone().setY(0.0).normalize().setY(0.8).normalize().multiply(1.3) // PRESET OR OVERRIDE MODES
        // Get Target from block ray trace or vector addition
        val targetLocation = getRayTraceBlock(player, model) ?: eyeDirection.clone().multiply(48.0).toLocation(player.world)
        // Spawn Missile
        val missile = (player.world.spawnEntity(player.eyeLocation, EntityType.SNOWBALL) as Snowball).also {
            it.item = mainHand.clone()
            it.addScoreboardTag(EntityTags.MAGIC_MISSILE)
            it.velocity = launchDirection
            it.shooter = player
            it.setHasLeftShooter(false)
            it.setGravity(false)
        }
        val maxTime = 20 * 10
        val delayTime = 20 * 2
        val period = 2
        val guided = true // Means will lock onto player target
        val launcher = MagicMissileLauncher(launchDirection, missile, maxTime, delayTime, guided, period, targetLocation)
        launcher.runTaskTimer(Odyssey.instance, 10, period.toLong())
    }

    fun ovalWandHandler(event: PlayerInteractEvent) {
        val player = event.player
        val offHandEquipment = player.equipment?.itemInOffHand
        var location = player.location.clone()
        val unitVector = player.eyeLocation.direction.clone()
        // MAKE OVAL
        val centroid = event.interactionPoint?.clone() ?: return // CHANGE? if does not work
        val distanceFromPoint = player.location.distance(centroid)
        if (distanceFromPoint > 15.0) return
        // Find intersection
        val enemies = centroid.getNearbyLivingEntities(distanceFromPoint).filter {
            it.location.distance(player.location) <= distanceFromPoint
        }

    }

    fun warpingWandHandler(event: PlayerInteractEvent) {
        val player = event.player
        val offHandEquipment = player.equipment!!.itemInOffHand
        val mainHandEquipment = player.equipment!!.itemInMainHand
        if (mainHandEquipment.type != Material.BOOK && mainHandEquipment.type != Material.ENCHANTED_BOOK) return
        if (offHandEquipment.itemMeta is Damageable) {
            (offHandEquipment.itemMeta as Damageable).damage -= 1
        }
        // Location and math
        val bigR = 8.0 // Big circle radius (distance)
        val center = player.location.clone()
        val unitVector = player.eyeLocation.direction.clone().normalize()
        val midpoint = center.clone().add(unitVector.clone().multiply(bigR / 2))
        // Create a vector from center to entity
        val createTrace = { entity: LivingEntity -> entity.location.clone().subtract(center).toVector().normalize() }
        // Check if is in cone by getting angle and checking distance
        val isInCone = { entity: LivingEntity -> unitVector.angle(createTrace(entity)) < 0.61 && entity.location.distance(center) <= bigR}
        val radiusMid = (bigR / 2) + (0.2 * (bigR / 2))
        val coneEntities = midpoint.getNearbyLivingEntities(radiusMid).filter { isInCone(it) }
        if (coneEntities.isEmpty()) return
        // Run attack
        for (entity in coneEntities) {
            player.attack(entity)
        }
        player.setCooldown(offHandEquipment.type, 5 * 20)
        if (offHandEquipment.itemMeta is Damageable) {
            (offHandEquipment.itemMeta as Damageable).damage -= 1
        }
        // Particles

    }


}
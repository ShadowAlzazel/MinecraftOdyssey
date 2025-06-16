package me.shadowalzazel.mcodyssey.common.arcane

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemLore
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.arcane.runes.*
import me.shadowalzazel.mcodyssey.common.arcane.util.CastingContext
import me.shadowalzazel.mcodyssey.common.arcane.util.RayTracerAndDetector
import me.shadowalzazel.mcodyssey.common.combat.AttackHelper
import me.shadowalzazel.mcodyssey.util.VectorParticles
import me.shadowalzazel.mcodyssey.common.tasks.arcane_tasks.MagicMissileLauncher
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.*
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.util.Transformation
import org.bukkit.util.Vector
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.abs

@Suppress("UnstableApiUsage")
interface ArcaneEquipmentManager : VectorParticles, AttackHelper, DataTagManager, RayTracerAndDetector {


    fun scrollConsumingHandler(event: PlayerItemConsumeEvent) {
        val player = event.player
        val equipment = player.equipment ?: return
        val offhandName = equipment.itemInOffHand.getItemNameId()
        when (offhandName) {
            "arcane_pen" -> arcanePenWithScrollCastingHandler(player)
            "enchanted_book" -> decryptingWithScrollHandler(event)
        }

    }

    fun decryptingWithScrollHandler(event: PlayerItemConsumeEvent) {
        val player = event.player
        val equipment = player.equipment ?: return
        val enchantedBook = equipment.itemInOffHand ?: return
        val scrolls = equipment.itemInMainHand ?: return
        // Check book enchants
        val storedEnchants = enchantedBook.getData(DataComponentTypes.STORED_ENCHANTMENTS) ?: return
        val enchantments = storedEnchants.enchantments()

        if (enchantments.isEmpty()) return
        val randomEnchant = enchantments.entries.random()

        val decryptedRune = EnchantingRuneDecrypter.decryptEnchantSimple(randomEnchant.key)

        val paperRune = ItemStack(Material.PAPER).apply {
            val displayTextLore = mutableListOf<TextComponent>()
            displayTextLore.add(Component.text(decryptedRune.displayName))
            setData(DataComponentTypes.LORE, ItemLore.lore(displayTextLore))
            setStringTag(ItemDataTags.STORED_ARCANE_RUNE, decryptedRune.name)
            val itemName = "${decryptedRune.name}_rune"
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("odyssey", itemName))
            setData(DataComponentTypes.ITEM_NAME, Component.text(itemName))
        }

        equipment.setItemInOffHand(ItemStack(Material.AIR))
        player.inventory.addItem(paperRune)

    }

    fun arcaneStylusDrawingHandler(event: PlayerInteractEvent) {
        val player = event.player

        // When using the player location, it takes the direction as well.
        // So the rune is placed in the players direction
        val pointLocation = event.interactionPoint ?: player.location

        val equipment = player.equipment ?: return
        val arcaneStylus = equipment.itemInMainHand ?: return

        val interactedBlock = event.clickedBlock
        println("BLOCK: $interactedBlock")
        if (interactedBlock == null) return

        // Math
        val blockPerpendicularVector = event.blockFace.direction
        val normalVector = blockPerpendicularVector.clone().normalize()

        val blockCenterLocation = interactedBlock.location.toCenterLocation()

        // This should get us right on the surface
        val normalLocation = blockCenterLocation.clone().add(normalVector.clone().multiply(0.5))
        // Set direction to normal
        normalLocation.direction = normalVector

        // Sneaking while using pen can rotate the item display
        if (!player.isSneaking) {
            // Get nearby
            val nearby = normalLocation.getNearbyEntities(0.01, 0.01, 0.01)
            if (nearby.isEmpty()) return
            // Get item display
            val displayRuneEntity = nearby.first { it is ItemDisplay }
            if (displayRuneEntity !is ItemDisplay) return
            // rotate using transformation
            val spinVector = when {
                // Top/bottom faces
                abs(normalVector.y) > 0.9f -> Vector(0f, 0f, 1f)
                // North/south faces
                abs(normalVector.z) > 0.9f -> Vector(0f, 0f, 1f)
                // East/west faces
                abs(normalVector.x) > 0.9f -> Vector(0f, 0f, 1f)

                else -> Vector(0f, 1f, 0f) // Default to Y-up
            }
            //println("--------------------")
            //println("Normal Vector: $normalVector}")
            //println("Spin Vector: $spinVector}")

            val angleRadians = Math.toRadians(45.0).toFloat()
            val deltaRotation = Quaternionf().fromAxisAngleRad(spinVector.toVector3f(), angleRadians)
            //println("Delta Rotation: $deltaRotation")

            // Get previous
            val previousTransformation = displayRuneEntity.transformation
            val rightRotationQ = previousTransformation.rightRotation
            //println("Previous RQ-Rotation: ${previousTransformation.rightRotation}")

            val newRightRotationQ = deltaRotation.mul(Quaternionf(rightRotationQ))

            val newTransformation = Transformation(
                previousTransformation.translation,
                previousTransformation.leftRotation,
                previousTransformation.scale,
                newRightRotationQ
            )

            //println("New RQ-Rotation: ${newTransformation.rightRotation}")
            displayRuneEntity.transformation = newTransformation
            //println("FINAL Transformation")
            //println(displayRuneEntity.transformation)

            /*
            // DOESNT WORK
            val pointVector = Vector3f(normalVector.x.toFloat(), normalVector.y.toFloat(), normalVector.z.toFloat())
            newTransformation.rightRotation.transform(pointVector)

            println("Pointing Vector: ${pointVector.normalize()}")

            normalLocation.world.spawnParticle(
                Particle.WITCH,
                normalLocation.clone().add(pointVector.x.toDouble(), pointVector.y.toDouble(), pointVector.z.toDouble()),
                15)

             */
            return
        }

        // Get the top item
        val bundleContents = arcaneStylus.getData(DataComponentTypes.BUNDLE_CONTENTS) ?: return
        if (bundleContents.contents().isEmpty()) return
        val topItem = bundleContents.contents().first()

        // Try and get a rune
        val rune = ArcaneRune.getRuneFromItem(topItem) ?: return
        val displayRuneItem = ItemStack(Material.PAPER).also {
            val itemName = "${rune.name}_rune_mark"
            it.setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("odyssey", "rune_mark"))
            // The selector DEPENDS on this
            it.setData(DataComponentTypes.ITEM_NAME, Component.text(itemName))
        }

        // TODO: Quaternion matrix transformation based on block face
        // TODO: The item will have a facing direction
        val itemDisplay = player.world.spawnEntity(normalLocation, EntityType.ITEM_DISPLAY) as ItemDisplay
        itemDisplay.also {
            it.setItemStack(displayRuneItem)
            it.brightness = Display.Brightness(15, 15)
            it.isGlowing = false
            it.viewRange = 32F
            it.isPersistent = false
            it.addScoreboardTag(EntityTags.RUNE_MARK)
        }

    }



    fun arcanePenWithScrollCastingHandler(caster: LivingEntity) {
        if (caster !is Player) return
        // ----------- BUILDING SPELL ----------
        val equipment = caster.equipment ?: return
        val arcanePen = equipment.itemInOffHand ?: return
        val scrolls = equipment.itemInMainHand ?: return
        val bundleContents = arcanePen.getData(DataComponentTypes.BUNDLE_CONTENTS) ?: return

        // Check if we can build spell
        val spellBuilder = ArcaneSpellBuilder(
            arcaneItem = arcanePen,
            additionalItems = bundleContents.contents())
        val canBuildSpell = spellBuilder.canBuildSpell()
        if (!canBuildSpell) return

        // Context params based on initial conditions
        val direction = caster.eyeLocation.direction.clone()

        // Form the starting spell context
        val spellContext = CastingContext(
            caster = caster,
            world = caster.world,
            castingLocation = caster.eyeLocation,
            direction = direction,
            target = null,
            targetLocation = null
        )

        val spell = spellBuilder.buildSpell(spellContext)
        spell.castSpell()
        //caster.world.playSound(caster.location, Sound.ENTITY_VILLAGER_WORK_CARTOGRAPHER, 1F, 0.5F)
    }

    fun spellScrollCastingHandler(caster: LivingEntity, spellScroll: ItemStack) {
        if (caster !is Player) return
        // ----------- BUILDING SPELL ----------

        // Check if we can build spell
        val spellBuilder = ArcaneSpellBuilder(arcaneItem = spellScroll)
        val canBuildSpell = spellBuilder.canBuildSpell()
        if (!canBuildSpell) return

        // Context params based on initial conditions
        val direction = caster.eyeLocation.direction.clone()

        // Form the starting spell context
        val spellContext = CastingContext(
            caster = caster,
            world = caster.world,
            castingLocation = caster.eyeLocation,
            direction = direction,
            target = null,
            targetLocation = null
        )

        val spell = spellBuilder.buildSpell(spellContext)
        spell.castSpell()
    }


    fun arcaneWandHandler(caster: LivingEntity) {
        if (caster !is Player) return
        val equipment = caster.equipment ?: return
        val arcaneTool = equipment.itemInMainHand
        val offHandItem = equipment.itemInOffHand
        if (caster.getCooldown(arcaneTool) > 0) return
        equipment.itemInMainHand.damage(1, caster)
        // ----------- BUILDING DEFAULT SPELL ----------

        // Create a default sequence
        val wandRunes = listOf<ArcaneRune>(
            ModifierRune.Range(16.0),
            ModifierRune.Amplify(4.0), // Default(2.0) + 4.0
            ModifierRune.Convergence(0.35),
            CastingRune.Beam(),
        )
        // Build the spell
        val spellBuilder = ArcaneSpellBuilder(
            arcaneItem = arcaneTool,
            additionalItems = listOf(offHandItem),
            providedRunes = wandRunes,
            providedSource = ArcaneSource.Magic)
        val canBuildSpell = spellBuilder.canBuildSpell()
        if (!canBuildSpell) return

        // Context params based on initial conditions
        val direction = caster.eyeLocation.direction.clone()
        //val target = getRayTraceEntity(caster, 16.0, 0.15)
        val target = getEntityRayTrace(
            caster.eyeLocation,
            caster.eyeLocation.direction,
            listOf(caster),
            16.0,
            0.15
        )
        val targetLocation: Location = if (target != null) {
            if (target is LivingEntity) target.eyeLocation else target.location
        } else {
            caster.eyeLocation.clone().add(direction.clone().normalize().multiply(16.0))
        }

        // Form the starting spell context
        val spellContext = CastingContext(
            caster = caster,
            world = caster.world,
            castingLocation = caster.eyeLocation,
            direction = direction,
            target = target,
            targetLocation = targetLocation
        )

        val spell = spellBuilder.buildSpell(spellContext)
        spell.castSpell()
        caster.setCooldown(equipment.itemInMainHand, 20)
    }

    fun arcaneScepterHandler(caster: LivingEntity) {
        if (caster !is Player) return
        val equipment = caster.equipment ?: return
        val arcaneTool = equipment.itemInMainHand
        val offHandItem = equipment.itemInOffHand
        if (caster.getCooldown(arcaneTool) > 0) return
        equipment.itemInMainHand.damage(1, caster)
        // ----------- BUILDING DEFAULT SPELL ----------

        // Create a default sequence
        val scepterRunes = listOf<ArcaneRune>(
            ModifierRune.Range(16.0),
            ModifierRune.Amplify(3.0), // Default(1.0) + 3.0
            ModifierRune.Convergence(0.1),
            DomainRune.Trace,
            CastingRune.Zone()
        )
        // Build the spell
        val spellBuilder = ArcaneSpellBuilder(
            arcaneItem = arcaneTool,
            additionalItems = listOf(offHandItem),
            providedRunes = scepterRunes,
            providedSource = ArcaneSource.Magic)
        val canBuildSpell = spellBuilder.canBuildSpell()
        if (!canBuildSpell) return

        // Form the starting spell context
        val spellContext = CastingContext(
            caster = caster,
            world = caster.world,
            castingLocation = caster.eyeLocation,
            direction = caster.eyeLocation.direction,
            target = null,
            targetLocation = null
        )

        val spell = spellBuilder.buildSpell(spellContext)
        spell.castSpell()
        caster.setCooldown(equipment.itemInMainHand, 30)
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
        //val circleLocation = getRayTraceLocation(user, range, aimAssist) ?: return
        val circleLocation = getHitLocationRayTrace(
            user.location,
            user.eyeLocation.direction,
            listOf(user),
            range,
            aimAssist
        ) ?: return


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
        val targetLocation = oldBlockRayTrace(player, model) ?: eyeDirection.clone().multiply(48.0).toLocation(player.world)
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

    private fun checkOffhandRunes(caster: LivingEntity, runes: MutableList<ArcaneRune>) {
        if (caster !is Player) return
        val equipment = caster.equipment ?: return

        // read
        // JUST TO SHOWCASE
        if (equipment.itemInOffHand.type == Material.BUNDLE) { // Temp bundle reader
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
                    "alexandrite" -> runes.add(CastingRune.Beam()) // overwrite or ADD!!!
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
                "alexandrite" -> runes.add(CastingRune.Beam()) // overwrite or ADD!!!
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

}
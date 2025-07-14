package me.shadowalzazel.mcodyssey.common.arcane.runes

import me.shadowalzazel.mcodyssey.common.arcane.RuneDataManager
import me.shadowalzazel.mcodyssey.common.arcane.util.ArcaneCaster
import me.shadowalzazel.mcodyssey.common.arcane.util.ArcaneTarget
import me.shadowalzazel.mcodyssey.common.combat.AttackHelper
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.damage.DamageType
import org.bukkit.entity.EntityType
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

@Suppress("UnstableApiUsage")
sealed class ArcaneSource(
    val name: String,
    val damageType: DamageType,
    val particle: Particle,
) : AttackHelper {

    companion object : RuneDataManager {
        /**
         * This gets an Arcane source from an item
         */
        fun getSourceFromRawItem(item: ItemStack): ArcaneSource? {
            return when (item.getItemNameId()) {
                // GEM-SOURCES
                "ruby" -> Fire
                "neptunian" -> Frost
                "amethyst_shard" -> Magic
                "jovianite" -> Radiant
                "jade" -> Aero
                "ender_eye" -> Void
                "soul_quartz" -> Soul
                // TOOL-SOURCES
                "arcane_blade" -> Magic
                "arcane_book" -> Magic
                "arcane_wand" -> Magic
                "warping_wand" -> Soul
                "arcane_scepter" -> Magic
                // Special
                "arcane_pen" -> Radiant
                // Scrolls
                "spell_scroll" -> Radiant
                else -> null
            }

        }

    }

    // The source of attribute and appearance of the magic being used
    data object Fire: ArcaneSource("fire", DamageType.ON_FIRE, Particle.FLAME)
    data object Frost: ArcaneSource("frost", DamageType.FREEZE, Particle.SNOWFLAKE)
    data object Magic: ArcaneSource("magic", DamageType.MAGIC, Particle.WITCH)
    data object Void: ArcaneSource("void", DamageType.OUT_OF_WORLD, Particle.ENCHANT)
    data object Radiant: ArcaneSource("radiant", DamageType.MAGIC, Particle.WAX_OFF)
    data object Soul: ArcaneSource("soul", DamageType.MAGIC, Particle.SCULK_SOUL)
    data object Aero: ArcaneSource("aero", DamageType.MAGIC, Particle.SMALL_GUST)

    /**
     * MAIN method to interact with SOURCE.
     * This does the damage effect and other special effects of the source.
     */
    fun invoke(
        target: ArcaneTarget,
        caster: ArcaneCaster,
        direction: Vector,
        bonus: Double = 0.0
    ) {
        // Get based on source
        val value = when(this@ArcaneSource) {
            Fire -> 2.0
            Aero -> 2.0
            Frost -> 2.0
            Magic -> 4.0
            Radiant -> 0.0 // TEMP
            Soul -> 3.0
            Void -> 3.0
        }

        val world = if (caster.isEntity) {
            caster.entityCaster!!.world
        } else {
            caster.blockCaster!!.world
        }

        // Damage Logic for Entity Caster -> Entity Target
        if (target.isEntity && target.entityTarget is LivingEntity) {
            // Damage Logic FOR Entity Damage source
            if (caster.isEntity) {
                val damageSource = createEntityDamageSource(caster.entityCaster!!, null, damageType)
                val damageValue = value + bonus
                target.entityTarget.damage(damageValue, damageSource)
            }
            // Bonus Effects
            when(this@ArcaneSource) {
                Fire -> {
                    target.entityTarget.fireTicks += 40
                }
                Frost -> {
                    target.entityTarget.freezeTicks += 40
                }
                Aero -> {
                    val magnitude = 2.0
                    val push = target.entityTarget.velocity.add(direction.normalize()).multiply(magnitude)
                    target.entityTarget.velocity = push
                }
                else -> {}
            }
        }
        // Logic for when target is a block
        if (target.isBlock && target.blockTarget != null) {
            when(this@ArcaneSource) {
                Aero -> {
                    val magnitude = 1.0
                    //val blockEntity = world.spawnEntity(target.getLocation(), EntityType.FALLING_BLOCK) as FallingBlock
                    //blockEntity.blockData = target.blockTarget.blockData.clone()
                    //blockEntity.blockState = target.blockTarget.state
                    //val push = blockEntity.velocity.add(direction.normalize()).multiply(magnitude)
                    // CHECK if movable here
                    //blockEntity.velocity = push
                }
                else -> {}
            }
        }
    }



}
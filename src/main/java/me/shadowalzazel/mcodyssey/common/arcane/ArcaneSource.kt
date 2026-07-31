package me.shadowalzazel.mcodyssey.common.arcane

import me.shadowalzazel.mcodyssey.common.combat.AttackHelper
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.damage.DamageType
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

// =====================================================================================
//  ARCANE CASTER  --  Holds who/what is casting (entity or block)
// =====================================================================================
/**
 * This caster can be an ENTITY or a BLOCK
 */
class ArcaneCaster(
    val entityCaster: org.bukkit.entity.Entity? = null,
    val blockCaster: Block? = null
) {

    val isBlock: Boolean
    val isEntity: Boolean

    init {
        isEntity = entityCaster != null
        isBlock = blockCaster != null
    }

    fun convertToTarget(): ArcaneTarget {
        return if (isBlock) {
            ArcaneTarget(blockTarget = blockCaster)
        } else {
            ArcaneTarget(entityTarget = entityCaster!!)
        }
    }

    fun getLocation(): org.bukkit.Location {
        return if (isEntity) {
            entityCaster!!.location
        } else { // isBlock
            blockCaster!!.location.toCenterLocation()
        }
    }

    fun toEntityList(): List<LivingEntity> {
        val entityList = mutableListOf<LivingEntity>()
        if (entityCaster is LivingEntity) entityList.add(entityCaster)
        return entityList
    }
}

// =====================================================================================
//  ARCANE TARGET  --  Holds who/what is being targeted (entity or block)
// =====================================================================================
/**
 * This target can be an ENTITY or a BLOCK
 */
class ArcaneTarget(
    val entityTarget: org.bukkit.entity.Entity? = null,
    val blockTarget: Block? = null
) {

    val isBlock: Boolean
    val isEntity: Boolean

    init {
        isEntity = entityTarget != null
        isBlock = blockTarget != null
    }

    fun getLocation(): org.bukkit.Location {
        return if (isEntity) {
            entityTarget!!.location
        } else { // isBlock
            blockTarget!!.location
        }
    }

    fun toEntityList(): List<LivingEntity> {
        val entityList = mutableListOf<LivingEntity>()
        if (entityTarget is LivingEntity) entityList.add(entityTarget)
        return entityList
    }
}

// =====================================================================================
//  ARCANE SOURCE  --  The element of the spell; does not change once casting.
//
//  Each element defines its own behaviour by overriding two hooks:
//    - affectEntity(...) : what it does to a living target (default: scaled damage)
//    - affectBlock(...)  : what it does to a block         (default: nothing)
//  Adding a new element is just a new `data object` with those overrides — no central
//  `when` to edit, nothing else to touch.
// =====================================================================================
sealed class ArcaneSource(
    val name: String,
    val damageType: DamageType,
    val particle: Particle,
) : AttackHelper {

    companion object : RuneDataManager {
        /** This gets an Arcane source from an item */
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
                "arcane_pen" -> Magic
                // Scrolls
                "spell_scroll" -> Magic
                else -> null
            }
        }

        // Extend this set to teach new elements what counts as undead.
        private val UNDEAD = setOf(
            EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.HUSK, EntityType.DROWNED,
            EntityType.ZOMBIFIED_PIGLIN, EntityType.SKELETON, EntityType.STRAY, EntityType.BOGGED,
            EntityType.WITHER_SKELETON, EntityType.PHANTOM, EntityType.WITHER,
            EntityType.SKELETON_HORSE, EntityType.ZOMBIE_HORSE
        )

        fun isUndead(entity: LivingEntity): Boolean = entity.type in UNDEAD
    }

    /** Base damage this element applies before the spell's bonus is added. Each element scales differently. */
    abstract val damageMultiplier: Double

    /**
     * MAIN entry. Dispatches to the element's entity/block behaviour based on the target.
     */
    fun invoke(target: ArcaneTarget, caster: ArcaneCaster, direction: Vector, bonus: Double = 0.0) {
        val entity = target.entityTarget
        if (target.isEntity && entity is LivingEntity) {
            affectEntity(entity, caster, direction, bonus)
        }
        val block = target.blockTarget
        if (target.isBlock && block != null) {
            affectBlock(block, caster, direction, bonus)
        }
    }

    /** What this element does to a living entity. Default: deal scaled damage. Override to customise. */
    protected open fun affectEntity(target: LivingEntity, caster: ArcaneCaster, direction: Vector, bonus: Double) {
        dealDamage(target, caster, damageMultiplier + bonus)
    }

    /** What this element does to a block. Default: nothing. Override to customise. */
    protected open fun affectBlock(block: Block, caster: ArcaneCaster, direction: Vector, bonus: Double) {}

    /** Standard armour-respecting damage, attributed to the caster when it is an entity. */
    protected fun dealDamage(target: LivingEntity, caster: ArcaneCaster, amount: Double) {
        if (amount <= 0.0) return
        if (caster.isEntity) {
            target.damage(amount, createEntityDamageSource(caster.entityCaster!!, null, damageType))
        } else {
            target.damage(amount)
        }
    }

    // ---------------------------------------------------------------------------------
    //  ATRIBUTES
    // ---------------------------------------------------------------------------------

    // FIRE — burns entities, and ignites the space above a struck block.
    data object Fire : ArcaneSource("fire", DamageType.ON_FIRE, Particle.FLAME) {
        override val damageMultiplier = 2.0

        override fun affectEntity(target: LivingEntity, caster: ArcaneCaster, direction: Vector, bonus: Double) {
            dealDamage(target, caster, damageMultiplier + bonus)
            target.fireTicks += 40
        }

        override fun affectBlock(block: Block, caster: ArcaneCaster, direction: Vector, bonus: Double) {
            val above = block.getRelative(BlockFace.UP)
            if (above.type.isAir) above.type = Material.FIRE
        }
    }

    // FROST — chills entities, and freezes water solid (glazes lava too).
    data object Frost : ArcaneSource("frost", DamageType.FREEZE, Particle.SNOWFLAKE) {
        override val damageMultiplier = 2.0

        override fun affectEntity(target: LivingEntity, caster: ArcaneCaster, direction: Vector, bonus: Double) {
            dealDamage(target, caster, damageMultiplier + bonus)
            target.freezeTicks += 60
        }

        override fun affectBlock(block: Block, caster: ArcaneCaster, direction: Vector, bonus: Double) {
            when (block.type) {
                Material.WATER -> block.type = Material.ICE
                Material.LAVA -> block.type = Material.OBSIDIAN
                else -> {}
            }
        }
    }

    // MAGIC — the reliable baseline: pure, high scaling, no side effects.
    data object Magic : ArcaneSource("magic", DamageType.MAGIC, Particle.WITCH) {
        override val damageMultiplier = 3.0
    }

    // RADIANT — smites the undead for double, but MENDS the living it touches.
    data object Radiant : ArcaneSource("radiant", DamageType.MAGIC, Particle.WAX_OFF) {
        override val damageMultiplier = 2.0

        override fun affectEntity(target: LivingEntity, caster: ArcaneCaster, direction: Vector, bonus: Double) {
            if (isUndead(target)) {
                dealDamage(target, caster, (damageMultiplier + bonus) * 2.0)
            } else {
                target.heal(damageMultiplier + bonus)
                target.world.spawnParticle(Particle.HEART, target.eyeLocation, 3, 0.3, 0.3, 0.3)
            }
        }
    }

    // SOUL — true damage: bypasses armour and resistance by reducing health directly.
    data object Soul : ArcaneSource("soul", DamageType.MAGIC, Particle.SCULK_SOUL) {
        override val damageMultiplier = 1.0

        override fun affectEntity(target: LivingEntity, caster: ArcaneCaster, direction: Vector, bonus: Double) {
            val amount = damageMultiplier + bonus
            target.health = (target.health - amount).coerceAtLeast(0.0)
            target.playHurtAnimation(0f)
        }
    }

    // AERO — knocks entities back, and blasts loose blocks into flying debris.
    data object Aero : ArcaneSource("aero", DamageType.MAGIC, Particle.SMALL_GUST) {
        override val damageMultiplier = 2.0

        override fun affectEntity(target: LivingEntity, caster: ArcaneCaster, direction: Vector, bonus: Double) {
            dealDamage(target, caster, damageMultiplier + bonus)
            val push = direction.clone().normalize().multiply(2.0).setY(0.4)
            target.velocity = target.velocity.add(push)
        }

        override fun affectBlock(block: Block, caster: ArcaneCaster, direction: Vector, bonus: Double) {
            /*
            if (block.type.isAir || block.isLiquid) return
            val debris = block.world.spawnFallingBlock(block.location.toCenterLocation(), block.blockData.clone())
            block.type = Material.AIR
            debris.setHurtEntities(true)
            debris.velocity = direction.clone().normalize().multiply(0.9).setY(0.35)

             */
        }
    }

    // VOID — displaces entities with a disorienting blink, and unmakes blocks (no drops).
    //        (Proposal — the "blink" is the most fun hook to iterate on.)
    data object Void : ArcaneSource("void", DamageType.OUT_OF_WORLD, Particle.ENCHANT) {
        override val damageMultiplier = 3.0

        override fun affectEntity(target: LivingEntity, caster: ArcaneCaster, direction: Vector, bonus: Double) {
            dealDamage(target, caster, damageMultiplier + bonus)
            blink(target)
        }

        override fun affectBlock(block: Block, caster: ArcaneCaster, direction: Vector, bonus: Double) {
            block.world.spawnParticle(Particle.PORTAL, block.location.toCenterLocation(), 20, 0.3, 0.3, 0.3)
            //block.type = Material.AIR
        }

        private fun blink(target: LivingEntity) {
            val from = target.location
            val dest = from.clone().add((Math.random() - 0.5) * 6.0, 0.0, (Math.random() - 0.5) * 6.0)
            target.world.spawnParticle(Particle.PORTAL, from, 30, 0.2, 0.5, 0.2)
            target.teleport(dest)
            target.world.spawnParticle(Particle.PORTAL, dest, 30, 0.2, 0.5, 0.2)
        }
    }
}
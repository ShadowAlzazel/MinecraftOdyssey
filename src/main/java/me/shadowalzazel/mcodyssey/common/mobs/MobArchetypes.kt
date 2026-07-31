package me.shadowalzazel.mcodyssey.common.mobs

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.boss.BossManager
import me.shadowalzazel.mcodyssey.common.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.*
import java.util.Random


// ──────────────────────────────────────────────────────────────────────────────
// ─────────────────────────── ARCHETYPE CATALOGUE ──────────────────────────────
// ──────────────────────────────────────────────────────────────────────────────

/**
 * Every reusable mob type lives here. Want a Vanguard in a new dungeon, a boss arena, or an
 * admin command? `MobArchetypes.VANGUARD.applyTo(mob, factory)`. Nothing about the shadow
 * chambers is baked in.
 */
object MobArchetypes {

    // ──────────────────────────────────────────────────────────────────────────────
    // Bosses

    val BOSS_HOG_RIDER = MobArchetype(
        id = "hog_rider",
        predicate = { mob -> mob.scoreboardTags.contains("odyssey.hog_rider") && mob is PiglinBrute},
        decorate = { mob ->
            BossManager.summon(Odyssey.instance, "hog_rider", mob.location)
        }
    )

    // ──────────────────────────────────────────────────────────────────────────────
    // Regular Mobs

    val TRIAL_ELITE_SHINY = MobArchetype(
        id = "trial_elite_shiny",
        predicate = hasTag(EntityTags.TRIAL_ELITE),
        stats = statProfile {
            attack(4.0, AttributeTags.MOB_ATTACK_DAMAGE)
            step(1.5, AttributeTags.MOB_STEP_HEIGHT)
        },
        decorate = { mob -> createShinyMob(mob, EquipmentPresets.shadowElite(), true) },
    )

    /** The "menacing" oversized mob. Same numbers in shadow chambers and sunken library. */
    val GIANT = MobArchetype(
        id = "giant",
        predicate = hasTag(EntityTags.GIANT),
        stats = statProfile {
            health(62.0, AttributeTags.MOB_HEALTH)
            reach(1.5, AttributeTags.MOB_REACH)
            attack(14.0, AttributeTags.MOB_ATTACK_DAMAGE)
            scale(2.0, AttributeTags.MOB_SCALE)
        },
    )

    /** Sunken library only wants giant *spiders*. */
    val GIANT_SPIDER = GIANT.onlyIf(isA<Spider>()).copy(id = "giant_spider")

    val VANGUARD = MobArchetype(
        id = "vanguard",
        predicate = hasTag(EntityTags.VANGUARD),
        stats = statProfile {
            health(30.0, AttributeTags.MOB_HEALTH)
            attack(3.0, AttributeTags.MOB_ATTACK_DAMAGE)
            scale(0.1, AttributeTags.MOB_SCALE)
            step(1.5, AttributeTags.MOB_STEP_HEIGHT)
        },
        decorate = { mob ->
            val weapon = buildVanguardWeapon()
            mob.equipment?.also {
                it.setItemInOffHand(ItemStack(Material.SHIELD))
                it.setItemInMainHand(weapon)
                it.itemInMainHandDropChance = 0.03F // TODO: scale with difficulty
            }
            createArmoredMob(mob, EquipmentPresets.vanguard(), enchantWeapon = true, replaceOldWeapon = false)
        },
    )

    val SAVAGE = MobArchetype(
        id = "savage",
        predicate = hasTag(EntityTags.SAVAGE),
        stats = statProfile {
            health(50.0, AttributeTags.MOB_HEALTH)
        },
        decorate = { mob ->
            mob.canPickupItems = true
            mob.clearActiveItem()
            val weapon = buildSavageWeapon()
            mob.equipment?.also {
                it.setItemInMainHand(weapon)
                it.setItemInOffHand(weapon.clone())
            }
        },
    )

    val RUINED = MobArchetype(
        id = "ruined",
        predicate = hasTag(EntityTags.RUINED),
        stats = statProfile {
            health(50.0, AttributeTags.MOB_HEALTH)
        },
        decorate = { mob ->
            mob.canPickupItems = true
            mob.clearActiveItem()
            val weapon = buildRuinedWeapon()
            mob.equipment?.also {
                it.setItemInMainHand(weapon)
            }
        },
    )

    val PREACHER = MobArchetype(
        id = "preacher",
        predicate = hasTag(EntityTags.PREACHER),
        stats = statProfile {
            health(130.0, AttributeTags.MOB_HEALTH)
        },
        decorate = { mob ->
            mob.canPickupItems = true
            mob.clearActiveItem()
            val weapon = buildPreacherWeapon()
            mob.equipment?.also {
                it.setItemInMainHand(weapon)
            }
        },
    )

    val SHADOW_BASIC = MobArchetype(
        id = "shadow_basic",
        predicate = UNTAGGED,
        stats = statProfile {
            attack(4.0, AttributeTags.MOB_ATTACK_DAMAGE)
            health(10.0, AttributeTags.MOB_HEALTH)
            armor(2.0, AttributeTags.MOB_ARMOR)
            step(0.5, AttributeTags.MOB_STEP_HEIGHT)
        },
        decorate = { mob ->
            createArmoredMob(mob, EquipmentPresets.shadowBasic(), enchantWeapon = true, replaceOldWeapon = true)
        },
    )

    val TERMINAL_GRID_BASIC = MobArchetype(
        id = "terminal_grid_basic",
        predicate = { true }, // used as a fallback, so it matches whatever is left
        stats = statProfile {
            attack(1.0, AttributeTags.MOB_ATTACK_DAMAGE)
            health(10.0, AttributeTags.MOB_HEALTH)
        },
        decorate = { mob ->
            createArmoredMob(mob, EquipmentPresets.terminalGrid(), enchantWeapon = true, replaceOldWeapon = true)
        },
    )

    /**
     * NOTE: the original checked `mob is Skeleton`, which does *not* match WitherSkeleton,
     * Stray or Bogged (they're siblings under AbstractSkeleton). The comment said
     * "Guard Skeletons and Wither Skeletons", so the code and comment disagreed.
     * Preserving the original behaviour — swap to `isA<AbstractSkeleton>()` if the comment was right.
     */
    val HYPERCUBIC_GUARD = MobArchetype(
        id = "hypercubic_guard",
        predicate = isA<Skeleton>(),
        stats = statProfile {
            attack(1.0, AttributeTags.MOB_ATTACK_DAMAGE)
            health(10.0, AttributeTags.MOB_HEALTH)
        },
        decorate = { mob ->
            createArmoredMob(mob, EquipmentPresets.hypercubicGuard(), enchantWeapon = true, replaceOldWeapon = true)
        },
    )

    val SUNKEN_GUARD = MobArchetype(
        id = "sunken_guard",
        predicate = { it is Skeleton || it is WitherSkeleton },
        stats = statProfile {
            attack(3.0, AttributeTags.MOB_ATTACK_DAMAGE)
            health(20.0, AttributeTags.MOB_HEALTH)
            armor(2.0, AttributeTags.MOB_ARMOR)
        },
        decorate = { mob ->
            createArmoredMob(mob, EquipmentPresets.sunkenGuard(), enchantWeapon = true, replaceOldWeapon = true)
        },
    )

    val GILDED_MARAUDER = MobArchetype(
        id = "gilded_marauder",
        predicate = { !it.scoreboardTags.contains("odyssey.hog_rider") && it is PiglinBrute },
        stats = statProfile {
            health(20.0, AttributeTags.MOB_HEALTH)
            armor(2.0, AttributeTags.MOB_ARMOR)
        },
        decorate = { mob ->
            createArmoredMob(mob, EquipmentPresets.gildedMarauder(), enchantWeapon = true, replaceOldWeapon = true)
            //mob.type
        },
    )

    /** Creaking stats differ per structure, so this is a factory rather than a constant. */
    fun creaking(health: Double, attack: Double, speed: Double? = null) = MobArchetype(
        id = "creaking",
        predicate = isA<Creaking>(),
        stats = statProfile {
            health(health, AttributeTags.MOB_HEALTH)
            attack(attack, AttributeTags.MOB_ATTACK_DAMAGE)
            speed?.let { speed(it, AttributeTags.MOB_MOVEMENT_SPEED) }
        },
    )



    // ──────────────────────────────────────────────────────────────────────────────
    // Weapon builder helpers

    internal fun MobFactory.buildVanguardWeapon(): ItemStack {
        val type = listOf(ToolType.POLEAXE, ToolType.HALBERD).random()
        val weapon = createToolStack(ToolMaterial.IRON, type)

        // Borrow a random enchant roll from a golden sword, then keep only what fits the polearm.
        val rolled = ItemStack(Material.GOLDEN_SWORD).enchantWithLevels(30, false, Random()).enchantments

        return weapon.apply {
            addUnsafeEnchantment(Enchantment.BREACH, 4)
            addUnsafeEnchantment(Enchantment.KNOCKBACK, 3)
            rolled.filterKeys { it.canEnchantItem(this) }
                .forEach { (enchant, level) -> addEnchantment(enchant, level) }
            updateToolTip()
        }
    }

    internal fun MobFactory.buildSavageWeapon(): ItemStack {
        return createToolStack(ToolMaterial.COPPER, ToolType.DAGGER).apply {
            addUnsafeEnchantment(OdysseyEnchantments.HEMORRHAGE, 4)
            itemMeta.displayName(Component.text("Brutal Dagger"))
        }
    }

    internal fun MobFactory.buildSavageKnightWeapon(): ItemStack {
        return createToolStack(ToolMaterial.COPPER, ToolType.LONGAXE).apply {
            addUnsafeEnchantment(OdysseyEnchantments.HEMORRHAGE, 4)
            itemMeta.displayName(Component.text("Brutal Longaxe"))
        }
    }

    internal fun MobFactory.buildRuinedWeapon(): ItemStack {
        return createToolStack(ToolMaterial.DIAMOND, ToolType.CLAYMORE).apply {
            addUnsafeEnchantment(OdysseyEnchantments.INVOCATIVE, 3)
            itemMeta.displayName(Component.text("Maligned Claymore"))
        }
    }

    // TODO: slots + enchants still unresolved from the original — left as a bare weapon until that's decided.
    internal fun MobFactory.buildPreacherWeapon(): ItemStack {
        return createToolStack(ToolMaterial.NETHERITE, ToolType.CLAYMORE)
    }

}
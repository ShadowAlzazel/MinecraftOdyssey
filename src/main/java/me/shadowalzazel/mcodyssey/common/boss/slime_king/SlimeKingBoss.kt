package me.shadowalzazel.mcodyssey.common.boss.slime_king

import me.shadowalzazel.mcodyssey.common.boss.AttackOption
import me.shadowalzazel.mcodyssey.common.boss.BossStats
import me.shadowalzazel.mcodyssey.common.boss.OdysseyBoss
import me.shadowalzazel.mcodyssey.common.boss.Dialogue
import me.shadowalzazel.mcodyssey.common.boss.DialogueKey
import me.shadowalzazel.mcodyssey.common.boss.LeapAttack
import me.shadowalzazel.mcodyssey.common.boss.TargetMode
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Slime
import org.bukkit.plugin.java.JavaPlugin

/**
 * The Slime King: a simple bruiser boss.
 *
 * Gimmick — it starts enormous and *shrinks as it's hurt*. Scale is driven by
 * the [Attribute.GENERIC_SCALE] attribute (not the slime's `size`), so the
 * visual/hitbox scale is fully decoupled from health: no accidental health
 * resets, and a `size = 1` body means it never splits into smaller slimes.
 */
class SlimeKing(
    plugin: JavaPlugin,
    private val spawnLocation: Location,
) : OdysseyBoss(plugin, KEY, NAME) {

    // Scale gimmick: huge at full health, down to a common slime near death.
    private val maxScale = 16.0
    private val minScale = 2.0

    override val stats = BossStats(
        maxHealth = 600.0,
        armor = 4.0,
        activationRadius = 32.0,
        attackRadius = 24.0,
        attackPeriodTicks = 20L * 4,
    )

    override val dialogue: Dialogue = SlimeKingDialogue.pack

    // Continuous (solid) health bar.
    override val bossBarTitle: Component = NAME
    override val bossBarColor: BossBar.Color = BossBar.Color.GREEN
    override val bossBarOverlay: BossBar.Overlay = BossBar.Overlay.PROGRESS

    // ---------------------------------------------------------------- spawning

    override fun createEntity(location: Location): LivingEntity {
        val slime = (location.world.spawnEntity(location, EntityType.SLIME) as Slime).apply {
            size = 1 // smallest body -> never splits; we scale it up via the attribute
            customName(NAME)
            isCustomNameVisible = true
            removeWhenFarAway = false
            isAware = true
        }
        entity = slime
        applyBaseAttributes() // set boss health AFTER size, since size would reset it
        slime.getAttribute(Attribute.SCALE)?.baseValue = maxScale
        slime.getAttribute(Attribute.JUMP_STRENGTH)?.baseValue = 1.3
        return slime
    }

    override fun onSpawn(location: Location) {
        playSoundNearby(Sound.ENTITY_SLIME_JUMP, 2.5f, 0.5f)
        announce(SlimeKingLine.ARRIVAL)
        activate() // simple boss: fights right away
    }

    // --------------------------------------------------------------- attacks

    override fun attackOptions(): List<AttackOption> = listOf(
        // Bounce high onto a player and slam a slimy shockwave on landing.
        AttackOption(
            weight = 3,
            attack = LeapAttack(
                leapPower = 1.7,
                horizontalPull = 1.3,
                onLand = SlimeShockwaveAttack(
                    maxRadius = 18.0,
                    damage = 24.0,
                    knockback = 1.5,
                    debris = Material.SLIME_BLOCK,
                ),
            ),
            target = TargetMode.RANDOM_PLAYER,
            line = SlimeKingLine.SLAM,
        ),
        // Simple in-place ground pound.
        AttackOption(
            weight = 2,
            attack = SlimeShockwaveAttack(
                maxRadius = 14.0,
                damage = 14.0,
                knockback = 1.0,
                debris = Material.SLIME_BLOCK,
            ),
            target = TargetMode.SELF,
            line = SlimeKingLine.SLAM,
        ),
    )

    // ------------------------------------------------------- shrinking gimmick

    override fun onDamaged(source: Entity?, amount: Double) {
        // Health isn't reduced until after this event resolves, so read it next tick.
        runLater(1L) { updateScale() }
    }

    private fun updateScale() {
        if (!isAlive) return
        val maxHp = entity.getAttribute(Attribute.MAX_HEALTH)?.value ?: stats.maxHealth
        val fraction = (entity.health / maxHp).coerceIn(0.0, 1.0)
        val scale = minScale + (maxScale - minScale) * fraction
        entity.getAttribute(Attribute.SCALE)?.baseValue = scale
    }

    // ------------------------------------------------------------- end of fight

    override fun onDefeat(killer: Player?) {
        killer?.giveExpLevels(25)
        announce(SlimeKingLine.DEFEATED, nearbyPlayers())
        nearbyPlayers().forEach {
            it.playSound(it, Sound.ENTITY_SLIME_DEATH, 1.5f, 0.6f)
            it.giveExp(1500)
        }
        // TODO: drop your unique Slime King loot here.
    }

    companion object {
        const val KEY = "slime_king"
        val NAME: Component = Component.text("The Slime King", TextColor.color(120, 220, 90))
    }
}

/* ============================================================================
 *  Slime King speech — minimal. Delete lines or the pack entirely if you'd
 *  rather he stay silent.
 * ========================================================================== */

enum class SlimeKingLine : DialogueKey {
    ARRIVAL,
    SLAM,
    DEFEATED;

    override val id: String get() = name
}

object SlimeKingDialogue {
    private val GREEN = TextColor.color(120, 220, 90)
    private val prefix = Component.text("[The Slime King] ", GREEN)

    val pack: Dialogue = Dialogue.of(prefix, TextColor.color(255, 255, 255)) {
        line(SlimeKingLine.ARRIVAL,"The King is here")
        line(SlimeKingLine.SLAM, "SQUELCH", "BOING")
        line(SlimeKingLine.DEFEATED, "*oozing out*")
    }
}
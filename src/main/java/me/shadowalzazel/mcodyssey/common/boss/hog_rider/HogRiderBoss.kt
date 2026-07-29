package me.shadowalzazel.mcodyssey.common.boss.hog_rider

import me.shadowalzazel.mcodyssey.common.boss.AttackOption
import me.shadowalzazel.mcodyssey.common.boss.BossStats
import me.shadowalzazel.mcodyssey.common.boss.Dialogue
import me.shadowalzazel.mcodyssey.common.boss.DialogueKey
import me.shadowalzazel.mcodyssey.common.boss.OdysseyBoss
import me.shadowalzazel.mcodyssey.common.boss.TargetMode
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Hoglin
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.PiglinBrute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * The Hog Rider: a mace-swinging Piglin Brute atop a baby Hoglin mount.
 *
 * A two-entity boss: the Brute is the "boss" (its health drives the bar and
 * its death ends the fight); the Hoglin is a tracked mount that gets cleaned
 * up with it. Both are launched together by the leap because the Brute rides
 * the Hoglin, and [LeapAttack] moves the vehicle.
 */
class HogRider(
    plugin: JavaPlugin,
    private val spawnLocation: Location,
) : OdysseyBoss(plugin, KEY, NAME) {

    /** The baby Hoglin the rider sits on. Tracked so we can clean it up. */
    var mount: Hoglin? = null
        private set

    override val stats = BossStats(
        maxHealth = 600.0,
        armor = 10.0,
        activationRadius = 40.0,
        attackRadius = 24.0,
        attackPeriodTicks = 20L * 6, // a special move every ~6s
    )

    override val dialogue: Dialogue = HogRiderDialogue.pack

    // --- boss bar (fixed: now actually shown + kept in sync) ---
    override val bossBarTitle: Component = NAME
    override val bossBarColor: BossBar.Color = BossBar.Color.YELLOW
    override val bossBarOverlay: BossBar.Overlay = BossBar.Overlay.NOTCHED_6
    override val bossBarRadius: Double = 48.0

    // ---------------------------------------------------------------- spawning

    override fun createEntity(location: Location): LivingEntity {
        val world = location.world

        val hog = (world.spawnEntity(location, EntityType.HOGLIN) as Hoglin).apply {
            setIsAbleToBeHunted(false)
            isImmuneToZombification = true
            ageLock = true
            setBaby()
            removeWhenFarAway = false
            getAttribute(Attribute.MAX_HEALTH)?.baseValue = stats.maxHealth
            health = stats.maxHealth
            addPotionEffects(
                listOf(
                    PotionEffect(PotionEffectType.FIRE_RESISTANCE, Int.MAX_VALUE, 0),
                    PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1),
                ),
            )
        }

        val rider = (world.spawnEntity(location, EntityType.PIGLIN_BRUTE) as PiglinBrute).apply {
            customName(NAME)
            isCustomNameVisible = true
            removeWhenFarAway = false
            isAggressive = true
            isAware = true
            isImmuneToZombification = true
            canPickupItems = false
            equipment.setItemInMainHand(createMace())
            equipment.itemInMainHandDropChance = 0f // set to 1f if you want the mace to drop
            addPotionEffects(
                listOf(
                    PotionEffect(PotionEffectType.FIRE_RESISTANCE, Int.MAX_VALUE, 0),
                ),
            )
            // Armor
            equipment.setHelmet(ItemStack(Material.NETHERITE_HELMET))
            equipment.setChestplate(ItemStack(Material.NETHERITE_CHESTPLATE))
            equipment.setLeggings(ItemStack(Material.NETHERITE_LEGGINGS))
            equipment.setBoots(ItemStack(Material.NETHERITE_BOOTS))
        }

        hog.addPassenger(rider)
        mount = hog
        entity = rider
        applyBaseAttributes()
        return rider
    }

    private fun createMace(): ItemStack {
        val mace = ItemStack(Material.MACE)
        val meta = mace.itemMeta
        // Mace-family enchants (1.21+). Swap to your OdysseyEnchantments as needed.
        meta.addEnchant(Enchantment.DENSITY, 5, true)
        meta.addEnchant(Enchantment.BREACH, 4, true)
        meta.addEnchant(Enchantment.WIND_BURST, 3, true)
        meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true)
        meta.addEnchant(Enchantment.UNBREAKING, 3, true)
        mace.itemMeta = meta
        return mace
    }

    override fun onSpawn(location: Location) {
        playSoundNearby(Sound.ENTITY_GHAST_SCREAM, 2.5f, 0.9f, radius = bossBarRadius)
        playSoundNearby(Sound.ITEM_GOAT_HORN_SOUND_6, 2.5f, 0.85f, radius = bossBarRadius)
        announce(HogRiderLine.ARRIVAL, nearbyPlayers(bossBarRadius))
        activate() // Hog Rider fights immediately, no dormant phase.
    }

    // --------------------------------------------------------------- attacks

    override fun attackOptions(): List<AttackOption> = listOf(
        // Move 1: leap ~20 blocks onto a target, then smash on landing.
        AttackOption(
            weight = 3,
            attack = LeapAttack(
                leapPower = 2.0,
                horizontalPull = 1.6,
                onLand = ShockwaveSmashAttack(maxRadius = 8.0, damage = 16.0, knockback = 1.4),
            ),
            target = TargetMode.RANDOM_PLAYER,
            line = HogRiderLine.LEAP,
        ),
        // Move 2: standing ground smash — a wave of debris rings from the hog.
        AttackOption(
            weight = 3,
            attack = ShockwaveSmashAttack(maxRadius = 12.0, damage = 12.0, knockback = 1.3),
            target = TargetMode.SELF,
            line = HogRiderLine.SMASH,
        ),
    )

    // ------------------------------------------------------------- end of fight

    override fun onDefeat(killer: Player?) {
        killer?.giveExpLevels(40)
        announce(HogRiderLine.DEFEATED, nearbyPlayers(bossBarRadius))
        nearbyPlayers(bossBarRadius).forEach {
            it.playSound(it, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f)
            it.giveExp(3550)
        }
        mount?.remove()
        mount = null
        // TODO: drop your unique Hog Rider loot here.
    }

    override fun onDepart() {
        mount?.remove()
        mount = null
    }

    companion object {
        const val KEY = "hog_rider"
        val NAME: Component = Component.text("Hog Rida", TextColor.color(255, 170, 0))
    }
}

/* ============================================================================
 *  Hog Rider speech — tiny pack, same system as the Ambassador. Edit freely.
 * ========================================================================== */

enum class HogRiderLine : DialogueKey {
    ARRIVAL,
    LEAP,
    SMASH,
    DEFEATED;

    override val id: String get() = name
}

object HogRiderDialogue {
    private val GOLD = TextColor.color(255, 170, 0)
    private val prefix = Component.text("[Hog Rida] ", GOLD)

    val pack: Dialogue = Dialogue.of(prefix, TextColor.color(255, 255, 255)) {
        line(HogRiderLine.ARRIVAL, "HOOOOOGGG RIIDDAAAAAA!!!")
        line(HogRiderLine.LEAP, "INCOMING!", "LOOK UP!")
        line(HogRiderLine.SMASH, "SMAAASH!", "BONK!")
        line(HogRiderLine.DEFEATED, "the hog... rides no more...")
    }
}
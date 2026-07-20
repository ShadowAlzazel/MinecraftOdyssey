package me.shadowalzazel.mcodyssey.common.boss.the_ambassador

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemEnchantments
import me.shadowalzazel.mcodyssey.common.boss.AttackOption
import me.shadowalzazel.mcodyssey.common.boss.BossManager
import me.shadowalzazel.mcodyssey.common.boss.BossState
import me.shadowalzazel.mcodyssey.common.boss.BossStats
import me.shadowalzazel.mcodyssey.common.boss.Dialogue
import me.shadowalzazel.mcodyssey.common.boss.GiftReceiver
import me.shadowalzazel.mcodyssey.common.boss.OdysseyBoss
import me.shadowalzazel.mcodyssey.common.boss.TargetMode
import me.shadowalzazel.mcodyssey.common.enchantments.OdysseyEnchantments
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.UUID

/**
 * The Ambassador: an envoy you can appease with gifts or provoke into a fight.
 *
 * All of its *content* (speech + trade table) lives in AmbassadorContent.kt.
 * This file is only behaviour: the trading state machine and how it hooks into
 * the shared boss framework.
 */
@Suppress("UnstableApiUsage")
class TheAmbassador(
    plugin: JavaPlugin,
    private val spawnLocation: Location,
) : OdysseyBoss(plugin, KEY, NAME), GiftReceiver {

    override val dialogue: Dialogue = AmbassadorDialogue.pack
    override val stats = BossStats(
        maxHealth = 800.0,
        armor = 6.0,
        attackPeriodTicks = 20L * 8
    )

    private val giftTable = GiftTable.ambassador()

    // --- trading state ---
    internal var patience: Double = 55.0
        private set
    internal var appeasement: Double = 0.0
        private set

    private val playerLikeness = mutableMapOf<UUID, Double>()
    private val giftCooldown = mutableMapOf<UUID, Long>()

    // ---------------------------------------------------------------- spawning

    private fun createWeapon(): ItemStack {
        val bow = ItemStack(Material.BOW).apply {
            addUnsafeEnchantment(Enchantment.POWER, 7)
            addUnsafeEnchantment(Enchantment.PUNCH, 3)
            addUnsafeEnchantment(Enchantment.INFINITY, 1)
        }
        return bow
    }

    override fun createEntity(location: Location): LivingEntity =
        (location.world.spawnEntity(location, EntityType.ILLUSIONER) as Illusioner).apply {
            customName(NAME)
            isCustomNameVisible = true
            removeWhenFarAway = false
            isCanJoinRaid = false
            isAware = false            // dormant until provoked
            canPickupItems = true      // needed for the gift mechanic
            equipment.itemInMainHandDropChance = 100f
            equipment.setItemInMainHand(createWeapon())
            addPotionEffects(
                listOf(
                    PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 300, 1),
                    PotionEffect(PotionEffectType.GLOWING, 20 * 300, 1),
                    PotionEffect(PotionEffectType.FIRE_RESISTANCE, Int.MAX_VALUE, 3),
                    PotionEffect(PotionEffectType.WATER_BREATHING, Int.MAX_VALUE, 3),
                    PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 2),
                ),
            )
        }.also { entity = it; applyBaseAttributes() }

    override fun onSpawn(location: Location) {
        val host = location.getNearbyPlayers(64.0).minByOrNull { it.location.distanceSquared(location) }
        playSoundNearby(Sound.ENTITY_EVOKER_PREPARE_SUMMON, 2.5f, 0.9f, radius = 64.0)
        announce(AmbassadorLine.ARRIVAL, nearbyPlayers(64.0), mapOf("host" to (host?.name ?: "this realm")))
        plugin.logger.info("The Ambassador has arrived at x:${location.blockX}, z:${location.blockZ}")
    }

    // --------------------------------------------------------------- combat loop

    override fun onActivate() {
        playSoundNearby(Sound.ENTITY_WITHER_SPAWN)
        (entity as? Illusioner)?.isAware = true
    }

    override fun attackOptions(): List<AttackOption> = listOf(
        AttackOption(4, SkyBombardAttack(), TargetMode.RANDOM_PLAYER, AmbassadorLine.SKY_BOMBARD, requiresPlayers = false),
        AttackOption(3, GravityLaunchAttack(), TargetMode.RANDOM_PLAYER, AmbassadorLine.GRAVITY_LAUNCH),
        AttackOption(3, FallingSingularityAttack(), TargetMode.ALL_PLAYERS, AmbassadorLine.FALLING_SINGULARITY),
        AttackOption(2, HijackAttack(), TargetMode.RANDOM_PLAYER, AmbassadorLine.HIJACK),
        AttackOption(2, GravityAnchorAttack(), TargetMode.ALL_PLAYERS, AmbassadorLine.GRAVITY_ANCHOR),
    )

    // --------------------------------------------------------- pre-combat: damage

    override fun onDamaged(source: Entity?, amount: Double) {
        if (state == BossState.ACTIVE) return // already fighting; let normal combat run
        val audience = nearbyPlayers()
        val damagerName = (source as? Player)?.name ?: "your kind"

        if (source !is Player) {
            appeasement -= (amount + 2.0)
            patience -= amount
            announce(
                if (patience <= 0) AmbassadorLine.NONPLAYER_PATIENCE_BROKEN else AmbassadorLine.NONPLAYER_STRUCK,
                audience,
            )
        } else {
            val id = source.uniqueId
            if (id !in playerLikeness) {
                playerLikeness[id] = 0.0
                announce(AmbassadorLine.FIRST_STRIKE, audience)
                return
            }
            appeasement -= (amount + 2.0)
            patience -= amount
            val likeness = playerLikeness.getValue(id)
            val line = when {
                likeness >= 65 -> AmbassadorLine.STRUCK_BELOVED
                likeness >= 25 -> AmbassadorLine.STRUCK_LIKED
                else -> AmbassadorLine.STRUCK_DISLIKED
            }
            announce(line, audience, mapOf("player" to source.name))
            playerLikeness[id] = likeness - amount
        }

        when {
            patience <= 0 -> { announce(AmbassadorLine.ENRAGED, audience); activate() }
            appeasement <= -5 -> announce(AmbassadorLine.APPEASEMENT_WARNING, audience)
            else -> announce(AmbassadorLine.STRUCK_NEUTRAL, audience, mapOf("player" to damagerName))
        }
    }

    // ----------------------------------------------------------- trading: gifts

    override fun onGift(giver: Player, item: Item) {
        val now = System.currentTimeMillis()
        val last = giftCooldown[giver.uniqueId]
        if (last != null && now - last < GIFT_COOLDOWN_MS) {
            AmbassadorDialogue.send(giver, AmbassadorLine.GIFT_COOLDOWN)
            return
        }
        giftCooldown[giver.uniqueId] = now
        processGift(giver, item)
    }

    private fun processGift(giver: Player, item: Item) {
        val stack = item.itemStack
        val material = stack.type
        val likeness = playerLikeness.getOrPut(giver.uniqueId) { 0.0 }

        if (likeness >= 100.0) {
            AmbassadorDialogue.send(giver, AmbassadorLine.GIFT_MAXED, mapOf("player" to giver.name))
            return
        }
        if (stack.itemMeta?.hasEnchants() == true || material == Material.ENCHANTED_BOOK) {
            appeasement -= 2.0
            patience -= 2.0
            AmbassadorDialogue.send(giver, AmbassadorLine.GIFT_REJECT_ENCHANTS)
            return
        }

        // Not on the menu: the Ambassador ignores it and keeps his composure.
        val reaction = giftTable.reactionFor(material) ?: return

        val extraValue = (likeness / 25).toInt() + 1
        reaction.reward(GiftContext(giver, this, extraValue))

        appeasement += reaction.likeness
        patience -= reaction.patiencePenalty
        val newLikeness = likeness + reaction.likeness
        playerLikeness[giver.uniqueId] = newLikeness
        item.remove()

        // Feedback: explicit reaction line, then generic mood particles/lines.
        reaction.message?.let { AmbassadorDialogue.send(giver, it, mapOf("player" to giver.name)) }
        when {
            reaction.likeness >= 10 -> world.spawnParticle(Particle.WITCH, giver.location, 35, 1.0, 1.0, 1.0)
            reaction.likeness >= 5 -> world.spawnParticle(Particle.HAPPY_VILLAGER, giver.location, 35, 1.0, 1.0, 1.0)
            reaction.likeness < -2 -> AmbassadorDialogue.send(giver, AmbassadorLine.GIFT_BAD)
        }
        if (newLikeness > 50.0 && reaction.message == null) {
            AmbassadorDialogue.send(giver, AmbassadorLine.GIFT_NICE, mapOf("player" to giver.name))
        }
    }

    // ------------------------------------------------------------- end of fight

    override fun onDefeat(killer: Player?) {
        val killerName = killer?.name ?: "an unknown hero"
        killer?.giveExpLevels(40)
        announce(AmbassadorLine.DEFEATED)
        entity.getNearbyEntities(64.0, 64.0, 64.0).filterIsInstance<Player>().forEach {
            it.sendMessage(Component.text("The Ambassador has departed ungracefully!", GOLD, TextDecoration.ITALIC))
            it.sendMessage(
                Component.text("With ", GOLD, TextDecoration.ITALIC)
                    .append(Component.text(killerName, ORANGE))
                    .append(Component.text(" taking the final blow!", GOLD)),
            )
            it.playSound(it, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f)
            it.giveExp(3550)
        }
        // TODO: drop your unique loot here (gilded book, primo gems, etc.)
    }

    override fun onDepart() {
        announce(AmbassadorLine.DEPARTED, nearbyPlayers(64.0))
        playSoundNearby(Sound.ENTITY_ELDER_GUARDIAN_CURSE, radius = 64.0)
    }

    companion object {
        const val KEY = "ambassador"
        private val PINK = TextColor.color(255, 85, 255)
        private val GOLD = TextColor.color(255, 255, 85)
        private val ORANGE = TextColor.color(255, 170, 0)
        val NAME: Component = Component.text("The Ambassador", PINK)
        private const val GIFT_COOLDOWN_MS = 1000L * 5

        /**
         * Backwards-compatible "world event" spawn: the Ambassador descends from
         * the sky onto a random online player's land, like the old createEvent().
         */
        fun summonEvent(plugin: JavaPlugin, world: World) {
            val host = world.players.randomOrNull() ?: return
            val spot = host.location.clone().add(
                (-28..28).random().toDouble(), 0.0, (-28..28).random().toDouble(),
            ).apply { y = 300.0 }
            BossManager.summon(plugin, KEY, spot)
        }
    }
}
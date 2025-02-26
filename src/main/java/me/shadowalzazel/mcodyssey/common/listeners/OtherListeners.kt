package me.shadowalzazel.mcodyssey.common.listeners

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Creeper
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.inventory.meta.SkullMeta

object OtherListeners : Listener {

    @EventHandler
    fun preventHelmetPlace(event: BlockPlaceEvent) {
        if (!event.itemInHand.hasItemMeta()) return
        if (event.blockPlaced.type != Material.CARVED_PUMPKIN) return
        if (event.itemInHand.itemMeta!!.hasCustomModelData()) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun chargedCreeperDeath(event: EntityDamageByEntityEvent) {
        if (event.entity !is Player) return
        if (event.damager.type != EntityType.CREEPER) return
        val player = event.entity as Player
        val creeper = event.damager as Creeper
        if (!creeper.isPowered) return
        if (event.finalDamage <= player.health) return
        if (player.equipment!!.itemInOffHand.type == Material.TOTEM_OF_UNDYING
            || player.equipment!!.itemInMainHand.type == Material.TOTEM_OF_UNDYING) return

        val skull = ItemStack(Material.PLAYER_HEAD, 1)
        val skullMeta = (skull.itemMeta as SkullMeta)
        skullMeta.playerProfile = player.playerProfile
        skullMeta.owningPlayer = player
        skull.also {
            it.itemMeta = skullMeta
        }

        player.world.dropItem(player.location, skull)
    }

    @EventHandler
    fun elytraBoost(event: PlayerElytraBoostEvent) {
        if ((event.itemStack.itemMeta!! as FireworkMeta).power > 3) {
            val power = ((event.itemStack.itemMeta as FireworkMeta).power)
            val boostFailureChance = 0.05 + (power * 0.05)
            val boostFailureDamage = power * 1.0
            val dudChance = 0.05

            if ((boostFailureChance * 100) > (0..100).random()) {
                if (dudChance * 10 > (0..10).random()) {
                    event.isCancelled = true
                    return
                }
                event.firework.detonate()
                createDetonatingFirework(event.player.location)
                event.player.damage(boostFailureDamage)
            }
        }
    }

    private fun createDetonatingFirework(targetLocation: Location): Firework {
        val randomColors = listOf(Color.BLUE, Color.RED, Color.YELLOW, Color.ORANGE, Color.AQUA)
        val superFirework: Firework = (targetLocation.world.spawnEntity(targetLocation, EntityType.FIREWORK_ROCKET) as Firework).apply {
            fireworkMeta = fireworkMeta.clone().also {
                it.addEffect(
                    FireworkEffect.builder()
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .withColor(randomColors.random())
                    .withFade(randomColors.random())
                    .trail(true)
                    .flicker(true)
                    .build()
                )
            }
            fireworkMeta.power = 1
            ticksToDetonate = 0
        }
        return superFirework
    }


    //IDEAS
    //  Ornament, Socket,

    // Get First space, then next space, if first set (quality) has then proceeded
    //  get the gem.


    // Percentages %
    // -----------
    // Diamond -> Increases durability (1 - 20)
    // Amethyst -> Increases damages against elites and bosses (5 - 30)
    // Emerald -> Increases Crit Damage (50 - 400)
    // Quartz -> Increases Base Damage (10 - 200)

    // Neptunian Diamond
    // Iojovian Emerald
    // Soul Quartz



    // Tiers
    // -----------
    // Regular
    // Flawless
    // IDK

    // Each item has ONE AND ONLY ONE socket MAX
    //

    // Maybe if kill with soul catalyst, takes Entity Type (if not custom) as name,
    // Soul Gem -> Increases Damage to that Entity Type (Custom Name Tag) by (10 - 60)


    // Add tags for gilded and blood moon "Elites"

    // IF WEAPON HAS SOCKET GEM/RUNE/SOUL

    // DO stuff


    // OR ---

    // If it has special Dungeon gem
    // DO effects

    // Diamond (Armor: 10% more resistance) (DeprecatedWeapon: )
    // Emerald
    // Amethyst
    // Soul Quartz
    // Ruby

    // Make gem refiner table/merchant
    // Smith weapons reset attack, hide attack, and speed, add custom Lore



}
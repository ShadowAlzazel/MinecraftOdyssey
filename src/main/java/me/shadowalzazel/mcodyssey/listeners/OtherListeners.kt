package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
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

    //@EventHandler
    fun chargedCreeperDeath(event: EntityDamageByEntityEvent) {
        if (event.entity !is Player) return
        if (event.damager.type != EntityType.CREEPER) return
        if (event.entity.isDead) println("DIED")

        val player = event.entity as Player

        val skull = ItemStack(Material.PLAYER_HEAD, 1).apply {
            (itemMeta as SkullMeta).playerProfile = player.playerProfile
        }
    }

    @EventHandler
    fun elytraBoost(event: PlayerElytraBoostEvent) {
        if ((event.itemStack.itemMeta!! as FireworkMeta).power > 3) {
            val power = ((event.itemStack.itemMeta as FireworkMeta).power)
            val boostFailureChance = 0.05 + (power * 0.05)
            val boostFailureDamage = power * 1.0
            val dudChance = 0.1

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
        val superFirework: Firework = (targetLocation.world.spawnEntity(targetLocation, EntityType.FIREWORK) as Firework).apply {
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
}
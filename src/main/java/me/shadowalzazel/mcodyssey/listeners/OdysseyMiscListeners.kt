package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.bosses.theAmbassador.AmbassadorBoss
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.entity.EntityType
import org.bukkit.entity.Snowman
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.weather.LightningStrikeEvent
import org.bukkit.event.world.TimeSkipEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object OdysseyMiscListeners : Listener {

    // Check if Ender Dragon Dies
    @EventHandler
    fun onDefeatEnderDragon(event: EntityDeathEvent) {
        val dragon = event.entity
        val endWorld = event.entity.world
        if (dragon.type == EntityType.ENDER_DRAGON) {
            for (aPlayer in endWorld.players) {
                aPlayer.sendMessage("${ChatColor.DARK_PURPLE}The end has just begun ...")

            }
        }
    }


    // Misc snow man immunity
    @EventHandler
    fun snowManDamage(event: CreatureSpawnEvent) {
        val snowSpawn = event.spawnReason
        val anEntity = event.entity
        if (snowSpawn == CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN) {
            val anEntityLocationX = anEntity.location.blockX
            val anEntityLocationY = anEntity.location.blockY
            val anEntityLocationZ = anEntity.location.blockZ
            //val anEntityBlock = anEntity.world.getBlockAt(anEntity.location.blockX, anEntity.location.blockY, anEntity.location.blockZ)
            if (anEntity.world.getTemperature(anEntityLocationX, anEntityLocationY, anEntityLocationZ) >= 1) {
                //change to powder snow
                val snowMan = event.entity as Snowman
                if (true) {
                    val snowSkin = PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999999, 1)
                    anEntity.addPotionEffect(snowSkin)
                }
            }
        }
    }



    @EventHandler
    fun chargeAmethyst(event: LightningStrikeEvent) {
        if (event.cause != LightningStrikeEvent.Cause.TRIDENT) {
            // DO CHARGE?
        }

    }

    // TEST
    // Elytra Mechanics
    @EventHandler
    fun elytraBoost(event: PlayerElytraBoostEvent) {
        var boostFailureChance = 0.15
        var boostFailureDamage = 20.0

        // Lower Boost Failure Chance
        if (event.itemStack.lore()?.contains(Component.text("Magma_Cream")) == true) { // Change to detect component var
            boostFailureChance -= 0.10
        }

        // DUD?

        if ((boostFailureChance * 100) > (0..100).random()) {

            // EXPLODE !!

            // Lower Boost Failure Damage
            if (event.itemStack.lore()?.contains(Component.text("Blaze_Powder")) == true) { // Change to detect component var
                boostFailureDamage -= 15.0
            }

            // Change to explode fireball
            event.player.damage(boostFailureDamage)


        }


    }


    // WHEN CRAFTING
    // DETECT IF ROCKET

}
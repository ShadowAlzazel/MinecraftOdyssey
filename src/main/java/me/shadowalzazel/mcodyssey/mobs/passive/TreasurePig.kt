package me.shadowalzazel.mcodyssey.mobs.passive

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.mobs.utility.FallingBlockTimer
import me.shadowalzazel.mcodyssey.mobs.utility.OdysseyMob
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Pig
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object TreasurePig: OdysseyMob("Treasure Pig") {

    fun createMob(odysseyWorld: World, spawningLocation: Location): Pig {

        // Treasure Pig Entity
        val treasurePigEntity = odysseyWorld.spawnEntity(spawningLocation, EntityType.PIG) as Pig
        val enhancedHealth = PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, 22)
        val enhancedSpeed = PotionEffect(PotionEffectType.SPEED, 99999, 4)
        val enhancedJump = PotionEffect(PotionEffectType.JUMP, 99999, 1)
        treasurePigEntity.addPotionEffects(listOf(enhancedHealth, enhancedSpeed, enhancedJump))

        treasurePigEntity.canPickupItems = true
        treasurePigEntity.health = 100.0
        treasurePigEntity.customName = "§6$name"
        treasurePigEntity.isCustomNameVisible = true
        treasurePigEntity.clearActiveItem()

        val someBlockData = MinecraftOdyssey.instance.server.createBlockData(Material.BARREL)
        val someBlock = odysseyWorld.spawnFallingBlock(treasurePigEntity.location.clone().add(0.0, 4.0, 0.0), someBlockData)
        someBlock.shouldAutoExpire(false)
        treasurePigEntity.addPassenger(someBlock)
        someBlock.ticksLived = 1
        someBlock.isPersistent = true
        // Add task timer
        val newTimer = FallingBlockTimer(someBlock)
        newTimer.runTaskTimer(MinecraftOdyssey.instance, 20 * 10, 20 * 10)

        return treasurePigEntity
    }

}
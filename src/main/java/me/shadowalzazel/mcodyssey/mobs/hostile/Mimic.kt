package me.shadowalzazel.mcodyssey.mobs.hostile

import me.shadowalzazel.mcodyssey.mobs.utility.OdysseyMob
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Vex
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

// Temp
object Mimic : OdysseyMob("Mimic") {

    fun createMob(odysseyWorld: World, spawningLocation: Location): Vex {

        // Treasure Pig Entity
        val vexChestEntity = odysseyWorld.spawnEntity(spawningLocation, EntityType.VEX) as Vex
        val enhancedHealth = PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, 22)
        val enhancedSpeed = PotionEffect(PotionEffectType.SPEED, 99999, 4)
        val enhancedJump = PotionEffect(PotionEffectType.JUMP, 99999, 1)
        vexChestEntity.addPotionEffects(listOf(enhancedHealth, enhancedSpeed, enhancedJump))

        vexChestEntity.canPickupItems = true
        vexChestEntity.health = 100.0
        vexChestEntity.customName = "§6$name"
        vexChestEntity.isCustomNameVisible = true
        vexChestEntity.isInvisible = true
        vexChestEntity.clearActiveItem()
        vexChestEntity.equipment.helmet = ItemStack(Material.CHEST, 1)

        return vexChestEntity
    }

    fun todo() {
        TODO("make some entity that has riding chest")
    }

}
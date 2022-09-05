package me.shadowalzazel.mcodyssey.mobs.passive

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.mobs.utility.FallingBlockTimer
import me.shadowalzazel.mcodyssey.mobs.utility.OdysseyMob
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Pig
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object TreasurePig: OdysseyMob("Treasure Pig", EntityType.PIG, 100.0) {

    override fun createMob(someWorld: World, spawningLocation: Location): Pig {
        // Some Block
        val someBlockData = MinecraftOdyssey.instance.server.createBlockData(Material.BARREL)
        val someBlock = someWorld.spawnFallingBlock(spawningLocation, someBlockData).apply {
            shouldAutoExpire(false)
            isPersistent = true
            ticksLived = 1
        }
        // Treasure Pig Entity
        val treasurePigEntity = (super.createMob(someWorld, spawningLocation) as Pig).apply {
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.SPEED, 99999, 4),
                PotionEffect(PotionEffectType.JUMP, 99999, 2)
            ))
            canPickupItems = true
            customName(Component.text(this@TreasurePig.odysseyName, TextColor.color(255, 170, 75)))
            isCustomNameVisible = true
            clearActiveItem()
            addPassenger(someBlock)
        }
        // Add task timer
        val newTimer = FallingBlockTimer(someBlock)
        newTimer.runTaskTimer(MinecraftOdyssey.instance, 20 * 10, 20 * 10)

        return treasurePigEntity
    }

}
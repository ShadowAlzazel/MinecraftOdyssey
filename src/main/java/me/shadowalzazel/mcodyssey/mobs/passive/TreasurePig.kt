package me.shadowalzazel.mcodyssey.mobs.passive

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.mobs.base.FallingBlockTimer
import me.shadowalzazel.mcodyssey.mobs.base.OdysseyMob
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Pig
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

object TreasurePig: OdysseyMob("Treasure Pig", EntityType.PIG, 100.0) {

    private val droppingLootTable = listOf(Ingredients.NEPTUNIAN_DIAMOND, Ingredients.JOVIAN_EMERALD, Ingredients.KUNZITE)

    override fun createMob(someWorld: World, spawningLocation: Location): Pig {
        // Some Block
        val someBlockData = Odyssey.instance.server.createBlockData(Material.BARREL)
        val someBlock = someWorld.spawnFallingBlock(spawningLocation, someBlockData).apply {
            shouldAutoExpire(false)
            isPersistent = false
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
        // Add falling block timer
        val newTimer = FallingBlockTimer(someBlock)
        newTimer.runTaskTimer(Odyssey.instance, 20 * 10, 20 * 10)
        // Add loot drop
        val droppingLootTask = DroppingLootTask(treasurePigEntity)
        droppingLootTask.runTaskTimer(Odyssey.instance, 20 * 10, 20 * 10)
        return treasurePigEntity
    }


    class DroppingLootTask(private val treasurePig: Pig) : BukkitRunnable() {
        private var counter = 0
        override fun run() {
            if (!treasurePig.isDead && counter < 40) {
                counter += 1
                with(treasurePig.world) {
                    dropItem(treasurePig.location, droppingLootTable.random().createItemStack(1))
                    dropItem(treasurePig.location, ItemStack(Material.GOLD_NUGGET, (1..2).random()))
                    playSound(treasurePig.location, Sound.ENTITY_WITHER_BREAK_BLOCK, 2.5F, 0.5F)
                    val goldBlockBreak = Material.GOLD_BLOCK.createBlockData()
                    spawnParticle(Particle.BLOCK_CRACK, treasurePig.location, 35, 0.95, 0.8, 0.95, goldBlockBreak)
                }
            }
            else {
                this.cancel()
            }
        }
    }



}
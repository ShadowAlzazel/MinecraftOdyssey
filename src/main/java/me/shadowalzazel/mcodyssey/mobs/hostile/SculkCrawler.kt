package me.shadowalzazel.mcodyssey.mobs.hostile

import me.shadowalzazel.mcodyssey.mobs.utility.OdysseyMob
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Zombie
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object SculkCrawler: OdysseyMob("Sculk Crawler") {

    fun createMob(odysseyWorld: World, spawningLocation: Location): Zombie {

        // Treasure Pig Entity
        val sculkCrawlerEntity = odysseyWorld.spawnEntity(spawningLocation, EntityType.ZOMBIE) as Zombie
        val enhancedHealth = PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, 22)
        val enhancedSpeed = PotionEffect(PotionEffectType.SPEED, 99999, 4)
        val enhancedJump = PotionEffect(PotionEffectType.JUMP, 99999, 1)
        sculkCrawlerEntity.addPotionEffects(listOf(enhancedHealth, enhancedSpeed, enhancedJump))

        sculkCrawlerEntity.canPickupItems = true
        sculkCrawlerEntity.health = 100.0
        sculkCrawlerEntity.setBaby()
        sculkCrawlerEntity.customName = "§1$name"
        sculkCrawlerEntity.isCustomNameVisible = true
        sculkCrawlerEntity.isInvisible = true
        sculkCrawlerEntity.isSilent = true
        sculkCrawlerEntity.clearActiveItem()
        sculkCrawlerEntity.equipment.helmet = ItemStack(Material.SCULK_SENSOR, 1)

        return sculkCrawlerEntity
    }

    fun todo() {
        TODO("have particles as legs ")
    }

}
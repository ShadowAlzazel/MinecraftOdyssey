package me.shadowalzazel.mcodyssey.mobs.hostile

import me.shadowalzazel.mcodyssey.mobs.utility.OdysseyMob
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Spider
import org.bukkit.entity.Zombie
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object SculkCrawler: OdysseyMob("Sculk Crawler") {

    fun createMob(odysseyWorld: World, spawningLocation: Location): Spider {

        // Treasure Pig Entity
        val sculkCrawlerEntity = odysseyWorld.spawnEntity(spawningLocation, EntityType.SPIDER) as Spider
        val enhancedHealth = PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, 22)
        val enhancedSpeed = PotionEffect(PotionEffectType.SPEED, 99999, 4)
        val enhancedJump = PotionEffect(PotionEffectType.JUMP, 99999, 1)
        sculkCrawlerEntity.addPotionEffects(listOf(enhancedHealth, enhancedSpeed, enhancedJump))

        sculkCrawlerEntity.canPickupItems = true
        sculkCrawlerEntity.health = 100.0
        sculkCrawlerEntity.customName = "dinnerbone" //§1
        sculkCrawlerEntity.isCustomNameVisible = false
        sculkCrawlerEntity.isInvisible = true
        sculkCrawlerEntity.isSilent = true
        sculkCrawlerEntity.clearActiveItem()

        return sculkCrawlerEntity
    }

    fun todo() {
        TODO("have particles as legs ")
    }

}
package me.shadowalzazel.mcodyssey.bosses

import me.shadowalzazel.mcodyssey.bosses.utility.OdysseyBoss
import org.bukkit.World
import org.bukkit.Location
import org.bukkit.ChatColor
import org.bukkit.entity.EntityType
import org.bukkit.entity.Illusioner
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

open class AmbassadorBoss : OdysseyBoss("The Ambassador", "Illusioner") {

    var patienceMeter: Double = 100.0
    var appeasementMeter: Double = 0.0
    var ambassadorBossEntity: Illusioner? = null
    var ambassadorActive: Boolean = false

    // spawner for boss entity
    private fun spawnBoss(odysseyWorld: World): Illusioner {
        //test coords
        val randomZLoc = (-10..10).random()
        val randomXLoc = (-10..10).random()
        val spawnBossLocation = Location(odysseyWorld, randomXLoc.toDouble(), 250.toDouble(), randomZLoc.toDouble())
        println("Spawned the Ambassador at $randomXLoc, $randomZLoc")
        return odysseyWorld.spawnEntity(spawnBossLocation, EntityType.ILLUSIONER) as Illusioner
    }

    fun createBoss(odysseyWorld: World) {
        val odysseyBossEntity: Illusioner = spawnBoss(odysseyWorld)
        // 600 tks = 30 sec
        // Add Potion Effects
        val voidFall = PotionEffect(PotionEffectType.SLOW_FALLING, 600, 1)
        val enhancedHealth = PotionEffect(PotionEffectType.HEALTH_BOOST, 9999, 150)
        val healHealth = PotionEffect(PotionEffectType.REGENERATION, 5, 100)
        val fallResistance = PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 2)
        val ankiFallEffects = listOf<PotionEffect>(fallResistance, enhancedHealth, healHealth, voidFall)
        odysseyBossEntity.addPotionEffects(ankiFallEffects)

        // Change Default Behaviour
        odysseyBossEntity.customName = "${ChatColor.LIGHT_PURPLE}$bossName"
        odysseyBossEntity.isCustomNameVisible = true
        odysseyBossEntity.removeWhenFarAway = false
        odysseyBossEntity.isCanJoinRaid = false
        odysseyBossEntity.isAware = false
        odysseyBossEntity.health = 600.0

        // Add Item

        ambassadorBossEntity = odysseyBossEntity
    }


}


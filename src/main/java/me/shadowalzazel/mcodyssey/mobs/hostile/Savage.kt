package me.shadowalzazel.mcodyssey.mobs.hostile

import me.shadowalzazel.mcodyssey.mobs.utility.OdysseyMob
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Zombie
import org.bukkit.entity.ZombieHorse
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object Savage : OdysseyMob("Savage") {

    fun createKnight(odysseyWorld: World, spawningLocation: Location): Zombie {
        val zombieSteed = odysseyWorld.spawnEntity(spawningLocation, EntityType.ZOMBIE_HORSE) as ZombieHorse
        val enhancedHealth = PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, 25)
        val enhancedSpeed = PotionEffect(PotionEffectType.SPEED, 99999, 2)
        zombieSteed.addPotionEffect(enhancedHealth)
        zombieSteed.addPotionEffect(enhancedSpeed)
        //skeletonSteed.inventory.saddle = ItemStack(Material.SADDLE, 1)
        val someKnight = createMob(odysseyWorld, spawningLocation)
        someKnight.customName = "Savage Knight"
        zombieSteed.isTamed = true
        zombieSteed.addPassenger(someKnight)
        zombieSteed.health = 100.0
        return someKnight

    }


    fun createMob(odysseyWorld: World, spawningLocation: Location): Zombie {

        // Vanguard Entity
        val savageEntity = odysseyWorld.spawnEntity(spawningLocation, EntityType.ZOMBIE) as Zombie
        val enhancedHealth = PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, 8)
        val enhancedStrength = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, 4)
        val enhancedSpeed = PotionEffect(PotionEffectType.FAST_DIGGING, 99999, 8)
        savageEntity.addPotionEffect(enhancedStrength)
        savageEntity.addPotionEffect(enhancedHealth)
        savageEntity.addPotionEffect(enhancedSpeed)

        savageEntity.canPickupItems = true
        savageEntity.health = 50.0
        savageEntity.customName = "Savage"
        savageEntity.clearActiveItem()

        // Spear
        val savageAxe = ItemStack(Material.IRON_AXE, 1)
        val savageAxeMeta = savageAxe.itemMeta
        savageAxeMeta.setDisplayName("${ChatColor.GRAY}Norinthian Axe")
        savageAxeMeta.addEnchant(Enchantment.DURABILITY, 3, true)
        savageAxeMeta.addEnchant(Enchantment.DAMAGE_ALL, 5, true)
        savageAxeMeta.addEnchant(Enchantment.KNOCKBACK, 3, true)
        savageAxe.itemMeta = savageAxeMeta

        // Add Item
        savageEntity.equipment.setItemInMainHand(savageAxe)
        savageEntity.equipment.setItemInOffHand(savageAxe)
        savageEntity.equipment.helmet = ItemStack(Material.CHAINMAIL_HELMET, 1)
        savageEntity.equipment.chestplate = ItemStack(Material.CHAINMAIL_CHESTPLATE, 1)
        savageEntity.equipment.leggings = ItemStack(Material.CHAINMAIL_LEGGINGS, 1)
        savageEntity.equipment.boots = ItemStack(Material.CHAINMAIL_BOOTS, 1)
        savageEntity.equipment.itemInMainHandDropChance = 0F
        savageEntity.equipment.itemInOffHandDropChance = 0F
        savageEntity.equipment.helmetDropChance = 0F
        savageEntity.equipment.chestplateDropChance = 0F
        savageEntity.equipment.leggingsDropChance = 0F
        savageEntity.equipment.bootsDropChance = 0F

        return savageEntity
    }



}
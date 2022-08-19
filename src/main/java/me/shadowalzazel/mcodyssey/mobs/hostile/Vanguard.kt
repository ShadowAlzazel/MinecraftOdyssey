package me.shadowalzazel.mcodyssey.mobs.hostile

import me.shadowalzazel.mcodyssey.mobs.utility.OdysseyMob
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Skeleton
import org.bukkit.entity.SkeletonHorse
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object Vanguard : OdysseyMob("Vanguard") {

    fun createKnight(odysseyWorld: World, spawningLocation: Location): Skeleton {
        val skeletonSteed = odysseyWorld.spawnEntity(spawningLocation, EntityType.SKELETON_HORSE) as SkeletonHorse
        val enhancedHealth = PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 300, 25)
        val enhancedSpeed = PotionEffect(PotionEffectType.SPEED, 20 * 300, 2)
        skeletonSteed.addPotionEffect(enhancedHealth)
        skeletonSteed.addPotionEffect(enhancedSpeed)
        //skeletonSteed.inventory.saddle = ItemStack(Material.SADDLE, 1)
        val someKnight = createMob(odysseyWorld, spawningLocation)
        someKnight.customName = "Vanguard Knight"
        skeletonSteed.isTamed = true
        skeletonSteed.addPassenger(someKnight)
        skeletonSteed.health = 100.0
        return someKnight

    }


    fun createMob(odysseyWorld: World, spawningLocation: Location): Skeleton {

        // Vanguard Entity
        val vanguardEntity = odysseyWorld.spawnEntity(spawningLocation, EntityType.SKELETON) as Skeleton
        val enhancedHealth = PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, 8)
        val enhancedStrength = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, 4)
        val enhancedSpeed = PotionEffect(PotionEffectType.FAST_DIGGING, 99999, 8)
        vanguardEntity.addPotionEffect(enhancedStrength)
        vanguardEntity.addPotionEffect(enhancedHealth)
        vanguardEntity.addPotionEffect(enhancedSpeed)

        vanguardEntity.canPickupItems = true
        vanguardEntity.health = 50.0
        vanguardEntity.customName = "Vanguard"
        vanguardEntity.clearActiveItem()

        // Spear
        val vanguardSpear = ItemStack(Material.IRON_SHOVEL, 1)
        val vanguardSpearMeta = vanguardSpear.itemMeta
        vanguardSpearMeta.setDisplayName("${ChatColor.GRAY}Norinthian Spear")
        vanguardSpearMeta.addEnchant(Enchantment.DURABILITY, 3, true)
        vanguardSpearMeta.addEnchant(Enchantment.DAMAGE_ALL, 5, true)
        vanguardSpearMeta.addEnchant(Enchantment.KNOCKBACK, 6, true)
        vanguardSpear.itemMeta = vanguardSpearMeta

        // Shield
        val vanguardShield = ItemStack(Material.SHIELD, 1)
        val vanguardShieldMeta = vanguardShield.itemMeta
        vanguardShieldMeta.setDisplayName("${ChatColor.GRAY}Norinthian Shield")
        vanguardShieldMeta.addEnchant(Enchantment.DURABILITY, 3, true)
        vanguardShieldMeta.addEnchant(Enchantment.PROTECTION_PROJECTILE, 5, true)
        vanguardShieldMeta.addEnchant(Enchantment.KNOCKBACK, 6, true)
        vanguardShield.itemMeta = vanguardShieldMeta

        // Add Item
        vanguardEntity.equipment.setItemInMainHand(vanguardSpear)
        vanguardEntity.equipment.setItemInOffHand(vanguardShield)
        vanguardEntity.equipment.helmet = ItemStack(Material.IRON_HELMET, 1)
        vanguardEntity.equipment.chestplate = ItemStack(Material.IRON_CHESTPLATE, 1)
        vanguardEntity.equipment.leggings = ItemStack(Material.IRON_LEGGINGS, 1)
        vanguardEntity.equipment.boots = ItemStack(Material.IRON_BOOTS, 1)
        vanguardEntity.equipment.itemInMainHandDropChance = 0F
        vanguardEntity.equipment.itemInOffHandDropChance = 0F
        vanguardEntity.equipment.helmetDropChance = 0F
        vanguardEntity.equipment.chestplateDropChance = 0F
        vanguardEntity.equipment.leggingsDropChance = 0F
        vanguardEntity.equipment.bootsDropChance = 0F

        return vanguardEntity
    }



}
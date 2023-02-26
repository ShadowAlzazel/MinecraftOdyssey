package me.shadowalzazel.mcodyssey.mobs.hostile

import me.shadowalzazel.mcodyssey.items.Weapons
import me.shadowalzazel.mcodyssey.mobs.utility.OdysseyMob
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
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

object Vanguard : OdysseyMob("Vanguard", EntityType.SKELETON, 30.0) {

    fun createKnight(odysseyWorld: World, spawningLocation: Location): Pair<Skeleton, SkeletonHorse> {
        // Modify Vanguard to Knight
        val someKnight = createMob(odysseyWorld, spawningLocation).apply {
            // Name
            customName(Component.text("Vanguard Knight"))
            // Claymore
            val newClaymore = Weapons.IRON_CLAYMORE.createItemStack(1).apply {
                addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5)
                addUnsafeEnchantment(Enchantment.KNOCKBACK, 3)
                itemMeta.displayName(Component.text("Norinthian Claymore"))
            }
            clearActiveItem()
            equipment.setItemInMainHand(newClaymore)
        }

        // Knight Steed
        val skeletonSteed = (odysseyWorld.spawnEntity(spawningLocation, EntityType.SKELETON_HORSE) as SkeletonHorse).apply {
            isTamed = false
            addPassenger(someKnight)
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 300, 25),
                PotionEffect(PotionEffectType.SPEED, 20 * 300, 2)
            ))
            health = 100.0
        }
        return Pair(someKnight, skeletonSteed)
    }


    override fun createMob(someWorld: World, spawningLocation: Location): Skeleton {
        val vanguardEntity = (super.createMob(someWorld, spawningLocation) as Skeleton).apply {
            // Effects
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, 4),
                PotionEffect(PotionEffectType.FAST_DIGGING, 99999, 8)))
            // Miscellaneous
            health = 50.0
            canPickupItems = true
            clearActiveItem()
            customName(Component.text(this@Vanguard.odysseyName, TextColor.color(220, 216, 75)))
            // Spear
            val newSpear = Weapons.IRON_SPEAR.createItemStack(1).apply {
                addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5)
                addUnsafeEnchantment(Enchantment.KNOCKBACK, 3)
                itemMeta.displayName(Component.text("Norinthian Spear"))
            }
            // Shield
            val newShield = ItemStack(Material.SHIELD, 1).apply {
                addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 5)
                addUnsafeEnchantment(Enchantment.KNOCKBACK, 5)
                itemMeta.displayName(Component.text("Norinthian Shield"))
            }
            // Add Items
            equipment.also {
                // TODO: Custom Models
                it.setItemInMainHand(newSpear)
                it.setItemInOffHand(newShield)
                it.helmet = ItemStack(Material.IRON_HELMET, 1)
                it.chestplate = ItemStack(Material.IRON_CHESTPLATE, 1)
                it.leggings = ItemStack(Material.IRON_LEGGINGS, 1)
                it.boots = ItemStack(Material.IRON_BOOTS, 1)
                it.itemInMainHandDropChance = 0F
                it.itemInOffHandDropChance = 0F
                it.helmetDropChance = 0F
                it.chestplateDropChance = 0F
                it.leggingsDropChance = 0F
                it.bootsDropChance = 0F
            }
        }
        return vanguardEntity
    }


}
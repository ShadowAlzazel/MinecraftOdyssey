package me.shadowalzazel.mcodyssey.mobs.hostile

import me.shadowalzazel.mcodyssey.items.Weapons
import me.shadowalzazel.mcodyssey.mobs.base.OdysseyMob
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
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

object Savage : OdysseyMob("Savage", EntityType.ZOMBIE, 30.0) {

    fun createKnight(odysseyWorld: World, spawningLocation: Location): Pair<Zombie, ZombieHorse> {
        // Modify Savage to Knight
        val someKnight = createMob(odysseyWorld, spawningLocation).apply {
            // Name
            customName(Component.text("Savage Knight"))
            // Claymore
            val newClaymore = Weapons.IRON_LONG_AXE.createItemStack(1).apply {
                addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5)
                addUnsafeEnchantment(Enchantment.KNOCKBACK, 3)
                itemMeta.displayName(Component.text("Norinthian Longaxe"))
            }
            clearActiveItem()
            equipment.setItemInOffHand(ItemStack(Material.AIR, 1))
            equipment.setItemInMainHand(newClaymore)
        }

        // Knight Steed
        val zombieSteed = (odysseyWorld.spawnEntity(spawningLocation, EntityType.ZOMBIE_HORSE) as ZombieHorse).apply {
            isTamed = false
            addPassenger(someKnight)
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 300, 25),
                PotionEffect(PotionEffectType.SPEED, 20 * 300, 2)
            ))
            health = 100.0
        }
        return Pair(someKnight, zombieSteed)
    }


    override fun createMob(someWorld: World, spawningLocation: Location): Zombie {
        val savageEntity = (super.createMob(someWorld, spawningLocation) as Zombie).apply {
            // Effects
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, 4),
                PotionEffect(PotionEffectType.FAST_DIGGING, 99999, 8)))
            // Miscellaneous
            health = 50.0
            canPickupItems = true
            clearActiveItem()
            customName(Component.text(this@Savage.odysseyName, TextColor.color(220, 216, 75)))
            // Dagger
            val newDagger = Weapons.IRON_DAGGER.createItemStack(1).apply {
                addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5)
                addUnsafeEnchantment(Enchantment.KNOCKBACK, 3)
                itemMeta.displayName(Component.text("Norinthian Dagger"))
            }
            // Add Items
            equipment.also {
                // TODO: Custom Models
                it.setItemInMainHand(newDagger)
                it.setItemInOffHand(newDagger)
                it.helmet = ItemStack(Material.CHAINMAIL_HELMET, 1)
                it.chestplate = ItemStack(Material.CHAINMAIL_CHESTPLATE, 1)
                it.leggings = ItemStack(Material.CHAINMAIL_LEGGINGS, 1)
                it.boots = ItemStack(Material.CHAINMAIL_BOOTS, 1)
                it.itemInMainHandDropChance = 0F
                it.itemInOffHandDropChance = 0F
                it.helmetDropChance = 0F
                it.chestplateDropChance = 0F
                it.leggingsDropChance = 0F
                it.bootsDropChance = 0F
            }
        }
        return savageEntity
    }

}
package me.shadowalzazel.mcodyssey.common.mobs.hostile

import me.shadowalzazel.mcodyssey.common.items.custom.Equipment
import me.shadowalzazel.mcodyssey.datagen.items.WeaponCreator
import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.common.mobs.base.OdysseyMob
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

object Savage : OdysseyMob("Savage", "savage", EntityType.ZOMBIE, 30.0) {

    fun createKnight(world: World, location: Location): Pair<Zombie, ZombieHorse> {
        // Longaxe Weapon
        val weapon = WeaponCreator.toolCreator.createToolStack(ToolMaterial.COPPER, ToolType.LONGAXE).apply {
            addUnsafeEnchantment(Enchantment.UNBREAKING, 3)
            addUnsafeEnchantment(Enchantment.SHARPNESS, 5)
            addUnsafeEnchantment(Enchantment.KNOCKBACK, 3)
            itemMeta.displayName(Component.text("Norinthian Longaxe"))
        }
        // Modify Savage to Knight
        val knight = createMob(world, location).apply {
            customName(Component.text("Savage Knight"))
            clearActiveItem()
            equipment.setItemInOffHand(ItemStack(Material.AIR, 1))
            equipment.setItemInMainHand(weapon)
        }
        // Zombie Steed
        val zombieSteed = (world.spawnEntity(location, EntityType.ZOMBIE_HORSE) as ZombieHorse).apply {
            isTamed = false
            addPassenger(knight)
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 300, 25),
                PotionEffect(PotionEffectType.SPEED, 20 * 300, 2)
            ))
            health = 100.0
        }
        return Pair(knight, zombieSteed)
    }


    override fun createMob(world: World, location: Location): Zombie {
        // Dagger
        val weapon = WeaponCreator.toolCreator.createToolStack(ToolMaterial.COPPER, ToolType.DAGGER).apply {
            addUnsafeEnchantment(Enchantment.UNBREAKING, 3)
            addUnsafeEnchantment(Enchantment.SHARPNESS, 5)
            addUnsafeEnchantment(Enchantment.KNOCKBACK, 3)
            itemMeta.displayName(Component.text("Norinthian Dagger"))
        }
        // Create new mob
        val entity = (super.createMob(world, location) as Zombie).apply {
            // Effects
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.STRENGTH, 99999, 4),
                PotionEffect(PotionEffectType.HASTE, 99999, 8)))
            // Miscellaneous
            health = 50.0
            canPickupItems = true
            clearActiveItem()
            customName(Component.text(this@Savage.displayName, TextColor.color(220, 216, 75)))
            // Add Items
            equipment.also {
                it.setItemInMainHand(weapon)
                it.setItemInOffHand(weapon.clone())
                it.helmet = Equipment.HORNED_HELMET.newItemStack(1)
                it.chestplate = ItemStack(Material.IRON_CHESTPLATE, 1)
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
        return entity
    }

}
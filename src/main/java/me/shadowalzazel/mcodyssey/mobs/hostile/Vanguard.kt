package me.shadowalzazel.mcodyssey.mobs.hostile

import me.shadowalzazel.mcodyssey.items.creators.WeaponCreator
import me.shadowalzazel.mcodyssey.items.utility.ToolMaterial
import me.shadowalzazel.mcodyssey.items.utility.ToolType
import me.shadowalzazel.mcodyssey.mobs.base.OdysseyMob
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

object Vanguard : OdysseyMob("Vanguard", "vanguard", EntityType.SKELETON, 30.0) {

    fun createKnight(world: World, location: Location): Pair<Skeleton, SkeletonHorse> {
        // Poleaxe Weapon
        val weapon = WeaponCreator.toolCreator.createToolStack(ToolMaterial.COPPER, ToolType.POLEAXE).apply {
            addUnsafeEnchantment(Enchantment.UNBREAKING, 3)
            addUnsafeEnchantment(Enchantment.SHARPNESS, 5)
            addUnsafeEnchantment(Enchantment.KNOCKBACK, 3)
            itemMeta.displayName(Component.text("Norinthian Poleaxe"))
        }
        // Modify Vanguard to Knight
        val someKnight = createMob(world, location).apply {
            customName(Component.text("Vanguard Knight"))
            clearActiveItem()
            equipment.setItemInMainHand(weapon)
        }
        // Knight Steed
        val skeletonSteed = (world.spawnEntity(location, EntityType.SKELETON_HORSE) as SkeletonHorse).apply {
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


    override fun createMob(world: World, location: Location): Skeleton {
        // Spear
        val weapon = WeaponCreator.toolCreator.createToolStack(ToolMaterial.COPPER, ToolType.SPEAR).apply {
            addUnsafeEnchantment(Enchantment.UNBREAKING, 3)
            addUnsafeEnchantment(Enchantment.SHARPNESS, 5)
            addUnsafeEnchantment(Enchantment.KNOCKBACK, 3)
            itemMeta.displayName(Component.text("Norinthian Spear"))
        }
        val shield = ItemStack(Material.SHIELD, 1).apply {
            addUnsafeEnchantment(Enchantment.UNBREAKING, 3)
            addUnsafeEnchantment(Enchantment.PROJECTILE_PROTECTION, 5)
            addUnsafeEnchantment(Enchantment.KNOCKBACK, 5)
            itemMeta.displayName(Component.text("Norinthian Shield"))
        }
        // Create new mob
        val entity = (super.createMob(world, location) as Skeleton).apply {
            // Effects
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.STRENGTH, 99999, 4),
                PotionEffect(PotionEffectType.HASTE, 99999, 8)))
            // Miscellaneous
            health = 50.0
            canPickupItems = true
            clearActiveItem()
            customName(Component.text(this@Vanguard.displayName, TextColor.color(220, 216, 75)))
            // Add Items
            equipment.also {
                it.setItemInMainHand(weapon)
                it.setItemInOffHand(shield)
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
        return entity
    }


}
package me.shadowalzazel.mcodyssey.mobs.hostile

import me.shadowalzazel.mcodyssey.items.Weapons
import me.shadowalzazel.mcodyssey.mobs.base.OdysseyMob
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.WitherSkeleton
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object PreacherOfTheAbyss : OdysseyMob("Preacher of the Abyss", EntityType.WITHER_SKELETON, 130.0) {

    override fun createMob(someWorld: World, spawningLocation: Location): WitherSkeleton {
        val preacherEntity = (super.createMob(someWorld, spawningLocation) as WitherSkeleton).apply {
            // Effects
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, 8),
                PotionEffect(PotionEffectType.FAST_DIGGING, 99999, 8),
                PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 99999, 0),
                PotionEffect(PotionEffectType.SPEED, 99999, 0)
            ))
            // Miscellaneous
            health = 150.0
            canPickupItems = true
            clearActiveItem()
            customName(Component.text(this@PreacherOfTheAbyss.odysseyName, TextColor.color(40, 6, 25)))
            // DeprecatedWeapon
            val newBlade = Weapons.ABZU_BLADE.createItemStack(1).apply {
            }
            // Add Items
            equipment.also {
                // TODO: Custom Models
                it.setItemInMainHand(newBlade)
                it.helmet = ItemStack(Material.NETHERITE_HELMET, 1)
                it.chestplate = ItemStack(Material.NETHERITE_CHESTPLATE, 1)
                it.leggings = ItemStack(Material.NETHERITE_LEGGINGS, 1)
                it.boots = ItemStack(Material.NETHERITE_BOOTS, 1)
                it.itemInMainHandDropChance = 100F
                it.itemInOffHandDropChance = 0F
                it.helmetDropChance = 0F
                it.chestplateDropChance = 0F
                it.leggingsDropChance = 0F
                it.bootsDropChance = 0F
            }
        }
        return preacherEntity
    }



}
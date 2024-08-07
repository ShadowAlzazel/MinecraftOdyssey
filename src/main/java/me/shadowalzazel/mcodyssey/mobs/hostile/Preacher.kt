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
import org.bukkit.entity.EntityType
import org.bukkit.entity.WitherSkeleton
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object Preacher : OdysseyMob("Preacher", "preacher", EntityType.WITHER_SKELETON, 130.0) {

    override fun createMob(world: World, location: Location): WitherSkeleton {
        return (super.createMob(world, location) as WitherSkeleton).apply {
            // Effects
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.STRENGTH, 99999, 8),
                PotionEffect(PotionEffectType.HASTE, 99999, 8),
                PotionEffect(PotionEffectType.RESISTANCE, 99999, 0),
                PotionEffect(PotionEffectType.SPEED, 99999, 0)
            ))
            // Miscellaneous
            canPickupItems = true
            clearActiveItem()
            customName(Component.text(this@Preacher.displayName, TextColor.color(40, 6, 25)))
            // Add Items /
            val weapon = WeaponCreator.toolCreator.createToolStack(ToolMaterial.NETHERITE, ToolType.CLAYMORE) // ADD SLOTS AND ENCHANTS
            equipment.also {
                it.setItemInMainHand(weapon) // ADD TRIMS
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
    }



}
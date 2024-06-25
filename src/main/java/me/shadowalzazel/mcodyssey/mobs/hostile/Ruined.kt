package me.shadowalzazel.mcodyssey.mobs.hostile

import me.shadowalzazel.mcodyssey.constants.MobTags

import me.shadowalzazel.mcodyssey.items.creators.WeaponCreator
import me.shadowalzazel.mcodyssey.items.utility.ToolMaterial
import me.shadowalzazel.mcodyssey.items.utility.ToolType
import me.shadowalzazel.mcodyssey.mobs.base.OdysseyMob
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Stray
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object Ruined : OdysseyMob("Ruined", MobTags.RUINED, EntityType.STRAY, 30.0) {

    override fun createMob(world: World, location: Location): Stray {
        val entity = (super.createMob(world, location) as Stray).apply {
            // Effects
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.STRENGTH, 99999, 4),
                PotionEffect(PotionEffectType.HASTE, 99999, 8)
            ))
            // Miscellaneous
            health = 50.0
            canPickupItems = true
            customName(Component.text(displayName, TextColor.color(220, 216, 75)))
            clearActiveItem()
            // Weapon
            val weapon = WeaponCreator.toolCreator.createToolStack(ToolMaterial.DIAMOND, ToolType.CLAYMORE).apply {
                addUnsafeEnchantment(Enchantment.UNBREAKING, 3)
                addUnsafeEnchantment(Enchantment.SHARPNESS, 5)
                addUnsafeEnchantment(Enchantment.KNOCKBACK, 3)
                itemMeta.displayName(Component.text("Maligned Claymore"))
            }
            // Add Items
            equipment.also {
                it.setItemInMainHand(weapon)
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

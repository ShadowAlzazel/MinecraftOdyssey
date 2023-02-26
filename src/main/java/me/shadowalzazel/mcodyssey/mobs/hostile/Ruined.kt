package me.shadowalzazel.mcodyssey.mobs.hostile

import me.shadowalzazel.mcodyssey.items.Weapons
import me.shadowalzazel.mcodyssey.mobs.utility.OdysseyMob
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Zombie
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object Ruined : OdysseyMob("Ruined", EntityType.STRAY, 30.0) {

    override fun createMob(someWorld: World, spawningLocation: Location): Zombie {
        val savageEntity = (super.createMob(someWorld, spawningLocation) as Zombie).apply {
            // Effects
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, 4),
                PotionEffect(PotionEffectType.FAST_DIGGING, 99999, 8)
            ))
            // Miscellaneous
            health = 50.0
            canPickupItems = true

            clearActiveItem()
            customName(Component.text(odysseyName, TextColor.color(220, 216, 75)))
            // Dagger
            val newClaymore = Weapons.DIAMOND_CLAYMORE.createItemStack(1).apply {
                addUnsafeEnchantment(Enchantment.DURABILITY, 3)
                addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5)
                addUnsafeEnchantment(Enchantment.KNOCKBACK, 3)
                itemMeta.displayName(Component.text("Maligned Claymore"))
            }
            // Add Items
            equipment.also {
                // TODO: Custom Models
                it.setItemInMainHand(newClaymore)
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

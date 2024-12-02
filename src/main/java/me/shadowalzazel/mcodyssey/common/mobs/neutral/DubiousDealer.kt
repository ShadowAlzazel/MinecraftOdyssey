package me.shadowalzazel.mcodyssey.common.mobs.neutral

import me.shadowalzazel.mcodyssey.common.mobs.base.OdysseyMob
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.WanderingTrader
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object DubiousDealer : OdysseyMob("Dubious Dealer", "dubious_dealer", EntityType.WANDERING_TRADER, 40.0) {

    override fun createMob(world: World, location: Location): WanderingTrader {
        return (super.createMob(world, location) as WanderingTrader).apply {
            // Effects
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.SPEED, 99999, 2)
            ))
            // Miscellaneous
            health = 40.0
            canPickupItems = false
            clearActiveItem()
            customName(Component.text(this@DubiousDealer.displayName))
            // Add Items
            equipment.also {
                it.helmet = ItemStack(Material.CHAINMAIL_HELMET, 1)
                it.chestplate = ItemStack(Material.CHAINMAIL_CHESTPLATE, 1)
                it.leggings = ItemStack(Material.CHAINMAIL_LEGGINGS, 1)
                it.boots = ItemStack(Material.CHAINMAIL_BOOTS, 1)
                it.helmetDropChance = 0F
                it.chestplateDropChance = 0F
                it.leggingsDropChance = 0F
                it.bootsDropChance = 0F
            }
            // Add Recipes
            //recipes.add(ArcaneSales.prismaticBookTrade())
            //recipes.add(ArcaneSales.lowTierTomeTrade())
            //recipes.add(ArcaneSales.lowTierTomeTrade())
            //recipes.add(ArcaneSales.midTierTomeTrade())
            //recipes.add(ArcaneSales.lowLevelArcaneBookTrade())
            //recipes.add(ArcaneSales.lowLevelArcaneBookTrade())
        }
    }

}
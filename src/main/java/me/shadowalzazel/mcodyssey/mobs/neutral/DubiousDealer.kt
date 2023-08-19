package me.shadowalzazel.mcodyssey.mobs.neutral

import me.shadowalzazel.mcodyssey.constants.MobTags
import me.shadowalzazel.mcodyssey.mobs.base.OdysseyMob
import me.shadowalzazel.mcodyssey.recipe_creators.merchant.ArcaneSales
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.WanderingTrader
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object DubiousDealer : OdysseyMob("Dubious Dealer", MobTags.DUBIOUS_DEALER, EntityType.WANDERING_TRADER, 40.0) {

    override fun createMob(world: World, location: Location): WanderingTrader {
        return (super.createMob(world, location) as WanderingTrader).apply {
            // Effects
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.SPEED, 99999, 2),
                PotionEffect(PotionEffectType.FAST_DIGGING, 99999, 8)
            ))
            // Miscellaneous
            health = 40.0
            canPickupItems = true
            clearActiveItem()
            customName(Component.text(this@DubiousDealer.displayName, TextColor.color(220, 116, 175)))
            // Add Items
            equipment.also {
                // TODO: Custom Models
                it.helmet = ItemStack(Material.CHAINMAIL_HELMET, 1)
                it.chestplate = ItemStack(Material.CHAINMAIL_CHESTPLATE, 1)
                it.leggings = ItemStack(Material.CHAINMAIL_LEGGINGS, 1)
                it.boots = ItemStack(Material.CHAINMAIL_BOOTS, 1)
                it.helmetDropChance = 0F
                it.chestplateDropChance = 0F
                it.leggingsDropChance = 0F
                it.bootsDropChance = 0F
            }
            // TODO!! Recipe
            setRecipe(1, ArcaneSales.createArcaneBookTrade())
            setRecipe(2, ArcaneSales.createLowTierTomeTrade())
            setRecipe(3, ArcaneSales.createLowTierTomeTrade())
            setRecipe(4, ArcaneSales.createLowTierTomeTrade())
            setRecipe(5, ArcaneSales.createLowTierGildedEnchantTrade())
            setRecipe(6, ArcaneSales.createLowTierGildedEnchantTrade())
        }
    }

}
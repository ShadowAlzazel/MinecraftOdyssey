package me.shadowalzazel.mcodyssey.mobs.neutral

import me.shadowalzazel.mcodyssey.mobs.utility.OdysseyMob
import me.shadowalzazel.mcodyssey.recipes.TradingRecipes
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

object DubiousDealer : OdysseyMob("Dubious Dealer", EntityType.WANDERING_TRADER, 40.0) {

    override fun createMob(someWorld: World, spawningLocation: Location): WanderingTrader {
        val dubiousDealerEntity = (super.createMob(someWorld, spawningLocation) as WanderingTrader).apply {

            // Effects
            addPotionEffects(listOf(
                PotionEffect(PotionEffectType.SPEED, 99999, 2),
                PotionEffect(PotionEffectType.FAST_DIGGING, 99999, 8)
            ))
            // Misc
            health = 40.0
            canPickupItems = true
            clearActiveItem()
            customName(Component.text(this@DubiousDealer.odysseyName, TextColor.color(220, 116, 175)))
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
            setRecipe(0, TradingRecipes.tradeList[0])
        }
        return dubiousDealerEntity
    }

}
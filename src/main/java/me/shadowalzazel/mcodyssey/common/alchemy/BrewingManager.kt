@file:Suppress("UnstableApiUsage")

package me.shadowalzazel.mcodyssey.common.alchemy

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.PotionContents
import me.shadowalzazel.mcodyssey.common.listeners.AlchemyListener.hasCustomEffectTag
import me.shadowalzazel.mcodyssey.common.listeners.AlchemyListener.hasOdysseyEffectTag
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.event.inventory.BrewEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionType

interface BrewingManager : DataTagManager {

    fun customBrewingHandler(event: BrewEvent) {
        val ingredient = event.contents.ingredient ?: return
        // Get model data


    }


    private fun brewedCustomPotion(event: BrewEvent) {
        val ingredient = event.contents.ingredient ?: return
        // Get model data
        val bottleModel = getBottleModel(ingredient.type)
        val capModel = getCapModel(ingredient.type)
        val brewerSlots = event.contents.contents.toList()
        // Loop for each item in the Brewing stand
        for (x in 0..2) {
            val item = brewerSlots[x]?.clone() ?: continue
            if (item.type == Material.AIR) continue
            val potionData = item.getData(DataComponentTypes.POTION_CONTENTS) ?: continue
            var isEnhancedPotion = true
            // Create Enhanced Potions
            when(ingredient.type) {
                Material.GLOW_BERRIES ->  event.results[x] = createUpgradedPlusPotion(item)
                Material.HONEY_BOTTLE ->  event.results[x] = createExtendedPlusPotion(item)
                Material.PRISMARINE_CRYSTALS ->  event.results[x] = createPotionVials(item)
                // Create AURA POTION when near, applies effects, GOOD or BAD (mini-beacon)
                else -> isEnhancedPotion = false
            }
            var result = event.results[x]
            // Model Customization
            val hasBasePotion = potionData.potion() != null
            val hasCustomEffects = (potionData.customEffects().isNotEmpty()) || item.hasCustomEffectTag()
            val hasOdysseyPotionEffects = item.hasOdysseyEffectTag()
            val hasCustomModel = item.getData(DataComponentTypes.ITEM_MODEL) != null
            // Set Model Data

            // Final
            event.results[x] = result
        }

    }

    private fun getCapModel(material: Material): String? {
        return when (material) {
            Material.REDSTONE -> "volumetric_bottle"
            Material.GLOWSTONE_DUST -> "square_bottle"
            Material.GLOW_BERRIES -> "square_bottle"
            Material.HONEY_BOTTLE -> "volumetric_bottle"
            else -> null
        }
    }

    private fun getBottleModel(material: Material): String? {
        return when (material) {
            Material.REDSTONE -> "volumetric_cap"
            Material.GLOWSTONE_DUST -> "short_cap"
            Material.GLOW_BERRIES -> "short_cap"
            Material.HONEY_BOTTLE -> "volumetric_cap"
            else -> null
        }
    }

    private fun createUpgradedPlusPotion(potion: ItemStack): ItemStack {
        if (potion.hasTag(ItemDataTags.IS_EXTENDED_PLUS)) return potion
        if (potion.hasTag(ItemDataTags.IS_UPGRADED_PLUS)) return potion
        val potionData = potion.getData(DataComponentTypes.POTION_CONTENTS) ?: return potion
        // Get all effects
        val rawEffects = potionData.potion()?.potionEffects ?: mutableListOf()
        val customEffects = potionData.customEffects()
        val allEffects = rawEffects + customEffects
        if (allEffects.isEmpty()) return potion
        // Create new effect list
        val newPotionData = PotionContents.potionContents().potion(PotionType.THICK)
        for (effect in allEffects) {
            newPotionData.addCustomEffect(PotionEffect(effect.type, (effect.duration * 0.6).toInt(), effect.amplifier + 1))
        }
        potion.setData(DataComponentTypes.POTION_CONTENTS, newPotionData)
        potion.addTag(ItemDataTags.IS_UPGRADED_PLUS)
        return potion
    }

    private fun createExtendedPlusPotion(potion: ItemStack): ItemStack {
        if (potion.hasTag(ItemDataTags.IS_EXTENDED_PLUS)) return potion
        if (potion.hasTag(ItemDataTags.IS_UPGRADED_PLUS)) return potion
        val potionData = potion.getData(DataComponentTypes.POTION_CONTENTS) ?: return potion
        // Get all effects
        val allEffects = (potionData.potion()?.potionEffects ?: mutableListOf()) + potionData.customEffects()
        if (allEffects.isEmpty()) return potion
        // Create new effect list
        val newPotionData = PotionContents.potionContents().potion(PotionType.THICK)
        for (effect in allEffects) {
            newPotionData.addCustomEffect(PotionEffect(effect.type, (effect.duration * 1.5).toInt(), effect.amplifier))
        }
        potion.setData(DataComponentTypes.POTION_CONTENTS, newPotionData)
        potion.addTag(ItemDataTags.IS_EXTENDED_PLUS)
        return potion
    }

    private fun createPotionVials(potion: ItemStack): ItemStack {
        if (potion.hasTag(ItemDataTags.IS_POTION_VIAL)) return potion
        val potionData = potion.getData(DataComponentTypes.POTION_CONTENTS) ?: return potion
        // Get all effects
        val allEffects = (potionData.potion()?.potionEffects ?: mutableListOf()) + potionData.customEffects()
        if (allEffects.isEmpty()) return potion
        // Create new effect list
        val newPotionData = PotionContents.potionContents().potion(PotionType.THICK)
        for (effect in allEffects) {
            newPotionData.addCustomEffect(PotionEffect(effect.type, (effect.duration * 0.4).toInt(), effect.amplifier))
        }
        potion.setData(DataComponentTypes.POTION_CONTENTS, newPotionData)
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 64)
        potion.addTag(ItemDataTags.IS_POTION_VIAL)
        potion.amount = 4
        return potion
    }

}
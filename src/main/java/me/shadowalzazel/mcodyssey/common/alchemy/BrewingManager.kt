@file:Suppress("UnstableApiUsage")

package me.shadowalzazel.mcodyssey.common.alchemy

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.PotionContents
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.RegistryTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.event.inventory.BrewEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionType

interface BrewingManager : RegistryTagManager, DataTagManager {

    fun customBrewingHandler(event: BrewEvent) {
        event.contents.ingredient ?: return
        brewedCustomPotion(event)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Sea Crystals -> Vials
    // Honey Bottle -> Extended+
    // Glow-berries -> Upgraded+
    // EXP Bottle -> Aura
    // Popped Chorus Fruit -> Concoctions

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
            if (!isPreviouslyEnhanced(item)) continue // Enhanced Potions can not be upgraded
            val potionData = item.getData(DataComponentTypes.POTION_CONTENTS) ?: continue
            // Create Enhanced Potions
            var createdEnhanced = true
            when(ingredient.type) {
                Material.GLOW_BERRIES ->  event.results[x] = createUpgradedPlusPotion(item) // Maybe move to Cauldron?
                Material.HONEY_BOTTLE ->  event.results[x] = createExtendedPlusPotion(item) // Maybe move to Cauldron?
                Material.EXPERIENCE_BOTTLE -> event.results[x] = createAuraPotion(item)
                Material.PRISMARINE_CRYSTALS ->  event.results[x] = createPotionVials(item)
                // Maybe create a sticky/spreading potion?
                else -> createdEnhanced = false
            }
            if (createdEnhanced) continue // Continue if created a new Enhanced Type
            // Get result
            var result = event.results[x] // Result != Item in Brewer
            result.setData(DataComponentTypes.MAX_STACK_SIZE, 16) // Set stack size
            // Effect Variables to control end result
            val hasBasePotion = potionData.potion() != null
            val hasCustomEffects = (potionData.customEffects().isNotEmpty())
            //val hasOdysseyPotionEffects = item.hasOdysseyEffectTag()
            val hasCustomModel = item.getData(DataComponentTypes.ITEM_MODEL) != null
            // Set Data from OLD item
            result = convertPotionType(result, getIngredientResult(ingredient))
            if (hasCustomEffects || hasBasePotion) {
                result.setData(DataComponentTypes.POTION_CONTENTS, potionData)
            }
            // Set Model
            if (!hasCustomModel) {
                result.updatePotionModel(bottleModel, capModel, "alchemy_potion")
            } else {
                result.updatePotionModel(bottleModel, capModel)
            }
            // TODO - Convert Odyssey Potions
            // Final
            event.results[x] = result
        }

    }

    /*-----------------------------------------------------------------------------------------------*/

    fun ItemStack.updatePotionModel(bottle: String?, cap: String?, newModel: String?=null) {
        // Create a new model
        if (newModel != null) {
            this.setData(DataComponentTypes.ITEM_MODEL, createOdysseyKey(newModel))
        }
        else if (this.getData(DataComponentTypes.ITEM_MODEL)?.value() != "alchemy_potion") {
            this.setData(DataComponentTypes.ITEM_MODEL, createOdysseyKey("alchemy_potion"))
        }
        // set custom data
        // TODO: Waiting for 1.21.4
    }

    private fun isPreviouslyEnhanced(potion: ItemStack): Boolean {
        //val hasPotionTag: (String) -> Boolean = { tag: String -> potion.hasTag(tag)}
        if (potion.hasTag(ItemDataTags.IS_EXTENDED_PLUS)) return false
        if (potion.hasTag(ItemDataTags.IS_UPGRADED_PLUS)) return false
        if (potion.hasTag(ItemDataTags.IS_POTION_VIAL)) return false
        if (potion.hasTag(ItemDataTags.IS_AURA_POTION)) return false
        else return true
    }

    private fun convertPotionType(potion: ItemStack, material: Material): ItemStack {
        val newPotion = potion.withType(material)
        return newPotion
    }

    /*
    private fun convertToLingeringPotion(potion: ItemStack): ItemStack {
        return convertPotionType(potion, Material.LINGERING_POTION)
    }

     */

    private fun getIngredientResult(ingredient: ItemStack): Material {
        // Get Result from Ingredient
        return when(ingredient.type) {
            Material.GUNPOWDER -> Material.SPLASH_POTION
            Material.DRAGON_BREATH -> Material.LINGERING_POTION
            else -> Material.POTION
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
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 16)
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
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 16)
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

    private fun createAuraPotion(potion: ItemStack): ItemStack {
        // Create AURA POTION when near, applies effects, GOOD or BAD (mini-beacon)
        if (potion.hasTag(ItemDataTags.IS_AURA_POTION)) return potion
        val potionData = potion.getData(DataComponentTypes.POTION_CONTENTS) ?: return potion
        // Get all effects
        val allEffects = (potionData.potion()?.potionEffects ?: mutableListOf()) + potionData.customEffects()
        if (allEffects.isEmpty()) return potion
        // Create new effect list
        val newPotionData = PotionContents.potionContents().potion(PotionType.THICK)
        for (effect in allEffects) {
            newPotionData.addCustomEffect(PotionEffect(effect.type, (effect.duration * 0.6).toInt(), effect.amplifier))
        }
        potion.setData(DataComponentTypes.POTION_CONTENTS, newPotionData)
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 16)
        potion.addTag(ItemDataTags.IS_AURA_POTION)
        potion.amount = 4
        return potion
    }

}
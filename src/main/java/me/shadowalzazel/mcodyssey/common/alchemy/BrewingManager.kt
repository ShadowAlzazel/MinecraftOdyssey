@file:Suppress("UnstableApiUsage")

package me.shadowalzazel.mcodyssey.common.alchemy

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Consumable
import io.papermc.paper.datacomponent.item.CustomModelData
import io.papermc.paper.datacomponent.item.PotionContents
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.RegistryTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
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
            val input = brewerSlots[x]?.clone() ?: continue
            if (input.type == Material.AIR) continue
            val oldPotionData = input.getData(DataComponentTypes.POTION_CONTENTS) ?: continue
            // Create Enhanced Potions
            var enhancedRecipe = true
            if (inputIsGeneric(input)) { // Enhanced Potions can not be upgraded
                when(ingredient.type) {
                    Material.GLOW_BERRIES ->  event.results[x] = createUpgradedPlusPotion(input) // Maybe move to Cauldron?
                    Material.HONEY_BOTTLE ->  event.results[x] = createExtendedPlusPotion(input) // Maybe move to Cauldron?
                    Material.EXPERIENCE_BOTTLE -> event.results[x] = createAuraPotion(input)
                    Material.PRISMARINE_CRYSTALS ->  {
                        event.results[x] = createPotionVials(input)
                        continue
                    }
                    // Maybe create a sticky/spreading potion?
                    else -> enhancedRecipe = false
                }
                // Update models for non vials
                if (enhancedRecipe) { // Continue if created a new Enhanced Type
                    event.results[x].updatePotionModel(bottleModel, capModel)
                    continue
                }
            }
            // Get result
            var result = event.results[x] // Result != Input item in Brewer
            // Checking for potion data and effects
            val hadBasePotion = oldPotionData.potion() != null
            val hadCustomEffects = (oldPotionData.customEffects().isNotEmpty())
            val hadCustomModel = input.getData(DataComponentTypes.ITEM_MODEL) != null
            //val hasOdysseyPotionEffects = item.hasOdysseyEffectTag() -> eventually add support for odyssey effects
            if (result.hasTag(ItemDataTags.IS_POTION_VIAL)) {
                // Update Model
                continue
            }
            // Set stack size
            result.setData(DataComponentTypes.MAX_STACK_SIZE, 16)
            // Set Data from OLD item
            if (hadCustomEffects) {
                result = convertPotionType(result, getIngredientResult(ingredient))
                result.setData(DataComponentTypes.POTION_CONTENTS, oldPotionData)
                // TODO: Do Non-vanilla upgrades
            }
            // Just set model for base potion
            if (hadBasePotion) {
                result.updatePotionModel(bottleModel, capModel)
            }
            // Set Model
            if (!hadCustomModel) {
                result.updatePotionModel(bottleModel, capModel, "alchemy_potion")
            } else {
                result.updatePotionModel(bottleModel, capModel)
            }
            // Final
            event.results[x] = result
        }

    }

    /*-----------------------------------------------------------------------------------------------*/

    fun ItemStack.updatePotionModel(bottle: String?, cap: String?, newModel: String?=null) {
        // TODO -> add models for splash and lingering
        // Create a new model
        if (newModel != null) {
            this.setData(DataComponentTypes.ITEM_MODEL, createOdysseyKey(newModel))
        }
        else if (this.getData(DataComponentTypes.ITEM_MODEL)?.value() != "alchemy_potion") {
            this.setData(DataComponentTypes.ITEM_MODEL, createOdysseyKey("alchemy_potion"))
        }
        // set custom data
        val oldModelData = this.getData(DataComponentTypes.CUSTOM_MODEL_DATA)
        val potionParts = oldModelData?.strings()?.toMutableList() ?: mutableListOf("alchemy_potion", "bottle", "cap")
        // Set
        if (bottle != null) potionParts[1] = bottle
        if (cap != null) potionParts[2] = cap
        val customData = CustomModelData.customModelData().addStrings(potionParts)
        if (oldModelData != null) { // Copy from ol
            customData.addFlags(oldModelData.flags())
            customData.addFloats(oldModelData.floats())
            customData.addColors(oldModelData.colors())
        }
        this.setData(DataComponentTypes.CUSTOM_MODEL_DATA, customData)
    }

    private fun inputIsGeneric(potion: ItemStack): Boolean {
        //val hasPotionTag: (String) -> Boolean = { tag: String -> potion.hasTag(tag)}
        if (potion.hasTag(ItemDataTags.IS_EXTENDED_PLUS)) return false
        if (potion.hasTag(ItemDataTags.IS_UPGRADED_PLUS)) return false
        if (potion.hasTag(ItemDataTags.IS_POTION_VIAL)) return false
        if (potion.hasTag(ItemDataTags.IS_AURA_POTION)) return false
        return true
    }

    private fun convertPotionType(potion: ItemStack, material: Material): ItemStack {
        val newPotion = potion.withType(material)
        return newPotion
    }

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
            Material.REDSTONE -> "tall_cap"
            Material.GLOWSTONE_DUST -> "short_cap"
            Material.GLOW_BERRIES -> "short_cap"
            Material.HONEY_BOTTLE -> "tall_cap"
            Material.EXPERIENCE_BOTTLE -> "ring_cap"
            else -> null
        }
    }

    private fun getBottleModel(material: Material): String? {
        return when (material) {
            Material.REDSTONE -> "volumetric_bottle"
            Material.GLOWSTONE_DUST -> "square_bottle"
            Material.GLOW_BERRIES -> "square_bottle"
            Material.HONEY_BOTTLE -> "volumetric_bottle"
            Material.EXPERIENCE_BOTTLE -> "aura_bottle"
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
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 8)
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
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 8)
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
        val consumable = Consumable.consumable().consumeSeconds(0.8F)
        consumable.sound(Key.key("entity.generic.drink"))
        potion.setData(DataComponentTypes.CONSUMABLE, consumable)
        potion.setData(DataComponentTypes.POTION_CONTENTS, newPotionData)
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 64)
        potion.setData(DataComponentTypes.ITEM_MODEL, createOdysseyKey("potion_vial"))
        potion.setData(DataComponentTypes.ITEM_NAME, Component.text("potion_vial")
            .decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false))
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
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 8)
        potion.setData(DataComponentTypes.ITEM_NAME, Component.text("aura_potion")
            .decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false))
        potion.addTag(ItemDataTags.IS_AURA_POTION)
        return potion
    }

}
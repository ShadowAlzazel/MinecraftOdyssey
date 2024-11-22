package me.shadowalzazel.mcodyssey.alchemy.base

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.items.Potions.createPotionStack
import me.shadowalzazel.mcodyssey.common.items.OdysseyItem
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

class AlchemyCauldronRecipe(
    private val alchemyResult: OdysseyItem,
    private val countedItems: Int,
    private val viableFuel: List<Material>,
    private val specificIngredients: List<ItemStack>, // For Materials and Specific Items
    private val isCombination: Boolean = false,
    private val combinationEffects: List<PotionEffectType> = listOf(), // For Detecting Potion Combinations
    private val comboOdysseyEffectList: List<String> = listOf() // For Detecting Odyssey Effect Tags
) : DataTagManager {

    // ??? Do mini-game
    // when hear ding, add chorus fruit for +10%

    // Validate the Cauldron Recipe
    fun validateRecipe(ingredients: MutableCollection<Item>, fuel: Material): Boolean {
        if (ingredients.size != countedItems) return false
        if (fuel !in viableFuel) return false
        // Filter Ingredients
        // Check and find specific ingredients like (Uncraftable Potions, OdysseyItems, or Specific Tagged)
        // Get specific potions if required
        val filteredIngredients = ingredients.filter { specificIngredientFinder(it.itemStack) }
        // Check specific items against ingredients
        if (filteredIngredients.size != specificIngredients.size) return false
        // Fore all filtered ingredients, if not in specific list return
        if (filteredIngredients.any { it.itemStack !in specificIngredients && !isAwkward(it.itemStack)}) return false
        // Get Potion With Effects for preset concoctions
        if (combinationEffects.isNotEmpty()) {
            val potionList = ingredients.filter { it.itemStack.checkEffectsIfInCombo() } // Check potions against combo list
            if (combinationEffects.size != potionList.size) return false
        }
        // Get Odyssey Effect with tag
        if (comboOdysseyEffectList.isNotEmpty()) {
            val odysseyList = ingredients.filter { it.itemStack.hasOdysseyEffect() }
            if (odysseyList.size != comboOdysseyEffectList.size) return false
        }
        // For Alchemy Combinations
        if (combinationEffects.isEmpty() && isCombination) {
            val ingredientPotions = ingredients.filter { it.itemStack.hasPotionEffects() }
            //ingredientPotions.forEach { println(it.itemStack) }
            // Can not combine concoctions
            if (ingredientPotions.any { it.itemStack.hasTag(ItemDataTags.IS_ALCHEMY_COMBINATION) }) return false
            // Check if over counted
            if (filteredIngredients.size + ingredientPotions.size != countedItems) return false
        }
        // Passed All Sentries
        return true
    }

    // Called After Async Recipe Check Success
    fun synchroSuccessHandler(ingredients: MutableCollection<Item>) {
        val location = ingredients.elementAt(0).location.clone()
        location.world.playSound(location, Sound.ITEM_BOTTLE_FILL, 2.5F, 0.8F)
        val result = if (alchemyResult is OdysseyPotion) {
            alchemyResult.createPotionStack()
        } else {
            alchemyResult.newItemStack()
        }
        // Check if is Combination
        if (isCombination) {
            val potionList = ingredients.filter { it.itemStack.hasPotionEffects() }
            val resultMeta = result.itemMeta as PotionMeta
            // Check if Preset Concoction
            val isPreset = combinationEffects.isNotEmpty()
            val concentration = if (isPreset) { 1.25 } else { maxOf(1.4 - (potionList.size * 0.2), 0.2) }
            val colors: MutableList<Color> = mutableListOf()
            // Apply potion effects
            for (item in potionList) {
                val potionStack = item.itemStack
                val potionMeta = potionStack.itemMeta as PotionMeta
                val customEffects = potionMeta.customEffects
                val baseEffects = potionMeta.basePotionType?.potionEffects ?: listOf()
                //println("Custom:  $customEffects")
                //println("Base:  $baseEffects")
                val effectList = customEffects + baseEffects
                effectList.forEach {
                    val newEffect = PotionEffect(it.type, (it.duration * concentration).toInt() + 1, it.amplifier)
                    resultMeta.addCustomEffect(newEffect, true)
                }
                if (!isPreset) {
                    if (potionMeta.color != null) { colors.add(potionMeta.color!!) }
                }
            }
            // Item Meta
            result.itemMeta = resultMeta
            result.addTag(ItemDataTags.IS_ALCHEMY_COMBINATION)
        }
        // Remove Items
        for (item in ingredients) item.remove()
        val cauldron = location.block
        // Run new Task
        AlchemyCauldronTask(cauldron, result).runTaskTimer(Odyssey.instance, 0, 2)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Utility functions
    private fun isAwkward(item: ItemStack): Boolean {
        val meta = item.itemMeta
        return if (meta is PotionMeta) {
            meta.basePotionType == PotionType.AWKWARD
        }
        else {
            false
        }
    }

    private fun specificIngredientFinder(item: ItemStack): Boolean {
        val meta = item.itemMeta
        return if (meta is PotionMeta) { // Check if potion is specific
            if (isAwkward(item)) return true
            val isInSpecificList = item in specificIngredients
            isInSpecificList
        }
        else if (isCombination) { // If combination check
            val isInSpecificList = item in specificIngredients
            isInSpecificList
        }
        else {
            true
        }
    }

    private fun ItemStack.checkEffectsIfInCombo(): Boolean {
        // Get Basic meta
        if (type != Material.POTION) return false
        val meta = itemMeta
        if (meta !is PotionMeta) return false
        val basePotionType = meta.basePotionType ?: return false
        // Check if concoction
        val isConcoction = hasTag(ItemDataTags.IS_ALCHEMY_COMBINATION)
        if (isConcoction) return false
        // has effects
        val foundCustomEffect = meta.customEffects.any { it.type in combinationEffects } // Find at least one match
        val foundPotionData = basePotionType.potionEffects.any { it.type in combinationEffects }
        return foundPotionData || foundCustomEffect
    }

    private fun ItemStack.hasOdysseyEffect(): Boolean {
        if (itemMeta !is PotionMeta) return false
        val isOdysseyEffect = hasTag(ItemDataTags.IS_CUSTOM_EFFECT)
        return isOdysseyEffect
    }

    private fun ItemStack.hasPotionEffects(): Boolean {
        // Get Basic meta
        if (type != Material.POTION) return false
        val meta = itemMeta
        if (meta !is PotionMeta) return false
        val basePotionType = meta.basePotionType ?: return false
        // Check against potion type
        val hasPotionDataEffect = basePotionType.potionEffects.size > 0
        val hasMetaEffect = meta.customEffects.size > 0
        return hasPotionDataEffect || hasMetaEffect
    }

}
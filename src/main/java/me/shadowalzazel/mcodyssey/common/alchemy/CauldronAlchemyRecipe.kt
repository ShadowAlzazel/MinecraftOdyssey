package me.shadowalzazel.mcodyssey.common.alchemy

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.PotionContents
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.alchemy.utility.IngredientChoice
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect

@Suppress("UnstableApiUsage")
class CauldronAlchemyRecipe (
    private val result: ItemStack,
    private val itemCount: Int,
    private val fuels: List<Material>,
    private val ingredients: List<IngredientChoice>,
    private val isCombination: Boolean = false
) : DataTagManager {

    fun validateRecipe(itemEntities: MutableCollection<Item>, fuel: Material): Boolean {
        // Easy Checks
        if (ingredients.size != itemCount) return false
        if (fuel !in fuels) return false
        // Loop through all Items to find the ingredientChoice
        var matchedItems = 0
        itemEntities.forEach {
            for (choice in ingredients) {
                if (choice.validateIngredient(it.itemStack)) {
                    matchedItems += 1
                    break
                }
            }
        }
        // If matchedItems != ingredient choice not all items where validated
        if (ingredients.size != matchedItems) return false
        // Passed All Sentries
        return true
    }

    fun successHandler(itemEntities: MutableCollection<Item>) {
        val location = itemEntities.elementAt(0).location.clone()
        val items = mutableListOf<ItemStack>()
        for (entity in itemEntities) { items.add(entity.itemStack) }
        val item = result.clone()
        if (isCombination) {
            createCombination(items, item)
        }
        // Remove Items
        for (entity in itemEntities) entity.remove()
        val cauldron = location.block
        // Run new Task
        CauldronBrewTask(cauldron, item).runTaskTimer(Odyssey.instance, 0, 2)
    }

    private fun createCombination(items: List<ItemStack>, result: ItemStack) {
        val potions = items.filter { it.hasEffects() }
        val isPresetCombo = !ingredients.any { it is IngredientChoice.AnyEffectChoice }
        val concentration = if (!isPresetCombo) { 1.25 } else { maxOf(1.4 - (potions.size * 0.2), 0.2) }
        // Apply effects
        val builder = PotionContents.potionContents()
        for (potion in potions) {
            // Add effects from previous potions
            val potionData = potion.getPotionContents() ?: continue
            val rawEffects = potionData.potion()?.potionEffects ?: mutableListOf()
            val customEffects = potionData.customEffects()
            val allEffects = rawEffects + customEffects
            if (allEffects.isEmpty()) continue
            allEffects.forEach {
                builder.addCustomEffect(PotionEffect(it.type, (it.duration * concentration).toInt() + 1, it.amplifier))
            }
        }
        result.setData(DataComponentTypes.POTION_CONTENTS, builder)
        result.setTag(ItemDataTags.IS_ALCHEMY_COMBINATION)
    }

    private fun ItemStack.hasEffects(): Boolean {
        val potionData = this.getPotionContents() ?: return false
        return (potionData.potion()?.potionEffects?.isNotEmpty() == true || potionData.customEffects().isNotEmpty())
    }

    private fun ItemStack.getPotionContents(): PotionContents? {
        return this.getData(DataComponentTypes.POTION_CONTENTS)
    }


}
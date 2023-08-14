package me.shadowalzazel.mcodyssey.alchemy.base

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.addIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.addStringTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasTag
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

class AlchemyCauldronRecipe(
    private val potion: OdysseyPotion, // TODO: CHANGE TO NON ITEM STACK
    private val ingredientSize: Int,
    private val viableFuel: List<Material>,
    private val specificList: List<ItemStack>, // For Materials and Specific Items
    private val combinationPotionDataList: List<PotionEffectType> = listOf(), // For Detecting Potion Combinations
    private val combinationOdysseyEffectList: List<String> = listOf(), // For Detecting Odyssey Effect Tags
    private val isCombination: Boolean = false
) {

    // Validate the Cauldron Recipe
    fun ingredientValidateHandler(ingredients: MutableCollection<Item>, fuel: Material): Boolean {
        if (ingredients.size != ingredientSize) return false
        if (fuel !in viableFuel) return false
        // Specific Ingredients Are (Uncraftable Potions, OdysseyItems, or Specific Tagged)
        val specificIngredients = ingredients.filter { it.itemStack.specificIngredientFinder() }
        val anyNotInList = specificIngredients.any { it.itemStack !in specificList }
        if (anyNotInList) return false
        // Get Potion With Effects
        val potionList = ingredients.filter { it.itemStack.potionDataFinder() }
        if (combinationPotionDataList.size != potionList.size) return false
        // Get Odyssey Effect with tag
        val odysseyList = ingredients.filter { it.itemStack.odysseyEffectFinder() }
        if (odysseyList.size != combinationOdysseyEffectList.size) return false
        return true
    }

    // Called After Async Recipe Check Success
    fun synchroSuccessHandler(ingredients: MutableCollection<Item>) {
        val location = ingredients.elementAt(0).location.clone()
        location.world.playSound(location, Sound.ITEM_BOTTLE_FILL, 2.5F, 0.8F)
        // Remove Items
        for (item in ingredients) item.remove()

        val cauldron = location.block
        val result = potion.createPotionStack() // TODO: DO SOMETHING HERE FOR MUNDANE?
        AlchemyCauldronTask(cauldron, result).runTaskTimer(Odyssey.instance, 0, 2)
    }

    // WHEN CREATING TAKE AVERAGE OF RGB C  OLOR
    // Maybe add in recipe, is combination?

    /*-----------------------------------------------------------------------------------------------*/

    internal fun OdysseyPotion.createPotionStack(): ItemStack {
        return createItemStack(1).apply {
            val potionMeta = itemMeta as PotionMeta
            if (potionColor != null) potionMeta.color = potionColor
            if (potionEffects != null && potionEffects.isNotEmpty()) {
                for (effect in potionEffects) { potionMeta.addCustomEffect(effect, true) }
            }
            if (isOdysseyEffect) {
                potionMeta.basePotionData = PotionData(PotionType.UNCRAFTABLE)
                addStringTag(ItemTags.ODYSSEY_EFFECT_TAG, odysseyEffectTag)
                addIntTag(ItemTags.ODYSSEY_EFFECT_TIME, odysseyEffectTimeInTicks) // Int
                addIntTag(ItemTags.ODYSSEY_EFFECT_AMPLIFIER, odysseyEffectAmplifier) // Int
            }
            else {

            }
            itemMeta = potionMeta
        }
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun ItemStack.specificIngredientFinder(): Boolean {
        if (itemMeta is PotionMeta) {
            if ((itemMeta as PotionMeta).basePotionData.type == PotionType.UNCRAFTABLE) return true
        }
        else {
            return true
        }
        // Add else if specific tag?
        return false
    }

    private fun ItemStack.potionDataFinder(): Boolean {
        if (itemMeta !is PotionMeta) return false
        val potionEffect = (itemMeta as PotionMeta).customEffects[0].type
        if (potionEffect !in combinationPotionDataList) return false
        return true
    }

    private fun ItemStack.odysseyEffectFinder(): Boolean {
        if (itemMeta !is PotionMeta) return false
        val isCustomEffect = hasTag(ItemTags.IS_CUSTOM_EFFECT)
        if (!isCustomEffect) return false
        return true
    }


}
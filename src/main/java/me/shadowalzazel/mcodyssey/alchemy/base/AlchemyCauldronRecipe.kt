package me.shadowalzazel.mcodyssey.alchemy.base

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.addTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasTag
import me.shadowalzazel.mcodyssey.items.Potions.createPotionStack
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
    private val potion: OdysseyPotion, // TODO: CHANGE TO NON ITEM STACK
    private val ingredientSize: Int,
    private val viableFuel: List<Material>,
    private val specificList: List<ItemStack>, // For Materials and Specific Items
    private val isCombination: Boolean = false,
    private val comboEffectTypeList: List<PotionEffectType> = listOf(), // For Detecting Potion Combinations
    private val comboOdysseyEffectList: List<String> = listOf() // For Detecting Odyssey Effect Tags
) {

    // Validate the Cauldron Recipe
    fun ingredientValidateHandler(ingredients: MutableCollection<Item>, fuel: Material): Boolean {
        if (ingredients.size != ingredientSize) return false
        if (fuel !in viableFuel) return false
        // Specific Ingredients Are (Uncraftable Potions, OdysseyItems, or Specific Tagged)
        val specificIngredients = ingredients.filter { it.itemStack.specificIngredientFinder() }
        if (specificIngredients.any { it.itemStack !in specificList }) return false
        if (specificIngredients.size != specificList.size) return false
        // Get Potion With Effects for preset concoctions
        if (comboEffectTypeList.isNotEmpty()) {
            val potionList = ingredients.filter { it.itemStack.potionDataFinder() }
            if (comboEffectTypeList.size != potionList.size) return false
        }
        // Get Odyssey Effect with tag
        if (comboOdysseyEffectList.isNotEmpty()) {
            val odysseyList = ingredients.filter { it.itemStack.odysseyEffectFinder() }
            if (odysseyList.size != comboOdysseyEffectList.size) return false
        }
        // For Alchemy Combinations
        if (comboEffectTypeList.isEmpty() && isCombination) {
            val ingredientPotions = ingredients.filter { it.itemStack.hasPotionEffectOrData() }
            ingredientPotions.forEach { println(it.itemStack) }
            // Can not combine concoctions -> MAYBE CHANGE THIS
            if (ingredientPotions.any { it.itemStack.hasTag(ItemTags.IS_ALCHEMY_COMBINATION) }) return false
            if (specificIngredients.size + ingredientPotions.size != ingredientSize) return false
        }
        // Passed All Sentries
        return true
    }

    // Called After Async Recipe Check Success
    fun synchroSuccessHandler(ingredients: MutableCollection<Item>) {
        val location = ingredients.elementAt(0).location.clone()
        location.world.playSound(location, Sound.ITEM_BOTTLE_FILL, 2.5F, 0.8F)
        val result = potion.createPotionStack()
        // Check if is Combination
        if (isCombination) {
            val potionList = ingredients.filter { it.itemStack.hasPotionEffectOrData() }
            val resultMeta = result.itemMeta as PotionMeta
            // Check if Preset Concoction
            val isPreset = comboEffectTypeList.isNotEmpty()
            val concentration = if (isPreset) { 1.25 } else { maxOf(1.4 - (potionList.size * 0.2), 0.2) }
            val colors: MutableList<Color> = mutableListOf()
            // Apply potion effects
            for (item in potionList) {
                val potion = item.itemStack
                val potionMeta = potion.itemMeta as PotionMeta
                val oldEffectList = if (potionMeta.hasCustomEffects()) { potionMeta.customEffects }
                else { listOf(potion.getEffectFromData()) }
                oldEffectList.forEach {
                    val newEffect = PotionEffect(it.type, (it.duration * concentration).toInt() + 10, it.amplifier)
                    resultMeta.addCustomEffect(newEffect, true)
                }
                if (!isPreset) {
                    if (potionMeta.color != null) { colors.add(potionMeta.color!!) }
                }
            }
            if (colors.isNotEmpty()) {
                var red = 0
                var green = 0
                var blue = 0
                colors.forEach {
                    red += it.red
                    green += it.green
                    blue += it.blue
                }
                val size = colors.size
                resultMeta.let {
                    it.color!!.red = blue / size
                    it.color!!.green = green / size
                    it.color!!.blue = blue / size
                }
            }
            result.itemMeta = resultMeta
            result.addTag(ItemTags.IS_ALCHEMY_COMBINATION)
        }
        // TODO: DO SOMETHING HERE FOR MUNDANE?
        // Remove Items
        for (item in ingredients) item.remove()
        val cauldron = location.block
        AlchemyCauldronTask(cauldron, result).runTaskTimer(Odyssey.instance, 0, 2)
    }
    // Maybe add in recipe, is combination?

    /*-----------------------------------------------------------------------------------------------*/

    private fun ItemStack.specificIngredientFinder(): Boolean {
        return if (itemMeta is PotionMeta) {
            //if ((itemMeta as PotionMeta).basePotionData.type == PotionType.UNCRAFTABLE) return true // MORE ROBUST?
            //if ((itemMeta as PotionMeta).basePotionData.type == PotionType.AWKWARD) return true
            (this in specificList)
        }
        // ADD ELSE IF FOR OTHER SPECIFICS
        else {
            true
        }
    }

    private fun ItemStack.potionDataFinder(): Boolean {
        if (type != Material.POTION) return false
        if (itemMeta !is PotionMeta) return false
        val isConcoction = hasTag(ItemTags.IS_ALCHEMY_COMBINATION)
        if (isConcoction) return false
        println("Is Not Concoction")
        println("Custom Effects: ${(itemMeta as PotionMeta).customEffects}")
        val foundCustomEffect = (itemMeta as PotionMeta).customEffects.any { it.type in comboEffectTypeList } // Find at least one match
        val foundPotionData = (itemMeta as PotionMeta).basePotionData.type.effectType in comboEffectTypeList
        return foundPotionData || foundCustomEffect
    }

    private fun ItemStack.odysseyEffectFinder(): Boolean {
        if (itemMeta !is PotionMeta) return false
        val isCustomEffect = hasTag(ItemTags.IS_CUSTOM_EFFECT)
        if (!isCustomEffect) return false
        return true
    }

    private fun ItemStack.hasPotionEffectOrData(): Boolean {
        if (type != Material.POTION) return false
        if (itemMeta !is PotionMeta) return false
        val hasPotionDataEffect = (itemMeta as PotionMeta).basePotionData.type.effectType != null
        val hasMetaEffect = (itemMeta as PotionMeta).customEffects.size > 0
        return hasPotionDataEffect || hasMetaEffect
    }

    private fun ItemStack.getEffectFromData(): PotionEffect {
        val potionData = (itemMeta as PotionMeta).basePotionData
        val baseTime: Int
        var extendedMultiplier: Double = 8.0 / 3.0
        var upgradedMultiplier: Double = 1.0 / 2.0
        var amplifier = if (potionData.isUpgraded) { 1 } else { 0 }
        when (potionData.type) {
            PotionType.TURTLE_MASTER -> {
                baseTime = 20 * 20
                upgradedMultiplier = 1.0
                extendedMultiplier = 2.0
            }
            PotionType.POISON, PotionType.REGEN -> {
                baseTime = 45 * 20
                extendedMultiplier = 2.0
            }
            PotionType.WEAKNESS, PotionType.SLOW_FALLING -> {
                baseTime = 90 * 20
            }
            PotionType.SLOWNESS -> {
                baseTime = 90 * 20
                upgradedMultiplier = 2.0 / 9.0
                if (potionData.isUpgraded) { amplifier = 3 }
            }
            PotionType.INSTANT_HEAL, PotionType.INSTANT_DAMAGE -> {
                baseTime = 0
            }
            else -> {
                baseTime = 3 * 60 * 20
            }
        }

        var time = baseTime.toDouble()
        if (potionData.isUpgraded) { time *= upgradedMultiplier }
        else if (potionData.isExtended) { time *= extendedMultiplier }
        return PotionEffect(potionData.type.effectType!!, time.toInt(), amplifier)
    }


}
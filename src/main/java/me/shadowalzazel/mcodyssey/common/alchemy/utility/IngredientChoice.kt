package me.shadowalzazel.mcodyssey.common.alchemy.utility

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.PotionContents
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

@Suppress("UnstableApiUsage")
sealed class IngredientChoice(open val amount: Int?) : DataTagManager {

    class MaterialChoice(val material: Material, amount: Int=1) : IngredientChoice(amount)
    class ItemNameChoice(val itemName: String, amount: Int=1) : IngredientChoice(amount)
    class SpecificChoice(val itemStack: ItemStack, amount: Int=1) : IngredientChoice(amount)
    class PotionEffectChoice(val effectType: PotionEffectType) : IngredientChoice(null)
    class PotionTypeChoice(val potionType: PotionType) : IngredientChoice(null)
    class AnyEffectChoice: IngredientChoice(null)
    class AnySingleEffectChoice: IngredientChoice(null)

    fun validateIngredient(item: ItemStack): Boolean {
        val validIngredient = when(this) {
            is MaterialChoice -> item.type == this.material
            is ItemNameChoice -> item.getItemIdentifier() == this.itemName
            is SpecificChoice -> item == this.itemStack
            is PotionEffectChoice -> this.validateItemEffect(item)
            is PotionTypeChoice -> getPotionContents(item)?.potion() == potionType
            is AnyEffectChoice -> hasEffects(item)
            is AnySingleEffectChoice -> getEffects(item)?.size == 1
        }
        val validAmount = if (this.amount != null) this.amount == item.amount else true
        return validIngredient && validAmount
    }


    private fun PotionEffectChoice.validateItemEffect(item: ItemStack): Boolean {
        val potionData = getPotionContents(item) ?: return false
        val allEffects = (potionData.potion()?.potionEffects ?: mutableListOf()) + potionData.customEffects()
        if (allEffects.isEmpty()) return false
        for (effect in allEffects) {
            if (effect.type == this.effectType) return true
        }
        return false
    }

    private fun getPotionContents(item: ItemStack): PotionContents? {
        return item.getData(DataComponentTypes.POTION_CONTENTS)
    }

    private fun getEffects(item: ItemStack): List<PotionEffect>? {
        val potionData = getPotionContents(item) ?: return null
        return (potionData.potion()?.potionEffects ?: listOf<PotionEffect>()) + potionData.customEffects()
    }

    private fun hasEffects(item: ItemStack): Boolean {
        val potionData = getPotionContents(item) ?: return false
        return (potionData.potion()?.potionEffects?.isNotEmpty() == true || potionData.customEffects().isNotEmpty())
    }

}

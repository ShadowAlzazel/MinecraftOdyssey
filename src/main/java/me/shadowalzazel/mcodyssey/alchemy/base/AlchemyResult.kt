package me.shadowalzazel.mcodyssey.alchemy.base

import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.addIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.addStringTag
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import org.bukkit.Color
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionType

class AlchemyResult(
    internal val potion: OdysseyItem,
    internal val potionColor: Color,
    internal val potionEffects: List<PotionEffect>? = null,
    internal val isOdysseyEffect: Boolean = false,
    internal val odysseyEffectTag: String = EffectTags.NO_EFFECT,
    internal val odysseyEffectTimeInTicks: Int = 0,
    internal val odysseyEffectAmplifier: Int = 0,
) {


    internal fun createPotionStack(): ItemStack {
        return potion.createItemStack(1).apply {
            val potionMeta = itemMeta as PotionMeta
            potionMeta.color = potionColor
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



}
package me.shadowalzazel.mcodyssey.alchemy.base

import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.items.utility.WeaponMaterial
import me.shadowalzazel.mcodyssey.items.utility.WeaponType
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionType

class OdysseyPotion(
    name: String,
    material: Material,
    displayName: Component? = null,
    lore: List<Component>? = null,
    customModel: Int? = null,
    internal val potionEffects: List<PotionEffect>? = null,
    internal val potionColor: Color? = null,
    internal val isOdysseyEffect: Boolean = false,
    internal val odysseyEffectTag: String = EffectTags.NO_EFFECT,
    internal val odysseyEffectTimeInTicks: Int = 0,
    internal val odysseyEffectAmplifier: Int = 0,
) : OdysseyItem(
    name = name,
    material = material,
    displayName = displayName,
    lore = lore,
    customModel = customModel,
    weaponMaterial = null,
    weaponType = null,
    enchantments = null) {

    fun createPotion(): ItemStack {
        val newPotion = this.createItemStack(1)
        if (potionEffects != null) {
            newPotion.itemMeta = (newPotion.itemMeta as PotionMeta).also {
                // Check if list not empty for non-custom effects; if empty then un-craft-able type
                if (potionEffects.isNotEmpty()) {
                    for (effect in potionEffects) { it.addCustomEffect(effect, true) }
                }
                else {
                    it.basePotionData = PotionData(PotionType.UNCRAFTABLE)
                }
                if (potionColor != null) {
                    it.color = potionColor
                }
            }
        }
        return newPotion
    }




}



package me.shadowalzazel.mcodyssey.alchemy.base

import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.common.items.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.potion.PotionEffect

class OdysseyPotion(
    name: String,
    material: Material,
    customName: String,
    lore: List<Component>? = null,
    customModel: Int? = null,
    internal val potionEffects: List<PotionEffect>? = null,
    internal val potionColor: Color? = null,
    internal val isOdysseyEffect: Boolean = false,
    internal val odysseyEffectTag: String = EffectTags.NO_EFFECT,
    internal val odysseyEffectTimeInTicks: Int = 0,
    internal val odysseyEffectAmplifier: Int = 1, // Starts at 1
) : OdysseyItem(
    itemName = name,
    overrideMaterial = material,
    customName = customName,
    lore = lore,
    customModel = customModel)
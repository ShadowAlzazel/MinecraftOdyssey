package me.shadowalzazel.mcodyssey.alchemy.base

import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.addIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.addStringTag
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionType

class OdysseyPotion(
    name: String,
    material: Material,
    displayName: Component? = null,
    lore: List<Component>? = null, // EMPTY LIST ->
    customModel: Int? = null,
    internal val potionEffects: List<PotionEffect>? = null,
    internal val potionColor: Color? = null,
    internal val isOdysseyEffect: Boolean = false,
    internal val odysseyEffectTag: String = EffectTags.NO_EFFECT,
    internal val odysseyEffectTimeInTicks: Int = 0,
    internal val odysseyEffectAmplifier: Int = 1, // Starts at 1
) : OdysseyItem(
    name = name,
    material = material,
    displayName = displayName,
    lore = lore,
    customModel = customModel,
    weaponMaterial = null,
    weaponType = null,
    enchantments = null) {

}



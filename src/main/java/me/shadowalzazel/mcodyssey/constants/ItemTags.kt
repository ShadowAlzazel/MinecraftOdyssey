package me.shadowalzazel.mcodyssey.constants

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.util.*


object ItemTags {

    // Seeds
    const val IS_ARCHAIC_SEED: String = "odyssey.is_archaic_seed"
    const val ARCHAIC_NAMESPACE: String = "odyssey.archaic_namespace"

    // Alchemy
    const val ALCHEMY_ARTILLERY_LOADED: String = "odyssey.alchemy_artillery_loaded"
    const val ALCHEMY_COPY_STORED: String = "odyssey.alchemy_copy_stored"
    const val ALCHEMICAL_AMMO_COUNT: String ="odyssey.alchemical_ammo_count"
    const val IS_ALCHEMY_COMBINATION: String = "odyssey.is_alchemy_combination" // For Alchemy Combinations/ Concoctions
    const val IS_CUSTOM_EFFECT: String = "odyssey.is_custom_effect"
    const val IS_ODYSSEY_EFFECT: String = "odyssey.is_odyssey_effect"

    // Custom Effects
    const val ODYSSEY_EFFECT_TIME: String = "odyssey.custom_effect_time" // Stores Int for ticks
    const val ODYSSEY_EFFECT_TAG: String = "odyssey.custom_effect_tag" // Stores String USE for Odyssey Effects
    const val ODYSSEY_EFFECT_AMPLIFIER: String = "odyssey.custom_effect_amplifier" // Stores Int

    // Potions
    const val POTION_CHARGES_LEFT: String = "odyssey.potion_charges_left" // NEEDS AN INT DATA TYPE
    const val IS_LARGE_POTION: String = "odyssey.is_large_potion" // Many Charges of same potency
    const val IS_POTION_VIAL: String = "odyssey.is_vial" // Multiple charges of lesser potency
    const val IS_EXTENDED_PLUS: String = "odyssey.is_extended_plus"
    const val IS_UPGRADED_PLUS: String = "odyssey.is_upgraded_plus"

    // Weapons
    const val AUTO_LOADER_LOADING: String = "odyssey.auto_loader_loading"

    // Engraving
    const val IS_ENGRAVED: String = "odyssey.is_engraved"
    const val ENGRAVED_BY: String = "engraved_by"

    // Tool Materials
    const val SOUL_STEEL_TOOL: String = "soul_steel_tool"
    const val NETHERITE_TOOL: String = "netherite_tool"

    // Equipment Classifications
    const val IS_KUNAI: String = "odyssey.is_kunai"
    const val IS_CHAKRAM: String = "odyssey.is_chakram"

    // Enchanting
    const val GILDED_SLOTS: String = "gilded_slots"
    const val ENCHANT_SLOTS: String = "enchant_slots"
    const val IS_SLOTTED: String = "odyssey.is_slotted"

    // Runes
    const val IS_RUNEWARE: String = "odyssey.is_runeware" // A finished runic vessel capable of holding multiple rune shards
    const val RUNEWARE_AUGMENT_COUNT: String = "odyssey.runeware_augment_count"
    const val IS_RUNESHERD: String = "odyssey.is_runesherd"
    const val HAS_RUNE_AUGMENT: String = "odyssey.has_rune_augment"

    // Compasses
    const val IS_SCULK_FINDER: String = "odyssey.is_sculk_finder"

    /*-----------------------------------------------------------------------------------------------*/

    fun PersistentDataContainer.hasOdysseyTag(): Boolean {
        return has(DataKeys.ITEM_KEY)
    }

    fun ItemStack.hasOdysseyItemTag(): Boolean {
        return itemMeta.persistentDataContainer.hasOdysseyTag()
    }

    fun ItemStack.getOdysseyTag(): String? {
        return itemMeta.persistentDataContainer[DataKeys.ITEM_KEY, PersistentDataType.STRING]
    }

    fun ItemStack.isThisItem(tag: String): Boolean {
        return this.getOdysseyTag() == tag
    }

    fun ItemStack.addTag(tag: String) {
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        itemMeta = itemMeta.also {
            it.persistentDataContainer.set(tagKey, PersistentDataType.BOOLEAN, true)
        }
    }

    fun ItemStack.hasTag(tag: String): Boolean {
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        return itemMeta.persistentDataContainer.has(tagKey)
    }

    fun ItemStack.removeTag(tag: String) {
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        itemMeta = itemMeta.also {
            it.persistentDataContainer.remove(tagKey)
        }
    }

    fun ItemStack.setIntTag(tag: String, count: Int) {
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        itemMeta = itemMeta.also {
            it.persistentDataContainer.set(tagKey, PersistentDataType.INTEGER, count)
        }
    }

    fun ItemStack.getIntTag(tag: String): Int? {
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        return itemMeta.persistentDataContainer[tagKey, PersistentDataType.INTEGER]
    }

    fun ItemStack.addStringTag(tag: String, text: String) {
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        itemMeta = itemMeta.also {
            it.persistentDataContainer.set(tagKey, PersistentDataType.STRING, text)
        }
    }

    fun ItemStack.getStringTag(tag: String): String? {
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        return itemMeta.persistentDataContainer[tagKey, PersistentDataType.STRING]
    }

    fun ItemStack.setUUIDTag(uuid: UUID) {
        itemMeta = itemMeta.also {
            it.persistentDataContainer.set(DataKeys.UUID_KEY, PersistentDataType.STRING, uuid.toString())
        }
    }

    fun ItemStack.getUUIDTag(): String {
        return itemMeta.persistentDataContainer[DataKeys.UUID_KEY, PersistentDataType.STRING] ?: UUID.randomUUID().toString()
    }

}
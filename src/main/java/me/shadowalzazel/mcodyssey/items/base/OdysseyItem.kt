package me.shadowalzazel.mcodyssey.items.base

import me.shadowalzazel.mcodyssey.constants.DataKeys
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.setIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.addTag
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.utility.WeaponMaterial
import me.shadowalzazel.mcodyssey.items.utility.WeaponType
import me.shadowalzazel.mcodyssey.listeners.EnchantingListeners.updateSlotLore
import me.shadowalzazel.mcodyssey.listeners.enchantment_listeners.MeleeListeners.isOdysseyEnchant
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType


open class OdysseyItem(
    val name: String,
    internal val material: Material,
    internal val displayName: Component? = null,
    internal val lore: List<Component>? = null,
    internal val customModel: Int? = null,
    internal val weaponMaterial: WeaponMaterial? = null,
    internal val weaponType: WeaponType? = null,
    internal val enchantments: MutableMap<Enchantment, Int>? = null
) {

    fun createItemStack(amount: Int): ItemStack {
        val itemStack = ItemStack(material, amount)
        // On item meta; Add lore, display name, custom model, damage stats, effects, color if applicable
        var enchantSlots = 0
        var gildedSlots = 0
        itemStack.itemMeta = (itemStack.itemMeta as ItemMeta).also {
            if (displayName != null) { it.displayName(displayName) }
            if (lore != null) { it.lore(lore) }
            if (customModel != null) { it.setCustomModelData(customModel) }
            if (enchantments != null) {
                for (enchant in enchantments) {
                    if (enchant.key.isOdysseyEnchant()) { gildedSlots += 1 } else { enchantSlots += 1 }
                    it.addEnchant(enchant.key, enchant.value, true)
                }
            }
            it.persistentDataContainer.set(DataKeys.ITEM_KEY, PersistentDataType.STRING, name)
        }
        if (enchantments != null) {
            itemStack.addTag(ItemTags.IS_SLOTTED)
            itemStack.setIntTag(ItemTags.ENCHANT_SLOTS, enchantSlots)
            itemStack.setIntTag(ItemTags.GILDED_SLOTS, gildedSlots)
            itemStack.updateSlotLore()
            itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
        return itemStack
    }


    // NOTES: Potions

    // CUSTOM AFFIXES
    // ITEM SLOT STATS?
    // ATK DAMAGE
    // HEALTH
    // MAKE POTION THAT CONVERTS POISON DAMAGE TO HEAL. THO TAKES DOUBLE DAMAGE FROM WITHER

    // flask_of_rose -> do damage when hit?
    // TO DO : -> when consume a custom model potion to keep the model
    // POTION OF FISHING
    // Make villagers killed from brew not affect your status

    // KERNEL POTION
    // BASIS FOR FLOWER POTION
    // WHEN COMBINED WITH FLOWER makes POTION THAT ACTS LIEK BONEMEAL BUT APPLIES FLOWER GROWTH/ NOISE ?!

    // Anchor - Yone E
    // Flashbang, Knock-Up, Freeze,

    // Necronomicon: Make Pages that can add to personal necronomicon using components
    // Original from Vail is to powerful

}
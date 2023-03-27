package me.shadowalzazel.mcodyssey.items.base

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.utility.WeaponMaterial
import me.shadowalzazel.mcodyssey.items.utility.WeaponType
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect


data class OdysseyItem(
    val name: String,
    internal val material: Material,
    internal val displayName: Component? = null,
    internal val lore: List<Component>? = null,
    internal val customModel: Int? = null,
    internal val weaponMaterial: WeaponMaterial? = null,
    internal val weaponType: WeaponType? = null,
    internal val potionEffects: List<PotionEffect>? = null,
    internal val potionColor: Color? = null) {

    // CUSTOM AFFIXES
    // ITEM SLOT STATS?
    // ATK DAMAGE
    // HEALTH


    fun createItemStack(amount: Int): ItemStack {
        val newItemStack = ItemStack(material, amount)
        // On item meta; Add lore, display name, custom model, damage stats, effects, color if applicable
        newItemStack.itemMeta = (newItemStack.itemMeta as ItemMeta).also {
            if (displayName != null) { it.displayName(displayName) }
            if (lore != null) { it.lore(lore) }
            if (customModel != null) { it.setCustomModelData(customModel) }
            //if (enchantments != null) { for (enchant in enchantments) { it.addEnchant(enchant.key, enchant.value, true) } }
            it.persistentDataContainer.set(NamespacedKey(Odyssey.instance, "item"), PersistentDataType.STRING, name)
        }
        return newItemStack
    }

    // NOTES: Potions

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
    // Original from Vail is to powerful to use

}
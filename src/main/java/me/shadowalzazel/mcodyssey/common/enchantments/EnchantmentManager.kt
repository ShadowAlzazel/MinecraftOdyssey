package me.shadowalzazel.mcodyssey.common.enchantments

import io.papermc.paper.datacomponent.DataComponentType
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemEnchantments
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import kotlin.collections.component1
import kotlin.collections.component2

@Suppress("UnstableApiUsage")
interface EnchantmentManager: DataTagManager {

    /*-- Shared component helpers ------------------------------------------------------------*/

    /** Which component this item stores its enchants in. Books use STORED_ENCHANTMENTS. */
    fun ItemStack.enchantComponent(): DataComponentType.Valued<ItemEnchantments> =
        if (type == Material.ENCHANTED_BOOK || type == Material.BOOK) DataComponentTypes.STORED_ENCHANTMENTS
        else DataComponentTypes.ENCHANTMENTS

    /**
     * Effective enchantments on an item as a plain map.
     * Merges normal gear (ENCHANTMENTS) and enchanted books (STORED_ENCHANTMENTS),
     * keeping the highest level if a key somehow appears in both.
     */

    fun ItemStack.readEnchants(): Map<Enchantment, Int> {
        val out = HashMap<Enchantment, Int>()
        getData(DataComponentTypes.ENCHANTMENTS)?.enchantments()?.forEach { (e, l) -> out[e] = l }
        getData(DataComponentTypes.STORED_ENCHANTMENTS)?.enchantments()?.forEach { (e, l) ->
            out.merge(e, l, ::maxOf)
        }
        return out
    }

    /*-- Simple boolean detectors ------------------------------------------------------------*/
    fun Enchantment.isOdysseyEnchantment(): Boolean {
        val key = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).getKey(this)
        return key?.namespace == "odyssey"
    }

    fun ItemStack.hasOdysseyEnchants(): Boolean {
        return this.enchantments.any { it.key.isOdysseyEnchantment() }
    }

    fun ItemStack.getOdysseyEnchantments(): Map<Enchantment, Int> {
        return this.enchantments.filter { it.key.isOdysseyEnchantment() }
    }

    fun Enchantment.getNameId(): String {
        return this.key.key
    }

    fun ItemStack.hasEnchantment(enchantment: Enchantment): Boolean {
        return this.enchantments.keys.contains(enchantment)
    }

    fun ItemStack.hasEnchantment(name: String): Boolean {
        return this.enchantments.keys.contains(getNamedEnchantment(name))
    }

    /*-- Registry getter methods ------------------------------------------------------------*/

    // Full method for getting an enchantment via name
    fun getNamedEnchantment(name: String): Enchantment? {
        var enchantment = getOdysseyEnchantmentFromString(name)
        if (enchantment != null) return enchantment
        enchantment = getMinecraftEnchantmentFromString(name)
        return enchantment
    }

    fun getOdysseyEnchantmentFromString(name: String): Enchantment? {
        val registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)
        return registry.get(NamespacedKey(Odyssey.instance, name))
    }

    fun getMinecraftEnchantmentFromString(name: String): Enchantment? {
        val registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)
        return registry.get(NamespacedKey.minecraft(name))
    }

    /*-- Custom property ------------------------------------------------------------*/

    // Does not contribute towards total Points
    fun ItemStack.addGildedEnchant(enchantment: Enchantment, newLevel: Int) {
        this.setStringTag(ItemDataTags.GILDED_ENCHANT, enchantment.getNameId())
        this.removeEnchantment(enchantment)
        this.addUnsafeEnchantment(enchantment, newLevel)
    }

    // Is Over the max
    fun ItemStack.addShinyEnchant(enchantment: Enchantment, newLevel: Int) {
        this.setStringTag(ItemDataTags.SHINY_ENCHANT, enchantment.getNameId())
        this.removeEnchantment(enchantment)
        this.addUnsafeEnchantment(enchantment, newLevel)
    }

}
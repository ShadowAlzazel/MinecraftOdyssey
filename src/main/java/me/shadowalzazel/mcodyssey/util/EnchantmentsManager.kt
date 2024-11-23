package me.shadowalzazel.mcodyssey.util

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

interface EnchantmentsManager: DataTagManager {

    /*-----------------------------------------------------------------------------------------------*/
    // Helper Functions
    fun Enchantment.isOdysseyEnchantment(): Boolean {
        val key = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).getKey(this)
        return key?.namespace == "odyssey"
    }

    fun Enchantment.enchantabilityCost(level: Int = 1): Int {
        return maxOf((this.anvilCost / 2), 1) * level
    }

    fun Enchantment.getNameId(): String {
        return this.key.key
    }

    fun ItemStack.hasOdysseyEnchants(): Boolean {
        return this.enchantments.any { it.key.isOdysseyEnchantment() }
    }

    fun ItemStack.hasEnchantment(enchantment: Enchantment): Boolean {
        return this.enchantments.keys.contains(enchantment)
    }

    fun ItemStack.hasEnchantment(name: String): Boolean {
        return this.enchantments.keys.contains(getNamedEnchantment(name))
    }

    fun ItemStack.getOdysseyEnchantments(): Map<Enchantment, Int> {
        return this.enchantments.filter { it.key.isOdysseyEnchantment() }
    }

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

    // Does not contribute towards total Points
    fun ItemStack.addGildedEnchant(enchantment: Enchantment, newLevel: Int) {
        this.addStringTag(ItemDataTags.GILDED_ENCHANT, enchantment.getNameId())
        this.removeEnchantment(enchantment)
        this.addUnsafeEnchantment(enchantment, newLevel)
    }

    // Is Over the max
    fun ItemStack.addShinyEnchant(enchantment: Enchantment, newLevel: Int) {
        this.addStringTag(ItemDataTags.SHINY_ENCHANT, enchantment.getNameId())
        this.removeEnchantment(enchantment)
        this.addUnsafeEnchantment(enchantment, newLevel)
    }

}
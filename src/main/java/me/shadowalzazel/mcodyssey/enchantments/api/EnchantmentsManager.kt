package me.shadowalzazel.mcodyssey.enchantments.api

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

interface EnchantmentsManager {

    /*-----------------------------------------------------------------------------------------------*/
    // Helper Functions
    fun Enchantment.isOdysseyEnchantment(): Boolean {
        val key = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).getKey(this)
        return key?.namespace == "odyssey"
    }

    fun Enchantment.enchantabilityCost(level: Int = 1): Int {
        return maxOf((this.anvilCost / 2), 1) * level
    }

    fun ItemStack.hasOdysseyEnchants(): Boolean {
        return this.enchantments.any { it.key.isOdysseyEnchantment() }
    }

    fun ItemStack.hasEnchantment(enchantment: Enchantment): Boolean {
        return this.enchantments.keys.contains(enchantment)
    }

    fun ItemStack.getOdysseyEnchantments(): Map<Enchantment, Int> {
        return this.enchantments.filter { it.key.isOdysseyEnchantment() }
    }

    fun getOdysseyEnchantmentFromString(name: String): Enchantment? {
        val registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)
        return registry.get(NamespacedKey(Odyssey.instance, name))
    }

    fun getMinecraftEnchantmentFromString(name: String): Enchantment? {
        val registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)
        return registry.get(NamespacedKey.minecraft(name))
    }

}
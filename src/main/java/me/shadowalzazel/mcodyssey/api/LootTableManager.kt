package me.shadowalzazel.mcodyssey.api

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.RegistryTagManager
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.LootTable as NmsLootTable
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.CraftLootTable
import org.bukkit.craftbukkit.util.CraftNamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootContext
import org.bukkit.loot.LootTable
import java.util.*

object LootTableManager : RegistryTagManager {

    private fun newLootKey(name: String, namespace: String): ResourceKey<NmsLootTable> {
        val nameKey = NamespacedKey(namespace, name)
        return ResourceKey.create(Registries.LOOT_TABLE, CraftNamespacedKey.toMinecraft(nameKey))
    }


    // Returns 1 item from a loot table
    private fun singleItemFromResource(name: String, namespace: String): ItemStack {
        val lootTableKey: ResourceKey<NmsLootTable> = newLootKey(name, namespace)
        // Using `type:chest`
        val lootTable = CraftLootTable.minecraftToBukkit(lootTableKey)
        val contextBuilder = LootContext.Builder(Odyssey.instance.overworld.spawnLocation).apply {
            lootedEntity(null)
            luck(0F)
            killer(null)
        }
        val context = contextBuilder.build()
        val items = lootTable.populateLoot(Random(0L), context)
        require(items.isNotEmpty())
        return items.first()
    }

    private fun getItemsFromResource(name: String, namespace: String): MutableCollection<ItemStack> {
        val lootTableKey: ResourceKey<NmsLootTable> = newLootKey(name, namespace)
        // Using `type:chest`
        val lootTable = CraftLootTable.minecraftToBukkit(lootTableKey)
        val contextBuilder = LootContext.Builder(Odyssey.instance.overworld.spawnLocation).apply {
            lootedEntity(null)
            luck(0F)
            killer(null)
        }
        val context = contextBuilder.build()
        val items = lootTable.populateLoot(Random(0L), context)
        return items
    }

    // Creates an item from the odyssey:item/* loot table
    fun createItemStackFromLoot(name: String): ItemStack {
        return singleItemFromResource("item/$name", "odyssey")
    }

    fun getResourceLootTable(name: String, namespace: String = "odyssey"): LootTable? {
        val lootTableKey: ResourceKey<NmsLootTable> = newLootKey(name, namespace)
        val lootTable = CraftLootTable.minecraftToBukkit(lootTableKey)
        return lootTable
    }

    fun getOdysseyLoot(name: String): ItemStack {
        return singleItemFromResource(name, "odyssey")
    }

    fun getMinecraftLoot(name: String): ItemStack {
        return singleItemFromResource(name, "minecraft")
    }

    fun getCustomLoot(name: String, namespace: String): ItemStack {
        return singleItemFromResource(name, namespace)
    }

}
package me.shadowalzazel.mcodyssey.api

import me.shadowalzazel.mcodyssey.Odyssey
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
        val random = Random((0L..100000L).random())
        val items = lootTable.populateLoot(random, context)
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

    /**
     * This creates an itemStack from the odyssey:item/. loot table.
     *
     * @param name The name of the loot table which should be the Item.
     * @return The loot item into ItemStack form.
     */
    fun createItemStackFromItemTable(name: String): ItemStack {
        return singleItemFromResource("item/$name", "odyssey")
    }

    /**
     * This creates a bukkit LootTable to use from a loot namespace.
     *
     * @param name The name of the loot table which should be the Item.
     * @param namespace The namespace to look for the loot. Defaults to "odyssey".
     * @return The bukkit representation of the loot table.
     */
    fun getResourceLootTable(name: String, namespace: String = "odyssey"): LootTable? {
        val lootTableKey: ResourceKey<NmsLootTable> = newLootKey(name, namespace)
        val lootTable = CraftLootTable.minecraftToBukkit(lootTableKey)
        return lootTable
    }

    /**
     * This creates an itemStack from the odyssey namespace loot table.
     *
     * e.g. name="chests/sunken_library/common", namespace="odyssey" generates an item
     * from that loot table.
     *
     * @param name The name of the loot table to get the item from.
     * @param namespace The namespace to look for the loot. Defaults to "odyssey".
     * @return The loot item into ItemStack form.
     */
    fun generateItemFromLootTable(name: String, namespace: String = "odyssey"): ItemStack {
        return singleItemFromResource(name, namespace)
    }

    private fun getMinecraftLoot(name: String): ItemStack {
        return singleItemFromResource(name, "minecraft")
    }

    private fun getCustomLoot(name: String, namespace: String): ItemStack {
        return singleItemFromResource(name, namespace)
    }

}
package me.shadowalzazel.mcodyssey.api

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.NamedKeys
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.LootTable
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.CraftLootTable
import org.bukkit.craftbukkit.util.CraftNamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootContext
import java.util.Random

object LootTableManager {

    val toLootKey: (String) -> ResourceKey<LootTable> = {
        name: String -> ResourceKey.create(Registries.LOOT_TABLE, CraftNamespacedKey.toMinecraft(NamedKeys.newKey(name)))
    }
    val toMinecraftKey: (String) -> ResourceKey<LootTable> = {
        name: String -> ResourceKey.create(Registries.LOOT_TABLE, CraftNamespacedKey.toMinecraft(NamespacedKey.minecraft(name)))
    }

    //val testItem = toLootKey("test")
    //val bastionItem = toMinecraftKey("chests/bastion_treasure")

    // Returns 1 item from a loot table
    private fun singleItemFromResource(resourceKey: ResourceKey<LootTable>): ItemStack {
        val lootTable = CraftLootTable.minecraftToBukkit(resourceKey)
        val builder = LootContext.Builder(Odyssey.instance.overworld.spawnLocation)
        val items = lootTable.populateLoot(Random(1L), builder.build())
        require(items.isNotEmpty())
        return items.first()
    }

    fun createItemStackFromLoot(name: String): ItemStack {
        return singleItemFromResource(toLootKey(name))
    }

    fun getOdysseyLoot(name: String): ItemStack {
        return singleItemFromResource(toLootKey(name))
    }

    fun getMinecraftLoot(name: String): ItemStack {
        return singleItemFromResource(toMinecraftKey(name))
    }

}
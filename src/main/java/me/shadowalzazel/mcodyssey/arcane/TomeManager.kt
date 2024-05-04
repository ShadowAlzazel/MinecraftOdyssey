package me.shadowalzazel.mcodyssey.arcane

import me.shadowalzazel.mcodyssey.enchantments.api.EnchantSlotManager
import me.shadowalzazel.mcodyssey.items.Miscellaneous
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.Repairable

internal interface TomeManager : EnchantSlotManager {

    /*-----------------------------------------------------------------------------------------------*/

    fun tomeOfDischargeOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack {
        if (!item.isSlotted()) {
            viewers.forEach { it.sendBarMessage("This item needs to be slotted to use this tome.") }
            return ItemStack(Material.AIR)
        }
        val meta = item.itemMeta
        val hasStoredEnchants = meta is EnchantmentStorageMeta && meta.storedEnchants.isEmpty()
        val hasOdysseyEnchants = item.hasOdysseyEnchants()
        if (!meta.hasEnchants() && !hasStoredEnchants && !hasOdysseyEnchants) {
            viewers.forEach { it.sendBarMessage("The item needs to be enchanted to use this tome.") }
            return ItemStack(Material.AIR)
        }
        val containers = item.getEnchantmentContainers().toList()
        val randomContainer = containers.random()
        item.removeEnchantViaContainer(randomContainer.first)
        // Remove Enchantment
        //val enchantments = if (meta is EnchantmentStorageMeta) { meta.storedEnchants } else { meta.enchants }
        //val odysseyEnchantments = item.getOdysseyEnchantments()
        /*
        val containerPairs = item.getEnchantmentContainers().toList().toMutableList()
        val randomInt = (0..<containerPairs.size).random()
        containerPairs.removeAt(randomInt)
        val enchantContainers = containerPairs.toList().toMap()
        // Call set from containers
        item.setEnchantmentsFromContainer(enchantContainers)
         */
        item.updateSlotLore()
        return item
    }

    fun tomeOfEmbraceOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack {
        if (!item.isSlotted()) {
            viewers.forEach { it.sendBarMessage("This item needs to be slotted to use this tome.") }
            return ItemStack(Material.AIR)
        }
        // Add Slot
        return item.apply {
            addEnchantSlot()
            updateSlotLore()
        }
    }

    private fun tomeOfHarmonyOnItem(item: ItemStack): ItemStack {
        return item.apply {
            itemMeta = itemMeta.also {
                if (it is Repairable) it.repairCost = 1
            }
        }
    }

    private fun tomeOfReplicationOnBook(item: ItemStack): ItemStack {
        return if (item.itemMeta.hasEnchants() || (item.itemMeta as EnchantmentStorageMeta).hasStoredEnchants()) {
            item.clone()
        } else {
            Miscellaneous.TOME_OF_REPLICATION.newItemStack(1)
        }
    }

}
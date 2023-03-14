package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.ItemModels
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.inventory.ItemStack

object EnchantingListeners {


    @EventHandler
    fun enchantingItemHandler(event: EnchantItemEvent) {

        when (event.item.type) {
            Material.BOOK -> {

            }
            else -> {

            }
        }
    }



    private fun enchantingBookHandler(eventItem: ItemStack) {
        when(eventItem.itemMeta.customModelData) {
            ItemModels.VOLUME_OF_TOOLS -> {
                eventItem.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_SWORD, 1)) }
            }
            ItemModels.VOLUME_OF_AXES -> {
                eventItem.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_AXE, 1)) }
            }
            ItemModels.VOLUME_OF_SWORDS -> {
                eventItem.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_SWORD, 1)) }
            }
            ItemModels.VOLUME_OF_POLE_ARMS -> {
                eventItem.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_HOE, 1)) }
            }
            ItemModels.VOLUME_OF_SPEARS -> {
                eventItem.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_SHOVEL, 1)) }
            }
            ItemModels.VOLUME_OF_CLUBS -> { // CLubs
                eventItem.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_PICKAXE, 1)) }
            }
        }
    }


    private fun getApplicableEnchants(eventItem: ItemStack, type: Material): Map<Enchantment, Int> {
        // Special Parameters
        val applicableList = eventItem.enchantments.filter { it.key.canEnchantItem(ItemStack(type, 1)) }
        val rerollList = eventItem.enchantments.filter { !it.key.canEnchantItem(ItemStack(type, 1)) }

        return applicableList
    }



}
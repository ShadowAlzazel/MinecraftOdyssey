package me.shadowalzazel.mcodyssey.common.listeners.enchantment_listeners

import me.shadowalzazel.mcodyssey.util.EnchantmentManager
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable


object MiscListeners : Listener, EnchantmentManager {


    @EventHandler
    fun itemDamageHandler(event: PlayerItemDamageEvent) {
        if (!event.item.hasItemMeta()) return
        if (!event.item.itemMeta.hasEnchants()) return
        val item = event.item
        for (enchant in item.enchantments) {
            when(enchant.key.getNameId()) {
                "moonpatch" -> {
                    //moonpatchEnchantment(event)
                }
                "parasitic_curse" -> {
                    parasiticCurseEnchantment(event, enchant.value)
                }
            }
        }

    }

    @EventHandler
    private fun healthRegenHandler(event: EntityRegainHealthEvent) {
        if (event.entity !is LivingEntity) return
        val entity = event.entity as LivingEntity
        val equipment = entity.equipment ?: return

        with(equipment) {
            if (itemInMainHand.hasEnchantment("chitin")) {
                chitinEnchantment(itemInMainHand, event.amount)
            }
            if (itemInOffHand.hasEnchantment("chitin")) {
                chitinEnchantment(itemInOffHand, event.amount)
            }
            if (helmet != null && helmet.hasEnchantment("chitin")) {
                chitinEnchantment(helmet, event.amount)
            }
            if (chestplate != null && chestplate.hasEnchantment("chitin")) {
                chitinEnchantment(chestplate, event.amount)
            }
            if (leggings != null && leggings.hasEnchantment("chitin")) {
                chitinEnchantment(leggings, event.amount)
            }
            if (boots != null && boots.hasEnchantment("chitin")) {
                chitinEnchantment(boots, event.amount)
            }
        }

    }

    private fun chitinEnchantment(item: ItemStack, amount: Double) {
        val meta = item.itemMeta
        if (meta !is Damageable) return
        if (!meta.hasDamage()) return
        val maxDurability = if (meta.hasMaxDamage()) {
            meta.maxDamage
        } else {
            400
        }
        val repairAmount = maxDurability * 0.01 * amount
        meta.damage = maxOf(meta.damage - 1 - (repairAmount).toInt(), 0)
        item.itemMeta = meta
        return
    }


    @EventHandler
    fun playerDeathHandler(event: PlayerDeathEvent) {
        val recallItems = mutableListOf<ItemStack>()

        for (item in event.drops) {
            if (!item.hasItemMeta()) continue
            if (!item.itemMeta.hasEnchants()) continue
            // Check
            if (item.hasEnchantment("fealty")) {
                recallItems.add(item)
            }
        }

        if (recallItems.size <= 0) return
        for (item in recallItems) {
            event.drops.remove(item)
            event.itemsToKeep.add(item)
        }
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun parasiticCurseEnchantment(event: PlayerItemDamageEvent, level: Int) {
        val rolled = (level * 10 > (0..100).random())
        if (!rolled) return
        //val itemDamage = event.damage
        event.damage = maxOf(event.damage - level, 0)
        event.player.damage(1.0)
    }



}
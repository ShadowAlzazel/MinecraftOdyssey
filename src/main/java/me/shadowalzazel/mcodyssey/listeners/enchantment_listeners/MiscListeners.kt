package me.shadowalzazel.mcodyssey.listeners.enchantment_listeners

import io.papermc.paper.world.MoonPhase
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.enchantments.api.EnchantmentsManager
import me.shadowalzazel.mcodyssey.listeners.utility.MoonwardPhase
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.inventory.ItemStack
import java.util.*

object MiscListeners : Listener, EnchantmentsManager {

    private val moonpatchPlayers = mutableListOf<UUID>()

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
    private fun moonpatchEnchantment(event: PlayerItemDamageEvent) {
        if (!event.player.world.hasSkyLight()) return
        if (event.player.world.isDayTime) return
        if (event.player.world.moonPhase == MoonPhase.NEW_MOON) return
        if (event.player.location.block.lightFromSky < 8) return
        event.damage = maxOf(event.damage - 1, 0)
        // TODO: TO EXPENSIVE TO RUN EVERY TIME MOVE LATER
        if (!moonpatchPlayers.contains(event.player.uniqueId)) {
            moonpatchPlayers.add(event.player.uniqueId)
            val moonwardPhase = MoonwardPhase(event.player)
            moonwardPhase.runTaskTimer(Odyssey.instance, 10, 20)
        }
    }

    private fun parasiticCurseEnchantment(event: PlayerItemDamageEvent, level: Int) {
        val rolled = (level * 10 > (0..100).random())
        if (!rolled) return
        //val itemDamage = event.damage
        event.damage = maxOf(event.damage - level, 0)
        event.player.damage(1.0)
    }



}
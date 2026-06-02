package me.shadowalzazel.mcodyssey.common.listeners

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemEnchantments
import me.shadowalzazel.mcodyssey.common.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.common.items.Item
import me.shadowalzazel.mcodyssey.common.listeners.utility.LootLogic
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Warden
import org.bukkit.entity.Wither
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
object LootListeners : Listener {

    @EventHandler
    fun mobDeathDropHandler(event: EntityDeathEvent) {
        if (event.entity.killer !is Player) return
        val mob = event.entity
        if (!mob.hasAI()) return
        val killer = mob.killer!!
        // Ectoplasm
        if (killer.location.block.biome.key.key == "soul_sand_valley"
            && mob.location.block.biome.key.key == "soul_sand_valley") {
            val mobLootLogic = LootLogic(33.3, mob, killer)
            if (mobLootLogic.roll(0.0)) {
                mob.world.dropItem(mob.location, Item.ECTOPLASM.newItemStack(1))
            }
        }
        // Boss drops
        when(mob) {
            is Warden -> {
                val book = ItemStack(Material.ENCHANTED_BOOK)
                val enchantments = ItemEnchantments.itemEnchantments().add(
                    OdysseyEnchantments.SCULK_SENSITIVE,
                    1
                )
                book.setData(DataComponentTypes.STORED_ENCHANTMENTS, enchantments)
                mob.world.dropItem(mob.location, book)
            }
            is Wither -> {
                val book = ItemStack(Material.ENCHANTED_BOOK)
                val enchantments = ItemEnchantments.itemEnchantments().add(
                    OdysseyEnchantments.STYX_ROSE,
                    1
                )
                book.setData(DataComponentTypes.STORED_ENCHANTMENTS, enchantments)
                mob.world.dropItem(mob.location, book)
            }
        }
    }

}
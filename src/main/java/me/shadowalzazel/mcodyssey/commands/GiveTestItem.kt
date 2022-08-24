package me.shadowalzazel.mcodyssey.commands

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import me.shadowalzazel.mcodyssey.items.OdysseyWeapons
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GiveTestItem : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (args.size == 1) {
                when (args[0]) {
                    "neptunian_diamond" -> {
                        sender.inventory.addItem(OdysseyItems.REFINED_NEPTUNIAN_DIAMONDS.createItemStack(1))
                    }
                    "iojovian_emerald" -> {
                        sender.inventory.addItem(OdysseyItems.REFINED_IOJOVIAN_EMERALDS.createItemStack(1))
                    }
                    "stuff" -> {
                        sender.inventory.addItem(OdysseyItems.IMPURE_ANTIMATTER_SHARD.createItemStack(1))
                        sender.inventory.addItem(OdysseyItems.IDESCINE_ESSENCE.createItemStack(1))
                        sender.inventory.addItem(OdysseyItems.NEUTRONIUM_BARK_SCRAPS.createItemStack(1))
                        sender.inventory.addItem(OdysseyItems.REFINED_NEPTUNIAN_DIAMONDS.createItemStack(1))
                    }
                    "necronomicon" -> {
                        sender.inventory.addItem(OdysseyWeapons.NECRONOMICON.createItemStack(1))
                    }
                    "bane_of_the_illager" -> {
                        sender.inventory.addItem(OdysseyItems.GILDED_BOOK.createGildedBook(OdysseyEnchantments.BANE_OF_THE_ILLAGER, 3))
                    }
                    "burst_barrage" -> {
                        sender.inventory.addItem(OdysseyItems.GILDED_BOOK.createGildedBook(OdysseyEnchantments.BURST_BARRAGE, 3))
                    }
                    "chain_reaction" -> {
                        sender.inventory.addItem(OdysseyItems.GILDED_BOOK.createGildedBook(OdysseyEnchantments.CHAIN_REACTION, 3))
                    }
                    else -> {
                        return false
                    }
                }
                return true
            }
        }
        return false
    }

}
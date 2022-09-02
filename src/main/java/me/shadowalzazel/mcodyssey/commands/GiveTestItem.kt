package me.shadowalzazel.mcodyssey.commands

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.items.OdysseyBooks
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
                        sender.inventory.addItem(OdysseyBooks.GILDED_BOOK.createGildedBook(OdysseyEnchantments.BANE_OF_THE_ILLAGER, 3))
                    }
                    "soul_rend" -> {
                        sender.inventory.addItem(OdysseyBooks.GILDED_BOOK.createGildedBook(OdysseyEnchantments.SOUL_REND, 1))
                    }
                    "decaying_touch" -> {
                        sender.inventory.addItem(OdysseyBooks.GILDED_BOOK.createGildedBook(OdysseyEnchantments.DECAYING_TOUCH, 1))
                    }
                    "gravity_well" -> {
                        sender.inventory.addItem(OdysseyBooks.GILDED_BOOK.createGildedBook(OdysseyEnchantments.GRAVITY_WELL, 1))
                    }
                    "freezing_aspect" -> {
                        sender.inventory.addItem(OdysseyBooks.GILDED_BOOK.createGildedBook(OdysseyEnchantments.FREEZING_ASPECT, 1))
                    }
                    "exploding" -> {
                        sender.inventory.addItem(OdysseyBooks.GILDED_BOOK.createGildedBook(OdysseyEnchantments.FREEZING_ASPECT, 1))
                    }
                    "burst_barrage" -> {
                        sender.inventory.addItem(OdysseyBooks.GILDED_BOOK.createGildedBook(OdysseyEnchantments.BURST_BARRAGE, 3))
                    }
                    "chain_reaction" -> {
                        sender.inventory.addItem(OdysseyBooks.GILDED_BOOK.createGildedBook(OdysseyEnchantments.CHAIN_REACTION, 3))
                    }
                    "tome_of_discharge" -> {
                        sender.inventory.addItem(OdysseyBooks.TOME_OF_DISCHARGE.createItemStack(1))
                    }
                    "tome_of_promotion" -> {
                        sender.inventory.addItem(OdysseyBooks.TOME_OF_PROMOTION.createItemStack(1))
                    }
                    "tome_of_replication" -> {
                        sender.inventory.addItem(OdysseyBooks.TOME_OF_REPLICATION.createItemStack(1))
                    }
                    "tome_of_harmony" -> {
                        sender.inventory.addItem(OdysseyBooks.TOME_OF_HARMONY.createItemStack(1))
                    }
                    //
                    "tome_of_infusion" -> {
                        sender.inventory.addItem(OdysseyBooks.TOME_OF_EXPENDITURE.createItemStack(1))
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
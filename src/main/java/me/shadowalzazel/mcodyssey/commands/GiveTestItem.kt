package me.shadowalzazel.mcodyssey.commands

import me.shadowalzazel.mcodyssey.alchemy.AlchemyPotions
import me.shadowalzazel.mcodyssey.alchemy.utility.AwkwardPotion
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
            /*
            val someBook = ItemStack(Material.ENCHANTED_BOOK, 1)
            someBook.addUnsafeEnchantment(OdysseyEnchantments.BUZZY_BEES, 2)
            // Make Enchantment Lore
            val someBookMeta = someBook.itemMeta
            val someBookLore = listOf("${ChatColor.GOLD}${OdysseyEnchantments.BUZZY_BEES.name} II")
            println(OdysseyEnchantments.BUZZY_BEES.displayName(1))
            someBookMeta.lore = someBookLore
            someBook.itemMeta = someBookMeta
            // Give
            sender.inventory.addItem(someBook)
             */
            if (args.size == 1) {
                when (args[0]) {
                    "neptunian_diamond" -> {
                        sender.inventory.addItem(OdysseyItems.REFINED_NEPTUNIAN_DIAMONDS.createItemStack(1))
                    }
                    "iojovian_emerald" -> {
                        sender.inventory.addItem(OdysseyItems.REFINED_IOJOVIAN_EMERALDS.createItemStack(1))
                    }
                    "necronomicon" -> {
                        sender.inventory.addItem(OdysseyWeapons.NECRONOMICON.createItemStack(1))
                    }
                    "bane_of_the_illager" -> {
                        sender.inventory.addItem(OdysseyItems.GILDED_BOOK.createGildedBook(OdysseyEnchantments.BANE_OF_THE_ILLAGER, 3))
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